package cn.eric.jdktools.file;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.log4j.Logger;

/**
 * FTPClient工具类
 * @author happyqing
 * @since 2016.7.20
 */
public class FtpUtil {

    private static Logger log = Logger.getLogger(FtpUtil.class);
    private FTPClient ftp;

    public FtpUtil() {
        ftp = new FTPClient();
        ftp.setControlEncoding("UTF-8"); //解决上传文件时文件名乱码
    }

    public FtpUtil(String controlEncoding) {
        ftp = new FTPClient();
        ftp.setControlEncoding(controlEncoding); //解决上传文件时文件名乱码
    }

    public void setTimeOut(int defaultTimeoutSecond, int connectTimeoutSecond, int dataTimeoutSecond){
        try {
            ftp.setDefaultTimeout(defaultTimeoutSecond * 1000);
            //ftp.setConnectTimeout(connectTimeoutSecond * 1000); //commons-net-3.5.jar
            ftp.setSoTimeout(connectTimeoutSecond * 1000); //commons-net-1.4.1.jar 连接后才能设置
            ftp.setDataTimeout(dataTimeoutSecond * 1000);
        } catch (SocketException e) {
            log.error("setTimeout Exception:", e);
        }
    }

    public FTPClient getFTPClient(){
        return ftp;
    }

    public void setControlEncoding(String charset){
        ftp.setControlEncoding(charset);
    }

    public void setFileType(int fileType) throws IOException {
        ftp.setFileType(fileType);
    }

    /**
     * Connect to FTP server.
     *
     * @param host
     *            FTP server address or name
     * @param port
     *            FTP server port
     * @param user
     *            user name
     * @param password
     *            user password
     * @throws IOException
     *             on I/O errors
     */
    public FTPClient connect(String host, int port, String user, String password) throws IOException {
        // Connect to server.
        try {
            ftp.connect(host, port);
        } catch (UnknownHostException ex) {
            throw new IOException("Can't find FTP server '" + host + "'");
        }

        // Check rsponse after connection attempt.
        int reply = ftp.getReplyCode();
        if (!FTPReply.isPositiveCompletion(reply)) {
            disconnect();
            throw new IOException("Can't connect to server '" + host + "'");
        }

        if ("".equals(user)) {
            user = "anonymous";
        }

        // Login.
        if (!ftp.login(user, password)) {
            disconnect();
            throw new IOException("Can't login to server '" + host + "'");
        }

        // Set data transfer mode.
        ftp.setFileType(FTP.BINARY_FILE_TYPE);
        //ftp.setFileType(FTP.ASCII_FILE_TYPE);

        // Use passive mode to pass firewalls.
        ftp.enterLocalPassiveMode();

        return ftp;
    }

    /**
     * Test connection to ftp server
     *
     * @return true, if connected
     */
    public boolean isConnected() {
        return ftp.isConnected();
    }

    /**
     * Disconnect from the FTP server
     *
     * @throws IOException
     *             on I/O errors
     */
    public void disconnect() throws IOException {

        if (ftp.isConnected()) {
            try {
                ftp.logout();
                ftp.disconnect();
            } catch (IOException ex) {
            }
        }
    }

