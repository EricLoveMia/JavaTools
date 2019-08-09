package cn.eric.jdktools.string;

import org.apache.commons.lang.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Eric
 * @version 1.0
 * @ClassName: ValidateUtil
 * @Description: 验证类型的类
 * @date 2018/12/28 11:35
 **/
public class ValidateUtil {


    static final String Phone_Check = "^(((13[0-9])|(14[145789])|(15([0-9]))|(16[6])|(17[0135678])|(18[0-9])|(19[89]))\\d{8})$";

    /**
     * @Author Eric
     * @Description 返回字符串中的第一个识别出来的车牌号
     * @Date 11:42 2018/12/28
     * @Param [str]
     * @return java.lang.String
     **/
    public static String getPlateNumberFromStr(String str){
        // 车牌号为7位 如果小于7位
        if(StringUtils.isNotBlank(str) && str.length() >= 7){
            for (int i = 0; i < str.length()-6; i++) {
                String substring = str.substring(i, i+7);
                if(checkPlateNumber(substring)){
                    return substring;
                }
            }
        }else{
            return str;  // 如果为空 或者 小于7个就把所有的都返回
        }
        return str; // 如果没有找到，就返回全部
    }

    /**
     * @Author Eric
     * @Description 检查输入的字符串是不是车牌号
     * @Date 11:41 2018/12/28
     * @Param [plateNumber]
     * @return boolean
     **/
    public static boolean checkPlateNumber(String plateNumber){
        // 正则表达式
        String hphmEx2 = "([京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领A-Z]{1}[A-Z]{1}(([0-9]{5}[DF])|([DF]([A-HJ-NP-Z0-9])[0-9]{4})))|([京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领A-Z]{1}[A-Z]{1}[A-HJ-NP-Z0-9]{4}[A-HJ-NP-Z0-9挂学警港澳]{1})";
        return Pattern.matches(hphmEx2, plateNumber);
    }

    /**
     * @Author Eric
     * @Description 检查手机号是否有效
     * @Date 11:48 2018/12/28
     * @Param [phoneNumber]
     * @return boolean
     **/
    public static boolean checkPhoneNumber(String phoneNumber){
//        if(StringUtils.isBlank(phoneCheck))
//            phoneCheck = Phone_Check;
        Pattern regex = Pattern.compile(Phone_Check);
        Matcher matcher = regex.matcher(phoneNumber);
        return matcher.matches();
    }

    /**
     * @Author Eric
     * @Description 检查手机号是否有效
     * @Date 11:48 2018/12/28
     * @Param [phoneNumber]
     * @return boolean
     **/
    public static boolean checkPhoneNumber(String phoneNumber,String phoneCheck){
        if(StringUtils.isBlank(phoneCheck))
            phoneCheck = Phone_Check;
        Pattern regex = Pattern.compile(phoneCheck);
        Matcher matcher = regex.matcher(phoneNumber);
        return matcher.matches();
    }

    /**
     * @Author Eric
     * @Description 检查邮箱是否有效
     * @Date 11:50 2018/12/28
     * @Param [email]
     * @return boolean
     **/
    public static boolean checkEmail(String email){
        String check = "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        Pattern regex = Pattern.compile(check);
        Matcher matcher = regex.matcher(email);
        return matcher.matches();
    }


}
