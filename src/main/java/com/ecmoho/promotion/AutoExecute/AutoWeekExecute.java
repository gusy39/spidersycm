package com.ecmoho.promotion.AutoExecute;

import com.ecmoho.base.Util.DateUtil;
import com.ecmoho.base.dao.SpiderAccountDao;
import com.ecmoho.base.model.SpiderSchqAcount;
import com.ecmoho.promotion.service.PromotionService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * Created by meidejing on 2016/7/11.
 */
@Component("autoWeekExecute")
public class AutoWeekExecute {
    private static final Log log= LogFactory.getLog(AutoWeekExecute.class);
    @Resource
    private SpiderAccountDao spiderAccountDao;
    @Resource(name = "subwayServiceImpl")
    private PromotionService subwayService;
    @Resource(name = "taobaoCustomersServiceImpl")
    private PromotionService taobaoCustomersService;
    @Resource(name = "majibaoServiceImpl")
    private PromotionService majibaoService;
    @Resource(name = "productBaoServiceImpl")
    private PromotionService productBaoService;
    @Resource(name = "diamondBoothServiceImpl")
    private PromotionService diamondService;
    @Resource(name = "accountantServiceImpl")
    private PromotionService accountantService;
    //直通车
    public void subwayWeekRun(){
        log.info("开始执行直通车按周爬取。。。。。。");
        List<SpiderSchqAcount> spiderSchqAccountList = spiderAccountDao.getAllSpiderSchqAccount();
        for (SpiderSchqAcount spiderSchqAcount:spiderSchqAccountList){
            if ("1".equals(spiderSchqAcount.getPromotionSubwayFlag())) {
                log.info("店铺："+spiderSchqAcount.getLoginName()+"，"+"开始直通车按周数据爬取。。。");
                subwayService.startGrap(spiderSchqAcount.getSid(), DateUtil.getNowDateStr("yyyy-MM-dd"),DateUtil.WEEK);
            }
        }
    }
    //淘宝客
    public void taobaoCustomersWeekRun(){
        log.info("开始执行淘宝客按周爬取。。。。。。");
        List<SpiderSchqAcount> spiderSchqAccountList = spiderAccountDao.getAllSpiderSchqAccount();
        for (SpiderSchqAcount spiderSchqAcount:spiderSchqAccountList) {
            if ("1".equals(spiderSchqAcount.getPromotionTaobaocustomersFlag())){
                log.info("店铺："+spiderSchqAcount.getLoginName()+"，"+"开始淘宝客按周数据爬取。。。");
                taobaoCustomersService.startGrap(spiderSchqAcount.getSid(), DateUtil.getNowDateStr("yyyy-MM-dd"),DateUtil.WEEK);
            }
        }
    }
    //麻吉宝
    public void majibaoWeekRun(){
        log.info("开始执行麻吉宝按周爬取。。。。。。");
        List<SpiderSchqAcount> spiderSchqAccountList = spiderAccountDao.getAllSpiderSchqAccount();
        for (SpiderSchqAcount spiderSchqAcount:spiderSchqAccountList) {
            if ("1".equals(spiderSchqAcount.getPromotionMajibaoFlag())){
                log.info("店铺："+spiderSchqAcount.getLoginName()+"，"+"开始麻吉宝按周数据爬取。。。");
                majibaoService.startGrap(spiderSchqAcount.getSid(),DateUtil.getNowDateStr("yyyy-MM-dd"),DateUtil.WEEK);
            }
        }
    }
    //品销宝
    public void productBaoWeekRun(){
        log.info("开始执行品销宝按周爬取。。。。。。");
        List<SpiderSchqAcount> spiderSchqAccountList = spiderAccountDao.getAllSpiderSchqAccount();
        for (SpiderSchqAcount spiderSchqAcount:spiderSchqAccountList) {
            if ("1".equals(spiderSchqAcount.getPromotionProductFlag())) {
                log.info("店铺："+spiderSchqAcount.getLoginName()+"，"+"开始品销宝按周数据爬取。。。");
                productBaoService.startGrap(spiderSchqAcount.getSid(),  DateUtil.getNowDateStr("yyyy-MM-dd"),DateUtil.WEEK);
            }
        }
    }
    //钻展
    public void diamondWeekRun(){
        log.info("开始执行钻石展按周爬取。。。。。。");
        List<SpiderSchqAcount> spiderSchqAccountList = spiderAccountDao.getAllSpiderSchqAccount();
        for (SpiderSchqAcount spiderSchqAcount:spiderSchqAccountList) {
            if ("1".equalsIgnoreCase(spiderSchqAcount.getPromotionDiamondFlag())) {
                log.info("店铺："+spiderSchqAcount.getLoginName()+"，"+"开始钻石展按周数据爬取。。。");
                diamondService.startGrap(spiderSchqAcount.getSid(),  DateUtil.getNowDateStr("yyyy-MM-dd"),DateUtil.WEEK);
            }
        }
    }
}
