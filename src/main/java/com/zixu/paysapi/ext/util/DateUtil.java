package com.zixu.paysapi.ext.util;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.DatatypeConverter;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 * 日期工具类
 * 
 * @author liulibo
 * 
 */
public class DateUtil {

	/**
	 * 日期与时间
	 */
	public final static String BOTH = "yyyy-MM-dd HH:mm:ss";
	/**
	 * 日期
	 */
	public final static String DATE = "yyyy-MM-dd";
	/**
	 * 时间
	 */
	public final static String TIME = "HH:mm:ss";
	/**
	 * 时分格式 如 12:30
	 */
	public final static String TMS = "HH:mm";
	/**
	 * 一日的毫秒数
	 */
	public static long MILLION_SECONDS_OF_DAYS = 24 * 60 * 60 * 1000L;// 86400000
	/**
	 * 
	 * 一小时的毫秒数
	 */
	public static long MILLION_SECONDS_OF_HOURS = 60 * 60 * 1000L;// 3600000;
	public static long MILLION_SECONDS_OF_MINUTES = 60 * 1000L;
	public static long MILLION_SECONDS = 1000L;

	private DateUtil() {

	}

	public static String currentDate() {
		return format(new Date(), DATE);
	}

	public static String currentTime() {
		return format(new Date(), TIME);
	}

	public static String currentDateTime() {
		return format(new Date(), BOTH);
	}

	public static String currentHM() {
		return format(new Date(), TMS);
	}

	/**
	 * 是否有效日期
	 * 
	 * @param str
	 * @param fmt
	 * @return
	 */
	public static boolean isValidDate(String str, String fmt) {
		Date date = parse(str, fmt);
		if (date == null) {
			return false;
		}
		String dateStr = format(date, fmt);
		if (dateStr.equals(str)) {
			return true;
		}
		return false;
	}

	/**
	 * 解析日期字符串
	 * 
	 * @param str
	 *            日期串
	 * @param fmt
	 *            日期格式
	 * @return
	 */
	public static Date parse(String str, String fmt) {
		Assert.notEmpty(str);
		Assert.notEmpty(fmt);
		SimpleDateFormat simDateFormat = new SimpleDateFormat(fmt);
		Date date = null;
		try {
			date = simDateFormat.parse(str);
		} catch (Exception e) {
		}
		return date;
	}

	/**
	 * 格式化日期
	 * 
	 * @param date
	 *            日期
	 * @param fmt
	 *            日期格式
	 * @return
	 */
	public static String format(Date date, String fmt) {
		Assert.notEmpty(date);
		Assert.notEmpty(fmt);
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(fmt);
		return simpleDateFormat.format(date);
	}

