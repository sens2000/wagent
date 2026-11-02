package com.sens.lush.service.impl;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.sens.lush.loginController;
import com.sens.lush.service.loginService;

@Service("loginService")
public class loginServiceImpl implements loginService {
	private static final Logger logger = LoggerFactory.getLogger(loginService.class);
    private static String namespace = "com.sens.lush.service.dao.loginDAO.";
    
    @Autowired
    @Qualifier("sqlSession")
    protected SqlSessionTemplate sqlSession;
//     protected SqlSessionTemplate sqlSession;
    
    @Autowired
    @Qualifier("sqlSessionIPS")  // ← IPS DB 명시적 지정
    protected SqlSessionTemplate sqlSessionIPS;
    
     @Override
     @Transactional
     
     public List<Map<String, Object>>  loginCheck(HashMap paramMap) throws Exception {
//	  	 Map<String, Object>  resultMap  = new HashMap<String, Object>();
	  	 
	   	 logger.info("loginCheck: "+ paramMap );
	   	 List<Map<String, Object>> list = sqlSession.selectList(namespace+"loginCheck",paramMap);
//	   	 List<Map<String, Object>> list = sqlSessionIPS.selectList(namespace+"loginCheck",paramMap);
//	   	 logger.info("List: "+ list );
	   	 
//		Iterator it = list.iterator();
//		HashMap map = new HashMap();
//		map = (HashMap)it.next();
//		if (list.size()==0) {
//			map.put("USERNAME","none");
//			list.add(map);
//		}
//		else {
//		   	 logger.info("USERNAME: "+ list.get(0).get("USERNAME") );
//		}
//	   	 logger.info("List1: "+ list );
//		 resultMap.put("data"     , list);
//		 logger.info("resultMap: "+ resultMap );
		 return list;
     }

     public List<Map<String, Object>>  posInfo(HashMap paramMap) throws Exception {
//	  	 Map<String, Object>  resultMap  = new HashMap<String, Object>();
	  	 
	   	 logger.info("posinfo: "+ paramMap );
	   	 List<Map<String, Object>> list = sqlSession.selectList(namespace+"PosInfo",paramMap);
//	   	 logger.info("List: "+ list );
	   	 
//		Iterator it = list.iterator();
//		HashMap map = new HashMap();
//		map = (HashMap)it.next();
//		if (list.size()==0) {
//			map.put("USERNAME","none");
//			list.add(map);
//		}
//		else {
//		   	 logger.info("USERNAME: "+ list.get(0).get("USERNAME") );
//		}
//	   	 logger.info("List1: "+ list );
//		 resultMap.put("data"     , list);
//		 logger.info("resultMap: "+ resultMap );
		 return list;
     }
     
     public Map<String,Object>  menuGroup(HashMap paramMap,String sGubun) throws Exception {
	  	 Map<String, Object>  resultMap  = new HashMap<String, Object>();
	  	 
	   	 logger.info("menuGroup: "+ paramMap );
	   	paramMap.put("PROGKIND",sGubun);
	   	 List<Map<String, Object>> list = sqlSession.selectList(namespace+"menuGroup",paramMap);
	   	 logger.info("menuGroup.List: "+ list );
	   	 
		HashMap map = new HashMap();
		if (list.size()==0) {
//			map.put("USERNAME","none");
//			list.add(map);
		}
		else {
		   	 logger.info("list"+ list );
		}
		 resultMap.put("data"     , list);
		 logger.info("menuGroup.resultMap: "+ resultMap );
		 return resultMap;
  }    

     public Map<String,Object>  menuAgent(HashMap paramMap,String sGubun) throws Exception {
	  	 Map<String, Object>  resultMap  = new HashMap<String, Object>();
	  	 
	   	 logger.info("menuAgent: "+ paramMap );
	   	paramMap.put("PROGKIND",sGubun);
	   	 List<Map<String, Object>> list = sqlSession.selectList(namespace+"menuAgent",paramMap);
	   	 logger.info("menuAgent.List: "+ list );
	   	 
		HashMap map = new HashMap();
		if (list.size()==0) {
//			map.put("USERNAME","none");
//			list.add(map);
		}
		else {
		   	 logger.info("list"+ list );
		}
		 resultMap.put("data"     , list);
		 logger.info("menuAgent.resultMap: "+ resultMap );
		 return resultMap;
  }         
     public List<Map<String, Object>>  gridGroupNews(HashMap paramMap) throws Exception {
	  	 
	   	 logger.info("selectGroupNews: "+ paramMap );
	   	 List<Map<String, Object>> list = sqlSession.selectList(namespace+"selectGroupNews",paramMap);
	   	 logger.info("selectGroupNews.List: "+ list );
	   	 
		 return list;
  }

     public List<Map<String, Object>>  gridComDay(HashMap paramMap) throws Exception {
	  	 
	   	 logger.info("selectComDay: "+ paramMap );
	   	 List<Map<String, Object>> list = sqlSession.selectList(namespace+"selectComDay",paramMap);
//	   	 logger.info("selectComDay.List: "+ list );
	   	 
		 return list;
  }         
     
}

