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
 * 麻吉宝模拟登录
 */
@Component("majibaoSeleniumSpider")
public class MajibaoSeleniumSpider extends SeleniumSpider{
    public static void main(String[] args) {
        MajibaoSeleniumSpider majibaoSeleniumSpider=new MajibaoSeleniumSpider();
        Map<String,String> loginUrlmap=new HashMap<String, String>();
        loginUrlmap.put("login_url","http://www.alimama.com/member/login.htm?forward=http%3A%2F%2Fmajibao.alimama.com%2Fmyunion.htm");
        loginUrlmap.put("red_url","http://majibao.alimama.com/myunion.htm");
        loginUrlmap.put("login_name","昂立旗舰店:技术");
        loginUrlmap.put("password","onlly2016");
        String refferCookie = majibaoSeleniumSpider.getCookie(loginUrlmap);
        System.out.println(refferCookie);
    }
    @Override
    public void loginPage(WebDriver webDriver, Map<String, String> spider) {
//      暂时先抓取哈药官方旗舰店数据，后期再调（暂定写死）
//        String login_name = "哈药官方旗舰店";
//        String password = "yiheng@hayao%*";
        String login_name = spider.get("login_name");
        String password = spider.get("password");
        String iframe_name="taobaoLoginIfr";
        String pcId="J_Quick2Static";
        String login_name_id = "TPL_username_1";
        String password_id = "TPL_password_1";
        String login_button_id = "J_SubmitStatic";
        String login_url=spider.get("login_url");

        try {
            int i = 0;
            int times = 4;
            webDriver.get(login_url);
            Thread.sleep(10040L);
            while (i<times) {
                    if (doesWebElementExistBySelector(webDriver, By.name(iframe_name))) {
                        webDriver.switchTo().frame(webDriver.findElement(By.name(iframe_name)));
                        WebElement pcElement = webDriver.findElement(By.id(pcId));
                        if (doesWebElementExist(pcElement)) {
                            pcElement.click();
                        }
                        WebElement userElement = webDriver.findElement(By.id(login_name_id));
                        userElement.clear();
                        userElement.sendKeys(login_name);
                        userElement.click();
                        Thread.sleep(3040L);
                        WebElement passElement = webDriver.findElement(By.id(password_id));
                        passElement.clear();
                        passElement.sendKeys(password);
                        passElement.click();
                        Thread.sleep(4000L);
                        WebElement moveElement = webDriver.findElement(By.id("nc_1_n1z"));
                        if (doesWebElementExist(moveElement)) {
                            Actions action = new Actions(webDriver);
                            // action.dragAndDrop(target,260);
                            action.click(webDriver.findElement(By.id("nc_1_n1z")));
                            action.dragAndDropBy(moveElement, 260, 0).build().perform();
                            Thread.sleep(15000L);
                        }
                        WebElement ensureElement = webDriver.findElement(By.id("nc_1__btn_2"));
                        if (!doesWebElementExist(ensureElement)) {
                            webDriver.findElement(By.id(login_button_id)).click();
                            Thread.sleep(10000L);
                        }
                        Thread.sleep(10000L);
                        webDriver.switchTo().defaultContent();
                        break;
                    }else{
                        if(i<=times){
                            i++;
                            webDriver.get(login_url);
                            Thread.sleep(10040L);
                        }else{
                            break;
                        }
                    }
                 }
               }catch(InterruptedException e){
                  webDriver.quit();
                  e.printStackTrace();
              }
        }
}
