package cn.eric.jdktools.encrypt;

import cn.eric.jdktools.common.Config;
import org.apache.log4j.Logger;

import java.security.MessageDigest;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author BinCain
 */
public class MD5Util {

    private static Logger logger = Logger.getLogger(MD5Util.class);

    private MD5Util() {
    }

    public static String createSignUTF8(String departmentFlag,String requestType)
    {
        HashMap<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("departmentFlag", departmentFlag);
        parameters.put("requestType", requestType);
        String secretKey = Config.getConfInfo("secretKey");
        return createSign("UTF-8", parameters,"secretKey",secretKey);
    }

    public static String createSignUTF8(HashMap<String, Object> parameters, String secretKey, String secretValue) {
        return createSign("UTF-8", parameters, secretKey, secretValue);
    }

    /**
     * @method      createSign
     * @author      BinCain
     * @return      java.lang.String
     * @date        2018/1/2 0002 10:33
     * @Description: 注意 secretKey 的值是否符合判断key
     */
    public static String createSign(String characterEncoding, HashMap<String, Object> parameters, String secretKey, String secretValue) {
        StringBuffer sb = new StringBuffer();
        String sign = "";
        // 默认（ASCII）升序
        try {
            SortedMap<String, Object> sortedMap = new TreeMap<String, Object>(parameters);
            Set entrySet = sortedMap.entrySet();
            Iterator iterator = entrySet.iterator();

            while (iterator.hasNext()) {
                Map.Entry entry = (Map.Entry) iterator.next();
                String k = (String) entry.getKey();
                Object v = entry.getValue();
                if (null != v && !"".equals(v)
                        && !"sign".equals(k) && !"key".equals(k) && !"secretKey".equals(k)) {
                    sb.append(k + "=" + v + "&");
                }
            }
            sb.append(secretKey +"="+ secretValue);

            sign = MD5Util.encodeMD5(sb.toString(), characterEncoding).toUpperCase();
        } catch (Exception e) {
            logger.info(e.toString());
        }
        return sign;
    }


    public static String encodeMD5(String origin, String charsetname) {
        String resultString = null;
        try {
            resultString = new String(origin);
            MessageDigest md = MessageDigest.getInstance("MD5");
            if (charsetname == null || "".equals(charsetname))
                resultString = byteArrayToHexString(md.digest(resultString.getBytes()));
            else
                resultString = byteArrayToHexString(md.digest(resultString
                        .getBytes(charsetname)));
        } catch (Exception exception) {
        }
        return resultString;
    }

    private static String byteArrayToHexString(byte b[]) {
        StringBuffer resultSb = new StringBuffer();
        for (int i = 0; i < b.length; i++)
            resultSb.append(byteToHexString(b[i]));

        return resultSb.toString();
    }

    private static String byteToHexString(byte b) {
        int n = b;
        if (n < 0)
            n += 256;
        int d1 = n / 16;
        int d2 = n % 16;
        return hexDigits[d1] + hexDigits[d2];
    }

    private static final String hexDigits[] = {"0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};


    public static void main(String[] args) {
        //字典序排序
/*        String s1 = encodeMD5(Constant.SECRET_BASE_DATA, "utf-8");
        String s2 = encodeMD5(Constant.SECRET_BASE_DATA, "UTF-8");
        logger.info("s1: " + s1);
        logger.info("s2: " + s2);
        logger.info(s1.equals(null));*/


/*        HashMap<String,Object> parameters=new HashMap<String,Object>();
        parameters.put("serial_no", "100");
        parameters.put("business_name","1");
        parameters.put("total_fee", "482212");
        parameters.put("trading_status", "00");
        parameters.put("device_info", "11003409");
        parameters.put("trading_type", "1");
        parameters.put("trading_time", "1541212121");
        parameters.put("key", "8s4d8d4s");

        String charSet = "UTF-8";
        logger.info(createSign(charSet,parameters,"secret","5sd12sd45s1d"));*/

        HashMap<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("departmentFlag", "JS");
        parameters.put("requestType", "V110");
        String charSet = "UTF-8";
        logger.info(createSign(charSet, parameters, "secretKey", "L5S2J0B1X3G1S4"));

        String paramValue="雷克萨斯es250";
        logger.info(getChinese(paramValue));

        String idCard = "330324197702163959";
        logger.info(getGender(idCard));


    }

    public static String getChinese(String paramValue) {
        String regex = "([\u4e00-\u9fa5]+)";
        String str = "";
        Matcher matcher = Pattern.compile(regex).matcher(paramValue);
        while (matcher.find()) {
            str+= matcher.group(0);
        }
        return str;
    }

    private static String getGender(String idCard)
    {
        String sexNum = idCard.substring(idCard.length()-2,idCard.length()-1);
        Integer resultNum = Integer.valueOf(sexNum)%2;
        if (resultNum==1)
        {
            return "1";
        }
        else
        {
            return "2";
        }
    }
}
