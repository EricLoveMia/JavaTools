/*
 * -------------------------------------------------------
 * @FileName ExceptionUtil.java
 * @Description 异常处理工具类
 * @Author 00294476
 * @Copyright www.want-want.com Ltd. All rights reserved.
 * 注意：本内容仅限于旺旺集团内部传阅，禁止外泄以及用于其他商业目的
 * -------------------------------------------------------
 */
package cn.eric.jdktools.exception;

/**
 * @description 异常处理工具类
 * @author eric
 * @version V1.0.0
 */
public final class ExceptionUtil {

	/**
	 * <获取异常信息栈里的具体信息>
	 * 
	 * @param e
	 * @return
	 */
	public static String getTrace(Exception e) {

		StringBuffer stringBuffer = new StringBuffer(e.toString() + "\n");
		StackTraceElement[] messages = e.getStackTrace();
		int length = messages.length;
		for (int i = 0; i < length; i++) {
			stringBuffer.append("\t" + messages[i].toString() + "\n");
		}
		return stringBuffer.toString();
	}

}
