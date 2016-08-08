package com.ecmoho.promotion.service.impl;

import com.ecmoho.base.Util.DateUtil;
import com.ecmoho.base.bean.HeaderBean;
import com.ecmoho.base.dao.SpiderAccountDao;
import com.ecmoho.base.model.SpiderSchqAcount;
import com.ecmoho.base.model.SpiderSchqChild;
import com.ecmoho.promotion.selenium.TaoBaoCustomersSeleniumSpider;
import com.ecmoho.promotion.service.PromotionService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.utils.HttpConstant;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by meidejing on 2016/6/29.
 * 淘宝客service
 */
@Service("taobaoCustomersServiceImpl")
public class TaobaoCustomersServiceImpl implements PromotionService{
    private static final Log log= LogFactory.getLog(TaobaoCustomersServiceImpl.class);
    @Resource
    private SpiderAccountDao spiderAccountDao;
    @Resource(name="taobaoCustomersHeaderBean")
    private HeaderBean taobaoCustomersHeaderBean;
    @Resource(name="taobaoCustomersProcessor")
    private PageProcessor pageProcessor;
    @Resource(name = "taoBaoCustomersSeleniumSpider")
    private TaoBaoCustomersSeleniumSpider taoBaoCustomersSeleniumSpider;
    @Override
    public void startGrap(int accountid, String dateStr,String grapfrequency) {
        SpiderSchqAcount spiderSchqAccount = spiderAccountDao.getSpiderSchqAccount(accountid);
        //获取直通车url信息

        SpiderSchqChild spiderSchqChild = spiderAccountDao.getSpiderSchqChildByChildAccount("promotion-taobaocustomers");
//      String refferCookie = spiderSchqAccount.getRefferCookie();
        //先写死登录url，后面配置数据库中，重新读取
        Map<String,String> loginUrlmap=new HashMap<String, String>();
        loginUrlmap.put("login_url","http://www.alimama.com/member/login.htm?forward=http%3A%2F%2Fad.alimama.com%2Fmyunion.htm%23!%2Freport%2Ftaoke_order%2F");
        loginUrlmap.put("red_url","http://ad.alimama.com/myunion.htm");
        loginUrlmap.put("login_name",spiderSchqAccount.getLoginName());
        loginUrlmap.put("password",spiderSchqAccount.getPassword());
        //实现自动登录获取cookie功能
        String refferCookie = taoBaoCustomersSeleniumSpider.getCookie(loginUrlmap);
        taobaoCustomersHeaderBean.setCookie(refferCookie);
        String getUrl=spiderSchqChild.getGeturl();
        Map<String, String> urlMap=new HashMap<String,String>();
        urlMap.put("accountid",accountid+"");
        urlMap.put("grapfrequency",grapfrequency);

        if ("day".equals(grapfrequency)){
            List<String> lastFifteenDays = DateUtil.getLastFifteenDays(dateStr);
            for (String dayStr:lastFifteenDays){
                createProcessor(urlMap,getUrl,dayStr,dayStr);
            }
        }else if ("week".equals(grapfrequency)){
            createProcessor(urlMap,getUrl,DateUtil.getLastWeekStartDate(dateStr),DateUtil.getLastWeekEndDate(dateStr));
        }else if ("month".equals(grapfrequency)){
            createProcessor(urlMap,getUrl,DateUtil.getLastMonthStartDate(dateStr),DateUtil.getLastMonthEndDate(dateStr));
        }else {
            log.info("执行周期传入有误。。。。");
            return;
        }
    }

    public void createProcessor(Map<String, String> urlMap,String getUrl,String startDate,String endDate){
        urlMap.put("create_at",startDate);
        urlMap.put("log_at",DateUtil.getNowDateStr("yyyy-MM-dd HH:mm:ss"));
        taobaoCustomersHeaderBean.setUrlMap(urlMap);
        //新建请求
        Request request=new Request(getUrl);
        Map nameValuePair = new HashMap();
        NameValuePair[] values = new NameValuePair[2];
        values[0] = new BasicNameValuePair("startTime", startDate);
        values[1] = new BasicNameValuePair("endTime", endDate);
        nameValuePair.put("nameValuePair", values);
        request.setExtras(nameValuePair);
        //设置请求方式为POST请求
        request.setMethod(HttpConstant.Method.POST);
        Spider.create(pageProcessor).addRequest(request).thread(2000).run();
    }
}
