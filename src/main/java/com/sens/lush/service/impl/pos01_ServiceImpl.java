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

import com.sens.lush.pos01_Controller;
import com.sens.lush.service.pos01_Service;

@Service("pos01_Service")
public class pos01_ServiceImpl implements pos01_Service {
	private static final Logger logger = LoggerFactory.getLogger(pos01_Service.class);
    private static String namespace = "pos01";
    
//     @Autowired
     @Resource(name = "sqlSession")
     protected SqlSessionTemplate sqlSession;

     public List<Map<String, Object>>  getPosno(HashMap paramMap) throws Exception {
	  	 Map<String, Object>  resultMap  = new HashMap<String, Object>();
	   	 logger.info("service" + ".getPosno : "+ paramMap );
	   	 List<Map<String, Object>> list = sqlSession.selectList(namespace+ "." + "selectPosno",paramMap);
	   	 logger.info("service" + ".getPosno : "+ list );
	   	 
		 return list;
     }         

     public List<Map<String, Object>>  getUserinfo(HashMap paramMap) throws Exception {
	  	 Map<String, Object>  resultMap  = new HashMap<String, Object>();
	   	 logger.info("service" + ".getUserinfo : "+ paramMap );
	   	 List<Map<String, Object>> list = sqlSession.selectList(namespace+ "." + "selectUserinfo",paramMap);
	   	 
	   	 logger.info("service" + ".getUserinfo : "+ list );
	   	 
		 return list;
     }         
     
     public List<Map<String, Object>>  getUsermst(HashMap paramMap) throws Exception {
	  	 Map<String, Object>  resultMap  = new HashMap<String, Object>();
	   	 logger.info("service" + ".getUsermst : "+ paramMap );
	   	 List<Map<String, Object>> list = sqlSession.selectList(namespace+ "." + "selectUsermst",paramMap);
	   	 
	   	 logger.info("service" + ".getUsermst : "+ list );
	   	 
		 return list;
     }         

     public List<Map<String, Object>>  getJepmmst(HashMap paramMap) throws Exception {
	  	 Map<String, Object>  resultMap  = new HashMap<String, Object>();
	   	 logger.info("service" + ".getJepmmst : "+ paramMap );
	   	 List<Map<String, Object>> list = sqlSession.selectList(namespace+ "." + "selectJepmmst",paramMap);
	   	 
	   	 logger.info("service" + ".getJepmmst : "+ list );
	   	 
		 return list;
     }         

     public List<Map<String, Object>>  getJepmjepmcd(HashMap paramMap) throws Exception {
	  	 Map<String, Object>  resultMap  = new HashMap<String, Object>();
	   	 logger.info("service" + ".getJepmjepmcd : "+ paramMap );
	   	 List<Map<String, Object>> list = sqlSession.selectList(namespace+ "." + "selectJepmjepmcd",paramMap);
	   	 
	   	 logger.info("service" + ".getJepmjepmcd : "+ list );
	   	 
		 return list;
     }         

     public List<Map<String, Object>>  getJepmfind(HashMap paramMap) throws Exception {
	  	 Map<String, Object>  resultMap  = new HashMap<String, Object>();
	   	 logger.info("service" + ".getJepmfind : "+ paramMap );
	   	 List<Map<String, Object>> list = sqlSession.selectList(namespace+ "." + "selectJepmfind",paramMap);
	   	 
	   	 logger.info("service" + ".getJepmfind : "+ list );
	   	 
		 return list;
     }         
     
     public List<Map<String, Object>>  getMemberfind(HashMap paramMap) throws Exception {
	  	 Map<String, Object>  resultMap  = new HashMap<String, Object>();
	   	 logger.info("service" + ".getMemberfind : "+ paramMap );
	   	 List<Map<String, Object>> list = sqlSession.selectList(namespace+ "." + "selectMemberfind",paramMap);
	   	 
	   	 logger.info("service" + ".getMemberfind : "+ list );
	   	 
		 return list;
     }         
     
