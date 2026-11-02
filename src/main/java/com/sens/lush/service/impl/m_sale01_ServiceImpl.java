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

import com.sens.lush.m_sale01_Controller;
import com.sens.lush.service.is0004_Service;
import com.sens.lush.service.m_sale01_Service;


@Service("m_sale01_Service")
public class m_sale01_ServiceImpl implements m_sale01_Service {
	
	
	private static final Logger logger = LoggerFactory.getLogger(m_sale01_Service.class);
    private static String namespace = "m_sale01";
    @Autowired
    @Qualifier("sqlSession")
    protected SqlSessionTemplate sqlSession;
//     protected SqlSessionTemplate sqlSession;
    
    @Autowired
    @Qualifier("sqlSessionIPS")  // ← IPS DB 명시적 지정
    protected SqlSessionTemplate sqlSessionIPS;
    
/*
//     (@Autowired) 사용하면안됨
     @Resource(name = "sqlSession")
     protected SqlSessionTemplate sqlSession;
     
     @Resource(name = "sqlSessionIPS")  // ← IPS DB 명시적 지정!
     protected SqlSessionTemplate sqlSessionIPS;
     @Override
     @Transactional
 */

     public List<Map<String, Object>>  gridList(HashMap paramMap) throws Exception {
	  	 Map<String, Object>  resultMap  = new HashMap<String, Object>();
	  	 
	   	 logger.info(namespace + "selectMain:param "+ paramMap );
	   	 List<Map<String, Object>> list = sqlSession.selectList(namespace+ "." + "selectMain",paramMap);
	   	 logger.info(namespace + "selectMain.List: "+ list );
	   	 
		 return list;
     }         

     public List<Map<String, Object>>  m_getCodeList(HashMap paramMap) throws Exception {
    	 logger.info("###### TEST_MARKER getCodeList 진입 ######");

    	 logger.info(namespace + "m_getCodeList.param "+ "(" + paramMap  + ")");
		
		List<Map<String, Object>> list = null;
		list = sqlSession.selectList(namespace+ "." + "m_SENSselectCustList",paramMap);
		logger.info(namespace + "m_getCodeList.List: "+ list );
		 return list;
     }             
     
     public List<Map<String, Object>>  m_getCustMCSK(HashMap paramMap) throws Exception {

    	 logger.info(namespace + "m_getCodeList.param "+ "(" + paramMap  + ")");
		
		List<Map<String, Object>> list = null;
		list = sqlSession.selectList(namespace+ "." + "m_getCustMCSK",paramMap);
		logger.info(namespace + "m_getCodeList.List: "+ list );
		 return list;
     }             

     public List<Map<String, Object>>  m_MCSKQuery(HashMap paramMap) throws Exception {

    	 logger.info(namespace + "m_MCSKQuery.param "+ "(" + paramMap  + ")");
		
		List<Map<String, Object>> list = null;
		list = sqlSession.selectList(namespace+ "." + "m_MCSKQuery",paramMap);
		logger.info(namespace + "m_MCSKQuery.List: "+ list );
		 return list;
     }             

     public List<Map<String, Object>>  m_JPNOMCSKQuery(HashMap paramMap) throws Exception {

    	 logger.info(namespace + "m_JPNOMCSKQuery.param "+ "(" + paramMap  + ")");
		
		List<Map<String, Object>> list = null;
		list = sqlSession.selectList(namespace+ "." + "m_JPNOMCSKQuery",paramMap);
		logger.info(namespace + "m_JPNOMCSKQuery.List: "+ list );
		 return list;
     }             

     public List<Map<String, Object>>  m_getJepumSearch(HashMap paramMap) throws Exception {

    	 logger.info(namespace + "m_getJepumSearch.param "+ "(" + paramMap  + ")");
		
		List<Map<String, Object>> list = null;
		list = sqlSession.selectList(namespace+ "." + "m_getJepumSearch",paramMap);
		logger.info(namespace + "m_getCodeList.List: "+ list );
		 return list;
     }             
     
     public List<Map<String, Object>>  m_getBrandSearch(HashMap paramMap) throws Exception {

    	 logger.info(namespace + "m_getBrandSearch.param "+ "(" + paramMap  + ")");
		
		List<Map<String, Object>> list = null;
		list = sqlSession.selectList(namespace+ "." + "m_getBrandSearch",paramMap);
		logger.info(namespace + "m_getCodeList.List: "+ list );
		 return list;
     }             
     
