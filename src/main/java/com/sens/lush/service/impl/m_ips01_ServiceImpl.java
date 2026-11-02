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
import org.springframework.beans.factory.annotation.Qualifier;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.sens.lush.m_ips01_Controller;
import com.sens.lush.service.is0004_Service;
import com.sens.lush.service.m_ips01_Service;


@Service("m_ips01_Service")
public class m_ips01_ServiceImpl implements m_ips01_Service {
	
	
	private static final Logger logger = LoggerFactory.getLogger(m_ips01_Service.class);
    private static String namespace = "m_ips01";
    
/*    
    @Autowired
    @Qualifier("sqlSession")
    protected SqlSessionTemplate sqlSession;
//     protected SqlSessionTemplate sqlSession;
    
    @Autowired
    @Qualifier("sqlSessionIPS")  // ← IPS DB 명시적 지정
    protected SqlSessionTemplate sqlSessionIPS;
*/
    

//     (@Autowired) 사용하면안됨
     @Resource(name = "sqlSession")
     protected SqlSessionTemplate sqlSession;
     
     @Resource(name = "sqlSessionIPS")  // ← IPS DB 명시적 지정!
     protected SqlSessionTemplate sqlSessionIPS;
     @Override
     @Transactional


    public List<Map<String, Object>>  loginCheck(HashMap paramMap) throws Exception {
//	  	 Map<String, Object>  resultMap  = new HashMap<String, Object>();
	  	 
	   	 logger.info("loginCheck: "+ paramMap );
	   	 List<Map<String, Object>> list = sqlSessionIPS.selectList(namespace + "." + "loginCheck",paramMap);
		 return list;
    }

    public List<Map<String, Object>>  posInfo(HashMap paramMap) throws Exception {
//	  	 Map<String, Object>  resultMap  = new HashMap<String, Object>();
	  	 
	   	 logger.info("posinfo: "+ paramMap );
	   	 List<Map<String, Object>> list = sqlSessionIPS.selectList(namespace+"PosInfo",paramMap);
		 return list;
    }
    
  
    public List<Map<String, Object>>  m_getShopList(HashMap paramMap) throws Exception {

   	 logger.info(namespace + "m_getSmsList.param "+ "(" + paramMap  + ")");
		
		List<Map<String, Object>> list = null;
		list = sqlSessionIPS.selectList(namespace+ "." + "m_ShopList",paramMap);
		logger.info(namespace + "m_ShopList.List: "+ list );
		 return list;
    }      

     public List<Map<String, Object>>  m_getSmsList(HashMap paramMap) throws Exception {

    	 logger.info(namespace + "m_getSmsList.param "+ "(" + paramMap  + ")");
		
		List<Map<String, Object>> list = null;
		list = sqlSessionIPS.selectList(namespace+ "." + "m_SHOPselectSmsList",paramMap);
		logger.info(namespace + "m_getSmsList.List: "+ list );
		 return list;
     }             
     
     public String  m_dataSmsSave(HashMap paramMap) throws Exception {
    	 
		String sRet="";

		logger.info("m_dataSmsSave.paramMap: "+ paramMap );
		logger.info("m_dataSmsSave.namespace: "+ namespace + "m_dataSKSave");
		sqlSessionIPS.update(namespace + "." + "m_dataSmsSave",paramMap);
		logger.info("m_dataSmsSave.namespace: "+ namespace );
		sqlSessionIPS.update(namespace + "." + "m_dataSmsUpdate",paramMap);
	    Object kjDate = paramMap.get("KJDATE");
	    if (kjDate != null && !kjDate.toString().trim().isEmpty()) {
	        logger.info("m_dataKJUpdate.namespace: " + namespace);
	        sqlSessionIPS.update(namespace + "." + "m_dataKJUpdate", paramMap);
	    }
		 return sRet;
     }       
     
     public String  m_LogSave(HashMap paramMap) throws Exception {
    	 
		String sRet="";

		logger.info("m_LogSave.paramMap: "+ paramMap );
		logger.info("m_LogSave.namespace: "+ namespace + "m_LogSave");
		sqlSessionIPS.update(namespace + "." + "m_LogSave",paramMap);
		logger.info("m_LogSave.namespace: "+ namespace );
		 return sRet;
     } 

}

