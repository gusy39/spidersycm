package com.ecmoho.base.Util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

import com.ecmoho.base.bean.HeaderBean;

public class UrlUtil {
	   
	   //获取抓取内容
	   public static String getUrlString(HeaderBean hb,String urlStr,String requestType) {
			URL url = null;
			HttpURLConnection http = null;
			String result = "";
			try {
				Random random = new Random();
				// 抓取频率，休息一段时间
				try {
					int rest=random.nextInt(5) * 1000;
					Thread.sleep(rest);
					System.out.println("休息时间："+rest+"s");
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				url = new URL(urlStr);
				http = (HttpURLConnection) url.openConnection();
				http.setUseCaches(false);
				http.setConnectTimeout(50000);// 超时时长
				http.setReadTimeout(50000);// ���ö�ȡ��ʱ
				http.setRequestMethod(requestType);
				http.setDoOutput(true);
				http.setDoInput(true);
				if(!"".equals(hb.getUserAgent())&&hb.getUserAgent()!=null){
					http.setRequestProperty("user-agent", hb.getUserAgent());
				}
				if(!"".equals(hb.getAccept())&&hb.getAccept()!=null){
					 http.setRequestProperty("accept", hb.getAccept());
				}
				if(!"".equals(hb.getAcceptLanguage())&&hb.getAcceptLanguage()!=null){
					http.setRequestProperty("accept-language", hb.getAcceptLanguage());
				}
				if(!"".equals(hb.getOrgin())&&hb.getOrgin()!=null){
					http.setRequestProperty("origin", hb.getOrgin());
				}
				if(!"".equals(hb.getReferer())&&hb.getReferer()!=null){
					http.setRequestProperty("referer", hb.getReferer());
				}
				if(!"".equals(hb.getCookie())&&hb.getCookie()!=null){
					http.setRequestProperty("cookie", hb.getCookie());
				}
				// http.setDoOutput(true);
				http.connect();
				if (http.getResponseCode() == 200) {
					// String set_cookie =
					// http.getFirstHeader("Set-Cookie").getValue();
					BufferedReader in = new BufferedReader(new InputStreamReader(
							http.getInputStream(), "utf-8"));
					String inputLine;
					while ((inputLine = in.readLine()) != null) {
						result += inputLine;
					}
					System.out.println(result);
					in.close();
				} else {
					// 抓取失败，返回无效
					System.out.println("抓取失败，当前cookie失效");
				}
			} catch (Exception e) {
				System.out.println("err");
			} finally {
				if (http != null)
					http.disconnect();
			}
			return result;
		}
		
}
