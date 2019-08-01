
package cn.eric.jdktools.code;

import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

/* *
 *类名：OrderNoUtil
 *功能：生成订单号
 *详细：工具类，可以用作获取系统日期、订单编号等
 */

public class OrderNoUtil {
	private static Logger logger = Logger.getLogger(OrderNoUtil.class);
	
	/** 年月日时分秒(无下划线) yyyyMMddHHmmssSSS */
	public static final String dt                  = "yyyyMMddHHmmssSSS";
    /** 年月日时分秒(无下划线) yyyyMMddHHmmss */
    public static final String dtLong                  = "yyyyMMddHHmmss";
    
    /** 完整时间 yyyy-MM-dd HH:mm:ss */
    public static final String simple                  = "yyyy-MM-dd HH:mm:ss";
    
    /** 年月日(无下划线) yyyyMMdd */
    public static final String dtShort                 = "yyyyMMdd";

	public static final String normalFormat = "yyyy-MM-dd";
	public static final SimpleDateFormat normalSF = new SimpleDateFormat(normalFormat);

    /**
     * 返回系统当前时间(精确到毫秒),作为一个唯一的订单编号
     * @return
     *      以yyyyMMddHHmmss为格式的当前系统时间
     */
	public  static String getOrderNum(){
		Date date=new Date();
		DateFormat df=new SimpleDateFormat(dt);
		return df.format(date);
	}
	/**
	 * 生成订单编号
     * 返回系统当前时间(精确到毫秒)+6位随机数,作为一个唯一的订单编号
     * @return
     *      以yyyyMMddHHmmss为格式的当前系统时间
     */
	public  static String getOrderNumNew(){
		return getSuffix("I");
	}
	//预支付交易流水号
	public  static String getTradeCode(){
		return getSuffix("TR");
	}
	public static String getPolicyNumNew()  {return getSuffix("P");}
	public static String getAccountName()  {return getSuffix("A");}
	public static String getAccName()  {return getSuffix("AC");}
	public static String getTrOrderNo() {return getSuffix("TR");}

	/**
	 * 活力商场 卡劵 接口
	 */
	public static String coupon(){
		return getSuffix("COUPON");
	}

	/**
	 * 活力商场 卡劵批次导入 接口
	 */
	public static String couponImportBatch(){
		return getSuffix("CIB");
	}

	/**
	 * 活力商场 卡劵批次导入 接口
	 */
	public static String cltTipImportBatch(){
		return getSuffix("CTBATCh");
	}

	//最新前缀
	private static String currPrefix = "";
	//最新前缀，生成过的后缀列表
	private static String oldSuffix = "";
	private static DateFormat df=new SimpleDateFormat("yyMMddHHmmssSSS");
	private static DecimalFormat decF = new DecimalFormat("00");
	private static Random rad=new Random();


	/**
	 * 返回一个唯唯一号
	 * @param tag
	 * @return
	 */
	public synchronized static String getSuffix(String tag) {
		String prefix = tag + df.format(new Date());// 前缀
		String suffix = decF.format(rad.nextInt(100));// 后缀
		if(prefix.equals(currPrefix)){//同一毫秒内
			if(oldSuffix.indexOf(suffix)>-1){//已经分配过
				try {
					Thread.sleep(1);//延时一毫秒
				} catch (Exception e) {
					logger.info("getSuffix",e);
				}
				prefix = tag + df.format(new Date());// 前缀
				currPrefix = prefix;//更改当前毫秒值
				suffix = decF.format(rad.nextInt(100));// 后缀
				oldSuffix="";//清空历史 后缀列表
				oldSuffix += ","+suffix;
				return prefix+suffix;
			}else{//生成的可用
				oldSuffix += ","+suffix;//添加到后缀列表中
				return prefix+suffix;
			}
		}else{
			currPrefix = prefix;//更改当前毫秒值
			return prefix+suffix;
		}
	}

	public static int getDisHours(Date fromDate,Date toDate)
	{
		long from = fromDate.getTime();
		long to = toDate.getTime();
		int hours = (int) ((to - from)/(1000 * 60 * 60));
		return hours;
	}

	/**
	 * 获取以当前为基础时间，count需要添加的月数，dateNum为每一个月固定的日期
	 * @param count
	 * @param dateNum
	 * @return
	 * @throws Exception
	 */
	public static final Date getFixMonthDay(int count,int dateNum) throws Exception
	{
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		c.add(Calendar.MONTH, 0);
		c.set(Calendar.DAY_OF_MONTH, 1);//设置为1号,当前日期既为本月第一天
		Date date = c.getTime();
		date = DateUtils.addMonths(date,count);
		date = DateUtils.addDays(date,dateNum-1);
		date = format.parse(format.format(date));
		return date;
	}

	public static String getNormalFormat(Date date)
	{
		return normalSF.format(date);
	}

	/**
	 * 获取当前日期，已yyyy-MM-dd格式返回
	 * @return
	 * @throws Exception
	 */
	public static final Date getCurrentDayDate() throws Exception
	{
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date now = new Date();
		return format.parse(format.format(now));
	}

}
