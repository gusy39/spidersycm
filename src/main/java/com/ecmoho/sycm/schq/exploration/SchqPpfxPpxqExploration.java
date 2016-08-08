package com.ecmoho.sycm.schq.exploration;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.ecmoho.base.model.SpiderSchqAcount;
import com.ecmoho.base.model.SpiderSchqChild;
import org.springframework.stereotype.Component;

/**
 * @author gusy
 * �г�����--Ʒ�Ʒ�������Ʒ������
 */
@Component("schqPpfxPpxqExploration")
public class SchqPpfxPpxqExploration extends SchqExploration{
	
	@Override
	public List<HashMap<String,String>> getUrlList(SpiderSchqAcount spiderSchqAcount, SpiderSchqChild spiderSchqChild, String dateStr) {
		 List<HashMap<String,String>> urlList=new ArrayList<HashMap<String,String>>();
		 HashMap<String,String> urlMap=null;
		 //获取当前时间
		 String nowDateStr=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		 int accountid=spiderSchqAcount.getSid();
		 //行业目录
		 List<HashMap<String, String>> hymlList=getHyml(schqHeaderBean);
			String childAccount=spiderSchqChild.getChildAccount();
			String geturl=spiderSchqChild.getGeturl();
			for(Map<String,String> mlMap:hymlList){
				 String cateId=mlMap.get("id");
				 Map<String,String> pplbMap=getPplb(schqHeaderBean, cateId, dateStr);
				 //����Ʒ��
				 for(Entry<String, String> entry:pplbMap.entrySet()){
					 String brandId=entry.getKey();//品牌id
					 String brandName=entry.getValue();//品牌名称
					 //终端来源
					 for(int j=0;j<=2;j++){
						//卖家来源
						for(int k=-1;k<=1;k++){
						   urlMap=new HashMap<String,String>();
						   String finalTargetUrl=geturl.replaceAll("##BID##", brandId).replaceAll("##CID##", cateId).replaceAll("##D##", dateStr).replaceAll("##DE##", j+"").replaceAll("##S##", k+"");
						   urlMap.put("childAccount",childAccount);
						   urlMap.put("accountid",accountid+"");
						   urlMap.put("create_at", dateStr);
						   urlMap.put("level", mlMap.get("level"));
						   urlMap.put("item1", mlMap.get("item1"));
						   urlMap.put("item2", mlMap.get("item2"));
						   urlMap.put("item3", mlMap.get("item3"));
						   urlMap.put("brandId", brandId);
						   urlMap.put("brandName", brandName);
						   urlMap.put("device", j+"");
						   urlMap.put("seller", k+"");
						   urlMap.put("log_at", nowDateStr);
						   urlMap.put("targetUrl", finalTargetUrl);
						   urlList.add(urlMap);
						}
					 }
				 }
			}
		return urlList;
	}
	
}
