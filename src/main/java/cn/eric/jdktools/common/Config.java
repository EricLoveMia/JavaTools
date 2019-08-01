package cn.eric.jdktools.common;

import org.apache.log4j.Logger;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Calendar;
import java.util.Properties;

@Component
public class Config {
    static Logger logger = Logger.getLogger(Config.class);
	private static Properties conf = new Properties();
	
	/**
	 * 根据状态得到对应的图标路径
	 * @param pro_key
	 * @return
	 */
	public static String getConfInfo(String pro_key){
		try {
			conf = PropertiesLoaderUtils.loadAllProperties("redis.properties");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return conf.getProperty(pro_key);
	}
	
	public static void main(String[] args) {
//		logger.info(getIconSrc("stay"));
//		logger.info(getColor("OCC"));
		String t1 = "00:00";
		String t2 = "11:11";
		String t3 = "12:12";
		String t4 = "02:12";
		logger.info(t2.compareTo(t1));
		logger.info(t3.compareTo(t2));
		logger.info(t1.compareTo(t3));
		Calendar cal = Calendar.getInstance();  
		int curWeek = cal.get(Calendar.DAY_OF_WEEK)-1;
		logger.info(curWeek);
//		logger.info(isOpenSystem());
	}
	
}
