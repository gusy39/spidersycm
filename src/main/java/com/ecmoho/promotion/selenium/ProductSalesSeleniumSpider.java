package com.ecmoho.promotion.selenium;

import com.ecmoho.base.selenium.SeleniumSpider;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2016/7/8.
 */
@Component("productSalesSeleniumSpider")
public class ProductSalesSeleniumSpider extends SeleniumSpider {

    @Override
    public void loginPage(WebDriver webDriver, Map<String, String> spider) {
//        String login_name = "哈药官方旗舰店";
//        String password = "yiheng@hayao%*";
        String login_name = spider.get("login_name");
        String password = spider.get("password");
        String change_static = "J_Quick2Static";
        String login_name_id = "TPL_username_1";
        String password_id = "TPL_password_1";
        String login_button = "J_SubmitStatic";
        String login_url = "http://branding.taobao.com/#!/report/index";


        try {
            int i=0;
            int times=4;
            webDriver.get(login_url);
            Thread.sleep(10040L);
            while (i<times) {
                if (doesWebElementExistBySelector(webDriver, By.tagName("iframe"))) {
                    WebElement frame = webDriver.findElement(By.tagName("iframe"));
                    webDriver.switchTo().frame(frame);
                    Thread.sleep(5000L);
                    //点击跳转到密码登陆
                    webDriver.findElement(By.id(change_static)).click();
                    Thread.sleep(5000L);
                    WebElement userElement = webDriver.findElement(By.id(login_name_id));
                    userElement.sendKeys(login_name);
                    Thread.sleep(3040L);
                    WebElement passElement = webDriver.findElement(By.id(password_id));
                    passElement.sendKeys(password);
                    Thread.sleep(3000L);
                    WebElement moveElement = webDriver.findElement(By.id("nc_1_n1z"));
                    if (doesWebElementExist(moveElement)) {
                        Actions action = new Actions(webDriver);
                        // action.dragAndDrop(target,260);
                        action.click(webDriver.findElement(By.id("nc_1_n1z")));
                        action.dragAndDropBy(moveElement, 210, 0).build().perform();
                        Thread.sleep(8000L);
                    }
                    WebElement ensureElement = webDriver.findElement(By.id("nc_1__btn_2"));
                    if (!doesWebElementExist(ensureElement)) {
                        webDriver.findElement(By.id(login_button)).click();
                        Thread.sleep(10000L);
                    }
                    Thread.sleep(5000L);
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
        } catch (InterruptedException e1) {
            webDriver.quit();
            e1.printStackTrace();
        }
    }
}
