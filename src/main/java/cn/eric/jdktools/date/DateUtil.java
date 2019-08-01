package cn.eric.jdktools.date;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

/**
 * @author Eric
 * @version 1.0
 * @ClassName: DateUtil
 * @Description: 线程安全的日期工具类
 * @company lsj
 * @date 2018/11/19 16:36
 **/
public class DateUtil {

    public static String PATTERN_YYYYMM = "yyyyMM";
    public static String PATTERN_YYYY_MM_DD = "yyyy-MM-dd";
    public static String PATTERN_YYYYMMDDHHMMSS = "yyyyMMddHHmmss";
    public static String PATTERN_YYYY_MM_DD_HHMMSS = "yyyy-MM-dd HH:mm:ss";
    public static String PATTERN_YYYY_MM_DD_HH = "yyyy-MM-dd HH";
    public static String PATTERN_YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";
    public static String PATTERN_YYYYMMDD = "yyyyMMdd";
    public static String PATTERN_YYYYMMDDHHMMSSSSS = "yyyyMMddHHmmssSSS";
    public static String PATTERN_HTTP = "EEE, dd MMM yyyy HH:mm:ss zzz";
    public static String PATTERN_YYYYMMDD_CHINESE = "yyyy年MM月dd日";
    public static String PATTERN_YYYYMMDD_HHMMSS_CHINESE = "yyyy年MM月dd日 HH:mm:ss";
    public static String PATTERN_YYYYMMDD_SLASH = "yyyy/MM/dd";
    public static String PATTERN_YYYYMMDD_HHMMSS_SLASH = "yyyy/MM/dd  HH:mm:ss";
    public static String PATTERN_YYYY_MM_DD_00 = "yyyy-MM-dd 00:00:00";

    public static String UTC_PATTERN_YYYY_MM_DD_T_HH_MM_SS_Z = "yyyy-MM-dd'T'HH:mm:ss'Z'";


    /**
     * 锁对象
     */
    private static final Object lockObj = new Object();

    /**
     * 存放不同的日期模板格式的sdf的Map
     */
    private static Map<String, ThreadLocal<SimpleDateFormat>> sdfMap = new HashMap<String, ThreadLocal<SimpleDateFormat>>();

    /**
     * 返回一个ThreadLocal的sdf,每个线程只会new一次sdf
     *
     * @param pattern
     * @return
     */
    private static SimpleDateFormat getSdf(final String pattern) {
        ThreadLocal<SimpleDateFormat> tl = sdfMap.get(pattern);

        // 此处的双重判断和同步是为了防止sdfMap这个单例被多次put重复的sdf
        if (tl == null) {
            synchronized (lockObj) {
                tl = sdfMap.get(pattern);
                if (tl == null) {
                    // 只有Map中还没有这个pattern的sdf才会生成新的sdf并放入map
                    System.out.println("put new sdf of pattern " + pattern + " to map");

                    // 这里是关键,使用ThreadLocal<SimpleDateFormat>替代原来直接new SimpleDateFormat
                    tl = new ThreadLocal<SimpleDateFormat>() {

                        @Override
                        protected SimpleDateFormat initialValue() {
                            //System.out.println("thread: " + Thread.currentThread() + " init pattern: " + pattern);
                            return new SimpleDateFormat(pattern);
                        }
                    };
                    sdfMap.put(pattern, tl);
                }
            }
        }

        return tl.get();
    }

    /**
     * 是用ThreadLocal<SimpleDateFormat>来获取SimpleDateFormat,这样每个线程只会有一个SimpleDateFormat
     * 时间格式化
     *
     * @param date
     * @param pattern
     * @return
     */
    public static String format(Date date, String pattern) {
        return getSdf(pattern).format(date);
    }

    /**
     * 时间反格式化
     *
     * @param dateStr
     * @param pattern
     * @return
     * @throws ParseException
     */
    public static Date parse(String dateStr, String pattern) {
        Date date = null;
        try {
            date = getSdf(pattern).parse(dateStr);
        } catch (Exception e) {

        }
        return date;
    }

    /**
     * 本地时间转 UTC 时间字符串
     *
     * @param date
     * @return
     */
    public static String localToUtcString(Date date, String pattern) {
        SimpleDateFormat sdf = getSdf(pattern);
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        return sdf.format(date);
    }

    /**
     * UTC 时间反格式化
     *
     * @param date
     * @param pattern
     * @return
     */
    public static Date utcStringToUtcDate(String date, String pattern) {
        SimpleDateFormat sdf = getSdf(pattern);
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date utcDate = null;
        try {
            utcDate = sdf.parse(date);
        } catch (Exception e) {

        }
        return utcDate;
    }

    /**
     * UTC 时间格式化
     *
     * @param date
     * @return
     */
    public static String utcDateToUtcString(Date date) {
        SimpleDateFormat sdf = getSdf("yyyy-MM-dd'T'HH:mm:ss'Z'");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        return sdf.format(date);
    }


    /**
     * UTC 时间字符串转本地时间
     *
     * @param date
     * @param pattern
     * @return
     */
    public static Date utcStringToLocalDate(String date, String pattern) {
        SimpleDateFormat sdf = getSdf(pattern);
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date localDate = null;
        try {
            localDate = sdf.parse(date);
        } catch (Exception e) {

        }
        return localDate;
    }

    /**
     * 北京时间转本地时间
     * @author Eric
     * @date 14:47 2019/3/13
     * @params date
     * @params pattern
     * @throws
     * @return java.util.Date
     **/
    public static Date gMT8StringToLocalDate(String date, String pattern) {
        SimpleDateFormat sdf = getSdf(pattern);
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        Date localDate = null;
        try {
            localDate = sdf.parse(date);
        } catch (Exception e) {

        }
        return localDate;
    }


    public static void main(String[] args) throws Exception {
        String s = localToUtcString(new Date(), UTC_PATTERN_YYYY_MM_DD_T_HH_MM_SS_Z);
        System.err.println(s);

        Date date = utcStringToLocalDate(s, UTC_PATTERN_YYYY_MM_DD_T_HH_MM_SS_Z);

        System.err.println(format(date, PATTERN_YYYY_MM_DD_HHMMSS));

        System.err.println("--------------------------------");
        String da = localToUtcString(new Date(), UTC_PATTERN_YYYY_MM_DD_T_HH_MM_SS_Z);
        System.err.println(da);
        Date utcDate = utcStringToUtcDate(da, UTC_PATTERN_YYYY_MM_DD_T_HH_MM_SS_Z);
        System.err.println(utcDateToUtcString(utcDate));


        Date date1 = gMT8StringToLocalDate("2019-03-13 14:49:00", DateUtil.PATTERN_YYYY_MM_DD_HHMMSS);
        System.out.println(date1);

    }


}