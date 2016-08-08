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
 * �г�����--��Ʒ����������Ʒ����
 */
@Component("schqCpfxCpxqExploration")
public class SchqCpfxCpxqExploration extends SchqExploration{

    @Override
	public List<HashMap<String,String>> getUrlList(SpiderSchqAcount spiderSchqAcount, SpiderSchqChild spiderSchqChild, String dateStr) {
		 List<HashMap<String,String>> urlList=new ArrayList<HashMap<String,String>>();
		 HashMap<String,String> urlMap=null;
		 String nowDateStr=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

		 int accountid=spiderSchqAcount.getSid();

		 List<HashMap<String, String>> hymlList=getHyml(schqHeaderBean);
			String childAccount=spiderSchqChild.getChildAccount();
			String geturl=spiderSchqChild.getGeturl();
			//遍历目录列表
			for(Map<String,String> mlMap:hymlList){
				 String cateId=mlMap.get("id");
				 //获取品牌列表
				 Map<String,String> pplbMap=getPplb(schqHeaderBean, cateId, dateStr);
				 //遍历品牌列表
				 for(Entry<String, String> pplbEntry:pplbMap.entrySet()){
					 String brandId=pplbEntry.getKey();//品牌id
					 String brandName=pplbEntry.getValue();//品牌名称
					 //获取产品列表
					 List<HashMap<String,String>> cplbList=getCpfxCpxqModels(schqHeaderBean, cateId, brandId,dateStr);
					 //遍历产品列表
					 for(int i=0;cplbList!=null&&i<cplbList.size();i++){
						 String modelId=cplbList.get(i).get("modelId");
						 String modelName=cplbList.get(i).get("modelName");
						 String spuId=cplbList.get(i).get("spuId");
						 //终端来源
						 for(int j=0;j<=2;j++){
							//卖家来源
							for(int k=-1;k<=1;k++){
							   urlMap=new HashMap<String,String>();
							   String finalTargetUrl=geturl.replaceAll("##BID##", brandId).replaceAll("##CID##", cateId).replaceAll("##MID##", modelId).replaceAll("##D##", dateStr).replaceAll("##DE##", j+"").replaceAll("##SID##", spuId).replaceAll("##S##", k+"");
							   System.out.println("finalTargetUrl��"+finalTargetUrl);
							   urlMap.put("childAccount",childAccount);
							   urlMap.put("accountid",accountid+"");
							   urlMap.put("create_at", dateStr);
							   urlMap.put("level", mlMap.get("level"));
							   urlMap.put("item1", mlMap.get("item1"));
							   urlMap.put("item2", mlMap.get("item2"));
							   urlMap.put("item3", mlMap.get("item3"));
							   urlMap.put("brandId", brandId);
							   urlMap.put("brandName", brandName);
							   urlMap.put("modelId",modelId);
							   urlMap.put("modelName", modelName);
							   urlMap.put("spuId", spuId);
							   urlMap.put("device", j+"");
							   urlMap.put("seller", k+"");
							   urlMap.put("log_at", nowDateStr);
							   urlMap.put("targetUrl", finalTargetUrl);
							   urlList.add(urlMap);
							 }
						 }
					 }
				 }
			}
		return urlList;
	}
}
