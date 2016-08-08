package com.ecmoho.sycm.schq.processor;
import javax.annotation.Resource;

import com.ecmoho.base.dao.SpiderDataDao;
import org.springframework.stereotype.Component;

import com.ecmoho.base.bean.HeaderBean;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 *获取解析页面url父类
 */
@Component("schqProcessor")
public class SchqProcessor implements PageProcessor{

	
	@Resource(name="schqHeaderBean")
	protected  HeaderBean schqHeaderBean;
    @Resource(name="spiderDataDao")
	protected SpiderDataDao spiderSchqDataDao;
	private Site site=Site.me().setTimeOut(3000).setRetryTimes(3).setSleepTime(10000);

	@Override
	public Site getSite() {
//		System.out.println(schqHeaderBean.getCookie());
		if(!"".equals(schqHeaderBean.getUserAgent())&&schqHeaderBean.getUserAgent()!=null){
			site.addHeader("user-agent", schqHeaderBean.getUserAgent());
		}
		if(!"".equals(schqHeaderBean.getAccept())&&schqHeaderBean.getAccept()!=null){
			site.addHeader("accept", schqHeaderBean.getAccept());
		}
		if(!"".equals(schqHeaderBean.getAcceptLanguage())&&schqHeaderBean.getAcceptLanguage()!=null){
			site.addHeader("accept-language", schqHeaderBean.getAcceptLanguage());
		}
		if(!"".equals(schqHeaderBean.getOrgin())&&schqHeaderBean.getOrgin()!=null){
			site.addHeader("origin", schqHeaderBean.getOrgin());
		}
		if(!"".equals(schqHeaderBean.getReferer())&&schqHeaderBean.getReferer()!=null){
			site.addHeader("referer", schqHeaderBean.getReferer());
		}
		if(!"".equals(schqHeaderBean.getCookie())&&schqHeaderBean.getCookie()!=null){
			site.addHeader("cookie", schqHeaderBean.getCookie());
		}
		return site;
	}
	@Override
	public void process(Page arg0) {
	}

}
