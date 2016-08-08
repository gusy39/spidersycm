package com.ecmoho.base.selenium;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;


import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;

//import com.ecmoho.sycm.schq.dao.SchqDbcom;

public class CopyOfSeleniumSpider {

	public static String login(Map<String, String> spider) {
		
//		   Map<String, String> spider = SchqDbcom.getSpider(account);
		
		    //�����˺�ģ���¼
		    String login_url = spider.get("login_url");
		    String red_url = spider.get("red_url");
		    
		    System.setProperty("webdriver.chrome.driver", "C:\\chromedriver.exe");
		  //System.setProperty("javax.xml.parsers.DocumentBuilderFactory","com.sun.org.apache.xerces.internal.jaxp.DocumentBuilderFactoryImpl");
		    WebDriver webDriver = new ChromeDriver();
		    webDriver.manage().window().maximize();
	        webDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	        
	        String cookieStr = "";
	        //���Դ���
	        int i=0;
	        int times=5;
	        try {
	        	 //���Ե�¼
		        loginPage(webDriver,spider);
		        
		    //��ͣ�ļ�⣬һ����ǰҳ��URL���ǵ�¼ҳ��URL����˵��������Ѿ���������ת
	            while (true) {
	            	
	                Thread.sleep(500L);
	                if (!webDriver.getCurrentUrl().startsWith(login_url)) {
	                	  webDriver.get(red_url);
	           	       try {
	           			   Thread.sleep(500L);
	           		    } catch (InterruptedException e) {
	           			// TODO Auto-generated catch block
	           			   e.printStackTrace();
	           		    }
	           	        webDriver.get(red_url);
	           	     try {
	           			   Thread.sleep(500L);
	           		    } catch (InterruptedException e) {
	           			// TODO Auto-generated catch block
	           			   e.printStackTrace();
	           		    }
	           		  //��ȡcookie������һ����ѭ������Ϊ�͵�¼�ɹ��ˣ���Ȼ������жϲ�̫�ϸ񣬿����ٽ����޸�
	           	        Set<Cookie> cookies = webDriver.manage().getCookies();
	           	      
	           	        for (Cookie cookie : cookies) {
	           	            cookieStr += cookie.getName() + "=" + cookie.getValue() + "; ";
	           	        }
//	           	     SchqDbcom.updateSpiderZxfxFlag(account, "1");
	           	        break;
	                }
	                else{
	                	 //���Ե�¼
	                	if(i<=times){
//	                		SchqDbcom.updateSpiderZxfxFlag(account, "0");
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

	        
	    //��ת���ɼ�ҳ��
	        
	        System.out.println(cookieStr);  
		//�˳����ر������
	        webDriver.quit();
	        return cookieStr;
	  }
	public static void loginPage(WebDriver webDriver,Map<String, String> spider ){
		String account=spider.get("account");
		String login_name = spider.get("login_name");
	    String password = spider.get("password");
	    String login_element =spider.get("login_element");
	    String[] login_elements = login_element.split("#");
	    String login_name_id = login_elements[0];
	    String password_id = login_elements[1];
	    String login_button = login_elements[2];
	    String login_url = spider.get("login_url");
		webDriver.get(login_url);

			//��Ϊ�Ա��ĵ�¼��ʵ��Ƕ����һ��iframe����ģ����Ե����л���iframe���ٲ���
		        
		        //webDriver.switchTo().frame(0);
		        //��֤��¼ҳ����ȫ��
		        try {
	    			   Thread.sleep(5040L);
	    		    } catch (InterruptedException e) {
	    		
	    			   e.printStackTrace();
	    		    }
		       webDriver.switchTo().frame("J_loginIframe");
		       Actions action=new Actions(webDriver);
			   //�����û���
		       WebElement  userElement=webDriver.findElement(By.id(login_name_id));
		       action.moveToElement(userElement);
		       action.click(userElement).build().perform();
		       action.release();
		       userElement.clear();
		       userElement.sendKeys(login_name);
		       try {
    			   Thread.sleep(3040L);
    		    } catch (InterruptedException e) {
    		
    			   e.printStackTrace();
    		    }
    		    
			    //�������� 
		        WebElement  passElement=webDriver.findElement(By.id(password_id));
		        action.moveToElement(passElement);
		        action.click(passElement).build().perform();
		        passElement.clear();
		        passElement.sendKeys(password);
		     
			//�����¼��ť
		        try {
					Thread.sleep(14000L);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		        webDriver.findElement(By.id(login_button)).click();
		        webDriver.switchTo().defaultContent();
			        try {
						Thread.sleep(10000L);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
		        
	}
	
	//Ԫ�ش�������ж�(���ж�Ԫ���Ƿ�����)
	public static boolean doesWebElementExist(WebDriver driver, By selector)
	 { 
	        try { 
	        	WebElement webElement= driver.findElement(selector); 
	            return webElement.isDisplayed(); 
	        } catch (NoSuchElementException e) { 
	            return false; 
	        } 
	    }
	
}
