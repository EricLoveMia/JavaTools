/*
 * -------------------------------------------------------
 * @FileName CookieUtil.java
 * @Description cookie工具类代码
 * @Author 00294476
 * @Copyright www.want-want.com Ltd. All rights reserved.
 * 注意：本内容仅限于旺旺集团内部传阅，禁止外泄以及用于其他商业目的
 * -------------------------------------------------------
 */
package cn.eric.jdktools.http;

import cn.eric.jdktools.exception.ExceptionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.regex.Pattern;

/**
 * @description cookie工具类
 * @author 00294476
 * @version V1.0.0
 */
public final class CookieUtil {

	protected static final Logger LOGGER = LoggerFactory.getLogger(CookieUtil.class);

	/**
	 * <得到Cookie的值，不解码>
	 * 
	 * @param request
	 * @param cookieName
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String getCookieValue(HttpServletRequest request, String cookieName)
			throws UnsupportedEncodingException {
		return getCookieValue(request, cookieName, false);
	}

	/**
	 * <得到Cookie的值，并以UTF-8的方式解码>
	 * 
	 * @param request
	 * @param cookieName
	 * @param isDecoder
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String getCookieValue(HttpServletRequest request, String cookieName, boolean isDecode)
			throws UnsupportedEncodingException {
		Cookie[] cookieList = request.getCookies();
		if (cookieList == null || cookieName == null) {
			return null;
		}
		String retValue = null;
		try {
			for (int i = 0; i < cookieList.length; i++) {
				if (cookieList[i].getName().equals(cookieName)) {
					if (isDecode) {
						retValue = URLDecoder.decode(cookieList[i].getValue(), "UTF-8");
					} else {
						retValue = cookieList[i].getValue();
					}
					break;
				}
			}
		} catch (UnsupportedEncodingException e) {
			LOGGER.error("Cookie Decode Error.", e);
			throw new UnsupportedEncodingException(ExceptionUtil.getTrace(e));
		}
		return retValue;
	}

	/**
	 * <得到Cookie的值，以自定义的方式解码>
	 * 
	 * @param request
	 * @param cookieName
	 * @param encodeString
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String getCookieValue(HttpServletRequest request, String cookieName, String encodeString)
			throws UnsupportedEncodingException {
		Cookie[] cookieList = request.getCookies();
		if (cookieList == null || cookieName == null) {
			return null;
		}
		String retValue = null;
		try {
			for (int i = 0; i < cookieList.length; i++) {
				if (cookieList[i].getName().equals(cookieName)) {
					retValue = URLDecoder.decode(cookieList[i].getValue(), encodeString);
					break;
				}
			}
		} catch (UnsupportedEncodingException e) {
			LOGGER.error("Cookie Decode Error.", e);
			throw new UnsupportedEncodingException(ExceptionUtil.getTrace(e));
		}
		return retValue;
	}

	/**
	 * <设置Cookie的值，不设置生效时间默认浏览器关闭即失效，也不编码>
	 * 
	 * @param request
	 * @param response
	 * @param cookieName
	 * @param cookieValue
	 * @throws Exception
	 */
	public static void setCookie(HttpServletRequest request, HttpServletResponse response, String cookieName,
			String cookieValue) throws Exception {
		setCookie(request, response, cookieName, cookieValue, -1);
	}

	/**
	 * <设置Cookie的值，在指定时间内生效，但不编码>
	 * 
	 * @param request
	 * @param response
	 * @param cookieName
	 * @param cookieValue
	 * @param cookieMaxage
	 * @throws Exception
	 */
	public static void setCookie(HttpServletRequest request, HttpServletResponse response, String cookieName,
			String cookieValue, int cookieMaxage) throws Exception {
		setCookie(request, response, cookieName, cookieValue, cookieMaxage, false);
	}

	/**
	 * <设置Cookie的值，不设置生效时间，UTF-8编码>
	 * 
	 * @param request
	 * @param response
	 * @param cookieName
	 * @param cookieValue
	 * @param isEncode
	 * @throws Exception
	 */
	public static void setCookie(HttpServletRequest request, HttpServletResponse response, String cookieName,
			String cookieValue, boolean isEncode) throws Exception {
		setCookie(request, response, cookieName, cookieValue, -1, isEncode);
	}

