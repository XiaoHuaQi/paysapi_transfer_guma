package com.zixu.paysapi.ext.transform;

import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.io.Reader;
import java.sql.Clob;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.transform.AliasedTupleSubsetResultTransformer;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

public class AliasToBeanResultTransformer extends AliasedTupleSubsetResultTransformer {
	private static final long serialVersionUID = 3754836255887536806L;
	private final Class<?> resultClass;
	private boolean isInitialized;
	private String[] aliases;
	private Map<String, String> aliaseFieldNameMap;

	public AliasToBeanResultTransformer(Class<?> resultClass) {
		if (resultClass == null) {
			throw new IllegalArgumentException("resultClass cannot be null");
		}
		isInitialized = false;
		this.resultClass = resultClass;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isTransformedValueATupleElement(String[] aliases, int tupleLength) {
		return false;
	}

	public Object transformTuple(Object[] tuple, String[] aliases) {
		Object result;
		try {
			if (!isInitialized) {
				initialize(aliases);
			} else {
				check(aliases);
			}
			result = resultClass.newInstance();
			BeanWrapper wrapper = new BeanWrapperImpl(result);
			for (int i = 0; i < aliases.length; i++) {
				String alias = aliases[i];
				if (alias != null) {
					String propertyName = aliaseFieldNameMap.get(alias);
					if (propertyName != null) {
						if(tuple[i] instanceof Clob){
								Reader inStreamDoc = ((Clob) tuple[i]).getCharacterStream();  
								char[] tempDoc = new char[(int) ((Clob) tuple[i]).length()];
								inStreamDoc.read(tempDoc);    
						        inStreamDoc.close();     
						        wrapper.setPropertyValue(propertyName, new String(tempDoc)); 
						}else{
							wrapper.setPropertyValue(propertyName, tuple[i]);
						}
					}
				}
			}
		} catch (InstantiationException e) {
			throw new HibernateException("Could not instantiate resultclass: " + resultClass.getName());
		} catch (IllegalAccessException e) {
			throw new HibernateException("Could not instantiate resultclass: " + resultClass.getName());
		}catch(SQLException | IOException e){
			throw new HibernateException("Could not instantiate resultclass: " + resultClass.getName());
		}
		return result;
	}

	public static String convertUnderscoreNameToPropertyName(String name) {
		StringBuilder result = new StringBuilder();
		boolean nextIsUpper = false;
		if (name != null && name.length() > 0) {
			if (name.length() > 1 && name.substring(1, 2).equals("_")) {
				result.append(name.substring(0, 1).toUpperCase());
			} else {
				result.append(name.substring(0, 1).toLowerCase());
			}
			for (int i = 1; i < name.length(); i++) {
				String s = name.substring(i, i + 1);
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

	private void initialize(String[] aliases) {
		PropertyDescriptor[] pds = BeanUtils.getPropertyDescriptors(resultClass);
		this.aliaseFieldNameMap = new HashMap<String, String>();
		for (String alias : aliases) {
			if (alias != null) {
				for (PropertyDescriptor pd : pds) {
					if (pd.getWriteMethod() != null) {
						if (pd.getName().equalsIgnoreCase(alias) || pd.getName().equalsIgnoreCase(convertUnderscoreNameToPropertyName(alias))) {
							aliaseFieldNameMap.put(alias, pd.getName());
							break;
						}
					}
				}
			}
		}
		this.aliases = new String[aliases.length];
		for (int i = 0; i < aliases.length; i++) {
			this.aliases[i] = aliases[i];
		}
		isInitialized = true;
	}

	private void check(String[] aliases) {
		if (!Arrays.equals(aliases, this.aliases)) {
			throw new IllegalStateException("aliases are different from what is cached; aliases=" + Arrays.asList(aliases) + " cached=" + Arrays.asList(this.aliases));
		}
	}

	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		AliasToBeanResultTransformer that = (AliasToBeanResultTransformer) o;
		if (!resultClass.equals(that.resultClass)) {
			return false;
		}
		if (!Arrays.equals(aliases, that.aliases)) {
			return false;
		}

		return true;
	}

	public int hashCode() {
		int result = resultClass.hashCode();
		result = 31 * result + (aliases != null ? Arrays.hashCode(aliases) : 0);
		return result;
	}
}
