package com.ecmoho.promotion.service.impl;

import com.ecmoho.base.Util.DateUtil;
import com.ecmoho.base.bean.HeaderBean;
import com.ecmoho.base.dao.SpiderAccountDao;
import com.ecmoho.base.model.SpiderSchqAcount;
import com.ecmoho.base.model.SpiderSchqChild;
import com.ecmoho.promotion.selenium.AccountantSeleniumSpider;
import com.ecmoho.promotion.service.PromotionService;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.utils.HttpConstant;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Zhangjunrui on 2016/7/6.
 * 账房获取数据
 */
@Component("accountantServiceImpl")
public class AccountantServiceImpl implements PromotionService{
    @Resource
    private SpiderAccountDao spiderAccountDao;
    @Resource(name="accountantHeaderBean")
    private HeaderBean accountantHeaderBean;
    @Resource(name="accountantProcessor")
    private PageProcessor pageProcessor;
    @Resource(name = "accountantSeleniumSpider")
    private AccountantSeleniumSpider accountantSeleniumSpider;
    @Override
    public void startGrap(int accountid, String dateStr,String grapfrequency) {
        //获取账户信息及cookie
        SpiderSchqAcount spiderSchqAccount = spiderAccountDao.getSpiderSchqAccount(accountid);
        //获取URL
        SpiderSchqChild spiderSchqChild = spiderAccountDao.getSpiderSchqChildByChildAccount("promotion-accountant");
//      String refer_cookie = spiderSchqAccount.getRefferCookie();
//      模拟登录，先写死账号，后期改造为数据库中读取

        Map<String,String> loginUrlmap=new HashMap<String, String>();
        loginUrlmap.put("login_url","https://pay.taobao.com/login.htm");
        loginUrlmap.put("red_url","https://pay.taobao.com/homePage.htm");
        loginUrlmap.put("login_name",spiderSchqAccount.getLoginName());
        loginUrlmap.put("password",spiderSchqAccount.getPassword());
        String refferCookie = accountantSeleniumSpider.getCookie(loginUrlmap);
        accountantHeaderBean.setCookie(refferCookie);
        String nowDateStr=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

        String url = spiderSchqChild.getGeturl();
//        String processorBean = spiderSchqChild.getProcessorBean();
//        PageProcessor schqProcessor= (PageProcessor) wac.getBean(processorBean);

        Calendar cal   =   Calendar.getInstance();
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse(dateStr);
        } catch (ParseException e) {
            System.out.println("输入的日期格式有误,应为yyyy-MM-dd");
        }
        cal.setTime(date);
        cal.add(Calendar.MONTH,   -1);
        String lastMonth = new SimpleDateFormat("yyyyMM").format(cal.getTime());
        Map<String, String> urlMap=new HashMap<String,String>();
        urlMap.put("accountid",accountid+"");
        urlMap.put("create_at", DateUtil.getLastMonthStartDate(dateStr));
        urlMap.put("log_at",nowDateStr);
        urlMap.put("grabfrequency","month");
        accountantHeaderBean.setUrlMap(urlMap);

        //新建请求
        Request request=new Request(url);
        Map nameValuePair = new HashMap();
        NameValuePair[] values = new NameValuePair[4];
        values[0] = new BasicNameValuePair("checkType", "monthly");

        values[1] = new BasicNameValuePair("billCycleBegin", lastMonth);
        values[2] = new BasicNameValuePair("billCycleEnd", lastMonth);

        values[3] = new BasicNameValuePair("quickSelectTime","preMonth");
        //        values[1] = new BasicNameValuePair("action", "payments/MonthlyUserBillAction");
        nameValuePair.put("nameValuePair", values);
        request.setExtras(nameValuePair);
        //设置请求方式为POST请求
        request.setMethod(HttpConstant.Method.POST);
        Spider.create(pageProcessor).addRequest(request).thread(5000).run();
    }
}