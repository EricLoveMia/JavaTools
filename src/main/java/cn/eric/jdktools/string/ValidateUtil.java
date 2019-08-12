package cn.eric.jdktools.string;

import org.apache.commons.lang.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
     * @return java.lang.String
     * @Author Eric
     * @Description 返回字符串中的第一个识别出来的车牌号
     * @Date 11:42 2018/12/28
     * @Param [str]
     **/
    public static String getPlateNumberFromStr(String str) {
        // 车牌号为7位 如果小于7位
        if (StringUtils.isNotBlank(str) && str.length() >= 7) {
            for (int i = 0; i < str.length() - 6; i++) {
                String substring = str.substring(i, i + 7);
                if (checkPlateNumber(substring)) {
                    return substring;
                }
            }
        } else {
            return str;  // 如果为空 或者 小于7个就把所有的都返回
        }
        return str; // 如果没有找到，就返回全部
    }

    /**
     * @return boolean
     * @Author Eric
     * @Description 检查输入的字符串是不是车牌号
     * @Date 11:41 2018/12/28
     * @Param [plateNumber]
     **/
    public static boolean checkPlateNumber(String plateNumber) {
        // 正则表达式
        String hphmEx2 = "([京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领A-Z]{1}[A-Z]{1}(([0-9]{5}[DF])|([DF]([A-HJ-NP-Z0-9])[0-9]{4})))|([京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领A-Z]{1}[A-Z]{1}[A-HJ-NP-Z0-9]{4}[A-HJ-NP-Z0-9挂学警港澳]{1})";
        return Pattern.matches(hphmEx2, plateNumber);
    }

    /**
     * @return boolean
     * @Author Eric
     * @Description 检查手机号是否有效
     * @Date 11:48 2018/12/28
     * @Param [phoneNumber]
     **/
    public static boolean checkPhoneNumber(String phoneNumber) {
//        if(StringUtils.isBlank(phoneCheck))
//            phoneCheck = Phone_Check;
        Pattern regex = Pattern.compile(Phone_Check);
        Matcher matcher = regex.matcher(phoneNumber);
        return matcher.matches();
    }

    /**
     * @return boolean
     * @Author Eric
     * @Description 检查手机号是否有效
     * @Date 11:48 2018/12/28
     * @Param [phoneNumber]
     **/
    public static boolean checkPhoneNumber(String phoneNumber, String phoneCheck) {
        if (StringUtils.isBlank(phoneCheck)) phoneCheck = Phone_Check;
        Pattern regex = Pattern.compile(phoneCheck);
        Matcher matcher = regex.matcher(phoneNumber);
        return matcher.matches();
    }

    /**
     * @return boolean
     * @Author Eric
     * @Description 检查邮箱是否有效
     * @Date 11:50 2018/12/28
     * @Param [email]
     **/
    public static boolean checkEmail(String email) {
        String check = "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        Pattern regex = Pattern.compile(check);
        Matcher matcher = regex.matcher(email);
        return matcher.matches();
    }

    /**
     * 用户名是否符合规范   ^[\u4E00-\u9FA5A-Za-z0-9]{6,16}$，支持中文、大小写字母、数字随意组合，默认长度6~16字符
     *
     * @return
     */
    public static boolean isValidUsername(String username) {
        System.out.println("输入的用户名不合法，6到20位");
        return isValidUsername(username, 6, 20);
    }

    /**
     * 用户名是否符合规范   ^[\u4E00-\u9FA5A-Za-z0-9]{6,16}$
     *
     * @param username
     * @param min
     * @param max
     * @return
     */
    public static boolean isValidUsername(String username, Integer min, Integer max) {
        if (StringUtils.isBlank(username)) {
            return false;
        }
        String reg = "^[\u4E00-\u9FA5A-Za-z0-9]{" + min + "," + max + "}$";
        System.out.println("输入的用户名太短或太长");
        return username.matches(reg);
    }

    /**
     * 是否有效手机号码2
     *
     * @param mobileNum
     * @return
     */
    public static boolean isMobileNum(String mobileNum) {
        if (null == mobileNum) {
            return false;
        }
        System.out.println(mobileNum.matches("^((13[0-9])|(14[4,7])|(15[^4,\\D])|(17[0-9])|(18[0-9]))(\\d{8})$"));
        return mobileNum.matches("^((13[0-9])|(14[4,7])|(15[^4,\\D])|(17[0-9])|(18[0-9]))(\\d{8})$");
    }

    /**
     * 是否有效邮箱
     *
     * @param email
     * @return
     */
    public static boolean isEmail(String email) {
        if (null == email) {
            return false;
        }
        System.out.println(email.matches("^\\w+((-\\w+)|(\\.\\w+))*\\@[A-Za-z0-9]+((\\.|-)[A-Za-z0-9]+)*\\.[A-Za-z0-9]+$"));
        return email.matches("^\\w+((-\\w+)|(\\.\\w+))*\\@[A-Za-z0-9]+((\\.|-)[A-Za-z0-9]+)*\\.[A-Za-z0-9]+$");
    }

    /**
     * 是否是QQ邮箱
     */
    public static boolean isQQEmail(String email) {
        if (null == email) return false;
        System.out.println(email.matches("^[\\s\\S]*@qq.com$"));
        return email.matches("^[\\s\\S]*@qq.com$");
    }

    /**
     * 是否为16-22位银行账号
     *
     * @param bankAccount
     * @return
     */
    public static boolean isBankAccount(String bankAccount) {
        if (null == bankAccount) {
            return false;
        }
        System.out.println(bankAccount.matches("^\\d{16,22}$"));
        return bankAccount.matches("^\\d{16,22}$");
    }

    /**
     * 是否是纯数字，不含空格
     *
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        if (StringUtils.isBlank(str)) {
            return false;
        }
        int sz = str.length();
        for (int i = 0; i < sz; i++) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 是否数值类型，整数或小数
     *
     * @param str
     * @return
     */
    public static boolean isNumericalValue(String str) {
        if (null == str) {
            return false;
        }
        return str.matches("^[+-]?(([1-9]{1}\\d*)|([0]{1}))(\\.(\\d)+)?$");
    }

    /**
     * 是否整数(^[+-]?(([1-9]{1}\\d*)|([0]{1}))$)
     *
     * @param str
     * @return
     */
    public static boolean isNumericInt(String str) {
        if (str == null) {
            return false;
        }
        return str.matches("(^[+-]?([0-9]|([1-9][0-9]*))$)");
    }

    /**
     * 是否正整数
     *
     * @param number
     * @return
     */
    public static boolean isNumericPositiveInt(String number) {
        if (null == number) {
            return false;
        }
        return number.matches("^[+-]?(([1-9]{1}\\d*)|([0]{1}))$");
    }

    /**
     * 判断是否是正整数数或者一位小数
     *
     * @param str
     * @return
     */
    public static boolean isOneDouble(String str) {
//
        if (str == null) {
            return false;
        }
        return str.matches("^(\\d+\\.\\d{1,1}|\\d+)$");
    }

    /**
     * 判断是否是正整数数或者一位小数
     *
     * @param str
     * @return
     */
    public static boolean isTwoDouble(String str) {
//
        if (str == null) {
            return false;
        }
        return str.matches("^(\\d+\\.\\d{1,2}|\\d+)$");
    }

    /**
     * 判断给定字符串是否小于给定的值(min)
     *
     * @param str
     * @param min
     * @return
     */
    public static boolean isNumLess(String str, float min) {
        if (str == null) {
            return false;
        }
        if (!isNumeric(str)) {
            return false;
        }
        float val = Float.parseFloat(str);
        return (val < min);
    }

    /**
     * 判断给定的字符串大于所给定的值
     *
     * @param str
     * @param max
     * @return
     */
    public static boolean isNumMore(String str, float max) {
        if (str == null) {
            return false;
        }
        if (!isNumeric(str)) {
            return false;
        }
        float val = Float.parseFloat(str);
        return (val > max);
    }

    /**
     * 是否小数
     *
     * @param str
     * @return
     */
    public static boolean isNumericDouble(String str) {
        if (str == null) {
            return false;
        }
        return str.matches("^[+-]?(([1-9]\\d*\\.?\\d+)|(0{1}\\.\\d+)|0{1})");
    }

    /**
     * 是否是16进制颜色值
     *
     * @param str
     * @return
     */
    public static boolean isColor(String str) {
        if (str == null) {
            return false;
        }
        return str.matches("(^([0-9a-fA-F]{3}|[0-9a-fA-F]{6})$)");
    }

    /**
     * 判断是否是Boolean值
     *
     * @param str
     * @return
     */
    public static boolean isBoolean(String str) {
        if (str == null) {
            return false;
        }
        return str.equals("true") || str.equals("false");
    }

    /**
     * 判断是否是日期,格式：yyyy-MM-dd
     *
     * @param str
     * @return
     */
    public static boolean isDate(String str) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            format.parse(str);
        } catch (ParseException e) {
            return false;
        }
        return true;
    }

    /**
     * 判断是否是日期,格式：yyyy-MM-dd hh:mm:ss
     *
     * @param str
     * @return
     * @author huangyunsong
     * @createDate 2015年12月8日
     */
    public static boolean isDateyyyyMMddhhmmss(String str) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        try {
            format.parse(str);
        } catch (ParseException e) {
            return false;
        }
        return true;
    }


}
