package cn.eric.jdktools.date;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateTimeUtil {
    private static final Logger log = LoggerFactory.getLogger(DateTimeUtil.class);

    public static final String EN_YYYY_MM_DD = "yyyy-MM-dd";
    public static final String EN_YYYY_MM_DD_HHMMSS = "yyyy-MM-dd HH:mm:ss";
    public static final String EN_YYYY_MM_DD_HHMMSS_SLASH = "yyyy/MM/dd HH:mm:ss";
    public static final String CH_YYYY_MM_DD_HHMMSS = "yyyy年MM月dd日 HH:mm:ss";
    public static final String YYYY_MM_DD_HHMMSS = "yyyyMMddHHmmss";
    public static final String CH_YYYY_MM_DD = "yyyy年MM月dd日";
    public static final String YYYYMMDD = "yyyyMMdd";
    public static final String YYYY_MM_DD_SLASH = "yyyy/MM/dd";
    public static final String YYYY_MM_DD_HHMM = "yyyyMMddHHmm";
    public static final String HH24MISS = "HHmmss";
    public static final String HH24MISS_HAS_SPLIT = "HH:mm:ss";
    public static final String EN_YYYY_MM = "yyyy-MM";
    public static final String CH_YYYY_MM = "yyyy年MM月";

    /**
     * 返回当前日期+时间
     *
     * @return Date 返回该日期
     */
    public final static Date getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        return calendar.getTime();
    }

    /**
     * 返回当前日期+时间
     *
     * @return Date 返回该日期
     */
    public final static String getCurrentDate(String pattern) {
        Calendar calendar = Calendar.getInstance();
        if (StringUtils.isEmpty(pattern)) {
            pattern = EN_YYYY_MM_DD_HHMMSS;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(calendar.getTime());
    }

    public static String formatCstTime(String cstDateStr) {
        if (StringUtils.isBlank(cstDateStr)) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
        Date date = null;
        try {
            date = (Date) sdf.parse(cstDateStr);
        } catch (ParseException e) {
            log.error("", e);
            return null;
        }
        String formatStr = new SimpleDateFormat("yyyy-MM-dd").format(date);
        return formatStr;
    }

    public static String formatComplexDate(String dateStr) {
        if (StringUtils.isBlank(dateStr)) {
            return "";
        }
        if (dateStr.length() == 10) {
            return dateStr;
        }
        if (dateStr.length() == 28) {
            return DateTimeUtil.formatCstTimeString(dateStr);
        }
        try {
            return DateTimeUtil.formatDateString(DateTimeUtil.parse(dateStr));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateStr;
    }

    public static String formatCstTimeString(String cstDateStr) {
        if (StringUtils.isBlank(cstDateStr)) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
        Date date = null;
        try {
            date = (Date) sdf.parse(cstDateStr);
        } catch (ParseException e) {
            log.error("", e);
            return null;
        }
        String formatStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
        return formatStr;
    }

    public static String format2Millisecond(Date date) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS").format(date);
    }

    public static Date parse(String time) throws ParseException {
        if (StringUtils.isEmpty(time)) {
            return null;
        }
        if (8 == time.length()) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
            formatter.setLenient(false);
            Date newDate = formatter.parse(time);
            formatter = new SimpleDateFormat("yyyy-MM-dd");
            time = formatter.format(newDate);
        }
        if (10 == time.length()) {
            time += " 00:00:00";
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = simpleDateFormat.parse(time);
        return date;
    }

    public static Date parseDate(String date) throws ParseException {
        if (8 == date.length()) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
            formatter.setLenient(false);
            Date newDate = formatter.parse(date);
            formatter = new SimpleDateFormat("yyyy-MM-dd");
            date = formatter.format(newDate);
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date d = simpleDateFormat.parse(date);
        return d;
    }

    public static Date parseAndGetNowIfFailed(String time) {
        try {
            return parse(time);
        } catch (ParseException e) {
        }
        return Calendar.getInstance().getTime();
    }

    public static String formatTimestampIgnoreException(Date time) {
        try {
            return formatTimestamp(time);
        } catch (ParseException e) {
            throw new IllegalArgumentException("date.getTime()=" + time.getTime());
        }
    }


    public static String formatTimestamp(Date time) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.format(time);
    }

    public static String formatDateIgnoreException(Date time) {
        try {
            return formatDate(time);
        } catch (ParseException e) {
            throw new IllegalArgumentException("date.getTime()=" + time.getTime());
        }
    }

    public static String formatDate(Date time) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return simpleDateFormat.format(time);
    }

    public static String formatDateString(Date time) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.format(time);
    }

    public static long getInterval(Date left, Date right) {
        return left.getTime() - right.getTime();
    }
}
