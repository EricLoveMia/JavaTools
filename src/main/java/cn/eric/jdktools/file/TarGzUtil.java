package cn.eric.jdktools.file;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Map;

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
        String test = TarGzUtil.readFileFromTar("D:\\data\\upload\\installer_20210611150242.tar.gz"
                , "user-data/conf/inventory.yml");
        Map map = YamlUtil.loadYamlString(test);
        // System.out.println(JsonUtil.toJson(map));
    }
}
