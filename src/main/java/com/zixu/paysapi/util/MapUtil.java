package com.zixu.paysapi.util;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 *	edit by TuYongKang
 */
public class MapUtil {

	private Map<String,Object> map = null;
	
	/**
	 *	构造函数
	 *	每个实例都使用单独的Map,这样就不存在线程同步问题
	 */
	public MapUtil(){
		map = new HashMap<String,Object>();
	}
	
	/**
	 *	向Map中添加元素,同时将原始对象MapUtil返回,从而达到类似JQ链式调用的效果
	 */
	public MapUtil put(String key,Object value){
		map.put(key, value);
		return this;
	}
	
	/**
	 *	获取装在好元素的Map
	 */
	public Map<String,Object> getMap(){
		return this.map;
	}
	
	/**
	 * 将对象转成Map
	 */
	public static Map<String,Object> parseObject(Object T) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		Class<? extends Object> c = T.getClass();
		Field[] ff = c.getDeclaredFields();
		for(Field f : ff){
			f.setAccessible(true);
			map.put(f.getName(), f.get(T));
		}
		return map;
	}
	
}
