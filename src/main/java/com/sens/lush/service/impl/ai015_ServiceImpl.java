package com.sens.lush.service.impl;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.sens.lush.ai015_Controller;
import com.sens.lush.service.ai015_Service;

@Service("ai015_Service")
public class ai015_ServiceImpl implements ai015_Service {
	private static final Logger logger = LoggerFactory.getLogger(ai015_Service.class);
    private static String namespace = "ai015";
    
//     @Autowired
     @Resource(name = "sqlSession")
     protected SqlSessionTemplate sqlSession;

     public List<Map<String, Object>> getList(String name) {
	  	 Map<String, Object>  resultMap  = new HashMap<String, Object>();
	  	 Map<String, Object>  paramMap  = new HashMap<String, Object>();
	  	 paramMap.put("name", name);
	   	 logger.info("getList" + ".selectMain:param "+ paramMap );
	   	 List<Map<String, Object>> list = sqlSession.selectList(namespace+ "." + "selectMain",paramMap);
	   	 logger.info("getList" + ".selectMain.List: "+ list );
	   	 
		 return list;
  }    
     
     public List<Map<String, Object>>  gridList(HashMap paramMap) throws Exception {
	  	 Map<String, Object>  resultMap  = new HashMap<String, Object>();
	  	 
	   	 logger.info("grid" + ".selectMain:param "+ paramMap );
	   	 List<Map<String, Object>> list = sqlSession.selectList(namespace+ "." + "selectMain",paramMap);
	   	 logger.info("grid" + ".selectMain.List: "+ list );
	   	 
		 return list;
  }         

     public String Insert(Map<String, Object> paramMap)  throws Exception {
	   	 	logger.info("ai015service.Insert: "+ paramMap );
			sqlSession.update(namespace + "." + "dataInsert",paramMap);
			return "";
     }
     
     public String Update(Map<String, Object> paramMap)  throws Exception {
	   	 	logger.info("ai015service.Update: "+ paramMap );
			sqlSession.update(namespace + "." + "dataUpdate",paramMap);
			return "";
  }
  
     public String Delete(Map<String, Object> paramMap)  throws Exception {
//    	 List<String> codeList = (List<String>) paramMap.get("CODE_LIST");
//    	 String first = codeList.get(0);   // "0025"
    	 
	   	 	logger.info("ai015service.Delete: "+ paramMap );
			sqlSession.update(namespace + "." + "dataDelete",paramMap);
			return "";
  }
  
     public String  dataSave(List<Map> paramMap,HashMap MapUser) throws Exception {
		Iterator it;
		HashMap map = new HashMap();
		String sRet="";

		logger.info("ai015save.paramMap: "+ paramMap );
		
		it = paramMap.iterator();
		logger.info("ai015save.it: "+ it );
		
		while (it.hasNext())
		{
			map = (HashMap)it.next();
			if (map.get("DEL") == null)		map.put("DEL", "N");
			if (map.get("NEW") == null)		map.put("NEW", "N");
			map.put("USER",MapUser);
			logger.info("ai015save.map: "+ map );
			if ("Y".equals(map.get("DEL"))){
				logger.info("ai015save.dataDelete: "+ map.get("DEL") );
				sqlSession.update(namespace + "." + "dataDelete",map);
			}
			else{
				 if(map.get("NEW").equals("Y")){
				   	 logger.info("ai015save.dataInsert: "+ map.get("DEL") );
						sqlSession.update(namespace + "." + "dataInsert",map);
				 }
				 else{
//						logger.info("ai015save.dataUpdate: "+ map.get("DEL") );
						sqlSession.update(namespace + "." + "dataUpdate",map);
				 }
			}
		}		
 		
		 return sRet;
  }  
     
}

