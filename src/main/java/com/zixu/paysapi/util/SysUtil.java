package com.zixu.paysapi.util;

import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;


/**
 * 系统辅助类
 * 
 * @author Administrator
 *
 */
public class SysUtil {
	// 编号前缀
	public static String SYS_PY_SBL = "SBL";
	// 编号分隔符
	public static String SYS_BNO_SP = "-";
	// 密码加密字符串
	public static String SYS_ENCODER_STR = "IY7LROU42ZTE3KSLTC2EGTG9HQ3P4L88";

	/**
	 * 投标编号
	 * 
	 * @return
	 */
	public synchronized static String generalBNO(String ext, Integer no) {
		StringBuilder sb = new StringBuilder();
		sb.append(SYS_PY_SBL);
		sb.append(SYS_BNO_SP);
		sb.append(ext);
		sb.append(SYS_BNO_SP);
		sb.append(generalDATE());
		sb.append(SYS_BNO_SP);
		String sno = no < 10 ? ("0" + no) : no.toString();
		sb.append(sno);
		return sb.toString();
	}

	/**
	 * 真实IP
	 * 
	 * @param request
	 * @return
	 */
	public static String getIPAddr(HttpServletRequest request) {
		if (request == null)
			return null;
		String ip = request.getHeader("X-Forwarded-For");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
			ip = request.getHeader("Proxy-Client-IP");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
			ip = request.getHeader("WL-Proxy-Client-IP");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
			ip = request.getHeader("HTTP_CLIENT_IP");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
			ip = request.getRemoteAddr();
		if ("127.0.0.1".equals(ip) || "0:0:0:0:0:0:0:1".equals(ip))
			try {
				ip = InetAddress.getLocalHost().getHostAddress();
			} catch (UnknownHostException unknownhostexception) {
			}
		return ip;
	}

	/**
	 * 日期
	 * 
	 * @return
	 */
	public synchronized static String generalDATE() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		return sdf.format(new Date()).toString();
	}

	/**
	 * 系统主键编号
	 * 
	 * @return
	 */
	public synchronized static String generalPK() {
		String newPK = getRandomString(32);
		return newPK;
	}

	private static int step = 0;

	public synchronized static String getRandomString(int length) { // length表示生成字符串的长度
		String base = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		Random random = new Random(System.currentTimeMillis() + step);
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
			int number = random.nextInt(base.length());
			sb.append(base.charAt(number));
		}
		step++;
		return sb.toString();
	}

	public synchronized static String getRandomNumber(int length) { // length表示生成字符串的长度
		String base = "0123456789";
		Random random = new Random(System.currentTimeMillis() + step);
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
			int number = random.nextInt(base.length());
			sb.append(base.charAt(number));
		}
		step++;
		return sb.toString();
	}

}
