package com.zixu.paysapi.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Base64;
import java.util.Date;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.digest.HmacUtils;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.cxf.wsdl.http.UrlEncoded;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.model.GeneratePresignedUrlRequest;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.region.Region;

import freemarker.cache.StrongCacheStorage;

public class FileUtil {

	/**
	 * 上传文件到腾讯COS
	 * 
	 * @param multfile
	 * @return
	 * @throws Exception
	 */
	public static String uploadCOS(MultipartFile multfile, HttpServletRequest request) throws Exception {

		File cosFile = new File(FileUtil.UploadFile((CommonsMultipartFile) multfile, request));

		COSCredentials cred = new BasicCOSCredentials("AKIDLSKyOwgJBoCLrBnFwObZIrDOK3ZqtkQs",
				"QdbLq66uUwBF7k9JI2oNiofIgspb8Qaa");
		ClientConfig clientConfig = new ClientConfig(new Region("ap-guangzhou"));
		COSClient cosClient = new COSClient(cred, clientConfig);
		String bucketName = "wanma-1251498119";
		String pathName = ((CommonsMultipartFile) multfile).getFileItem().getName();
		String key = "/paysapi/img/" + new Date().getTime()+"/"+ RandomUtil.getRandomString(12) + pathName.substring(pathName.lastIndexOf("."), pathName.length());
		String cdnUrl = "https://wanma-1251498119.cos.ap-guangzhou.myqcloud.com";
		PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, cosFile);
		cosClient.putObject(putObjectRequest);
		cosClient.shutdown();
		Date expiration = new Date(new Date().getTime() + 5 * 60 * 10000);
		String url = cdnUrl+key;
		cosFile.delete();
		GeneratePresignedUrlRequest  req = new GeneratePresignedUrlRequest("wanma-1251498119", key);
		Date expirationDate = new Date(System.currentTimeMillis() + 30L * 60L * 1000L);
		req.setExpiration(expirationDate);
		URL downloadUrl = cosClient.generatePresignedUrl(req);
		String downloadUrlStr = downloadUrl.toString();
		
		return url;
	}

	/**
	 * 用IO流上传文件
	 * 
	 * @param file
	 * @return 成功返回文件的名字
	 * @throws IOException
	 */
	public static String UploadFile(CommonsMultipartFile file, HttpServletRequest request) throws IOException {
		try {
			String fileName = new Date().getTime() + file.getOriginalFilename();
			String juedui_path = request.getServletContext().getRealPath("");
			String url = juedui_path + "/file/";
			File files = new File(url);
			if (!files.exists()) {
				files.mkdir();
			}
			String urls = url + fileName;
			OutputStream os = new FileOutputStream(urls);
			InputStream is = file.getInputStream();
			int temp;
			while ((temp = is.read()) != (-1)) {
				os.write(temp);
			}
			os.flush();
			os.close();
			is.close();
			return urls;

		} catch (FileNotFoundException e) {
			return "0";
		}
	}
	public static File multipartFileToFile(MultipartFile file) {
        CommonsMultipartFile cf= (CommonsMultipartFile)file; 
        DiskFileItem fi = (DiskFileItem)cf.getFileItem(); 
        return fi.getStoreLocation();
	}
	
	public static String uploadZXCOS(MultipartFile multfile, HttpServletRequest request) throws Exception {

		File cosFile = new File(FileUtil.UploadFile((CommonsMultipartFile) multfile, request));

		COSCredentials cred = new BasicCOSCredentials("AKIDLSKyOwgJBoCLrBnFwObZIrDOK3ZqtkQs",
				"QdbLq66uUwBF7k9JI2oNiofIgspb8Qaa");
		ClientConfig clientConfig = new ClientConfig(new Region("ap-guangzhou"));
		COSClient cosClient = new COSClient(cred, clientConfig);
		String bucketName = "wanma-1251498119";
		String pathName = ((CommonsMultipartFile) multfile).getFileItem().getName();
		String key = "/wmpayer/img/" + new Date().getTime()+"/"+ RandomUtil.getRandomString(12) + pathName.substring(pathName.lastIndexOf("."), pathName.length());
		String cdnUrl = "https://wanma-1251498119.cos.ap-guangzhou.myqcloud.com";
		PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, cosFile);
		cosClient.putObject(putObjectRequest);
		cosClient.shutdown();
		Date expiration = new Date(new Date().getTime() + 5 * 60 * 10000);
		String url = cdnUrl+key;
		cosFile.delete();
		GeneratePresignedUrlRequest  req = new GeneratePresignedUrlRequest("wanma-1251498119", key);
		Date expirationDate = new Date(System.currentTimeMillis() + 30L * 60L * 1000L);
		req.setExpiration(expirationDate);
		URL downloadUrl = cosClient.generatePresignedUrl(req);
		String downloadUrlStr = downloadUrl.toString();
		
		return url;
	}
	
	
	
}