    /**
     * Get file from ftp server into given output stream
     *
     * @param ftpFileName
     *            file name on ftp server
     * @param out
     *            OutputStream
     * @throws IOException
     */
    public void retrieveFile(String ftpFileName, OutputStream out) throws IOException {
        try {
            // Get file info.
            FTPFile[] fileInfoArray = ftp.listFiles(ftpFileName);
            if (fileInfoArray == null || fileInfoArray.length == 0) {
                throw new FileNotFoundException("File '" + ftpFileName + "' was not found on FTP server.");
            }

            // Check file size.
            FTPFile fileInfo = fileInfoArray[0];
            long size = fileInfo.getSize();
            if (size > Integer.MAX_VALUE) {
                throw new IOException("File '" + ftpFileName + "' is too large.");
            }

            // Download file.
            if (!ftp.retrieveFile(ftpFileName, out)) {
                throw new IOException("Error loading file '" + ftpFileName + "' from FTP server. Check FTP permissions and path.");
            }

            out.flush();

        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException ex) {
                }
            }
        }
    }

    /**
     * Put file on ftp server from given input stream
     *
     * @param ftpFileName
     *            file name on ftp server
     * @param in
     *            InputStream
     * @throws IOException
     */
    public void storeFile(String ftpFileName, InputStream in) throws IOException {
        try {
            if (!ftp.storeFile(ftpFileName, in)) {
                throw new IOException("Can't upload file '" + ftpFileName + "' to FTP server. Check FTP permissions and path.");
            }
        } finally {
            try {
                in.close();
            } catch (IOException ex) {
            }
        }
    }

    /**
     * 修改名称
     * @param from
     * @param to
     * @throws IOException
     */
    public boolean rename(String from, String to) throws IOException {
        return ftp.rename(from, to);
    }

    /**
     * Delete the file from the FTP server.
     *
     * @param ftpFileName
     *            server file name (with absolute path)
     * @throws IOException
     *             on I/O errors
     */
    public void deleteFile(String ftpFileName) throws IOException {
        if (!ftp.deleteFile(ftpFileName)) {
            throw new IOException("Can't remove file '" + ftpFileName + "' from FTP server.");
        }
    }

    /**
     * Upload the file to the FTP server.
     *
     * @param ftpFileName
     *            server file name (with absolute path)
     * @param localFile
     *            local file to upload
     * @throws IOException
     *             on I/O errors
     */
    public void upload(String ftpFileName, File localFile) throws IOException {
        // File check.
        if (!localFile.exists()) {
            throw new IOException("Can't upload '" + localFile.getAbsolutePath() + "'. This file doesn't exist.");
        }

        // Upload.
        InputStream in = null;
        try {
            in = new BufferedInputStream(new FileInputStream(localFile));
            if (!ftp.storeFile(ftpFileName, in)) {
                throw new IOException("Can't upload file '" + ftpFileName + "' to FTP server. Check FTP permissions and path.");
            }

        } finally {
            try {
                in.close();
            } catch (IOException ex) {
            }
        }
    }

    /**
     * 上传目录（会覆盖)
     * @param remotePath 远程目录 /home/test/a
     * @param localPath 本地目录 D:/test/a
     * @throws IOException
     */
    public void uploadDir(String remotePath, String localPath) throws IOException {
        File file = new File(localPath);
        if (file.exists()) {
            if(!ftp.changeWorkingDirectory(remotePath)){
                ftp.makeDirectory(remotePath);  //创建成功返回true，失败（已存在）返回false
                ftp.changeWorkingDirectory(remotePath); //切换成返回true，失败（不存在）返回false
            }
            File[] files = file.listFiles();
            for (File f : files) {
                if (f.isDirectory() && !f.getName().equals(".") && !f.getName().equals("..")) {
                    uploadDir(remotePath + "/" + f.getName(), f.getPath());
                } else if (f.isFile()) {
                    upload(remotePath + "/" + f.getName(), f);
                }
            }
        }
    }

    /**
     * Download the file from the FTP server.
     *
     * @param ftpFileName
     *            server file name (with absolute path)
     * @param localFile
     *            local file to download into
     * @throws IOException
     *             on I/O errors
     */
    public void download(String ftpFileName, File localFile) throws IOException {
        // Download.
        OutputStream out = null;
        try {
            // Get file info.
            FTPFile[] fileInfoArray = ftp.listFiles(ftpFileName);
            if (fileInfoArray == null || fileInfoArray.length == 0) {
                throw new FileNotFoundException("File " + ftpFileName + " was not found on FTP server.");
            }

            // Check file size.
            FTPFile fileInfo = fileInfoArray[0];
            long size = fileInfo.getSize();
            if (size > Integer.MAX_VALUE) {
                throw new IOException("File " + ftpFileName + " is too large.");
            }

            // Download file.
            out = new BufferedOutputStream(new FileOutputStream(localFile));
            if (!ftp.retrieveFile(ftpFileName, out)) {
                throw new IOException("Error loading file " + ftpFileName + " from FTP server. Check FTP permissions and path.");
            }

            out.flush();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException ex) {
                }
            }
        }
    }

    /**
     * 下载目录（会覆盖)
     * @param remotePath 远程目录 /home/test/a
     * @param localPath 本地目录 D:/test/a
     * @return
     * @throws IOException
     */
    public void downloadDir(String remotePath, String localPath) throws IOException {
        File file = new File(localPath);
        if(!file.exists()){
            file.mkdirs();
        }
        FTPFile[] ftpFiles = ftp.listFiles(remotePath);
        for (int i = 0; ftpFiles!=null && i<ftpFiles.length; i++) {
            FTPFile ftpFile = ftpFiles[i];
            if (ftpFile.isDirectory() && !ftpFile.getName().equals(".") && !ftpFile.getName().equals("..")) {
                downloadDir(remotePath + "/" + ftpFile.getName(), localPath + "/" + ftpFile.getName());
            } else {
                download(remotePath + "/" + ftpFile.getName(), new File(localPath + "/" + ftpFile.getName()));
            }
        }
    }

    /**
     * List the file name in the given FTP directory.
     *
     * @param filePath
     *            absolute path on the server
     * @return files relative names list
     * @throws IOException
     *             on I/O errors
     */
    public List<String> listFileNames(String filePath) throws IOException {
        List<String> fileList = new ArrayList<String>();

        FTPFile[] ftpFiles = ftp.listFiles(filePath);
        for (int i = 0; ftpFiles!=null && i<ftpFiles.length; i++) {
            FTPFile ftpFile = ftpFiles[i];
            if (ftpFile.isFile()) {
                fileList.add(ftpFile.getName());
            }
        }

        return fileList;
    }

    /**
     * List the files in the given FTP directory.
     *
     * @param filePath
     *            directory
     * @return list
     * @throws IOException
     */
    public List<FTPFile> listFiles(String filePath) throws IOException {
        List<FTPFile> fileList = new ArrayList<FTPFile>();

        FTPFile[] ftpFiles = ftp.listFiles(filePath);
        for (int i = 0; ftpFiles!=null && i<ftpFiles.length; i++) {
            FTPFile ftpFile = ftpFiles[i];
            //            FfpFileInfo fi = new FfpFileInfo();
            //            fi.setName(ftpFile.getName());
            //            fi.setSize(ftpFile.getSize());
            //            fi.setTimestamp(ftpFile.getTimestamp());
            //            fi.setType(ftpFile.isDirectory());
            fileList.add(ftpFile);
        }

        return fileList;
    }


    /**
     * Send an FTP Server site specific command
     *
     * @param args
     *            site command arguments
     * @throws IOException
     *             on I/O errors
     */
    public void sendSiteCommand(String args) throws IOException {
        if (ftp.isConnected()) {
            try {
                ftp.sendSiteCommand(args);
            } catch (IOException ex) {
            }
        }
    }

    /**
     * Get current directory on ftp server
     *
     * @return current directory
     */
    public String printWorkingDirectory() {
        if (!ftp.isConnected()) {
            return "";
        }

        try {
            return ftp.printWorkingDirectory();
        } catch (IOException e) {
        }

        return "";
    }

    /**
     * Set working directory on ftp server
     *
     * @param dir
     *            new working directory
     * @return true, if working directory changed
     */
    public boolean changeWorkingDirectory(String dir) {
        if (!ftp.isConnected()) {
            return false;
        }

        try {
            return ftp.changeWorkingDirectory(dir);
        } catch (IOException e) {
        }

        return false;
    }

    /**
     * Change working directory on ftp server to parent directory
     *
     * @return true, if working directory changed
     */
    public boolean changeToParentDirectory() {
        if (!ftp.isConnected()) {
            return false;
        }

        try {
            return ftp.changeToParentDirectory();
        } catch (IOException e) {
        }

        return false;
    }

    /**
     * Get parent directory name on ftp server
     *
     * @return parent directory
     */
    public String printParentDirectory() {
        if (!ftp.isConnected()) {
            return "";
        }

        String w = printWorkingDirectory();
        changeToParentDirectory();
        String p = printWorkingDirectory();
        changeWorkingDirectory(w);

        return p;
    }

    /**
     * 创建目录
     * @param pathname
     * @throws IOException
     */
    public boolean makeDirectory(String pathname) throws IOException {
        return ftp.makeDirectory(pathname);
    }

    public static void main(String[] args) throws Exception {
        FtpUtil ftpUtil = new FtpUtil("UTF-8");
        ftpUtil.connect("1.2.3.4", 21, "testuser", "testuser");
        //ftpUtil.setTimeOut(60, 60, 60);
        ftpUtil.upload("/home/testuser/文件1.txt", new File("E:/image/FTPClient/FTPClient测试/文件1.txt"));
        ftpUtil.download("/home/testuser/文件1.txt", new File("E:/image/FTPClient/FTPClient测试/文件1.txt"));
        ftpUtil.uploadDir("/home/testuser/FTPClient测试", "E:/image/FTPClient/FTPClient测试");
        ftpUtil.downloadDir("/home/testuser/FTPClient测试", "E:/image/FTPClient/FTPClient测试");

        ByteArrayOutputStream bos = new ByteArrayOutputStream(); //自动增长
        ftpUtil.retrieveFile("/home/testuser/文件1.txt", bos);
        System.out.println(bos.size());
        String contentStr = new String(bos.toByteArray(),"GBK");
        System.out.println(contentStr);
        ftpUtil.disconnect();
    }

}