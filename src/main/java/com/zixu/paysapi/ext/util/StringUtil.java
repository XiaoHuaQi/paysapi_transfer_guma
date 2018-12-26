package com.zixu.paysapi.ext.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class StringUtil {

	/**
	 * 修剪不可见字符
	 * 
	 * @param str
	 * @return
	 */
	public static String trimInvisibleChar(String str) {
		if (str == null) {
			return null;
		}
		// 第一步:将多个连续的不可见字符转换成单个空格
		// 第二步:将所有单个的空格、制表符、换页符等等不可见字符转换成空格;
		return str.replaceAll("\\s{2,}", " ").replaceAll("\\s{1}", " ");
	}
	/**
	 * 两个对象是否相等 主要防止null对象的出现，减少代码量
	 * 
	 * @param value1
	 * @param value2
	 * @return boolean
	 */
	public static boolean isEqual(Object value1, Object value2) {
		boolean flag = false;
		if (null != value1) {
			if (null != value2) {
				flag = value2.equals(value1);
			}
		} else {
			if (null == value2) {
				flag = true;
			}
		}
		return flag;
	}
	/**
	 * 当前值是否为空 如果只是输入空格，也认为为空
	 * 
	 * @param value
	 * @return boolean
	 */
	public static boolean isNull(String value) {
		boolean flag = false;
		if ((value == null) ||"null".equals(value) || (value.trim().length() == 0)) {
			flag = true;
		}
		return flag;
	}
	
	public static String in(String columnName, List<String> keys) {
		int sum = keys.size();
		int a = sum / 1000;
		int b = sum % 1000;
		List<String> l = new ArrayList<String>();
		for (int i = 0; i < a; i++) {
			l.add(columnName + " in (" + join(keys.subList(i * 1000, (i + 1) * 1000), ",", "'") + ")");
		}
		if (b != 0) {
			l.add(columnName + " in (" + join(keys.subList(a * 1000, keys.size()), ",", "'") + ")");
		}
		return join(l, " or ", null);
	}

	public static String join(Iterator<?> iterator, String separator, String wrapper) {
		if (iterator == null) {
			return null;
		}
		if (!iterator.hasNext()) {
			return "";
		}
		if (wrapper == null) {
			wrapper = "";
		}
		Object first = iterator.next();
		if (!iterator.hasNext()) {
			return first != null ? wrapper + first.toString() + wrapper : "";
		}
		StringBuffer buf = new StringBuffer(256);
		if (first != null) {
			buf.append(wrapper + first + wrapper);
		}
		while (iterator.hasNext()) {
			if (separator != null) {
				buf.append(separator);
			}
			Object obj = iterator.next();
			if (obj != null) {
				buf.append(wrapper + obj + wrapper);
			}
		}
		return buf.toString();
	}

	public static String join(Collection<?> collection, String separator, String wrapper) {
		if (collection == null) {
			return null;
		} else {
			return join(collection.iterator(), separator, wrapper);
		}
	}
}