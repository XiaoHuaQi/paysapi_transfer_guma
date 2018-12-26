package com.zixu.paysapi.ext.util;

import org.springframework.util.StringUtils;

public class NameUtil {

	/**
	 * 属性名 转换为 下划线名
	 * 
	 * @param propertyName
	 * @return
	 */
	public static String propertyNameToUnderscoreName(String propertyName) {
		if (!StringUtils.hasLength(propertyName)) {
			return "";
		}
		StringBuilder result = new StringBuilder();
		result.append(propertyName.substring(0, 1).toLowerCase());
		for (int i = 1; i < propertyName.length(); i++) {
			String s = propertyName.substring(i, i + 1);
			String slc = s.toLowerCase();
			if (!s.equals(slc)) {
				result.append("_").append(slc);
			} else {
				result.append(s);
			}
		}
		return result.toString();
	}

	/**
	 * 下划线名 转换为 属性名
	 * 
	 * @param underscoreName
	 * @return
	 */
	public static String underscoreNameToPropertyName(String underscoreName) {
		StringBuilder result = new StringBuilder();
		boolean nextIsUpper = false;
		if (underscoreName != null && underscoreName.length() > 0) {
			if (underscoreName.length() > 1 && underscoreName.substring(1, 2).equals("_")) {
				result.append(underscoreName.substring(0, 1).toUpperCase());
			} else {
				result.append(underscoreName.substring(0, 1).toLowerCase());
			}
			for (int i = 1; i < underscoreName.length(); i++) {
				String s = underscoreName.substring(i, i + 1);
				if (s.equals("_")) {
					nextIsUpper = true;
				} else {
					if (nextIsUpper) {
						result.append(s.toUpperCase());
						nextIsUpper = false;
					} else {
						result.append(s.toLowerCase());
					}
				}
			}
		}
		return result.toString();
	}

	public static void main(String[] args) {
		System.out.println(propertyNameToUnderscoreName("abcDef"));
		System.out.println(underscoreNameToPropertyName("ABC_DEF"));
	}
}