	/**
	 * 计算日期加月
	 * 
	 * @param date
	 * @param months
	 * @return
	 */
	public static Date addMonth(Date date, int months) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.MONTH, months);
		return c.getTime();
	}

	/**
	 * 计算日期加小时
	 * 
	 * @param date
	 * @param hour
	 * @return
	 */
	public static Date addHour(Date date, int hour) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.HOUR_OF_DAY, hour);
		return c.getTime();
	}

	public static int getWeek() {
		GregorianCalendar ca = new GregorianCalendar();
		return ca.get(7) - 1 == 0 ? 7 : ca.get(7) - 1;
	}

	/**
	 * 计算日期加天数
	 * 
	 * @param date
	 * @param days
	 * @return
	 */
	public static Date addDay(Date date, int days) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DAY_OF_MONTH, days);
		return c.getTime();
	}

	/**
	 * 计算日期加天数
	 * 
	 * @param date
	 * @param days
	 * @return
	 */
	public static String addDay(String str, int days, String fmt) {
		Calendar c = Calendar.getInstance();
		c.setTime(parse(str, fmt));
		c.add(Calendar.DAY_OF_MONTH, days);
		Date date = c.getTime();
		return format(date, fmt);
	}

	/**
	 * 计算日期加分钟
	 * 
	 * @param date
	 * @param minutes
	 * @return
	 */
	public static Date addMinutes(Date date, int minutes) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.MINUTE, minutes);
		return c.getTime();
	}

	public static String addMinutes(String str, int minutes, String type) {
		Calendar c = Calendar.getInstance();
		c.setTime(parse(str, type));
		c.add(Calendar.MINUTE, minutes);
		return format(c.getTime(), type);
	}

	public static boolean between(String now, String stime, String etime,
			String type) {

		return parse(now, type).after(parse(stime, type))
				&& parse(now, type).before(parse(etime, type));

	}

	/**
	 * 计算天数差
	 * 
	 * @param sd
	 * @param ed
	 * @return
	 */
	public static int subDateDays(Date sd, Date ed, long type) {
		Assert.notEmpty(sd);
		Assert.notEmpty(ed);
		Long eds = ed.getTime();
		Long sds = sd.getTime();
		return (int) ((eds - sds) / type);
	}

	/**
	 * 计算天数差
	 * 
	 * @param sd
	 * @param ed
	 * @return
	 */
	public static int subDateDays(String sd, String ed, long type) {
		Assert.notEmpty(sd);
		Assert.notEmpty(ed);
		Long eds = parse(ed, DATE).getTime();
		Long sds = parse(sd, DATE).getTime();
		return (int) ((eds - sds) / type);
	}

	public static int subDatePars(String sd, String ed, long type) {
		if (StringUtil.isNull(ed) || StringUtil.isNull(sd)) {
			return 0;
		}
		Long eds = parse(ed, BOTH).getTime();
		Long sds = parse(sd, BOTH).getTime();
		return (int) ((eds - sds) / type);
	}

	/**
	 * 
	 * @Title: getRelWorkDay
	 * @Description: 用于匹配工作日
	 * @param str
	 *            类似{"1","2","3","4"}
	 * @return Map<Integer,Boolean>
	 * @throws
	 * 
	 */
	public static Map<Integer, Boolean> getRelWorkDay(String... str) {
		Map<Integer, Boolean> map = new HashMap<Integer, Boolean>();
		map.put(1, false);
		map.put(2, false);
		map.put(3, false);
		map.put(4, false);
		map.put(5, false);
		map.put(6, false);
		map.put(7, false);
		for (String s : str) {
			if (map.containsKey(Integer.parseInt(s))) {
				map.put(Integer.parseInt(s), true);
			}
		}
		return map;
	}

	/**
	 * 判断某段时间是否在指定的时间段内(包含开始时间等于指定时间段的结束时间)
	 * 
	 * @param begin
	 *            开始时间
	 * @param end
	 *            结束时间
	 * @param tBegin
	 *            开始时间段
	 * @param tEnd
	 *            结束时间段
	 * @param fmt
	 * @return
	 */
	public static boolean isContain(String begin, String end, String tBegin,
			String tEnd, String fmt) {
		long beginTime = parse(begin, fmt).getTime();
		long endTime = parse(end, fmt).getTime();
		long bTime = parse(tBegin, fmt).getTime();
		long eTime = parse(tEnd, fmt).getTime();
		if (bTime > endTime || eTime <= beginTime) {
			return false;
		}
		return true;
	}

	/**
	 * 将日期字符串,转换成XMLGregorianCalendar对象
	 * 
	 * @param str
	 *            日期字符串
	 * @param fmt
	 *            日期格式
	 * @return
	 */
	public static XMLGregorianCalendar toXMLGC(String str, String fmt) {
		GregorianCalendar cal = new GregorianCalendar();
		XMLGregorianCalendar gc = null;
		try {
			Date date = parse(str, fmt);
			cal.setTime(date);
			gc = DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return gc;
	}

	/**
	 * 
	 * @Title: isSame
	 * @Description: 开始时间是否和结束时间相同
	 * @param start
	 * @param end
	 * @param fmt
	 * @return boolean
	 * @throws
	 * 
	 */
	public static boolean isSame(String start, String end, String fmt) {
		boolean flag = false;
		flag = StringUtil.isNull(start);
		flag = StringUtil.isNull(end);
		if (flag)
			return false;
		return parse(start, fmt).equals(parse(end, fmt));
	}

	/**
	 * 
	 * @Title: isAfter
	 * @Description: 开始时间是在结束时间之后
	 * @param start
	 * @param end
	 * @param fmt
	 * @return boolean
	 * @throws
	 * 
	 */
	public static boolean isAfter(String start, String end, String fmt) {
		boolean flag = false;
		flag = StringUtil.isNull(start);
		flag = StringUtil.isNull(end);
		if (flag)
			return false;
		return parse(start, fmt).after(parse(end, fmt));
	}

	public static String marginTime(List<String> sList, List<String> eList,
			String fmt) {
		Collections.sort(sList);
		Collections.sort(eList);
		for (int i = 1; i < sList.size(); i++) {
			String sp = sList.get(i - 1);
			String sn = sList.get(i);
			if (sp.compareTo(sn) <= 0 && sn.compareTo(eList.get(i - 1)) <= 0) {
				if (eList.get(i).compareTo(eList.get(i - 1)) >= 0) {
					eList.remove(i - 1);
				}
				sList.remove(i);
				i--;
			}
		}
		String result = "";
		for (int i = 0; i < sList.size(); i++) {
			result += sList.get(i) + "~" + eList.get(i)+",";
		}
		return result.substring(0,result.length()-1);
	}
	
	 
    public static Date strToDateLong(String strDate) {
    	 SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	 ParsePosition pos = new ParsePosition(0);
    	 Date strtodate = formatter.parse(strDate, pos);
    	 return strtodate;
    }
    
    public static Date strToDateLongyyyyMMddHHmmss(String strDate) {
   	 SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
   	 ParsePosition pos = new ParsePosition(0);
   	 Date strtodate = formatter.parse(strDate, pos);
   	 return strtodate;
   }
    
    public static Date strToDate(String strDate) {
    	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    	ParsePosition pos = new ParsePosition(0);
    	Date strtodate = formatter.parse(strDate, pos);
    	return strtodate;
    }
	

	/**
	 * 将XMLGregorianCalendar日期字符串转换成java.util.Date对象
	 * 
	 * @param str
	 * @return
	 */
	public static Date toDate(String str) {
		return DatatypeConverter.parseDate(str).getTime();
	}

	public static void main(String[] args) {
		System.out.println(DateUtil.parse("99991231235959", "yyyyMMddHHmmss"));
		
	}
}
