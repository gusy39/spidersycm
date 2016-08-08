package com.ecmoho.promotion.selenium;

import com.ecmoho.base.selenium.SeleniumSpider;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by meidejing on 2016/7/8.
 * 直通车模拟登录
 */
//
@Component("subWaySeleniumSpider")
public class SubWaySeleniumSpider extends SeleniumSpider{

    public static void main(String[] args) {
        SubWaySeleniumSpider subWaySeleniumSpider=new SubWaySeleniumSpider();
        Map<String,String> loginUrlmap=new HashMap<String, String>();
        loginUrlmap.put("login_url","http://subway.simba.tmall.hk/index.jsp#!/login");
        loginUrlmap.put("red_url","http://subway.simba.tmall.hk/#!/home");

        loginUrlmap.put("login_name","vitabiotics海外旗舰店:技术");
        loginUrlmap.put("password","ecmoho123");
        String refferCookie = subWaySeleniumSpider.getCookie(loginUrlmap);
        System.out.println(refferCookie);
    }
    @Override
    public void loginPage(WebDriver webDriver, Map<String, String> spider) {
//      暂时先抓取哈药官方旗舰店数据，后期再调（暂定写死）
//        String login_name = "哈药官方旗舰店:小q";
//        String password = "hayao@jishu";
        String login_name = spider.get("login_name");
        String password = spider.get("password");
        String iframe_classname="login-ifr";
        String login_name_id = "TPL_username_1";
        String password_id = "TPL_password_1";
        String login_button_id = "J_SubmitStatic";
        String login_url=spider.get("login_url");

        try {
            int i=0;
            int times=4;
            webDriver.get(login_url);
            Thread.sleep(10040L);
            while (i<times) {
                //      webDriver.switchTo().frame("J_loginIframe");
                if (doesWebElementExistBySelector(webDriver, By.className(iframe_classname))) {
                    webDriver.switchTo().frame(webDriver.findElement(By.className(iframe_classname)));
                    //�����û���
                    WebElement userElement = webDriver.findElement(By.id(login_name_id));
                    userElement.clear();
                    userElement.sendKeys(login_name);
                    Thread.sleep(3040L);
                    WebElement passElement = webDriver.findElement(By.id(password_id));
                    passElement.clear();
                    passElement.sendKeys(password);
                    passElement.click();
                    //�����¼��ť
                    Thread.sleep(4000L);

                    WebElement moveElement = webDriver.findElement(By.id("nc_1__bg"));
                    if (doesWebElementExist(moveElement)) {
                        Actions action = new Actions(webDriver);
                        // action.dragAndDrop(target,260);
                        action.click(webDriver.findElement(By.id("nc_1__bg")));
                        action.dragAndDropBy(moveElement, 260, 0).build().perform();
                        Thread.sleep(8000L);
                    }
                    WebElement ensureElement = webDriver.findElement(By.id("nc_1__btn_2"));
                    if (!doesWebElementExist(ensureElement)) {
                        webDriver.findElement(By.id(login_button_id)).click();
                        Thread.sleep(10000L);
                    }
                    webDriver.switchTo().defaultContent();
                    break;
                }else {
                    if(i<=times){
                        i++;
                        webDriver.get(login_url);
                        Thread.sleep(10040L);
                    }else{
                        break;
                    }
                }
            }
        } catch (InterruptedException e) {
            webDriver.quit();
            e.printStackTrace();
        }

    }
}
