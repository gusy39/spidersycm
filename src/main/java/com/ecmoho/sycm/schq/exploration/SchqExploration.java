package com.ecmoho.sycm.schq.exploration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.ecmoho.base.dao.SpiderAccountDao;
import com.ecmoho.base.model.SpiderSchqAcount;
import com.ecmoho.base.model.SpiderSchqChild;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ecmoho.base.Util.UrlUtil;
import com.ecmoho.base.bean.HeaderBean;

@Component("schqExploration")
public class SchqExploration{

	@Resource(name="schqHeaderBean")
	protected HeaderBean schqHeaderBean;
	@Resource(name = "spiderAccountDao")
	protected SpiderAccountDao spiderAccountDao;

	public List<HashMap<String,String>> getUrlList(SpiderSchqAcount spiderSchqAcount, SpiderSchqChild spiderSchqChild, String date){
		return null;
	};
	//获取行业目录
    public List<HashMap<String, String>> getHyml(HeaderBean schqHeaderBean){
		
		 //��ҵĿ¼������Ϣ
//		 Map<String, Object> hymlMap=schqDbcom.getSpiderChild("hyml");
		SpiderSchqChild SchqHymlChild = spiderAccountDao.getSpiderSchqChildByChildAccount("hyml");
//		String hymlUrlArr[]=StringUtil.objectVerString(hymlMap.get("geturl")).split("####");
		String hymlUrlArr[]=SchqHymlChild.getGeturl().split("####");
		 Document pageHtmlDoc=Jsoup.parse(UrlUtil.getUrlString(schqHeaderBean,hymlUrlArr[0],"GET"));
		 String edition=pageHtmlDoc.select("[name=marketVersion]").attr("content");
		 String hymlStr=hymlUrlArr[1].replaceAll("##ED##", edition);
		 String hymlResult=UrlUtil.getUrlString(schqHeaderBean,hymlStr,"GET");
    	System.out.println("hymlResult:"+hymlResult);
 	    List<HashMap<String, String>> hymlList=new ArrayList<HashMap<String,String>>();
		JSONObject hymlJsonObject=JSON.parseObject(hymlResult);
		JSONArray dataJsonArray=hymlJsonObject.getJSONObject("content").getJSONArray("data");
         //Ŀ¼�б�һ��Ŀ¼
		HashMap<String,String> fHymlMap=null;
		//Ŀ¼�б�һ��Ŀ¼id����
		HashMap<String,String> flevelMap=new HashMap<String,String>();
		//Ŀ¼�б����Ŀ¼
		HashMap<String,String> sHymlMap=null;
		//Ŀ¼�б����Ŀ¼id����
		HashMap<String,String> sLevelMap=new HashMap<String,String>();;
		//Ŀ¼�б�����Ŀ¼
		HashMap<String,String> tHymlMap=null;
		for(int j=0;j<dataJsonArray.size();j++){
			String id=dataJsonArray.getJSONArray(j).getString(0);
			String parentId=dataJsonArray.getJSONArray(j).getString(1);
			String mlname=dataJsonArray.getJSONArray(j).getString(2);
			String fparentId=dataJsonArray.getJSONArray(j).getString(6);
			if(parentId.equalsIgnoreCase("0")){
				fHymlMap=new HashMap<String,String>();
				fHymlMap.put("id", id);
				fHymlMap.put("parentId", parentId);
				fHymlMap.put("level", "1");
				fHymlMap.put("item1", mlname);
				fHymlMap.put("item2", "");
				fHymlMap.put("item3", "");
				fHymlMap.put("fparentId", fparentId);
				fHymlMap.put("level", "1");
				hymlList.add(fHymlMap);
				flevelMap.put(id, mlname);
			}
		}
		for(int k=0;k<dataJsonArray.size();k++){
			String id=dataJsonArray.getJSONArray(k).getString(0);
			String parentId=dataJsonArray.getJSONArray(k).getString(1);
			String mlname=dataJsonArray.getJSONArray(k).getString(2);
			String fparentId=dataJsonArray.getJSONArray(k).getString(6);
			if(flevelMap.containsKey(parentId)){
				sHymlMap=new HashMap<String,String>();
				
				sHymlMap.put("id", id);
				sHymlMap.put("parentId", parentId);
				sHymlMap.put("level", "2");
				sHymlMap.put("item1",flevelMap.get(parentId));
				sHymlMap.put("item2", mlname);
				sHymlMap.put("item3", "");
				sHymlMap.put("fparentId", fparentId);
				hymlList.add(sHymlMap);
				sLevelMap.put(id, mlname);
			}
		}
		for(int k=0;k<dataJsonArray.size();k++){
			String id=dataJsonArray.getJSONArray(k).getString(0);
			String parentId=dataJsonArray.getJSONArray(k).getString(1);
			String mlname=dataJsonArray.getJSONArray(k).getString(2);
			String fparentId=dataJsonArray.getJSONArray(k).getString(6);
			if(sLevelMap.containsKey(parentId)){
				tHymlMap=new HashMap<String,String>();
				tHymlMap.put("id", id);
				tHymlMap.put("parentId", parentId);
				tHymlMap.put("level", "0");
				tHymlMap.put("item1",flevelMap.get(fparentId));
				tHymlMap.put("item2", sLevelMap.get(parentId));
				tHymlMap.put("item3", mlname);
				tHymlMap.put("fparentId", fparentId);
				hymlList.add(tHymlMap);
			}
		}
 	   return hymlList;
    }
    //获取品牌列表
    public Map<String,String> getPplb(HeaderBean schqHeaderBean,String cateId,String getday){

    	Map<String,String> dataMap=new HashMap<String,String>();
		SpiderSchqChild pplbSchqChild = spiderAccountDao.getSpiderSchqChildByChildAccount("pplb");
		String getUrl=pplbSchqChild.getGeturl();
		String targetUrl=getUrl.replaceAll("##CID##", cateId).replaceAll("##D##", getday);
		String targetResult=UrlUtil.getUrlString(schqHeaderBean,targetUrl,"GET");
		JSONObject jsonObject=JSON.parseObject(targetResult);
		JSONArray jsonArray=jsonObject.getJSONObject("content").getJSONArray("data");
		System.out.println("cateId:"+cateId);
		for(int i=0;jsonArray!=null&&i<jsonArray.size();i++){
			JSONObject brandJsonObject=jsonArray.getJSONObject(i);
			String brandId=brandJsonObject.getString("brandId");
			String brandName=brandJsonObject.getString("brandName");
			dataMap.put(brandId,brandName);
		}
    	return dataMap;
    }
    
