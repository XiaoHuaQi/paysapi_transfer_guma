package com.zixu.paysapi.util;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.aspectj.weaver.ast.Var;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.lang.reflect.Method;


import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletResponse;
public class DownlaodExcel {

/*public static void excel(HttpServletResponse resp,List<?> dataList,String[] headers,Map<String, Object> headerMap,ActivityService activityService){
	  
	
	//处理自定义字段
	OrderDto orderDto = (OrderDto) dataList.get(0);
	
	ActivityDto activityDto =  activityService.findById(orderDto.getActivityId());
	
	if(activityDto == null) {
		return;
	}
	String[] headerKey = new String[20];
	String customContent = activityDto.getCustomContent();
	if(customContent != null && !customContent.equals("")) {
		String[] key = customContent.split(",");
		int i = 0;
		for (String string : key) {
			List<String> list = new ArrayList<String>();
			list.add(string);
			headerMap.put(string, list);
			headerKey[i] = string;
			i++;
		}
	}
	
	String xlsxFileName = "订单报表("+DateUtil.getStringBranch()+")";
	  resp.setCharacterEncoding("utf-8");
		resp.setContentType("application/vnd.ms-excel");
		resp.setHeader("content-disposition", "attachment;filename="+ xlsxFileName + ".xlsx");
		OutputStream out=null;
		try {
			out = resp.getOutputStream();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		SXSSFWorkbook workBook = new SXSSFWorkbook();  
      Sheet sheet = workBook.createSheet("order");//创建一个工作薄对象  
      sheet.setDefaultColumnWidth(20);//设置表格默认宽度
      *//*************************表头样式*********************************//*
     CellStyle style = workBook.createCellStyle();//创建样式对象  
    style.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
      style.setFillForegroundColor(HSSFColor.LIGHT_CORNFLOWER_BLUE.index);//背景颜色设置
      style.setBorderBottom(XSSFCellStyle.BORDER_THIN);
	    style.setBorderLeft(XSSFCellStyle.BORDER_THIN);
	    style.setBorderRight(XSSFCellStyle.BORDER_THIN);
	    style.setBorderTop(XSSFCellStyle.BORDER_THIN);//顶部边框粗线
	    style.setAlignment(XSSFCellStyle.ALIGN_CENTER);//水平居中 
      style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//垂直居中  
	    style.setWrapText( true );
      // 设置字体  
      Font font = workBook.createFont();// 创建字体对象  
      font.setFontHeightInPoints((short) 12);// 设置字体大小  
      style.setFont(font);// 将字体加入到样式对象  
      
      *//*************************数据样式*********************************//*
      CellStyle dataStyle = workBook.createCellStyle();// 创建样式对象  
      dataStyle.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
      dataStyle.setFillForegroundColor(HSSFColor.WHITE.index);//背景颜色设置
      dataStyle.setBorderBottom(XSSFCellStyle.BORDER_THIN);
      dataStyle.setBorderLeft(XSSFCellStyle.BORDER_THIN);
      dataStyle.setBorderRight(XSSFCellStyle.BORDER_THIN);
      dataStyle.setBorderTop(XSSFCellStyle.BORDER_THIN);
      dataStyle.setAlignment(XSSFCellStyle.ALIGN_LEFT);
      dataStyle.setVerticalAlignment(XSSFCellStyle.ALIGN_LEFT);
      //设置字体  
      Font dataFont = workBook.createFont();// 创建字体对象  
      dataFont.setFontHeightInPoints((short) 12);// 设置字体大小  
      dataStyle.setFont(dataFont);// 将字体加入到样式对象  
	        
      *//******************************写标题**************************************//*
  	int r = 0;//行号
  	int c = 0;//列号
  	Row row = sheet.createRow(r++);
  	for(Iterator<Map.Entry<String, Object>> entry = headerMap.entrySet().iterator(); entry.hasNext();) {
  		
  		Entry<String, Object> e = entry.next();
  		Object o = e.getValue();
  		if(o instanceof List) {
  			List<String> list = (List<String>)o;
  			if (list.size() == 1) {
					row.setHeightInPoints(23);// 设置行高23像素  
					Cell cell = row.createCell(c);
					cell.setCellValue(e.getKey());
					cell.setCellStyle(style);
					//sheet.addMergedRegion(new CellRangeAddress(0, 1, c, c));
					c++;
				} else {
					for (int i = 0; i < list.size(); i++) {
						Cell cell = row.createCell(c);
						cell.setCellStyle(style);
						cell.setCellValue(e.getKey());
						c++;
					}
					//sheet.addMergedRegion(new CellRangeAddress(0, 0, c - list.size(), c - 1));
				}
  		}
  	}
  	Row dataRow = null;
	for (Object bap : dataList) {
		
		dataRow = sheet.createRow(r++);
		for (int j = 0; j < headers.length; j++) {
			
			if (StringUtils.isNotBlank(headers[j])) {
				Cell cell = dataRow.createCell(j);
				cell.setCellStyle(dataStyle);
				String getMethodName = "get" + headers[j];
				try {
					Class tCls = bap.getClass();
					Method getMethod = tCls.getMethod(getMethodName, new Class[] {});//--
					Object value = getMethod.invoke(bap, new Object[] {});
					if (value != null) {
						// 判断值的类型后进行强制类型转换
						String textValue = null;
						if (value instanceof Date) {
							Date d = (Date) value;
							SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
							textValue = sdf.format(d);
						} else {
							// 其他数据类型都当作字符串简单处理
							textValue = value.toString();
						}
						if (textValue != null) {
							Pattern p = Pattern.compile("^[-+]?(\\d+(\\.\\d*)?|\\.\\d+)([eE][-+]?\\d+)?$"); 
							Matcher matcher = p.matcher(textValue);
							if (matcher.matches()) { // 是数字当作double处理
								double d = Double.parseDouble(textValue);
								BigDecimal b = new BigDecimal(d);
								double df = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
								cell.setCellValue(df);
							} else {
								cell.setCellValue(textValue);
							}
						} else {
							cell.setCellValue("");
						}
					}
				} catch (Exception e) {
					
					e.printStackTrace();
				}
			}
		}
		
		if(headerMap.size() > 13) {
			int k = 0;
			for (int i = 13;i< headerMap.size();i++) {
				Cell cell = dataRow.createCell(i);
				cell.setCellStyle(dataStyle);
				try {
					OrderDto order = (OrderDto) bap;
					if(order.getInfoCentent() != null) {
						JSONObject jsonObject = JSONObject.parseObject(order.getInfoCentent());
						if(jsonObject != null) {
							String value = jsonObject.getString(headerKey[k]);
							if (value != null) {
								cell.setCellValue(value);
							}
						}
						
					}
					if(headerMap.size() == i+1) {
						cell = dataRow.createCell(i+1);
						cell.setCellStyle(dataStyle);
						if(order.getCombination() != null && !order.getCombination().equals("")) {
							JSONArray jsonArray = JSONArray.parseArray(order.getCombination());
							if(jsonArray != null && jsonArray.size() > 0) {
								String text = "";
								for (Object object : jsonArray) {
									text += JSONObject.parseObject(JSONObject.toJSONString(object)).get("name")+",";
								}
								cell.setCellValue(text.equals("") ? "" : text.substring(0,text.length()-1));
							}
						}
					}
					
				} catch (Exception e) {
					
					e.printStackTrace();
				}
				k++;
			}
		}
		
		
	}
	try {
		workBook.write(out);
	} catch (IOException e) {
		e.printStackTrace();
	}
	  
  }
  

  public static Map<String,Object> orderMap(){
	    List<String> list1 = new ArrayList<String>();
		List<String> list2 = new ArrayList<String>();
		List<String> list3 = new ArrayList<String>();
		List<String> list4 = new ArrayList<String>();
		List<String> list5 = new ArrayList<String>();
		List<String> list6 = new ArrayList<String>();
		List<String> list7 = new ArrayList<String>();
		List<String> list8 = new ArrayList<String>();
		List<String> list9 = new ArrayList<String>();
		List<String> list10 = new ArrayList<String>();
		List<String> list11 = new ArrayList<String>();
		List<String> list12 = new ArrayList<String>();
		List<String> list13 = new ArrayList<String>();
		
		list1.add("订单号");
		list2.add("活动名称");
		list3.add("联系人/领队姓名");
		list4.add("联系人/领队电话");
		list5.add("团队名称");
		list6.add("身份证号码");
		list7.add("金额");
		list8.add("数量");
		list9.add("下单时间");
		list10.add("衣服尺码");
		list11.add("性别");
		list12.add("备注");
		list13.add("状态");
		
		Map<String, Object> headerMap = new LinkedHashMap<String, Object>();
		headerMap.put("订单号", list1);
		headerMap.put("活动名称", list2);
		headerMap.put("联系人/领队姓名", list3);
		headerMap.put("联系人/领队电话", list4);
		headerMap.put("团队名称", list5);
		headerMap.put("身份证号码", list6);
		headerMap.put("金额", list7);
		headerMap.put("数量", list8);
		headerMap.put("下单时间", list9);
		headerMap.put("衣服尺码", list10);
		headerMap.put("性别", list11);
		headerMap.put("备注", list12);
		headerMap.put("状态", list13);
		
		return headerMap;
  }*/
}
