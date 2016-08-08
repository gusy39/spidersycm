package com.ecmoho.promotion.service.impl;

import com.alibaba.fastjson.JSON;
import com.ecmoho.base.Util.DateUtil;
import com.ecmoho.base.Util.UrlUtil;
import com.ecmoho.base.bean.HeaderBean;
import com.ecmoho.base.dao.SpiderAccountDao;
import com.ecmoho.base.model.SpiderSchqAcount;
import com.ecmoho.base.model.SpiderSchqChild;
import com.ecmoho.promotion.selenium.MajibaoSeleniumSpider;
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
@Service("majibaoServiceImpl")
public class MajibaoServiceImpl implements PromotionService{
    private static final Log log= LogFactory.getLog(MajibaoServiceImpl.class);
    @Resource
    private SpiderAccountDao spiderAccountDao;
    @Resource(name="majibaoHeaderBean")
    private HeaderBean majibaoHeaderBean;
    @Resource(name="majibaoProcessor")
    private PageProcessor pageProcessor;
    @Resource(name = "majibaoSeleniumSpider")
    private MajibaoSeleniumSpider majibaoSeleniumSpider;
    @Override
    public void startGrap(int accountid, String dateStr,String grapfrequency) {
        SpiderSchqAcount spiderSchqAccount = spiderAccountDao.getSpiderSchqAccount(accountid);
        //获取直通车url信息

        SpiderSchqChild spiderSchqChild = spiderAccountDao.getSpiderSchqChildByChildAccount("promotion-majibao");
//        String refferCookie = spiderSchqAccount.getRefferCookie();
        //后期要实现自动登录获取cookie功能
        Map<String,String> loginUrlmap=new HashMap<String, String>();

        loginUrlmap.put("login_url","http://www.alimama.com/member/login.htm?forward=http%3A%2F%2Fmajibao.alimama.com%2Fmyunion.htm");
        loginUrlmap.put("red_url","http://majibao.alimama.com/myunion.htm");
        loginUrlmap.put("login_name",spiderSchqAccount.getLoginName());
        loginUrlmap.put("password",spiderSchqAccount.getPassword());
        String refferCookie = majibaoSeleniumSpider.getCookie(loginUrlmap);
        majibaoHeaderBean.setCookie(refferCookie);


        String getUrlArr[]=spiderSchqChild.getGeturl().split("####");
        String loginMessageUrl=getUrlArr[0];
        String taskDetailReportUrl=getUrlArr[1];
        String loginMessage= UrlUtil.getUrlString(majibaoHeaderBean,loginMessageUrl,"POST");
        if ("".equals(loginMessage)){
            log.info("cookie没正确获取或失效。。");
            return;
        }
        String shopKeeperId ="";
        //获取店铺id
        try {
            shopKeeperId =JSON.parseObject(loginMessage).getJSONObject("data").getString("shopKeeperId");
        }catch (Exception e){
              log.info("json解析异常或cookie未正常获取");
        }
        if (shopKeeperId==null||"".equals(shopKeeperId)){
            log.info("cookie没正确获取或失效。。");
            return;
        }
        Map<String, String> urlMap=new HashMap<String,String>();
        urlMap.put("accountid",accountid+"");
        urlMap.put("grapfrequency",grapfrequency);

        if("day".equals(grapfrequency)){
            List<String> lastFifteenDays = DateUtil.getLastFifteenDays(dateStr);
            for (String dayStr:lastFifteenDays){
                createProcessor(urlMap,shopKeeperId,taskDetailReportUrl,dayStr,dayStr);
            }
        }else if("week".equals(grapfrequency)){
            createProcessor(urlMap,shopKeeperId,taskDetailReportUrl,DateUtil.getLastWeekStartDate(dateStr),DateUtil.getLastWeekEndDate(dateStr));
        }else if ("month".equals(grapfrequency)){
            createProcessor(urlMap,shopKeeperId,taskDetailReportUrl,DateUtil.getLastMonthStartDate(dateStr),DateUtil.getLastMonthEndDate(dateStr));
        }else {
            log.info("传入执行周期有误。。。。");
            return;
        }
    }
    public void createProcessor(Map<String,String> urlMap,String shopKeeperId,String taskDetailReportUrl,String startDate,String endDate){
        urlMap.put("create_at",startDate);
        urlMap.put("log_at",DateUtil.getNowDateStr("yyyy-MM-dd HH:mm:ss"));
        majibaoHeaderBean.setUrlMap(urlMap);


        //新建请求
        Request request=new Request(taskDetailReportUrl);
        Map nameValuePair = new HashMap();
        NameValuePair[] values = new NameValuePair[7];
        values[0] = new BasicNameValuePair("startTime", startDate);
        values[1] = new BasicNameValuePair("endTime", endDate);
        values[2] = new BasicNameValuePair("shopKeeperId", shopKeeperId);
        values[3] = new BasicNameValuePair("taskStep", "1");
        values[4] = new BasicNameValuePair("pageSize", "10");
        values[5] = new BasicNameValuePair("pageNo", "1");
        values[6] = new BasicNameValuePair("queryType", "0");
        nameValuePair.put("nameValuePair", values);
        request.setExtras(nameValuePair);
        //设置请求方式为POST请求
        request.setMethod(HttpConstant.Method.POST);
        Spider.create(pageProcessor).addRequest(request).thread(2000).run();
    }
}
