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
}
