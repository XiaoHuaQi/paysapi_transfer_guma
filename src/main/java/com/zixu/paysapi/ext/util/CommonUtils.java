package com.zixu.paysapi.ext.util;

import java.util.List;
import java.util.Map;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

public class CommonUtils {
	/**
	 * Check whether the object is null or not. If it is, throw an exception and
	 * display the message.
	 * 
	 * @param object
	 *            the object to check.
	 * @param message
	 *            the message to display if the object is null.
	 */
	public static void assertNotNull(final Object object, final String message) {
		if (object == null) {
			throw new IllegalArgumentException(message);
		}
	}
	/** 
	 *
	 * @Title:  checkNull 
	 * @Description: 判断集合是否为空,主要针对list,array,map
	 * @param obj
	 * @return boolean 不为空返回true    
	 * @throws 
	 *
	 */
	public static boolean checkNotNull(Object obj){
		if(obj instanceof List){
			return obj!=null&&((List<?>)obj).size()>0;
		}else if(obj instanceof Object[]){
			return obj!=null&&((Object[])obj).length>0;
		}else if(obj instanceof Map){
			return obj!=null&&((Map<?, ?>)obj).size()>0;
		}
		return obj!=null;
	}
	
	 /** 
	 *
	 * @Title:  getPinYinHeadChar 
	 * @Description:获取中文字串拼音首字母 
	 * @param str
	 * @return string
	 *
	 */
	public static String getPinYinHeadChar(String str) {
	        String convert = "";
	        for (int j = 0; j < str.length(); j++) {
	            char word = str.charAt(j);
	            String[] pinyinArray = null;
				try {
					pinyinArray = PinyinHelper.toHanyuPinyinStringArray(word,new HanyuPinyinOutputFormat());
				} catch (BadHanyuPinyinOutputFormatCombination e) {
					e.printStackTrace();
				}
	            if (checkNotNull(pinyinArray)) {
	                convert += pinyinArray[0].charAt(0);
	            } else {
	                convert += word;
	            }
	        }
	        return convert.toLowerCase();
	    }
	 /** 
	 *
	 * @Title:  getPingYin 
	 * @Description: 获取中文字符串拼音
	 * @param src
	 * @return String     
	 * @throws 
	 *
	 */
	public static String getPingYin(String src) {
	        char[] t1 = null;
	        t1 = src.toCharArray();
	        String[] t2 = new String[t1.length];
	        HanyuPinyinOutputFormat t3 = new HanyuPinyinOutputFormat();
	        t3.setCaseType(HanyuPinyinCaseType.LOWERCASE);
	        t3.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
	        t3.setVCharType(HanyuPinyinVCharType.WITH_V);
	        String t4 = "";
	        int t0 = t1.length;
	        try {
	            for (int i = 0; i < t0; i++) {
	                // 判断是否为汉字字符
	                if (java.lang.Character.toString(t1[i]).matches("[\\u4E00-\\u9FA5]+")) {
	                    t2 = PinyinHelper.toHanyuPinyinStringArray(t1[i], t3);
	                    t4 += t2[0];
	                } else {
	                    t4 += java.lang.Character.toString(t1[i]);
	                }
	            }
	            return t4;
	        } catch (Exception e1) {
	            e1.printStackTrace();
	        }
	        return t4;
	    }
}
