/**
 * Copyright(c) Jade Techonologies Co., Ltd.
 */
package cn.eric.jdktools.data;

import java.math.BigDecimal;

/**
 * 格式化数字工具类
 */
public class NumUtil
{
    /**
     * 保留两位小数点
     * @param value
     * @return
     */
    public static double keepTwoPoint(double value)
    {
        BigDecimal b = new BigDecimal(value);
        double  result = b.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
        return result;
    }

    public static double keepFourPoint(double value)
    {
        BigDecimal b = new BigDecimal(value);
        double  result = b.setScale(4,BigDecimal.ROUND_HALF_UP).doubleValue();
        return result;
    }

    public static double keepSixPoint(double value)
    {
        BigDecimal b = new BigDecimal(value);
        double  result = b.setScale(6,BigDecimal.ROUND_HALF_UP).doubleValue();
        return result;
    }

    /**
     * 从命令行接收一个数，在其中调用 checkNum() 方法对其进行
     * 验证，并返回相应的值
     * @return 如果输入合法，返回输入的这个数
     */
    public static String getNum(String loanmoney) {



        // 判断用户输入是否合法
        // 若合法，返回这个值；若非法返回 "0"
        if(checkNum(loanmoney)) {
            return loanmoney;
        } else {
            return "";
        }
    }

    /**
     * 判断用户输入的数据是否合法，用户只能输入大于零的数字，不能输入其它字符
     * @param s String
     * @return 如果用户输入数据合法，返回 true，否则返回 false
     */
    private static boolean checkNum(String loanmoney) {
        // 如果用户输入的数里有非数字字符，则视为非法数据，返回 false
        try {
            float f = Float.valueOf(loanmoney);
            // 如果这个数小于零则视为非法数据，返回 false
            if(f < 0) {
                System.out.println("非法数据，请检查！");
                return false;
            }else {
                return true;
            }
        } catch (NumberFormatException s) {
            System.out.println("非法数据，请检查！");
            return false;
        }
    }

    /**
     * 把用户输入的数以小数点为界分割开来，并调用 numFormat() 方法
     * 进行相应的中文金额大写形式的转换
     * 注：传入的这个数应该是经过 roundString() 方法进行了四舍五入操作的
     * @param s String
     * @return 转换好的中文金额大写形式的字符串
     */
    public static String splitNum(String loanmoney) {
        // 如果传入的是空串则继续返回空串
        if("".equals(loanmoney)) {
            return "";
        }
        // 以小数点为界分割这个字符串
        int index = loanmoney.indexOf(".");
        // 截取并转换这个数的整数部分
        String intOnly = loanmoney.substring(0, index);
        String part1 = numFormat(1, intOnly);
        // 截取并转换这个数的小数部分
        String smallOnly = loanmoney.substring(index + 1);
        String part2 = numFormat(2, smallOnly);
        // 把转换好了的整数部分和小数部分重新拼凑一个新的字符串
        String newS = part1 + part2;
        return newS;
    }

    /**
     * 对传入的数进行四舍五入操作
     * @param loanmoney 从命令行输入的那个数
     * @return 四舍五入后的新值
     */
    public static String roundString(String loanmoney) {
        // 如果传入的是空串则继续返回空串
        if("".equals(loanmoney)) {
            return "";
        }
        // 将这个数转换成 double 类型，并对其进行四舍五入操作
        double d = Double.parseDouble(loanmoney);
        // 此操作作用在小数点后两位上
        d = (d * 100 + 0.5) / 100;
        // 将 d 进行格式化
        loanmoney = new java.text.DecimalFormat("##0.000").format(d);
        // 以小数点为界分割这个字符串
        int index = loanmoney.indexOf(".");
        // 这个数的整数部分
        String intOnly = loanmoney.substring(0, index);
        // 规定数值的最大长度只能到万亿单位，否则返回 "0"
        if(intOnly.length() > 13) {
            System.out.println("输入数据过大！（整数部分最多13位！）");
            return "";
        }
        // 这个数的小数部分
        String smallOnly = loanmoney.substring(index + 1);
        // 如果小数部分大于两位，只截取小数点后两位
        if(smallOnly.length() > 2) {
            String roundSmall = smallOnly.substring(0, 2);
            // 把整数部分和新截取的小数部分重新拼凑这个字符串
            loanmoney = intOnly + "." + roundSmall;
        }
        return loanmoney;
    }