     public String  m_dataSKSave(HashMap paramMap) throws Exception {
    	 
		String sRet="";

		logger.info("dataSKSave.paramMap: "+ paramMap );
		logger.info("dataSKSave.namespace: "+ namespace );
		sqlSession.update(namespace + "." + "m_dataSKSave",paramMap);
 		
		 return sRet;
     }       
     
     
     public String  m_dataMCSave(Map<String, Object>[] mapData,HashMap mapMst) throws Exception {
    	 
		String sRet="";

		logger.info("m_dataMCSave.mapMst: "+ mapMst );
		logger.info("m_dataMCSave.mapData: "+ mapData );
		
    	/*
    	 * MCSK = 매출/수금
    	 * PROC =NEW/EDIT/DEL
        */
		if (mapMst.get("MCSK").equals("매출")){
			if (!mapMst.get("PROC").equals("NEW")){
	    		logger.info("m_dataMCSave.m_dataMCMinus_subul: "  );
	        	sqlSession.update(namespace + "." + "m_dataMCMinus_subul",mapMst);
	        	
				logger.info("m_dataMCSave.m_dataMCDel_mst: "  );
		    	sqlSession.update(namespace + "." + "m_dataMCDel_mst",mapMst);
		    	
				logger.info("m_dataMCSave.m_dataMCDel_dtl: "  );
		    	sqlSession.update(namespace + "." + "m_dataMCDel_dtl",mapMst);
				
			}
			if (!mapMst.get("PROC").equals("DEL")){
		        for (int i = 0; i < mapData.length; i++) {
		        	mapData[i].put("SEQ",i+1);
		        	mapData[i].put("MST",mapMst);
		            System.out.println("[" + (i+1) + "] " + mapData[i]);
		        	sqlSession.update(namespace + "." + "m_dataMCSave_dtl",mapData[i]);
		        	
		    		logger.info("m_dataMCSave.m_dataMCupdate_subul: "  );
		        	sqlSession.update(namespace + "." + "m_dataMCupdate_subul",mapData[i]);
		        	
		    		logger.info("m_dataMCSave.m_dataMCinsert_subul: "  );
		        	sqlSession.update(namespace + "." + "m_dataMCinsert_subul",mapData[i]);
		        }
	        
				logger.info("m_dataMCSave.m_dataMCSave_mst: "  );
		    	sqlSession.update(namespace + "." + "m_dataMCSave_mst",mapMst);
			}
		}
    	
		if (mapMst.get("MCSK").equals("수금")){
			if (!mapMst.get("PROC").equals("NEW")){
				logger.info("m_dataMCSave.m_dataSKDel_mst: "  );
		    	sqlSession.update(namespace + "." + "m_dataSKDel_mst",mapMst);
			}
		}
		 return sRet;
     }       
     public String  dataSave(List<Map> paramMap,HashMap MapUser) throws Exception {
		Iterator it;
		HashMap map = new HashMap();
		String sRet="";

		logger.info(namespace + "save.paramMap: "+ paramMap );
		
		it = paramMap.iterator();
		logger.info(namespace + "save.it: "+ it );
		
		while (it.hasNext())
		{
			map = (HashMap)it.next();
			if (map.get("DEL") == null)		map.put("DEL", "N");
			if (map.get("NEW") == null)		map.put("NEW", "N");
			map.put("USER",MapUser);

			logger.info(namespace + "save.map: "+ map );
			
			if (map.get("DEL").equals("Y")){
			   	logger.info(namespace + "save.dataDelete: "+ map );
				sqlSession.update(namespace + "." + "dataDelete",map);
			}
			else{
				 if(map.get("NEW").equals("Y")){
				   	logger.info(namespace + "save.dataInsert: "+ map );
					sqlSession.update(namespace + "." + "dataInsert",map);
				 }else{
				   	logger.info(namespace + "save.dataUpdate: "+ map );
					sqlSession.update(namespace + "." + "dataUpdate",map);
				 }
			}
		}		
 		
		 return sRet;
     }  


}

