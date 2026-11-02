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

import com.sens.lush.service.codeMgr_Service;

//		List result = sqlSession.selectList("sy2073Mapper.findSheetList", paramMap);

@Service("codeMgr_Service")
public class codeMgr_ServiceImpl implements codeMgr_Service {
	private static final Logger logger = LoggerFactory.getLogger(codeMgr_Service.class);
    private static String namespace = "codeMgr";
    
     @Autowired
     @Resource(name = "sqlSession")
     protected SqlSessionTemplate sqlSession;

     public List<Map<String, Object>>  getCodeList(HashMap paramMap,String sParm,String sKind) throws Exception {

    	 logger.info("grid" + "getCodeList.param "+ sParm + "(" + paramMap  + ")");
		
		List<Map<String, Object>> list = null;
		if (sParm.equals("code_mst")){
			paramMap.put("CODEGU", sKind);
			list = sqlSession.selectList(namespace+ "." + "selectCodeList",paramMap);
		}else if (sParm.equals("ACCTGRPNAME")){
			list = sqlSession.selectList(namespace+ "." + "selectAcctGrpNm",paramMap);
		}else if (sParm.equals("SAWONLIST")){
			list = sqlSession.selectList(namespace+ "." + "selectSawonList",paramMap);
		}else if (sParm.equals("JEPMBRANDLIST")){
			list = sqlSession.selectList(namespace+ "." + "selectJepmBrandList",paramMap);
		}else if (sParm.equals("JEPMKINDLIST")){
			list = sqlSession.selectList(namespace+ "." + "selectJepmKindList",paramMap);
		}else if (sParm.equals("TEST")){
			list = sqlSession.selectList(namespace+ "." + "select_codemst_test",paramMap);
		}
		
		logger.info("grid" + "getCodeList.List: "+ list );
		HashMap map = new HashMap();
		if (list.size()==0) {
				map.put("value","none");
				map.put("text","none");
				list.add(map);
				list.add(map);
				logger.info("grid" + "getCodeList.none add: ");
		}
		
		 return list;
  }         

     public List<Map<String, Object>>  m_getCodeList(HashMap paramMap,String sParm,String sKind) throws Exception {
    	 logger.info("###### TEST_MARKER getCodeList 진입 ######");
    	 logger.info("grid" + "getCodeList.param "+ sParm + "(" + paramMap  + ")");
		
		List<Map<String, Object>> list = null;
		if (sParm.equals("code_mst")){
			paramMap.put("CODEGU", sKind);
			list = sqlSession.selectList(namespace+ "." + "selectCodeList",paramMap);
		}else if (sParm.equals("ACCTGRPNAME")){
			list = sqlSession.selectList(namespace+ "." + "selectAcctGrpNm",paramMap);
		}else if (sParm.equals("SAWONLIST")){
			list = sqlSession.selectList(namespace+ "." + "selectSawonList",paramMap);
		}else if (sParm.equals("JEPMBRANDLIST")){
			list = sqlSession.selectList(namespace+ "." + "selectJepmBrandList",paramMap);
		}else if (sParm.equals("m_JEPMBRANDLIST")){
//			paramMap.put("CODEGU", "브랜드");
			list = sqlSession.selectList(namespace+ "." + "m_SENSselectCodeList",paramMap);
		}else if (sParm.equals("m_CUSTLIST")){
			list = sqlSession.selectList(namespace+ "." + "m_SENSselectCustList",paramMap);
		}else if (sParm.equals("JEPMKINDLIST")){
			list = sqlSession.selectList(namespace+ "." + "selectJepmKindList",paramMap);
	    } else {
	        logger.warn("grid" + "getCodeList: 매칭되는 sParm 없음 -> [" + sParm + "]");
	        list = new java.util.ArrayList<Map<String, Object>>();
	    }
		
		logger.info("grid" + "m_getCodeList.List: "+ list );
		HashMap map = new HashMap();
		/*
		if (list.size()==0) {
				map.put("value","none");
				map.put("text","none");
				list.add(map);
				list.add(map);
				logger.info("grid" + "getCodeList.none add: ");
		}
		*/
		 return list;
  }         
}

