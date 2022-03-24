package cn.eric.jdktools.file.jar;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Company: 苏州渠成易销网络科技有限公司
 *
 * @version 1.0.0
 * @description: jar包合并并执行的测试方法
 * @author: 钱旭
 * @date: 2022-01-13 18:29
 **/
public class MergeTest {
    private final static String OUT_FILE_NAME_PREFIX = "base_";
    private final static String OUT_FILE_NAME_SUFFIX = ".jar";


    public static void main(String[] args) throws IOException {
        String userdir = System.getProperty("user.dir");
        System.out.println("userdir=" +userdir);

        String assetsPath = "/Users/eric/Documents/workplace/JavaTools-master/src/main/resources/assets";
        FatJarCreator fatjarCreator = new FatJarCreator();
        File fileOne = new File("/Users/eric/Documents/workplace/JavaTools-master/src/main/resources/templete/ipaas-component-java8-sample-1.0.0.jar");
        File fileTwo = new File("/Users/eric/Documents/workplace/JavaTools-master/src/main/resources/templete/ipaas-component-java-common-1.2.0-SNAPSHOT.jar");
        List<File> files = Arrays.asList(fileOne, fileTwo);

        String outDirPath = userdir+"/out/";
        String outFileName = outDirPath+getName();
        File outDir = new File(outDirPath);
        if(!outDir.exists()){
            outDir.mkdirs();
        }
        if (fatjarCreator.createByFile(files,assetsPath,outFileName)) {
            System.out.println("outFileName:"+outFileName);
        }

        // 执行java -jar 方法
        String command = "java -jar " + outFileName + " " + outDirPath + "params.json" + " " + outFileName;
        System.out.println(command);
        Process exec = Runtime.getRuntime().exec(command);
        while (exec.isAlive()) {

        }
        exec.destroy();
        //System.out.println("Process exitValue: " + exitVal);
    }


    private static String getName(){
        String name = null;

        Date date = new Date();
        SimpleDateFormat dateformat = new SimpleDateFormat("MMddHHmm");
        String dateSuffix = dateformat.format(date);

        name = OUT_FILE_NAME_PREFIX+dateSuffix+OUT_FILE_NAME_SUFFIX;

        return name;
    }
}
