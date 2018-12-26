package com.zixu.payment.mysql;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.SimpleTimeZone;
import java.util.TimeZone;


public class DateUtil {


    private DateUtil() {
    }

    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String TRADE_DATE_FORMAT = "yyyyMMdd";
    public static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String DATEBRANCH_FORMAT = "yyyy-MM-dd HH:mm";
    public static final String TIME_FORMAT = "HH:mm:ss";

    //private static SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
    //private static SimpleDateFormat dateTimeFormat = new SimpleDateFormat(DATETIME_FORMAT);
    private static SimpleDateFormat timeFormat = new SimpleDateFormat(TIME_FORMAT);
    private static SimpleDateFormat branchFormat = new SimpleDateFormat(DATEBRANCH_FORMAT);
    private static SimpleDateFormat tradeBranchFormat = new SimpleDateFormat(TRADE_DATE_FORMAT);
    
    public static String getStringDate() {
    	int newTime=(int)(8 * 60 * 60 * 1000);
        TimeZone timeZone;
        String[] ids = TimeZone.getAvailableIDs(newTime);
        if (ids.length == 0) {
            timeZone = TimeZone.getDefault();
        } else {
            timeZone = new SimpleTimeZone(newTime, ids[0]);
        }
    
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setTimeZone(timeZone);
        
        return sdf.format(new Date());
    }
    
    public static String getStringTradeDate() {
    	return tradeBranchFormat.format(new Date());
    }

    public static String getStringDateTime() {
        return getFormatedDateString(8);
    }

    public static String getStringTime() {
        return timeFormat.format(new Date());
    }
    public static String getStringBranch() {
    	return branchFormat.format(new Date());
    }

    public static Date parse(String dateStr, String format) throws ParseException {
        return new SimpleDateFormat(format).parse(dateStr);
    }

    public static Date getCurrDate() {
        return new Date();
    }

    public static String getFormatDate(String format) {
        return new SimpleDateFormat(format).format(new Date());
    }

    public static String getFormatDate(Date date, String format) {
        return new SimpleDateFormat(format).format(date);
    }

    public static String getCurrentDate() {
        String format = "yyyy-MM-dd";
        Date date = new Date();
        date.setTime(System.currentTimeMillis());
        if (format == null || "".equals(format.trim())) {
            format = DATETIME_FORMAT;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    public static String getCurrentTime() {
        String format = "yyyyMMddHHmmss";
        Date date = new Date();
        date.setTime(System.currentTimeMillis());
        if (format == null || "".equals(format.trim())) {
            format = DATETIME_FORMAT;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }
    
    public static String dateToStrLong(Date dateDate) {
    	 SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	 String dateString = formatter.format(dateDate);
    	 return dateString;
    }
    public static String dateToStr(Date dateDate) {
    	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    	String dateString = formatter.format(dateDate);
    	return dateString;
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
    
    public static String getYesterday(String nowDate) {
    	Date as = new Date(DateUtil.strToDate(nowDate).getTime()-24*60*60*1000);
	    SimpleDateFormat matter1 = new SimpleDateFormat("yyyy-MM-dd");
		String time = matter1.format(as);
		return time;
    }
    public static String getFutureDate(String yearStr) {
    	
    	try {
			int year = Integer.valueOf(yearStr);
			
			return dateToStr(new Date(new BigDecimal(year*365).multiply(new BigDecimal(86400000)).add(new BigDecimal(new Date().getTime())).longValue()));
		} catch (Exception e) {
			// TODO: handle exception
		}
    	
    	return null;
    }
    
    public static String getFormatedDateString(float timeZoneOffset){
        if (timeZoneOffset > 13 || timeZoneOffset < -12) {
            timeZoneOffset = 0;
        }
        
        int newTime=(int)(timeZoneOffset * 60 * 60 * 1000);
        TimeZone timeZone;
        String[] ids = TimeZone.getAvailableIDs(newTime);
        if (ids.length == 0) {
            timeZone = TimeZone.getDefault();
        } else {
            timeZone = new SimpleTimeZone(newTime, ids[0]);
        }
    
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(timeZone);
        return sdf.format(new Date());
    }
    
}
