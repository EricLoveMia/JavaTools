package cn.eric.jdktools.file;

import org.apache.commons.lang3.StringUtils;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @program: YamlUtil
 * @description: yml配置处理工具
 * @author: YCKJ2725
 * @create: 2021/3/30
 **/
public class YamlUtil {

    /**
     * 功能描述:
     * 〈加载yml文件〉
     *
     * @return : java.util.Map<?,?>
     * @params : [fileName]
     * @author : cwl
     * @date : 2019/10/22 13:52
     */
    public static Map loadYaml(String fileName) throws FileNotFoundException {
        try (InputStream in = new FileInputStream(fileName)) {
            return StringUtils.isNotEmpty(fileName) ? (LinkedHashMap) new Yaml().load(in) : null;
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public static Map loadYamlString(String fileString) {
        return StringUtils.isNotEmpty(fileString) ? (LinkedHashMap) new Yaml().load(fileString) : null;
    }

    /**
     * 功能描述:
     * 〈往yml文件中写数据,数据为map〉
     *
     * @return : void
     * @params : [fileName, map]
     * @author : cwl
     * @date : 2019/10/22 13:52
     */
    public static void dumpYaml(String fileName, Map<?, ?> map) throws IOException {
        if (StringUtils.isNotEmpty(fileName)) {
            FileWriter fileWriter = new FileWriter(new File(fileName));
            DumperOptions options = new DumperOptions();
            options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
            Yaml yaml = new Yaml(options);
            yaml.dump(map, fileWriter);
            fileWriter.close();
        }
    }

    /**
     * 功能描述:
     * 〈根据key查询yml中的数据〉
     *
     * @return : java.lang.Object
     * @params : [map, qualifiedKey]
     * @author : cwl
     * @date : 2019/10/22 13:53
     */
    public static Object getProperty(Map<?, ?> map, Object qualifiedKey) {
        if (map != null && !map.isEmpty() && qualifiedKey != null) {
            String input = String.valueOf(qualifiedKey);
            if (!"".equals(input)) {
                if (input.contains(".")) {
                    int index = input.indexOf(".");
                    String left = input.substring(0, index);
                    String right = input.substring(index + 1);
                    return getProperty((Map<?, ?>) map.get(left), right);
                } else if (map.containsKey(input)) {
                    return map.get(input);
                } else {
                    return null;
                }
            }
        }
        return null;
    }

    /**
     * 功能描述:
     * 〈设置yml中的值〉
     *
     * @return : void
     * @params : [map, qualifiedKey, value]
     * @author : cwl
     * @date : 2019/10/22 13:53
     */
    @SuppressWarnings("unchecked")
    public static void setProperty(Map<?, ?> map, Object qualifiedKey, Object value) {
        if (map != null && !map.isEmpty() && qualifiedKey != null) {
            String input = String.valueOf(qualifiedKey);
            if (!input.equals("")) {
                if (input.contains(".")) {
                    int index = input.indexOf(".");
                    String left = input.substring(0, index);
                    String right = input.substring(index + 1);
                    setProperty((Map<?, ?>) map.get(left), right, value);
                } else {
                    ((Map<Object, Object>) map).put(qualifiedKey, value);
                }
            }
        }
    }
}