	/**
	 * <设置Cookie的值，在指定时间内生效，UTF-8编码>
	 * 
	 * @param request
	 * @param response
	 * @param cookieName
	 * @param cookieValue
	 * @param cookieMaxage
	 * @param isEncode
	 * @throws Exception
	 */
	public static void setCookie(HttpServletRequest request, HttpServletResponse response, String cookieName,
			String cookieValue, int cookieMaxage, boolean isEncode) throws Exception {
		doSetCookie(request, response, cookieName, cookieValue, cookieMaxage, isEncode);
	}

	/**
	 * <设置Cookie的值，在指定时间内生效，自定义编码参数>
	 * 
	 * @param request
	 * @param response
	 * @param cookieName
	 * @param cookieValue
	 * @param cookieMaxage
	 * @param encodeString
	 * @throws Exception
	 */
	public static void setCookie(HttpServletRequest request, HttpServletResponse response, String cookieName,
			String cookieValue, int cookieMaxage, String encodeString) throws Exception {
		doSetCookie(request, response, cookieName, cookieValue, cookieMaxage, encodeString);
	}

	/**
	 * <删除Cookie带cookie域名>
	 * 
	 * @param request
	 * @param response
	 * @param cookieName
	 * @throws Exception
	 */
	public static void deleteCookie(HttpServletRequest request, HttpServletResponse response, String cookieName)
			throws Exception {
		doSetCookie(request, response, cookieName, "", 0, false);
	}

	/**
	 * <设置Cookie的值，并使其在指定时间内生效，UTF-8编码>
	 * 
	 * @param request
	 * @param response
	 * @param cookieName
	 * @param cookieValue
	 * @param cookieMaxage
	 * @param isEncode
	 * @throws Exception
	 */
	private static final void doSetCookie(HttpServletRequest request, HttpServletResponse response, String cookieName,
			String cookieValue, int cookieMaxage, boolean isEncode) throws Exception {
		try {
			if (cookieValue == null) {
				cookieValue = "";
			} else if (isEncode) {
				cookieValue = URLEncoder.encode(cookieValue, "UTF-8");
			}
			Cookie cookie = new Cookie(cookieName, cookieValue);
			if (cookieMaxage > 0)
				cookie.setMaxAge(cookieMaxage);
			if (null != request)
				// 设置域名的cookie
				cookie.setDomain(getDomainName(request));
			cookie.setPath("/");
			response.addCookie(cookie);
		} catch (Exception e) {
			LOGGER.error("Cookie Encode Error.", e);
			throw new Exception(ExceptionUtil.getTrace(e));
		}
	}

	/**
	 * <设置Cookie的值，并使其在指定时间内生效，自定义编码>
	 * 
	 * @param request
	 * @param response
	 * @param cookieName
	 * @param cookieValue
	 * @param cookieMaxage
	 * @param encodeString
	 * @throws Exception
	 */
	private static final void doSetCookie(HttpServletRequest request, HttpServletResponse response, String cookieName,
			String cookieValue, int cookieMaxage, String encodeString) throws Exception {
		try {
			if (cookieValue == null) {
				cookieValue = "";
			} else {
				cookieValue = URLEncoder.encode(cookieValue, encodeString);
			}
			Cookie cookie = new Cookie(cookieName, cookieValue);
			if (cookieMaxage > 0)
				cookie.setMaxAge(cookieMaxage);
			if (null != request)
				// 设置域名的cookie
				cookie.setDomain(getDomainName(request));
			cookie.setPath("/");
			response.addCookie(cookie);
		} catch (Exception e) {
			LOGGER.error("Cookie Encode Error.", e);
			throw new Exception(ExceptionUtil.getTrace(e));
		}
	}

	/**
	 * <得到cookie的域>
	 * 
	 * @param request
	 * @return
	 * @throws URISyntaxException
	 */
	private static final String getDomainName(HttpServletRequest request) throws URISyntaxException {
		String domainName = null;
		String serverName = request.getRequestURL().toString();
		String serverName1 = request.getServerName();
		int serverPort = request.getServerPort();
		if (serverName == null || serverName.equals("")) {
			domainName = "";
		} else {
			URI uri = new URI(serverName);
			String host = uri.getHost();
			if (Pattern.compile("(?i)[a-z]").matcher(host).find()) {
				// 如果是包含字母，是域名
				final String[] domains = host.split("\\.");
				int len = domains.length;
				if (len > 3) {
					domainName = domains[len - 3] + "." + domains[len - 2] + "." + domains[len - 1];
				} else if (len <= 3 && len > 1) {
					domainName = domains[len - 2] + "." + domains[len - 1];
				} else {
					domainName = host;
				}
			} else {
				domainName = host;
			}
		}
		LOGGER.info("domainName:{}", domainName);
		return domainName;
	}

}