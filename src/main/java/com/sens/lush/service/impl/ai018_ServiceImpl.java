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

import com.sens.lush.ai018_Controller;
import com.sens.lush.service.ai018_Service;

@Service("ai018_Service")
public class ai018_ServiceImpl implements ai018_Service {
	private static final Logger logger = LoggerFactory.getLogger(ai018_Service.class);
    private static String namespace = "ai018";
    private static final long FINAL_STAGE = 4;

    
//     @Autowired
     @Resource(name = "sqlSession")
     protected SqlSessionTemplate sqlSession;


     public List<Map<String, Object>>  list(HashMap paramMap) throws Exception {
	  	 Map<String, Object>  resultMap  = new HashMap<String, Object>();
	  	 
	   	 logger.info("grid" + "selectMain:param "+ paramMap );
	   	 List<Map<String, Object>> list = sqlSession.selectList(namespace+ "." + "selectList",paramMap);
	   	 logger.info("grid" + "selectMain.List: "+ list );
	   	 
		 return list;
  }         

     public List<String>  selectCode(String Codegu) throws Exception {
	  	 Map<String, Object>  paramMap  = new HashMap<String, Object>();
	  	paramMap.put("codegu", Codegu); 
	   	 List<String> list = sqlSession.selectList(namespace+ "." + "selectCode",paramMap);
	   	 
		 return list;
  }         
     
     public void updateStatus( Map<String, Object> body) {
/*    	 
         if (status == null || !ALLOWED_STATUS.contains(status)) {
             throw new IllegalArgumentException("허용되지 않은 상태값입니다: " + status);
         }
*/         
	   	 logger.info("ai018" + "updateStatus.body: "+ body );
		sqlSession.update(namespace + "." + "updateStatus",body);
//         attendanceMapper.updateStatus(id, body);
     }

   
    
     public void updateCheckin( Map<String, Object> body) {
	   	 logger.info("ai018" + "updateStatus.body: "+ body );
		sqlSession.update(namespace + "." + "updateCheckin",body);
     }
     
     public void updateCheckout( Map<String, Object> body) {
	   	 logger.info("ai018" + "updateStatus.body: "+ body );
		sqlSession.update(namespace + "." + "updateCheckout",body);
     }
     
     public void updateEquip( Map<String, Object> body) {
	   	 logger.info("ai018" + "updateStatus.body: "+ body );
		sqlSession.update(namespace + "." + "updateEquip",body);
     }
     
     public void updateapprove( Map<String, Object> body) {
    	 
    	logger.info("ai018" + "updateapprove.body: "+ body );
    	String sId = String.valueOf(body.get("id"));
    	Long Newstage = Long.valueOf(String.valueOf(body.get("stage")));
    	logger.info("ai018" + "updateapprove.Newstage: "+ Newstage );
    	List<Map<String, Object>> list = sqlSession.selectList(namespace+ "." + "selectById",body);
    	logger.info("ai018" + "updateapprove.list: "+ list );
    	
    	if (list.isEmpty()) {
            throw new IllegalArgumentException("존재하지 않는 근태 건입니다: " + sId);
        }
    	Map<String, Object> row = list.get(0);
    	Long Oldstage = Long.valueOf(String.valueOf(row.get("stage")));

    	logger.info("ai018" + "updateapprove.Oldstage: "+ Oldstage );
    	
        // 화면에서는 "다음 결재자" 단계만 탭할 수 있으므로 서버에서도 순차 결재만 허용합니다.
        // (건너뛰기 결재가 필요하면 이 검증을 완화하고, 결재자 role 검증을 추가하세요 —
        //  예: 세션의 로그인 사용자 직급이 STAGES[requestedStage-1] 과 일치하는지 확인)
        if (Newstage == null || Newstage != Oldstage + 1 || Newstage > FINAL_STAGE) {
            throw new IllegalArgumentException(
                    "잘못된 결재 단계입니다. 현재 단계=" + Newstage + ", 요청 단계=" + Newstage);
        }
        
//        body.put("stage", Newstage); 
    	logger.info("ai018" + "updateapprove.body: "+ body );
		sqlSession.update(namespace + "." + "updateStage",body);

//		attendanceMapper.updateStage(id, requestedStage);
//        row.setStage(requestedStage);
        
        
     }

     public void updatesend( Map<String, Object> body) {
    	 
    	logger.info("ai018" + "updateSent.body: "+ body );
    	String sId = String.valueOf(body.get("id"));
    	List<Map<String, Object>> list = sqlSession.selectList(namespace+ "." + "selectById",body);
    	logger.info("ai018" + "updateapprove.list: "+ list );
    	
    	if (list.isEmpty()) {
            throw new IllegalArgumentException("존재하지 않는 근태 건입니다: " + sId);
        }
    	Map<String, Object> row = list.get(0);
    	Long Oldstage = Long.valueOf(String.valueOf(row.get("stage")));
    	Long Oldsent = Long.valueOf(String.valueOf(row.get("sent")));

    	logger.info("ai018" + "updateSent.Oldstage: "+ Oldstage );
    	Long stage = Oldstage == null ? 0 : Oldstage;
        if (stage < FINAL_STAGE) {
            throw new IllegalStateException("결재가 완료되지 않아 전송할 수 없습니다: " + sId);
        }
        
        
        if (Oldsent == 1) {
            return ;
        }
        
        body.put("sent", 1); 
    	logger.info("ai018" + "updateSent.body: "+ body );
		sqlSession.update(namespace + "." + "updateSent",body);

//		attendanceMapper.updateStage(id, requestedStage);
//        row.setStage(requestedStage);
        
        
     }
          
     public String  dataSave(List<Map> paramMap,HashMap MapUser) throws Exception {
		Iterator it;
		HashMap map = new HashMap();
		String sRet="";

		logger.info("ai018save.paramMap: "+ paramMap );
		
		it = paramMap.iterator();
		logger.info("ai018save.it: "+ it );
		
		while (it.hasNext())
		{
			map = (HashMap)it.next();
			if (map.get("DEL") == null)		map.put("DEL", "N");
			if (map.get("NEW") == null)		map.put("NEW", "N");
			map.put("USER",MapUser);
			logger.info("ai018save.map: "+ map );
			if ("Y".equals(map.get("DEL"))){
				logger.info("ai018save.dataDelete: "+ map.get("DEL") );
				sqlSession.update(namespace + "." + "dataDelete",map);
			}
			else{
				 if(map.get("NEW").equals("Y")){
				   	 logger.info("ai018save.dataInsert: "+ map.get("DEL") );
						sqlSession.update(namespace + "." + "dataInsert",map);
				 }
				 else{
//						logger.info("ai018save.dataUpdate: "+ map.get("DEL") );
						sqlSession.update(namespace + "." + "dataUpdate",map);
				 }
			}
		}		
 		
		 return sRet;
  }  
     
}