    /**
     * 把传入的数转换为中文金额大写形式
     * @param flag int 标志位，1 表示转换整数部分，2 表示转换小数部分
     * @param s String 要转换的字符串
     * @return 转换好的带单位的中文金额大写形式
     */
    private static String numFormat(int flag, String loanmoney) {
        int sLength = loanmoney.length();
        // 货币大写形式
        String bigLetter[] = {"零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖"};
        // 货币单位
        String unit[] = {"元", "拾", "佰", "仟", "万",
                // 拾万位到仟万位
                "拾", "佰", "仟",
                // 亿位到万亿位
                "亿", "拾", "佰", "仟", "万"};
        String small[] = {"分", "角"};
        // 用来存放转换后的新字符串
        String newS = "";
        // 逐位替换为中文大写形式
        for(int i = 0; i < sLength; i ++) {
            if(flag == 1) {
                // 转换整数部分为中文大写形式（带单位）
                newS = newS + bigLetter[loanmoney.charAt(i) - 48] + unit[sLength - i - 1];
            } else if(flag == 2) {
                // 转换小数部分（带单位）
                newS = newS + bigLetter[loanmoney.charAt(i) - 48] + small[sLength - i - 1];
            }
        }
        return newS;
    }

    /**
     * 把已经转换好的中文金额大写形式加以改进，清理这个字
     * 符串里面多余的零，让这个字符串变得更加可观
     * 注：传入的这个数应该是经过 splitNum() 方法进行处理，这个字
     * 符串应该已经是用中文金额大写形式表示的
     * @param s String 已经转换好的字符串
     * @return 改进后的字符串
     */
    public static String cleanZero(String loanmoney) {
        // 如果传入的是空串则继续返回空串
        if("".equals(loanmoney)) {
            return "";
        }
        // 如果用户开始输入了很多 0 去掉字符串前面多余的'零'，使其看上去更符合习惯
        while(loanmoney.charAt(0) == '零') {
            // 将字符串中的 "零" 和它对应的单位去掉
            loanmoney = loanmoney.substring(2);
            // 如果用户当初输入的时候只输入了 0，则只返回一个 "零"
            if(loanmoney.length() == 0) {
                return "零";
            }
        }
        // 字符串中存在多个'零'在一起的时候只读出一个'零'，并省略多余的单位
        /* 由于本人对算法的研究太菜了，只能用4个正则表达式去转换了，各位大虾别介意哈... */
        String regex1[] = {"零仟", "零佰", "零拾"};
        String regex2[] = {"零亿", "零万", "零元"};
        String regex3[] = {"亿", "万", "元"};
        String regex4[] = {"零角", "零分"};
        // 第一轮转换把 "零仟", 零佰","零拾"等字符串替换成一个"零"
        for(int i = 0; i < 3; i ++) {
            loanmoney = loanmoney.replaceAll(regex1[i], "零");
        }
        // 第二轮转换考虑 "零亿","零万","零元"等情况
        // "亿","万","元"这些单位有些情况是不能省的，需要保留下来
        for(int i = 0; i < 3; i ++) {
            // 当第一轮转换过后有可能有很多个零叠在一起
            // 要把很多个重复的零变成一个零
            loanmoney = loanmoney.replaceAll("零零零", "零");
            loanmoney = loanmoney.replaceAll("零零", "零");
            loanmoney = loanmoney.replaceAll(regex2[i], regex3[i]);
        }
        // 第三轮转换把"零角","零分"字符串省略
        for(int i = 0; i < 2; i ++) {
            loanmoney = loanmoney.replaceAll(regex4[i], "");
        }
        // 当"万"到"亿"之间全部是"零"的时候，忽略"亿万"单位，只保留一个"亿"
        loanmoney = loanmoney.replaceAll("亿万", "亿");
        return loanmoney;
    }

    /**
     * 测试程序的可行性
     * @param args
     */
    public static void main(String[] args) {
        System.out.println("\n--------将数字转换成中文金额的大写形式------------\n");
        NumUtil t2r = new NumUtil();
        String money= getNum("bbb600ttt98726a");
        System.out.println(money);

        String money1= splitNum("17800260026.26");
        System.out.println(money1);

        String money2 = roundString("3027830056.34426");
        System.out.println(money2);

        String money3 = numFormat(1, "37356653");
        System.out.println(money3);

        String money4 = numFormat(2, "34");
        System.out.println(money4);

        String money5 = cleanZero("零零零零零零壹佰柒拾捌万贰仟陆佰贰拾陆元贰角陆分");
        System.out.println(money5);

    }

}
