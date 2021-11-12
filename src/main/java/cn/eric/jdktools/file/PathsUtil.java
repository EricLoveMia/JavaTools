package cn.eric.jdktools.file;

import cn.eric.jdktools.date.DateUtil;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @ClassName PathsUtil
 * @Description: TODO
 * @Author YCKJ2725
 * @Date 2021/11/5
 * @Version V1.0
 **/
public class PathsUtil {

    public static void main(String[] args) throws IOException {

        List<String> packageList = new ArrayList<>();
        packageList.add("SystemOut——213.log");
        packageList.add("SystemOut.log");
        String installerPathTarget = "D:\\data\\file";
        // 检查并创建备份文件夹
        Path backPath = Paths.get(installerPathTarget, "backup"
                , DateUtil.format(new Date(), DateUtil.PATTERN_YYYYMMDD));
        if (!Files.exists(backPath)) {
            Files.createDirectory(backPath);
        }
        // 获取所有的一键部署包
        Path dir = Paths.get(installerPathTarget);
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
            for (Path path : stream) {
                // 找到就转移到备份文件夹中
                if (packageList.contains(path.getFileName().toString())) {
                    System.out.println("begin to move path " + path.getFileName() + " to" + backPath.getFileName());
                    Files.move(path, backPath.resolve(path.getFileName().toString()),
                            StandardCopyOption.REPLACE_EXISTING);
                    // TODO 发送邮件
                }
            }
        } catch (IOException e) {

        }
    }
}