     public String setJepmdownkey(Map<String, Object> paramMap)  throws Exception {
	   	 logger.info("service" + ".setJepmdownkey : "+ paramMap );
		sqlSession.update(namespace + "." + "downUpdate",paramMap);
		return "";
     }
     
     public String insertMc20mst(Map<String, Object> paramMap)  throws Exception {
    	 logger.info("service" + ".insertMc20mst : "+ paramMap );
		sqlSession.update(namespace + "." + "InsertMc20mstJson",paramMap);
		return "";
     }
     
     public String insertMc20dtl(Map<String, Object> paramMap)  throws Exception {
    	 logger.info("service" + ".insertMc20dtl : "+ paramMap );
		sqlSession.update(namespace + "." + "InsertMc20dtlJson",paramMap);
		return "";
     }
     
     public String insertJepmmst(Map<String, Object> paramMap)  throws Exception {
    	 logger.info("service" + ".insertJepmmst : "+ paramMap );
		sqlSession.update(namespace + "." + "InsertJepmmstJson",paramMap);

		logger.info("service" + ".insertJepmall : "+ paramMap );

		sqlSession.update(namespace + "." + "insertJepmall",paramMap);
		return "";
     }
               
     public String insertMember(Map<String, Object> paramMap)  throws Exception {
    	 logger.info("service" + ".insertMember : "+ paramMap );
	    Object memnoObj = paramMap.get("memno");
	    String memno = (memnoObj == null) ? "" : memnoObj.toString().trim();

	    if (memno.isEmpty()) {
	    	paramMap.put("memno6", "");
	        sqlSession.insert(namespace + ".InsertMemberJson", paramMap);
	    } else {
	        sqlSession.update(namespace + ".UpdateMemberJson", paramMap);
	    }	    


		return "";
     }
               
          
     
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
	   	 	logger.info("pos01service.Insert: "+ paramMap );
			sqlSession.update(namespace + "." + "dataInsert",paramMap);
			return "";
     }
     
     public String Update(Map<String, Object> paramMap)  throws Exception {
	   	 	logger.info("pos01service.Update: "+ paramMap );
			sqlSession.update(namespace + "." + "dataUpdate",paramMap);
			return "";
     }
  
     public String Delete(Map<String, Object> paramMap)  throws Exception {
//    	 List<String> codeList = (List<String>) paramMap.get("CODE_LIST");
//    	 String first = codeList.get(0);   // "0025"
    	 
	   	 	logger.info("pos01service.Delete: "+ paramMap );
			sqlSession.update(namespace + "." + "dataDelete",paramMap);
			return "";
     }
  
     public String  dataSave(List<Map> paramMap,HashMap MapUser) throws Exception {
		Iterator it;
		HashMap map = new HashMap();
		String sRet="";

		logger.info("pos01save.paramMap: "+ paramMap );
		
		it = paramMap.iterator();
		logger.info("pos01save.it: "+ it );
		
		while (it.hasNext())
		{
			map = (HashMap)it.next();
			if (map.get("DEL") == null)		map.put("DEL", "N");
			if (map.get("NEW") == null)		map.put("NEW", "N");
			map.put("USER",MapUser);
			logger.info("pos01save.map: "+ map );
			if ("Y".equals(map.get("DEL"))){
				logger.info("pos01save.dataDelete: "+ map.get("DEL") );
				sqlSession.update(namespace + "." + "dataDelete",map);
			}
			else{
				 if(map.get("NEW").equals("Y")){
				   	 logger.info("pos01save.dataInsert: "+ map.get("DEL") );
						sqlSession.update(namespace + "." + "dataInsert",map);
				 }
				 else{
//						logger.info("pos01save.dataUpdate: "+ map.get("DEL") );
						sqlSession.update(namespace + "." + "dataUpdate",map);
				 }
			}
		}		
 		
		 return sRet;
  }  
     
}

