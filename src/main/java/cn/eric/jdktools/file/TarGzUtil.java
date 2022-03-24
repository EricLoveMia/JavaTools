package cn.eric.jdktools.file;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * @ClassName TarGzUtils
 * @Description: tar包解压缩工具类
 * @Author YCKJ2725
 * @Date 2021/7/2
 * @Version V1.0
 **/
public class TarGzUtil {

    protected static final Logger logger = LoggerFactory.getLogger(TarGzUtil.class);

    public static String readFileFromTar(String archiveFileName, String filePath) {
        String snapshot = "";
        File tarGzFile = new File(archiveFileName);
        BufferedReader br = null;
        try (FileInputStream tarGzFileIs = new FileInputStream(tarGzFile);
             CompressorInputStream cis = new GzipCompressorInputStream(tarGzFileIs, true);
             TarArchiveInputStream tis = new TarArchiveInputStream(cis)) {

            TarArchiveEntry entry = tis.getNextTarEntry();
            StringBuffer ret = null;
            while (entry != null) {
                if (entry.getName().contains(filePath)) {
                    br = new BufferedReader(new InputStreamReader(tis, StandardCharsets.UTF_8));

                    // Read the snapshot file line by line
                    String line = br.readLine();
                    while (line != null) {
                        // add the line to the snapshot
                        if (ret == null) {
                            ret = new StringBuffer(line);
                        } else {
                            ret.append("\n").append(line);
                        }
                        // Read next line.
                        line = br.readLine();
                    }
                    // end of loop reading the lines

                    if (ret != null) {
                        snapshot = ret.toString();
                    }
                    break;
                }
                entry = tis.getNextTarEntry();
            }
        } catch (IOException e) {
            logger.error("获取inventory文件失败", e);
        } finally {
            finalMethod(br);
        }
        return snapshot;
    }

    public static String readFileFromTar(File tarGzFile, String filePath) {
        String snapshot = "";
        BufferedReader br = null;
        try (FileInputStream tarGzFileIs = new FileInputStream(tarGzFile);
             CompressorInputStream cis = new GzipCompressorInputStream(tarGzFileIs, true);
             TarArchiveInputStream tis = new TarArchiveInputStream(cis)) {

            TarArchiveEntry entry = tis.getNextTarEntry();
            StringBuffer ret = null;
            while (entry != null) {
                if (entry.getName().contains(filePath)) {
                    br = new BufferedReader(new InputStreamReader(tis, StandardCharsets.UTF_8));

                    // Read the snapshot file line by line
                    String line = br.readLine();
                    while (line != null) {
                        // add the line to the snapshot
                        if (ret == null) {
                            ret = new StringBuffer(line);
                        } else {
                            ret.append("\n").append(line);
                        }
                        // Read next line.
                        line = br.readLine();
                    }
                    // end of loop reading the lines

                    if (ret != null) {
                        snapshot = ret.toString();
                    }
                    break;
                }
                entry = tis.getNextTarEntry();
            }
        } catch (IOException e) {
            logger.error("获取inventory文件失败", e);
        } finally {
            finalMethod(br);
        }
        return snapshot;
    }

    private static void finalMethod(BufferedReader br) {
        if (br != null) {
            try {
                br.close();
            } catch (IOException e) {
                logger.error("释放资源失败", e);
            }
        }
    }

    public static void main(String[] args) throws IOException {
        String str = "{\"ExampleComponent\":{\"responseJsonschema\":\"{\\\"$schema\\\":\\\"https://json-schema.org/draft/2019-09/schema\\\",\\\"type\\\":\\\"object\\\",\\\"properties\\\":{\\\"description\\\":{\\\"type\\\":\\\"string\\\",\\\"title\\\":\\\"结果说明\\\",\\\"description\\\":\\\"结果说明\\\"},\\\"resultPoint\\\":{\\\"type\\\":\\\"number\\\",\\\"title\\\":\\\"结果值\\\",\\\"description\\\":\\\"结果值\\\"}}}\",\"requestJsonschema\":\"{\\\"$schema\\\":\\\"https://json-schema.org/draft/2019-09/schema\\\",\\\"type\\\":\\\"object\\\",\\\"properties\\\":{\\\"caseOne\\\":{\\\"type\\\":\\\"number\\\",\\\"title\\\":\\\"操作数1\\\"},\\\"caseTwo\\\":{\\\"type\\\":\\\"number\\\",\\\"title\\\":\\\"操作数2\\\"}}}\",\"generalParameterJsonschema\":\"{\\\"$schema\\\":\\\"https://json-schema.org/draft/2019-09/schema\\\",\\\"type\\\":\\\"object\\\",\\\"properties\\\":{\\\"operate\\\":{\\\"type\\\":\\\"string\\\",\\\"title\\\":\\\"操作\\\",\\\"description\\\":\\\"操作类型 sum：求和 max：求最大值\\\"}}}\"}}";

        JSONObject json = JSONObject.parseObject(str);
        System.out.println(json.toJSONString());


        Map<String, Object> result = JSONObject.parseObject(json.toJSONString(),Map.class);
        System.out.println(JSONObject.toJSONString(result));
    }
}
