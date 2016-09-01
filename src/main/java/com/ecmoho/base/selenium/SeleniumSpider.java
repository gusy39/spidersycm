package com.ecmoho.base.selenium;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.ecmoho.base.Util.StringUtil;
import org.openqa.selenium.chrome.ChromeDriver;

//ϵͳ��¼ģ��
public abstract class SeleniumSpider {
	//��ȡwebDriver,����ʵ��
	public final WebDriver getWebDriver(){
//		System.setProperty("webdriver.chrome.driver", "C:\\chromedriver.exe");
		//System.setProperty("javax.xml.parsers.DocumentBuilderFactory","com.sun.org.apache.xerces.internal.jaxp.DocumentBuilderFactoryImpl");
		WebDriver webDriver= new ChromeDriver();
//		webDriver.manage().window().maximize();
		webDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		return webDriver;
	};
	//ҳ���¼�߼�,����ʵ��
	public abstract void loginPage(WebDriver webDriver,Map<String, String> spider);
	
	public final String getCookie(Map<String, String> spider){
		WebDriver webDriver=getWebDriver();
		String cookie=login(webDriver, spider);
		webDriver.quit();
		return cookie;
	}
	//尝试登录，获取cookie
	public final String login(WebDriver webDriver,Map<String, String> spider){
		String login_url=spider.get("login_url");
		String red_url=spider.get("red_url");
		  //�����˺�ģ���¼
		  String cookieStr = "";
	        //���Դ���
	        int times=5;
	        int i=1;
	        try {
	        	 //���Ե�¼
		        loginPage(webDriver,spider);
		        //��ͣ�ļ�⣬һ����ǰҳ��URL���ǵ�¼ҳ��URL����˵��������Ѿ���������ת
	            while (i<times) {
	                Thread.sleep(500L);
	                if (!webDriver.getCurrentUrl().startsWith(login_url)) {
	                	  webDriver.get(red_url);
	           			   Thread.sleep(4000L);
	           	           //webDriver.get(red_url);
	           			   //Thread.sleep(2000L);
	           		       //获取cookie
	           	           Set<Cookie> cookies = webDriver.manage().getCookies();
						   for (Cookie cookie : cookies) {
							   cookieStr += cookie.getName() + "=" + cookie.getValue() + "; ";
						   }
	           	        break;
	                }
	                else{
	                	 //登录失败重试
	                	if(i<=times){
	                		i++;
	                		loginPage(webDriver,spider);
	                	}else{
	                		break;
	                	}
	                }
	            }
	        } catch (InterruptedException e) {
	            e.printStackTrace();
	            return "";
	        }
		    //�˳����ر������
	        webDriver.quit();
	        return cookieStr;
	}
    //判断元素是否存在
	public static boolean doesWebElementExist(WebElement webElement) {
		try {
			return webElement.isDisplayed();
		} catch (NoSuchElementException e) {
			return false;
		}
	}
	public static boolean doesWebElementExistBySelector(WebDriver driver, By selector)
	{
		try
		{
			driver.findElement(selector);
			return true;
		}
		catch (NoSuchElementException e)
		{
			return false;
		}
	}
	
}
