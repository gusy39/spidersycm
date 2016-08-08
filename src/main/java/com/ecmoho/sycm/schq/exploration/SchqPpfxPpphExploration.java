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
 * Ʒ�Ʒ�������Ʒ������
 */
@Component("schqPpfxPpphExploration")
public class SchqPpfxPpphExploration extends SchqExploration{
	
	@Override
	public List<HashMap<String,String>> getUrlList(SpiderSchqAcount spiderSchqAcount, SpiderSchqChild spiderSchqChild, String dateStr) {
		 List<HashMap<String,String>> urlList=new ArrayList<HashMap<String,String>>();
		 HashMap<String,String> urlMap=null;
		 //�洢����
		 String nowDateStr=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		 int accountid=spiderSchqAcount.getSid();
		 List<HashMap<String, String>> hymlList=getHyml(schqHeaderBean);
			String childAccount=spiderSchqChild.getChildAccount();
			String geturl=spiderSchqChild.getGeturl();
			for(Map<String,String> mlMap:hymlList){
				String targetUrl=geturl.replaceAll("##D##", dateStr).replaceAll("##CID##", mlMap.get("id"));
				//终端来源
				for(int j=0;j<=2;j++){
				   //卖家来源
					for(int k=-1;k<=1;k++){
					   urlMap=new HashMap<String,String>();
					   String finalTargetUrl=targetUrl.replaceAll("##DE##", j+"").replaceAll("##S##", k+"");
					   urlMap.put("childAccount",childAccount);
					   urlMap.put("targetUrl", finalTargetUrl);
					   urlMap.put("accountid",accountid+"");
					   urlMap.put("create_at", dateStr);
					   urlMap.put("level", mlMap.get("level"));
					   urlMap.put("item1", mlMap.get("item1"));
					   urlMap.put("item2", mlMap.get("item2"));
					   urlMap.put("item3", mlMap.get("item3"));
					   urlMap.put("device", j+"");
					   urlMap.put("seller", k+"");
					   urlMap.put("log_at", nowDateStr);
					   urlList.add(urlMap);
				   }
			  }
			}
		return urlList;
	}
	
}
