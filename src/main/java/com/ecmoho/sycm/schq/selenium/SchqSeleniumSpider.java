package com.ecmoho.sycm.schq.selenium;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import com.ecmoho.base.Util.StringUtil;
import com.ecmoho.base.selenium.SeleniumSpider;
@Component("schqSeleniumSpider")
public class SchqSeleniumSpider extends SeleniumSpider {
	
	



	@Override
	public void loginPage(WebDriver webDriver, Map<String, String> spider) {
		String startType="";
		if("auto".equalsIgnoreCase(startType)){
			String login_name = StringUtil.objectVerString(spider.get("login_name"));
		    String password = StringUtil.objectVerString(spider.get("password"));
		    String login_element =StringUtil.objectVerString(spider.get("login_element"));
		    String[] login_elements = login_element.split("#");
		    String login_name_id = login_elements[0];
		    String password_id = login_elements[1];
		    String login_button = login_elements[2];
		    String login_url = StringUtil.objectVerString(spider.get("login_url"));
			webDriver.get(login_url);
	
				//��Ϊ�Ա��ĵ�¼��ʵ��Ƕ����һ��iframe����ģ����Ե����л���iframe���ٲ���
			  try {
			        //webDriver.switchTo().frame(0);
			        //��֤��¼ҳ����ȫ��
		    	   Thread.sleep(5040L);
		    		  
		    			   //			        J_Quick2Static
			        WebElement  pcElement=webDriver.findElement(By.id("J_Quick2Static"));
			        if(doesWebElementExist(pcElement)){
			        	pcElement.click();
			        }
	//		       webDriver.switchTo().frame("J_loginIframe");
//			       Actions action=new Actions(webDriver);
				   //�����û���
			        WebElement  userElement=webDriver.findElement(By.id(login_name_id));
	//		       action.moveToElement(userElement);
	//		       action.click(userElement).build().perform();
	//		       action.release();
	//		       userElement.clear();
			       userElement.sendKeys(login_name);
	    		   Thread.sleep(3040L);
				    //�������� 
			        WebElement  passElement=webDriver.findElement(By.id(password_id));
	//		        action.moveToElement(passElement);
	//		        action.click(passElement).build().perform();
	//		        passElement.clear();
			        passElement.sendKeys(password);
			        //�����¼��ť
					Thread.sleep(3000L);
		            webDriver.findElement(By.id(login_button)).click();
		            webDriver.switchTo().defaultContent();
				}catch (InterruptedException e1) {
					 webDriver.quit();
					 e1.printStackTrace();
				}    
		    }else if("manual".equalsIgnoreCase(startType)){
		    	
			      String login_url = StringUtil.objectVerString(spider.get("login_url"));
				  webDriver.get(login_url);
		
					//��Ϊ�Ա��ĵ�¼��ʵ��Ƕ����һ��iframe����ģ����Ե����л���iframe���ٲ���
				        
			        //webDriver.switchTo().frame(0);
			        //��֤��¼ҳ����ȫ��
		          try {
	    			      Thread.sleep(2040L);
		                  webDriver.get(login_url);
	    			      Thread.sleep(200000);
	    		       }catch (InterruptedException e) {
	    			      e.printStackTrace();
	    			      webDriver.quit();
	    		       }
		      }
	    }

}
