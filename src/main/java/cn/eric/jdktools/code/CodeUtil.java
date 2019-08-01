/**
 * Copyright(c) Jade Techonologies Co., Ltd.
 */
package cn.eric.jdktools.code;

import java.util.Random;

/**
 * 随机产生数字和字母的字符串
 * @author Eric
 * @date 14:59 2019/8/1
 **/
public class CodeUtil
{
    private static String[] numArr = {"0","1","2","3","4","5","6","7","8","9"};
    private static String[] letArr = {"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z"};

    //new Random().nextInt(10)产生的是0-9的随机数

    /**
     * 产生六位随机数，每一位都可以是数字
     * @return
     */
    public static String createPolicyViewCode()
    {
        String viewCode = "";
        for(int i=1;i<=6;i++)
        {
            String code = numArr[new Random().nextInt(10)];
            viewCode+=code;
        }
        return viewCode;
    }

    public static String createTransCode()
    {
        String viewCode = "";
        for(int i=1;i<=6;i++)
        {
            String code = letArr[new Random().nextInt(26)];
            viewCode+=code;
        }
        return viewCode;
    }

    public static void main(String[] args)
    {
        System.out.println(createPolicyViewCode());
    }
}
