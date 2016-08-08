package com.ecmoho.promotion.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ecmoho.base.Util.DateUtil;
import com.ecmoho.base.Util.UrlUtil;
import com.ecmoho.base.bean.HeaderBean;
import com.ecmoho.base.dao.SpiderAccountDao;
import com.ecmoho.base.model.SpiderSchqAcount;
import com.ecmoho.base.model.SpiderSchqChild;
import com.ecmoho.promotion.selenium.SubWaySeleniumSpider;
import com.ecmoho.promotion.service.PromotionService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.*;

import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.utils.HttpConstant;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by meidejing on 2016/6/29.
 */
@Service("subwayServiceImpl")
public class SubwayServiceImpl implements PromotionService{
    private static final Log log= LogFactory.getLog(SubwayServiceImpl.class);
    @Resource
    private SpiderAccountDao spiderAccountDao;
    @Resource(name="subWayHeaderBean")
    private HeaderBean subWayHeaderBean;
    @Resource(name="subWayProcessor")
    private PageProcessor pageProcessor;
    @Resource(name="subWaySeleniumSpider")
    private SubWaySeleniumSpider subWaySeleniumSpider;
    @Override
    public void startGrap(int accountid, String dateStr,String grapfrequency) {
        SpiderSchqAcount spiderSchqAccount = spiderAccountDao.getSpiderSchqAccount(accountid);
        //获取直通车url信息
        SpiderSchqChild spiderSchqChild = spiderAccountDao.getSpiderSchqChildByChildAccount("promotion-subway");
        Map<String,String> loginUrlmap=new HashMap<String, String>();
        //数据库中获取url
        String getUrl=spiderSchqChild.getGeturl();

        //海外店铺登录及爬取URL有所不同
        if (spiderSchqAccount.getLoginName().indexOf("海外")!=-1){
            loginUrlmap.put("login_url","http://subway.simba.tmall.hk/index.jsp#!/login");
            loginUrlmap.put("red_url","http://subway.simba.tmall.hk/#!/home");
            getUrl=getUrl.replaceAll("taobao.com","tmall.hk");
            subWayHeaderBean.setReferer("http://subway.simba.tmall.hk/index.jsp");
            subWayHeaderBean.setOrgin("subway.simba.tmall.hk");
        }else{
            loginUrlmap.put("login_url","http://subway.simba.taobao.com/#!/login");
            loginUrlmap.put("red_url","http://subway.simba.taobao.com/#!/report/bpreport/index");
        }
        loginUrlmap.put("login_name",spiderSchqAccount.getLoginName());
        log.info("登录账户:"+spiderSchqAccount.getLoginName());
        loginUrlmap.put("password",spiderSchqAccount.getPassword());
//      String refferCookie = spiderSchqAccount.getRefferCookie();
//      自动登录url信息（后期存入数据库中，从数据库操作）
        String refferCookie = subWaySeleniumSpider.getCookie(loginUrlmap);
        log.info("refferCookie："+refferCookie);
        subWayHeaderBean.setCookie(refferCookie);

        String userInfoStr= UrlUtil.getUrlString(subWayHeaderBean,getUrl.split("####")[0],"POST");
        log.info("userInfoStr:"+userInfoStr);
        if ("".equals(userInfoStr)){
            log.info("Token获取失败或已失效。。。。");
            return;
        }
        String token="";
        try{
            JSONObject userInfoJsonObject = JSON.parseObject(userInfoStr);
            if ("601".equalsIgnoreCase(JSON.parseObject(userInfoStr).getString("code"))){
                //用户未登录或登录过期
                log.info("Token获取失败或已失效。。。。");
                return ;
            }
            //获取关键参数token
            token=userInfoJsonObject.getJSONObject("result").getString("token");
            if (token==null||"".equals(token)){
                log.info("token获取失败");
            }
        }catch (Exception e){

        }

        Map<String,String> urlMap=new HashMap<String,String>();
        urlMap.put("accountid",accountid+"");

        urlMap.put("grapfrequency",grapfrequency);

        if ("day".equals(grapfrequency)){
            List<String> lastFifteenDays = DateUtil.getLastFifteenDays(dateStr);
            for (String dayStr:lastFifteenDays){
                createProcessor(urlMap,token,getUrl,dayStr,dayStr);
            }
        }else if("week".equals(grapfrequency)){
            createProcessor(urlMap,token,getUrl,DateUtil.getLastWeekStartDate(dateStr),DateUtil.getLastWeekEndDate(dateStr));
        }else if ("month".equals(grapfrequency)){
            createProcessor(urlMap,token,getUrl,DateUtil.getLastMonthStartDate(dateStr),DateUtil.getLastMonthEndDate(dateStr));
        }else {
            log.info("执行周期传入有误。。。。");
            return;
        }
    }
    public void createProcessor(Map<String,String> urlMap,String token,String getUrl,String startDate,String endDate){
        String geturlArr[] = getUrl.replaceAll("##SD##",startDate).replaceAll("##ED##",endDate).split("####");
        String log_at=DateUtil.getNowDateStr("yyyy-MM-dd HH:mm:ss");
        for(int i=1;i<geturlArr.length;i++){
            String url=geturlArr[i];
            urlMap.put("create_at",startDate);
            urlMap.put("log_at",log_at);
            if(i==1){//获取直通车报表信息URL
                urlMap.put("urlType","isshop");
            }else if(i==2){//获取数据占比图信息URL
                urlMap.put("urlType","network");
            }
            subWayHeaderBean.setUrlMap(urlMap);
            Request request = new Request(url+"&token="+token);
            //设置请求方式为POST请求
            request.setMethod(HttpConstant.Method.POST);
            Spider.create(pageProcessor).addRequest(request).thread(2000).run();
        }
    }
}
