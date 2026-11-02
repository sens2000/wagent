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

import com.sens.lush.is0001_Controller;
import com.sens.lush.service.is0001_Service;

@Service("is0001_Service")
public class is0001_ServiceImpl implements is0001_Service {
	private static final Logger logger = LoggerFactory.getLogger(is0001_Service.class);
    private static String namespace = "is0001";
    
     @Autowired
     @Resource(name = "sqlSession")
     protected SqlSessionTemplate sqlSession;


     public List<Map<String, Object>>  gridList(HashMap paramMap) throws Exception {
	  	 Map<String, Object>  resultMap  = new HashMap<String, Object>();
	  	 
	   	 logger.info("grid" + "selectMain:param "+ paramMap );
	   	 List<Map<String, Object>> list = sqlSession.selectList(namespace+ "." + "selectMain",paramMap);
	   	 logger.info("grid" + "selectMain.List: "+ list );
	   	 
		 return list;
  }         

     public String  dataSave(List<Map> paramMap,HashMap MapUser) throws Exception {
		Iterator it;
		HashMap map = new HashMap();
		String sRet="";

		logger.info("is0001save.paramMap: "+ paramMap );
		
		it = paramMap.iterator();
		logger.info("is0001save.it: "+ it );
		
		while (it.hasNext())
		{
			map = (HashMap)it.next();
			if (map.get("DEL") == null)		map.put("DEL", "N");
			if (map.get("NEW") == null)		map.put("NEW", "N");
			map.put("USER",MapUser);
			logger.info("is0001save.map: "+ map );
			if ("Y".equals(map.get("DEL"))){
				logger.info("is0001save.dataDelete: "+ map.get("DEL") );
				sqlSession.update(namespace + "." + "dataDelete",map);
			}
			else{
				 if(map.get("NEW").equals("Y")){
				   	 logger.info("is0001save.dataInsert: "+ map.get("DEL") );
						sqlSession.update(namespace + "." + "dataInsert",map);
				 }
				 else{
//						logger.info("is0001save.dataUpdate: "+ map.get("DEL") );
						sqlSession.update(namespace + "." + "dataUpdate",map);
				 }
			}
		}		
 		
		 return sRet;
  }  
     
}

