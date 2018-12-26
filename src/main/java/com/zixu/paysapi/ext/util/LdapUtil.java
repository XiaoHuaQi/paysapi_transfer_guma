package com.zixu.paysapi.ext.util;

import javax.naming.Name;
import javax.naming.ldap.LdapName;
import javax.naming.ldap.Rdn;

public class LdapUtil {
	/**
	 * 去掉开头的数字
	 * 
	 * @param name
	 * @return
	 */
	public static String trimStartDigital(String name) {
		String result = null;
		if (name != null) {
			result = name.replaceFirst("^\\d+", "");
		}
		if (result.trim().length() == 0) {
			return name;
		}
		return result;
	}

	/**
	 * 获得当前Name的RDN
	 * 
	 * @param dn
	 * @return
	 */
	public static Rdn getRdn(Name name) {
		if (name == null || name.size() == 0) {
			return null;
		}
		return ((LdapName) name).getRdn(name.size() - 1);
	}

	/**
	 * 获得上级的Name
	 * 
	 * @param name
	 * @return
	 */
	public static Name getParentName(Name name) {
		if (name == null || name.size() == 0) {
			return null;
		}
		return name.getPrefix(name.size() - 1);
	}
}
