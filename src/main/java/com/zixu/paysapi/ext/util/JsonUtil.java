package com.zixu.paysapi.ext.util;


import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class JsonUtil {
	private final static ObjectMapper objectMapper = Jackson2ObjectMapperBuilder.json().serializationInclusion(JsonInclude.Include.NON_NULL).build().configure(SerializationFeature.INDENT_OUTPUT, true);
	private final static JsonEncoding encoding = JsonEncoding.UTF8;

	public static String json(Object object) throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		JsonGenerator generator = objectMapper.getFactory().createGenerator(bos, encoding);
		objectMapper.writeValue(generator, object);
		generator.flush();
		return new String(bos.toByteArray(), encoding.getJavaName());
	}

	public static <T> T object(String json, Class<T> valueType) throws IOException {
		return objectMapper.readValue(json, valueType);
	}

	public static <T> List<T> list(String json, Class<T> valueType) throws IOException {
		JsonParser parser = objectMapper.getFactory().createParser(json);
		CollectionType type = TypeFactory.defaultInstance().constructCollectionType(ArrayList.class, valueType);
		MappingIterator<List<T>> it = objectMapper.readValues(parser, type);
		if (it.hasNext()) {
			return it.next();
		}
		return new ArrayList<T>();
	}
	
	public static String getJsonByUserNames(List<?> list,String...names){
		StringBuilder json = new StringBuilder();     
        json.append("[");     
       if (list != null && list.size() > 0) {     
           for (Object obj : list) {     
                json.append(objectToJson(obj,names));     
                json.append(",");     
            }     
            json.setCharAt(json.length() - 1, ']');     
        } else {     
            json.append("]");     
        }     
       return json.toString(); 
	}
	 public static String beanToJson(Object bean, String[] names) {     
         StringBuilder json = new StringBuilder();     
         json.append("{");     
         PropertyDescriptor[] props = null;     
        try {     
             props = Introspector.getBeanInfo(bean.getClass(), Object.class)     
                     .getPropertyDescriptors();     
         } catch (IntrospectionException e) {     
         }     
        if (props != null) {     
            for (int i = 0; i < props.length; i++) {     
                try {    
                     String name = objectToJson(props[i].getName()); 
					if (names == null) {
						String value = objectToJson(props[i].getReadMethod()
								.invoke(bean));
						json.append(name);
						json.append(":");
						json.append(value);
						json.append(",");
					} else {
						for (String username : names) {
							if (username.equals(name.replace("\"", ""))) {
								String value = objectToJson(props[i]
										.getReadMethod().invoke(bean));
								json.append(name);
								json.append(":");
								json.append(value);
								json.append(",");
							}
						}
					}
                 } catch (Exception e) {     
                 }     
             }     
             json.setCharAt(json.length() - 1, '}');     
         } else {     
             json.append("}");     
         }     
        return json.toString();     
     }   
	 public static String objectToJson(Object object,String...names) {     
         StringBuilder json = new StringBuilder();     
        if (object == null) {     
             json.append("\"\"");     
         } else if (object instanceof String || object instanceof Integer) {   
             json.append("\"").append(object.toString()).append("\"");    
         } else {     
             json.append(beanToJson(object,names));     
         }     
        return json.toString();     
     }  
}
