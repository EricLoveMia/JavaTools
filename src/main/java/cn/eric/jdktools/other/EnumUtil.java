package cn.eric.jdktools.other;

import org.apache.commons.lang3.EnumUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.List;

/**
 * @ClassName EnumUtil
 * @Description: 枚举工具类
 * @Author eric
 * @Date 2021/5/19
 * @Version V1.0
 **/
public class EnumUtil {

    private static final Logger logger = LoggerFactory.getLogger(EnumUtil.class);

    /**
     * 判断某个枚举是否包某个code值
     *
     * @param enumClass 需要判断是否存在那个枚举类中
     * @param code      需要判断的值
     * @return 包含返回true，否则返回false
     */
    public static boolean isInclude(Class enumClass, int code) {
        List enumList = EnumUtils.getEnumList(enumClass);
        for (int i = 0; i < enumList.size(); i++) {
            Object en = enumList.get(i);
            Class<?> enClass = en.getClass();
            try {
                // 需要与枚举类方法对应
                Method method = enClass.getMethod("getCode");
                Object invoke = method.invoke(en);
                if (Integer.parseInt(invoke.toString()) == code) {
                    return true;
                }
            } catch (Exception e) {
                logger.error("枚举执行getCode方法失败...", e);
            }
        }
        return false;
    }

    /**
     * 判断某个枚举是否包某个code值
     *
     * @param enumClass 需要判断是否存在那个枚举类中
     * @param code      需要判断的值
     * @return 包含返回true，否则返回false
     */
    public static boolean isInclude(Class enumClass, String code) {
        List enumList = EnumUtils.getEnumList(enumClass);
        for (int i = 0; i < enumList.size(); i++) {
            Object en = enumList.get(i);
            Class<?> enClass = en.getClass();
            try {
                // 需要与枚举类方法对应
                Method method = enClass.getMethod("getCode");
                Object invoke = method.invoke(en);
                if (invoke.toString().equals(code)) {
                    return true;
                }
            } catch (Exception e) {
                logger.error("枚举执行getCode方法失败...", e);
            }
        }
        return false;
    }

}
