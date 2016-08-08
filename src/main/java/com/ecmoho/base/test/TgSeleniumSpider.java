package com.ecmoho.base.test;

import java.awt.event.InputEvent;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.spec.KeySpec;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
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

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.InputEvent;


@Component("tgSeleniumSpider")

/**
 * Created by liubin on 2016/6/30.
 */
public class TgSeleniumSpider{

    private Robot robot;

    public WebDriver getWebDriver() {
        System.setProperty("webdriver.chrome.driver", "C:\\chromedriver.exe");
        //System.setProperty("javax.xml.parsers.DocumentBuilderFactory","com.sun.org.apache.xerces.internal.jaxp.DocumentBuilderFactoryImpl");
        WebDriver webDriver=null;

        webDriver= new ChromeDriver();
        webDriver.manage().window().maximize();
        webDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        return webDriver;
    }

    public void loginPage(WebDriver webDriver) {

        String login_name = "campbells金宝汤旗舰店:推广";
        String password = "ecmoho111";
        String login_element ="";
        String login_name_id = "TPL_username_1";
        String password_id = "TPL_password_1";
        String login_button = "J_SubmitStatic";
        String login_url = "http://zhitongche.taobao.com/index.html";
        webDriver.get(login_url);

        //鍒囨崲鍒癷frame
        try {
            Thread.sleep(10000L);
            Actions action = new Actions(webDriver);
            webDriver.switchTo().defaultContent();
            WebElement frame = webDriver.findElement(By.id("login_iframe"));
            webDriver.switchTo().frame(frame);

            WebElement  userElement=webDriver.findElement(By.id(login_name_id));
            userElement.sendKeys(login_name);
            Thread.sleep(1050L);
            action.sendKeys(Keys.ENTER);
            //action.keyUp(Keys.ENTER);
            Thread.sleep(3040L);
            //锟斤拷锟斤拷锟斤拷锟斤拷
            WebElement  passElement=webDriver.findElement(By.id(password_id));
            passElement.sendKeys(password);
            //锟斤拷锟斤拷锟铰硷拷锟脚�
            Thread.sleep(3000L);
            //妯℃嫙婊戝潡
            //纭畾婊戝潡浣嶅瓙
            WebElement target=webDriver.findElement(By.id("nocaptcha"));

            // action.dragAndDrop(target,260);
            action.click(webDriver.findElement(By.id("nocaptcha")));
            action.moveToElement(target,260,0);
            action.release();
            //妯℃嫙榧犳爣鎿嶄綔
//                int x =0;
//                int y =0;
//
//
//                robot.mouseMove(x, y);
//                robot.mousePress(InputEvent.BUTTON1_MASK);
//                robot.delay(100);
//                robot.mouseMove(x, y);
//                robot.mouseRelease(InputEvent.BUTTON1_MASK);
            //鍒ゆ柇婊戝潡鏄惁鎴愬姛

            webDriver.findElement(By.id(login_button)).click();
            webDriver.switchTo().defaultContent();

//            Thread.sleep(5000L);
//            webDriver.switchTo().frame(frame);
//            // action.dragAndDrop(target,260);
//            action.click(webDriver.findElement(By.id("nocaptcha")));
//            action.moveToElement(target,260,0);
//            action.release();

        }catch (InterruptedException e1) {
            webDriver.quit();
            e1.printStackTrace();
        }

    }
    public static void main(String[] args) {
        TgSeleniumSpider tgSeleniumSpider=new TgSeleniumSpider();
        WebDriver webDriver = tgSeleniumSpider.getWebDriver();
        tgSeleniumSpider.loginPage(webDriver);


    }



}