   //获取产品列表
	public  List<HashMap<String,String>> getCpfxCpxqModels(HeaderBean schqHeaderBean,String cateId,String brandId,String yesterdayday){
		List<HashMap<String, String>> dataList=new ArrayList<HashMap<String,String>>();
		HashMap<String,String> dataMap=null;
//		Map<String, Object> pplbMap=schqDbcom.getSpiderChild("cpfx-cpxq-cplb");
		SpiderSchqChild SchqChildCplb = spiderAccountDao.getSpiderSchqChildByChildAccount("cpfx-cpxq-cplb");
		String cpxqPplbUrl=SchqChildCplb.getGeturl();
		String cpxqPplbTargetUrl=cpxqPplbUrl.replaceAll("##D##", yesterdayday).replaceAll("##CID##", cateId).replaceAll("##BID##", brandId);
		String cpxqPplbResult=UrlUtil.getUrlString(schqHeaderBean, cpxqPplbTargetUrl,"GET");
		JSONObject jsonObject=JSON.parseObject(cpxqPplbResult);
		JSONArray jsonArray=jsonObject.getJSONObject("content").getJSONArray("data");
	    for(int i=0;jsonArray!=null&&i<jsonArray.size();i++){
	    	dataMap=new HashMap<String,String>();
	    	JSONObject dataObject=jsonArray.getJSONObject(i);
	    	dataMap.put("modelId",dataObject.getString("modelId"));
	    	dataMap.put("modelName",dataObject.getString("modelName"));
	    	dataMap.put("spuId",dataObject.getString("spuId"));
	    	dataList.add(dataMap);
	    } 
		return dataList;
	}
}
