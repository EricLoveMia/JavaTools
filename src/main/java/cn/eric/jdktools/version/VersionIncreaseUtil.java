package cn.eric.jdktools.version;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import org.apache.commons.lang.StringUtils;

import java.util.List;

/**
 * @version 1.0.0
 * @description: 版本号递增工具
 * @author: eric
 * @date: 2022-02-07 10:12
 **/
public class VersionIncreaseUtil {

    public static String upgradeVersion(String version) {
        String upgradeVsersion;
        if (StringUtils.isEmpty(version)) {
            version = "1.0.0";
        }
        //将版本号拆解成整数数组
        List<String> strArr = Splitter.on(".").splitToList(version);
        Integer[] ints = new Integer[strArr.size()];
        for (int i = 0; i<ints.length; i++) {
            ints[i] = Integer.parseInt(strArr.get(i));
        }
        //递归调用
        upgradeVersion(ints, ints.length - 1);
        //数组转字符串
        upgradeVsersion = Joiner.on(".").join(ints);
        return upgradeVsersion;

    }

    private static void upgradeVersion(Integer[] ints, int index) {
        if (index == 0) {
            ints[0] = ints[0] + 1;
        } else {
            int value = ints[index] + 1;
            if (value < 99) {
                ints[index] = value;
            } else {
                ints[index] = 0;
                upgradeVersion(ints, index - 1);
            }
        }
    }

    public static void main(String[] args) {
        // 子版本号最大99
        System.out.println("version:1.0.0|" + upgradeVersion("1.0.0"));
        System.out.println("version:1.0.9|" + upgradeVersion("1.0.9"));
        System.out.println("version:1.1.9|" + upgradeVersion("1.1.9"));
        System.out.println("version:1.9.9|" + upgradeVersion("1.9.9"));
        System.out.println("version:1.9.99|" + upgradeVersion("1.9.99"));
    }
}

