package com.ecmoho.sycm.schq.exploration;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ecmoho.base.model.SpiderSchqAcount;
import com.ecmoho.base.model.SpiderSchqChild;
import org.springframework.stereotype.Component;

/**
 * @author gusy
 * �г����顪����Ⱥ����--������Ⱥ
 */
@Component("schqRqhxSellerExploration")
public class SchqRqhxSellerExploration extends SchqExploration{
	@Override
	public List<HashMap<String,String>> getUrlList(SpiderSchqAcount spiderSchqAcount, SpiderSchqChild spiderSchqChild, String dateStr) {
		 List<HashMap<String,String>> urlList=new ArrayList<HashMap<String,String>>();
		 HashMap<String,String> urlMap=null;
		 //�洢����
		 String nowDateStr=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
//		 Map<String, Object> taskMap=schqDbcom.getSpider(account);
		 int accountid=spiderSchqAcount.getSid();
		 List<HashMap<String, String>> hymlList=getHyml(schqHeaderBean);
			String geturl=spiderSchqChild.getGeturl();
			String childAccount=spiderSchqChild.getChildAccount();
			for(Map<String,String> mlMap:hymlList){
				//
				int length="rqhx-seller-xjfb".equalsIgnoreCase(childAccount)?-1:1;
				//终端来源
				for(int k=-1;k<=length;k++){
				   urlMap=new HashMap<String,String>();
				   String finalTargetUrl=geturl.replaceAll("##D##", dateStr).replaceAll("##CID##", mlMap.get("id")).replaceAll("##S##", k+"");
				   urlMap.put("childAccount", childAccount);
				   urlMap.put("targetUrl", finalTargetUrl);
				   urlMap.put("accountid",accountid+"");
				   urlMap.put("create_at", dateStr);
				   urlMap.put("level", mlMap.get("level"));
				   urlMap.put("item1", mlMap.get("item1"));
				   urlMap.put("item2", mlMap.get("item2"));
				   urlMap.put("item3", mlMap.get("item3"));
				   urlMap.put("seller", k+"");
				   urlMap.put("log_at", nowDateStr);
				   urlList.add(urlMap);
				}
			}
		return urlList;
	}
	
}
