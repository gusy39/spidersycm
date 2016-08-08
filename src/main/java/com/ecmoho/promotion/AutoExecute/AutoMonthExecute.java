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
@Component("autoMonthExecute")
public class AutoMonthExecute {
    private static final Log log= LogFactory.getLog(AutoMonthExecute.class);
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
    public void subwayMonthRun(){
        log.info("开始执行直通车按月爬取。。。。。。");
        List<SpiderSchqAcount> spiderSchqAccountList = spiderAccountDao.getAllSpiderSchqAccount();
        for (SpiderSchqAcount spiderSchqAcount:spiderSchqAccountList){
            if ("1".equals(spiderSchqAcount.getPromotionSubwayFlag())) {
                log.info("店铺："+spiderSchqAcount.getLoginName()+"，"+"开始按月直通车数据爬取。。。");
                subwayService.startGrap(spiderSchqAcount.getSid(), DateUtil.getNowDateStr("yyyy-MM-dd"),DateUtil.MONTH);
            }
        }
    }
    //淘宝客
    public void taobaoCustomersMonthRun(){
        log.info("开始执行淘宝客按月爬取。。。。。。");
        List<SpiderSchqAcount> spiderSchqAccountList = spiderAccountDao.getAllSpiderSchqAccount();
        for (SpiderSchqAcount spiderSchqAcount:spiderSchqAccountList) {
            if ("1".equals(spiderSchqAcount.getPromotionTaobaocustomersFlag())){
                log.info("店铺："+spiderSchqAcount.getLoginName()+"，"+"开始按月淘宝客数据爬取。。。");
                taobaoCustomersService.startGrap(spiderSchqAcount.getSid(), DateUtil.getNowDateStr("yyyy-MM-dd"),DateUtil.MONTH);
            }
        }

    }
    //麻吉宝
    public void majibaoMonthRun(){
        log.info("开始执行麻吉宝按月爬取。。。。。。");
        List<SpiderSchqAcount> spiderSchqAccountList = spiderAccountDao.getAllSpiderSchqAccount();
        for (SpiderSchqAcount spiderSchqAcount:spiderSchqAccountList) {
            if ("1".equals(spiderSchqAcount.getPromotionMajibaoFlag())){
                log.info("店铺："+spiderSchqAcount.getLoginName()+"，"+"开始按月麻吉宝数据爬取。。。");
                majibaoService.startGrap(spiderSchqAcount.getSid(), DateUtil.getNowDateStr("yyyy-MM-dd"),DateUtil.MONTH);
            }
        }
    }
    //品销宝
    public void productBaoMonthRun(){
        log.info("开始执行品销宝按月爬取。。。。。。");
        List<SpiderSchqAcount> spiderSchqAccountList = spiderAccountDao.getAllSpiderSchqAccount();
        for (SpiderSchqAcount spiderSchqAcount:spiderSchqAccountList) {
            if ("1".equals(spiderSchqAcount.getPromotionProductFlag())) {
                log.info("店铺："+spiderSchqAcount.getLoginName()+"，"+"开始按月品销宝数据爬取。。。");
                productBaoService.startGrap(spiderSchqAcount.getSid(), DateUtil.getNowDateStr("yyyy-MM-dd"),DateUtil.MONTH);
            }
        }
    }
    //钻展
    public void diamondMonthRun(){
        log.info("开始执行钻石展按月爬取。。。。。。");
        List<SpiderSchqAcount> spiderSchqAccountList = spiderAccountDao.getAllSpiderSchqAccount();
        for (SpiderSchqAcount spiderSchqAcount:spiderSchqAccountList) {
            if ("1".equalsIgnoreCase(spiderSchqAcount.getPromotionDiamondFlag())) {
                log.info("店铺："+spiderSchqAcount.getLoginName()+"，"+"开始按月钻石展数据爬取。。。");
                diamondService.startGrap(spiderSchqAcount.getSid(), DateUtil.getNowDateStr("yyyy-MM-dd"),DateUtil.MONTH);
            }
        }
    }
    //账房
    public void accountantMonthRun(){
        log.info("开始执行账房按月爬取。。。。。。");
        List<SpiderSchqAcount> spiderSchqAccountList = spiderAccountDao.getAllSpiderSchqAccount();
        for (SpiderSchqAcount spiderSchqAcount:spiderSchqAccountList) {
            if ("1".equals(spiderSchqAcount.getPromotionAccountantFlag())) {
                log.info("店铺："+spiderSchqAcount.getLoginName()+"，"+"开始按月账房数据爬取。。。");
                accountantService.startGrap(spiderSchqAcount.getSid(), DateUtil.getNowDateStr("yyyy-MM-dd"),DateUtil.MONTH);
            }
        }
    }
}
