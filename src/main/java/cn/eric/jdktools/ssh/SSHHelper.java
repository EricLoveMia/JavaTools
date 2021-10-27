package cn.eric.jdktools.ssh;

import com.jcraft.jsch.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

/**
 * java ssh登录linux以后的一些操作方式
 *
 * @version V1.0
 */
public class SSHHelper {
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    private String charset = Charset.defaultCharset().toString();
    private Session session;

    public SSHHelper(String host, Integer port, String user, String password) throws JSchException {
        connect(host, port, user, password);
    }

    /**
     * 连接sftp服务器
     *
     * @param host     远程主机ip地址
     * @param port     sftp连接端口，null 时为默认端口
     * @param user     用户名
     * @param password 密码
     * @return
     * @throws JSchException
     */
    private Session connect(String host, Integer port, String user, String password) throws JSchException {
        JSch jsch = new JSch();
        if (port != null) {
            session = jsch.getSession(user, host, port.intValue());
        } else {
            session = jsch.getSession(user, host);
        }
        session.setPassword(password);
        //设置第一次登陆的时候提示，可选值:(ask | yes | no)
        session.setConfig("StrictHostKeyChecking", "no");
        session.setConfig("PreferredAuthentications", "publickey,keyboard-interactive,password");
        //30秒连接超时
        session.connect(5000);
        return session;
    }

    public SSHResInfo sendCmd(String command, String sandboxId) {
        return sendCmd(command, 200, sandboxId);
    }

    /*
     * 执行命令，返回执行结果
     * @param command 命令
     * @param delay 估计shell命令执行时间
     * @return String 执行命令后的返回
     * @throws IOException
     * @throws JSchException
     */
    public SSHResInfo sendCmd(String command, int delay, String sandboxId) {
        SSHResInfo result = null;
        byte[] tmp = new byte[1024]; //读数据缓存
        Channel channel = null;
        try {
            channel = session.openChannel("exec");
            ChannelExec ssh = (ChannelExec) channel;
            //返回的结果可能是标准信息,也可能是错误信息,所以两种输出都要获取
            //一般情况下只会有一种输出.
            //但并不是说错误信息就是执行命令出错的信息,如获得远程java JDK版本就以
            //ErrStream来获得.
            InputStream stdStream = ssh.getInputStream();
            InputStream errStream = ssh.getErrStream();

            ssh.setCommand(command);
            ssh.connect();

            //开始获得SSH命令的结果
            while (true) {
                //获得错误输出
                String errResultStr = readStreamInfo(errStream, tmp);
                //获得标准输出
                String strBufferStr = readStreamInfo(stdStream, tmp);
                if (ssh.isClosed()) {
                    int code = ssh.getExitStatus();
                    if (code != 0) {
                        logger.warn("场景ID【{}】  exit-status:{},errResultStr:{} ", sandboxId, code, errResultStr);
                    }
                    result = new SSHResInfo(code, strBufferStr, errResultStr);
                    break;
                }
                Thread.sleep(delay <= 50 ? 50 : delay);
            }
        } catch (Exception ee) {
            logger.warn("Exception", ee);
        } finally {
            if (channel != null) {
                channel.disconnect();
            }
        }

        return result;
    }

    /**
     * 实时打印日志信息
     */
    public SSHResInfo shellCmd(String command, String sandboxId) {
        SSHResInfo result = null;
        ChannelShell channel = null;
        OutputStream os = null;
        InputStream in = null;
        try {
            channel = (ChannelShell) session.openChannel("shell");
            in = channel.getInputStream();
            channel.setPty(true);
            channel.connect();
            os = channel.getOutputStream();
            os.write((command + "\r\n").getBytes());
            os.write("exit\r\n".getBytes());
            os.flush();
            byte[] tmp = new byte[1024];
            while (true) {
                StringBuilder message = new StringBuilder();
                while (in.available() > 0) {
                    int i = in.read(tmp, 0, 1024);
                    if (i < 0) {
                        break;
                    }
                    message.append(new String(tmp, 0, i));
                }
                if (channel.isClosed()) {
                    if (in.available() > 0) continue;
                    int code = channel.getExitStatus();
                    if (code != 0) {
                        logger.warn("沙箱场景ID【{}】  exit-status:{},errResultStr:{} ", sandboxId, code, message.toString());
                    }
                    result = new SSHResInfo(code, message.toString(), message.toString());
                    break;
                }
                Thread.sleep(1000);
            }
        } catch (Exception e) {
            logger.warn("shell Exception", e);
        } finally {
            closeShell(channel, os, in);
        }
        return result;
    }

    public void closeShell(ChannelShell channel, OutputStream os, InputStream in) {
        try {
            if (os != null) {
                os.close();
            }
            if (in != null) {
                in.close();
            }
        } catch (IOException e) {
            logger.warn("IOException", e);
        }
        if (channel != null) {
            channel.disconnect();
        }
    }

    /**
     * 获取流输出信息
     *
     * @param inputStream
     * @param tmp
     * @return
     */
    public String readStreamInfo(InputStream inputStream, byte[] tmp) {
        StringBuilder stringBuilder = new StringBuilder();
        while (true) {
            try {
                if (inputStream.available() <= 0) {
                    break;
                }
                int i = inputStream.read(tmp, 0, 1024);
                if (i < 0) {
                    break;
                }
                stringBuilder.append(new String(tmp, 0, i));
            } catch (IOException e) {
                logger.warn("IOException", e);
            }
        }
        return stringBuilder.toString();
    }

    /**
     * 下载文件 采用默认的传输模式：OVERWRITE
     *
     * @param src linux服务器文件地址
     * @param dst linux服务器文件地址
     * @throws JSchException
     * @throws SftpException
     */
    public boolean fileDownload(String src, String dst, String sandboxId) {

        logger.info("沙箱【{}】开始下载安装日志 src:{}，dst:{}", sandboxId, src, dst);
        // src 是linux服务器文件地址,dst 本地存放地址
        boolean isSuccess = true;
        ChannelSftp channelSftp = null;
        try {
            channelSftp = (ChannelSftp) session.openChannel("sftp");
            // 远程连接
            channelSftp.connect();
            // 下载文件,多个重载方法
            channelSftp.get(src, dst);
            // 切断远程连接,quit()等同于exit(),都是调用disconnect()
            channelSftp.quit();
        } catch (Exception e) {
            logger.warn("【{}】下载文件异常", sandboxId, e);
            isSuccess = false;
        }
        return isSuccess;
    }

    /**
     * 用完记得关闭，否则连接一直存在，程序不会退出
     */
    public void close() {
        if (session.isConnected()) {
            session.disconnect();
        }
    }


}