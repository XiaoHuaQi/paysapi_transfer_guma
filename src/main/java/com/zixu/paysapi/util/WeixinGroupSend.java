package com.zixu.paysapi.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

/**
 * 
 * 
 */
public class WeixinGroupSend {
	private static final Logger LOGGER = Logger
			.getLogger(WeixinGroupSend.class);
	private static int id = 0;

	/**
	 * 读取微信 Token的url
	 */
	private final String GET_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential";

	/**
	 * 识别公众号 身份的appid,与 secret 一起作为用户身份标识
	 */
	private final String APPID = "wxc073823c5c60c84e";

	/**
	 * 识别公众号 身份的secret,与appid一起作为用户身份标识
	 */
	private final String SECRET = "72f452f097c163cfb39215ad49b66ece";

	public static void main(String[] args) {

		WeixinGroupSend weixin = new WeixinGroupSend();

		// String urlstr =
		// "https://api.weixin.qq.com/cgi-bin/message/mass/send?access_token="
		// + weixin.getAccess_token();

		// String url =
		// "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token="
		// + weixin.getAccess_token();
		// String reqjson = weixin.createGroupText(weixin.getOpenids(""));
		// System.out.println("WeixinGroupSend.main():" + reqjson);
		String u = "{\"touser\":\"oE5AD1JIpHUrSSTMZ_XAZZ1bziUs\",\"msgtype\":\"text\",\"text\":{\"content\":\"测试\"}}";
		System.out.println(weixin.createGroupText(
				"oE5AD1JIpHUrSSTMZ_XAZZ1bziUs", "测试"));
		// System.out.println(weixin.sendMessage(url, u));
	}

	/**
	 * 获取指定的 Token
	 * 
	 * @Title: getAccess_token
	 * @Description: TODO(获取指定的 Token)
	 * @param @return 参数
	 * @return String 返回类型
	 * @throws
	 */
	public String getAccess_token() {

		String access_token = null;
		StringBuffer action = new StringBuffer();

		// 构造读取Token的url
		action.append(GET_TOKEN_URL).append("&appid=").append(APPID)
				.append("&secret=").append(SECRET);

		String resp = httpClientGet(action.toString());

		JSONObject jsonObject = JSONObject.fromObject(resp);

		// 读取 access_token
		Object object = jsonObject.get("access_token");
		if (object != null) {
			access_token = String.valueOf(object);
		}
		return access_token;

	}

	/**
	 * 获取 关注者列表
	 * 
	 * @Title: getOpenids
	 * @Description: TODO(获取 关注者列表)
	 * @param @return 参数
	 * @return JSONArray 返回类型
	 * @throws
	 */
	public JSONArray getOpenids(String next_openid) {

		JSONArray array = null;

		String urlstr = "https://api.weixin.qq.com/cgi-bin/user/get?access_token=ACCESS_TOKEN&next_openid=NEXT_OPENID";
		urlstr = urlstr.replace("ACCESS_TOKEN", getAccess_token());

		/*
		 * next_openid 上一次拉取列表的最后一个用户的OPENID,同时会作为下一次拉取的起始地址。
		 */
		if (StringUtils.isBlank(next_openid)) {
			urlstr = urlstr.replace("NEXT_OPENID", "");
		} else {
			urlstr = urlstr.replace("NEXT_OPENID", next_openid);
		}

		String resp = httpClientGet(urlstr);
		JSONObject jsonObject = JSONObject.fromObject(resp);
		array = jsonObject.getJSONObject("data").getJSONArray("openid");
		return array;
	}

	public String createGroupText(String openid, String content) {
		JSONObject gjson = new JSONObject();
		gjson.put("touser", openid);
		gjson.put("msgtype", "text");
		JSONObject text = new JSONObject();
		text.put("content", content);
		gjson.put("text", text);
		return gjson.toString();
	}

	/**
	 * 发送消息
	 * 
	 * @Title: httpClient
	 * @Description: TODO(发送消息)
	 * @param sendUrl
	 *            消息请求的url 必传(http请求格式)
	 * @param sendContent
	 *            消息的内容 必传(json串格式)
	 * @return String 返回的消息记录
	 * @throws
	 */
	public String sendMessage(String sendUrl, String sendContent) {

		String message = null;

		OutputStream os = null;
		try {

			URL httpclient = new URL(sendUrl);
			HttpURLConnection conn = (HttpURLConnection) httpclient
					.openConnection();

			conn.setConnectTimeout(5000);

			conn.setReadTimeout(2000);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.connect();
			os = conn.getOutputStream();
			os.write(sendContent.getBytes("UTF-8"));// 传入参数
			os.flush();
			os.close();

			InputStream is = conn.getInputStream();
			int size = is.available();
			byte[] jsonBytes = new byte[size];
			is.read(jsonBytes);
			message = new String(jsonBytes, "UTF-8");
		} catch (MalformedURLException e) {
			e.printStackTrace();
			LOGGER.info(this.getClass() + " " + e.getMessage());
			throw new RuntimeException(e);
		} catch (IOException e) {
			e.printStackTrace();
			LOGGER.info(this.getClass() + " " + e.getMessage());
			throw new RuntimeException(e);
		} finally {
			if (os != null) {
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
					LOGGER.info(this.getClass() + " 关闭输出流出现错误   "
							+ e.getMessage());
				}
			}
		}

		return message;
	}

	/**
	 * 发起一个get请求
	 * 
	 * @Title: httpClientGet
	 * @Description: TODO( get 请求 )
	 * @param urlstr
	 *            需要发起的请求的url
	 * @return String 读取到的json串的字符串形式
	 * @throws
	 */
	public static String httpClientGet(String urlstr) {

		URL url;
		InputStream is = null;

		String resp = "";

		try {

			url = new URL(urlstr);
			HttpURLConnection http = (HttpURLConnection) url.openConnection();
			http.setRequestMethod("GET");
			http.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			http.setDoInput(true);
			is = http.getInputStream();

			int size = is.available();
			byte[] buf = new byte[size];
			is.read(buf);
			resp = new String(buf, "UTF-8");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// 关闭输入流
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return resp;
	}
}
