package com.sens.lush.service.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
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

import com.sens.lush.ai101_Controller;
import com.sens.lush.service.ai101_Service;

@Service("ai101_Service")
public class ai101_ServiceImpl implements ai101_Service {
	private static final Logger logger = LoggerFactory.getLogger(ai101_Service.class);
    private static String namespace = "ai101";
    
//     @Autowired
     @Resource(name = "sqlSession")
     protected SqlSessionTemplate sqlSession;

     @Resource(name = "sqlSessionIPS")  // ← IPS DB 명시적 지정!
     protected SqlSessionTemplate sqlSessionIPS;     

     @Override
     public List<Map<String, Object>> getSmsList(String custcd) throws Exception {
         Map<String, Object> paramMap = new HashMap<String, Object>();
         
         paramMap.put("custcd", custcd);
         return sqlSession.selectList(namespace + "." + "selectSmsList", paramMap);
     }

     @Override
     public Map<String, Object> getIpsSmsCnt() throws Exception {
         Map<String, Object> paramMap = new HashMap<String, Object>();
         String shopno = sqlSession.selectOne(namespace + "." + "getShopno", paramMap);
         paramMap.put("shopno", shopno);
         
         return (Map<String, Object>) sqlSessionIPS.selectOne(namespace + "." + "IPSgetSmsCnt", paramMap);
     }

     @Override
     public int addIpsData(Map<String, Object> paramMap) throws Exception {
         Map<String, Object> param = new HashMap<String, Object>();
         String kindshopno = sqlSession.selectOne(namespace + "." + "getShopno", param);
         
         String custcd = (String) paramMap.get("CUSTCD");
	   	 logger.info("service" + "addIpsData.paramMap "+ paramMap );

 	    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
 	    String sDatetime = sdf.format(new Date());
 	    
         String sendkey = kindshopno + sDatetime.substring(2, 14);
         Map<String, Object> insertParam = new HashMap<String, Object>();
         insertParam.put("kindshopno", kindshopno);
         insertParam.put("sendkey", sendkey);
         insertParam.put("custcd", custcd);
         insertParam.put("handno", paramMap.get("HANDNO"));
         insertParam.put("msg", paramMap.get("MEMO"));
         insertParam.put("recvno", paramMap.get("RECVNO"));

	   	 logger.info("service" + "addIpsData.insertParam "+ insertParam );
         
         sqlSessionIPS.insert(namespace + "." + "IPSinsertSmsData", insertParam);
         sqlSessionIPS.insert(namespace + "." + "IPSinsertSmsMst", insertParam);
         

         return sqlSession.insert(namespace + "." + "insertSmsSendData", insertParam);
     }
     
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

		logger.info("ai101save.paramMap: "+ paramMap );
		
		it = paramMap.iterator();
		logger.info("ai101save.it: "+ it );
		
		while (it.hasNext())
		{
			map = (HashMap)it.next();
			if (map.get("DEL") == null)		map.put("DEL", "N");
			if (map.get("NEW") == null)		map.put("NEW", "N");
			map.put("USER",MapUser);
			logger.info("ai101save.map: "+ map );
			if ("Y".equals(map.get("DEL"))){
				logger.info("ai101save.dataDelete: "+ map.get("DEL") );
				sqlSession.update(namespace + "." + "dataDelete",map);
			}
			else{
				 if(map.get("NEW").equals("Y")){
				   	 logger.info("ai101save.dataInsert: "+ map.get("DEL") );
						sqlSession.update(namespace + "." + "dataInsert",map);
				 }
				 else{
//						logger.info("ai101save.dataUpdate: "+ map.get("DEL") );
						sqlSession.update(namespace + "." + "dataUpdate",map);
				 }
			}
		}		
 		
		 return sRet;
     }  

     public List<Map<String, Object>>  selectCustList(Map<String, Object> paramMap) throws Exception {
	  	 Map<String, Object>  resultMap  = new HashMap<String, Object>();
	  	 
	   	 logger.info("grid" + "selectCustList "+ paramMap );
	   	 List<Map<String, Object>> list = sqlSession.selectList(namespace+ "." + "selectCustList",paramMap);
	   	 logger.info("grid" + "selectCustList.List: "+ list );
	   	 
		 return list;
     }  

     public List<Map<String, Object>>  detailView(Map<String, Object> paramMap) throws Exception {

 	    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
 	    String sDatetime = sdf.format(new Date());
 	    
 	    String sToday = sDatetime.substring(0, 8);

 	    String custcd = (String) paramMap.get("custcd");
	  	paramMap.put("CUSTCD", custcd);
	  	paramMap.put("FRDATE", sToday);
	  	paramMap.put("TODATE", sToday);
        
	   	 logger.info("grid" + ".selectMisu.param "+ paramMap );
	   	 List<Map<String, Object>> janlist = sqlSession.selectList(namespace+ "." + "selectMisu",paramMap);
	   	 logger.info("grid" + ".selectMisu.janlist: "+ janlist );
     	 Map<String, Object> rowday = janlist.get(0);
     	 Object janamtObj = rowday.get("JANAMT");
     	 long janamt = (janamtObj != null) ? ((Number) janamtObj).longValue() : 0L;
     	
	   	 logger.info("grid" + "selectSaleSKList "+ paramMap );
	   	 List<Map<String, Object>> list = sqlSession.selectList(namespace+ "." + "selectSaleSKList",paramMap);
	   	 logger.info("grid" + "selectSaleSKList.List: "+ list );

	   	for (int i = list.size() - 1; i >= 0; i--) {
	   	    Map<String, Object> row = list.get(i);
	   	    
	   	    row.put("MISU", janamt);

	   	    Object mcskObj = row.get("MCSK");
	   	    String mcsk = (mcskObj != null) ? mcskObj.toString() : "";

	   	    Object amtObj = row.get("AMT");
	   	    long amt = (amtObj != null) ? ((Number) amtObj).longValue() : 0L;

	   	    Object banpumObj = row.get("BANPUM");
	   	    long banpum = (banpumObj != null) ? ((Number) banpumObj).longValue() : 0L;

	   	    Object chshObj = row.get("CASHAMT");
	   	    long cash = (chshObj != null) ? ((Number) chshObj).longValue() : 0L;

	   	    Object saleObj = row.get("SALEAMT");
	   	    long sale = (saleObj != null) ? ((Number) saleObj).longValue() : 0L;

	   	    if ("매출".equals(mcsk)) {
	   	        janamt = janamt - amt + banpum;
	   	    }
	   	    else {
	   	        janamt = janamt + cash + sale;
	   	    }

	   	}
	   	
		 return list;
     }  
     
     public List<Map<String, Object>>  selectJepmList(Map<String, Object> paramMap) throws Exception {
	  	 Map<String, Object>  resultMap  = new HashMap<String, Object>();
	  	 
	   	 logger.info("grid" + "selectJepmList "+ paramMap );
	   	 List<Map<String, Object>> list = sqlSession.selectList(namespace+ "." + "selectJepmList",paramMap);
	   	 logger.info("grid" + "selectJepmList.List: "+ list );
	   	 
		 return list;
     }       
     public List<Map<String, Object>> selectMisu(Map<String, Object> paramMap) throws Exception {
	  	 Map<String, Object>  resultMap  = new HashMap<String, Object>();
	  	 
	   	 logger.info("grid" + ".selectMisu.param "+ paramMap );
	   	 List<Map<String, Object>> list = sqlSession.selectList(namespace+ "." + "selectMisu",paramMap);
	   	 logger.info("grid" + ".selectMisu.List: "+ list );
	   	 
		 return list;    	 
     }
     
     public String saveSale(String Stat,Map<String, Object> body) throws Exception {
    	    String custcd = (String) body.get("CUSTCD");
    	    String ipdate = (String) body.get("IPDATE"); // "2026-09-01" (input type=date)
    	    String jpno = (String) body.get("JPNO");
    	    
    	    logger.info("saveSale.parm.custcd: "+ custcd);
    	    logger.info("saveSale.parm.ipdate: "+ ipdate);
    	    logger.info("saveSale.parm.jpno: "+ jpno);
    	    
    	    String cdate = (ipdate != null) ? ipdate.replace("-", "") : ""; // "20260901" 형태로 변환 (실제 컬럼 포맷에 맞게 조정)
    	 
    	    List<Map<String, Object>> products = (List<Map<String, Object>>) body.get("products");
    	    if (Stat.equals("save")) {
	    	    if (products == null || products.isEmpty()) {
	    	        throw new IllegalArgumentException("저장할 판매제품이 없습니다.");
	    	    }
    	    }
    	    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
    	    String sDatetime = sdf.format(new Date());
    	    
    	    String sToday = sDatetime.substring(0, 8);
    	    String year = sDatetime.substring(3, 4);        // "2024"
    	    String month = sDatetime.substring(4, 6);       // "03"
    	    String sMM="";
    	    if (month.equals("10")) {
    	        sMM = "A";
    	    } else if (month.equals("11")) {
    	        sMM = "B";
    	    } else if (month.equals("12")) {
    	        sMM = "C";
    	    } else {
    	        sMM = sDatetime.substring(5, 6);;
    	    }
    	    
    	    String day = sDatetime.substring(6, 8);         // "24"
    	    String hour = sDatetime.substring(8, 10);       // "15"
    	    String minute = sDatetime.substring(10, 12);    // "30"
    	    String second = sDatetime.substring(12, 14);    // "45"
    	    long lNo = Long.parseLong(hour) * 3600 + Long.parseLong(minute) * 3600 + Long.parseLong(second);
    	    String sNo = String.format("%05d", lNo);
    	    String sJpno = "01" + year + sMM + day + sNo;
    	    String sStat = "NEW";
    	    if (jpno == null || jpno.trim().isEmpty()) {
    	    	sStat = "NEW";
    	    	jpno = sJpno;
    	    }
    	    else {
    	    	sStat = "EDIT";
    	    }
    	    
    	    logger.info("saveSale.sJpno: "+ sJpno);
    	    logger.info("saveSale.Jpno: "+ jpno);
    	    logger.info("saveSale.sStat: "+ sStat);

    	    Map<String, Object> parmMap = new HashMap<String, Object>();
    	    parmMap.put("custcd", custcd);
    	    parmMap.put("cdate", cdate);
    	    parmMap.put("YYMM", cdate.substring(0, 6));
    	    parmMap.put("jpno", jpno);
    	    parmMap.put("OLDJPNO", jpno);

//    	    map.put("products", products);

    	    if (sStat != null && sStat.equals("EDIT")){
        	    logger.info("saveSale.MCMinus_subul.map "+ parmMap );
    			sqlSession.update(namespace + "." + "MCMinus_subul",parmMap);
    	    	
        	    logger.info("saveSale.m_dataMCDel_dtl.map "+ parmMap );
    			sqlSession.update(namespace + "." + "m_dataMCDel_dtl",parmMap);

    			logger.info("saveSale.m_dataMCDel_mst.parmMap "+ parmMap );
    			sqlSession.update(namespace + "." + "m_dataMCDel_mst",parmMap);
    	    	
    	    }
    	    
    	    if (Stat.equals("save")) {
	    	    int seq = 1;
	    	    for (Map<String, Object> p : products) {
	    	        Map<String, Object> map = new HashMap<String, Object>();
	    	        map.put("custcd", custcd);
	    	        map.put("cdate", cdate);
	    	        map.put("jpno", jpno);
	    	        map.put("seq", seq++);
	    	        map.put("jepmno", p.get("JEPMNO"));
	    	        map.put("saleprice", p.get("SALEPRICE"));
	    	        map.put("qty", p.get("QTY"));
	    	        map.put("amt", p.get("AMT"));
	    	        map.put("remk", p.get("REMK"));
	    	        Object qtyObj = p.get("QTY");
	    	        int qty = (qtyObj != null) ? ((Number) qtyObj).intValue() : 0;

	    	        if (qty != 0) {
		        	    logger.info("saveSale.insertDtl.map "+ map );
		    			sqlSession.update(namespace + "." + "insertDtl",map);
	    	        }
	    	    }    	    
	
	    	    logger.info("saveSale.insertMst.parmMap "+ parmMap );
				sqlSession.update(namespace + "." + "insertMst",parmMap);
	
				logger.info("saveSale.MC_subul.map "+ parmMap );
				sqlSession.update(namespace + "." + "MC_subul",parmMap);
    	    }
			
			String sRet="";
			
			return sRet;
     }
     
     /** 기간 + 거래처 조건으로 판매 내역 목록(전표 단위로 그룹)을 조회 (ai101_sale_query.do) */
     public   List<Map<String, Object>> selectSaleQueryList(Map<String, Object> paramMap)  throws Exception {
//	  	 Map<String, Object>  resultMap  = new HashMap<String, Object>();
	  	 
	   	 logger.info("grid" + "selectSaleQueryList "+ paramMap );
	   	 List<Map<String, Object>> list = sqlSession.selectList(namespace+ "." + "selectSaleQueryList",paramMap);
	   	 logger.info("grid" + "selectSaleQueryList.List: "+ list );
	   	 
		 return list;    	 
     }
      
     /** jpno(전표번호) 1건의 헤더 정보(거래처/일자) 조회 (ai101_sale_detail.do) */
     public  Map<String, Object> selectSaleHeader(Map<String, Object> paramMap) throws Exception {
	   	 logger.info("grid" + "selectSaleHeader "+ paramMap );
	   	 List<Map<String, Object>> list = sqlSession.selectList(namespace+ "." + "selectSaleHeader",paramMap);
	   	 logger.info("grid" + "selectSaleHeader.List: "+ list );
	   	Map<String, Object>  resultMap  = new HashMap<String, Object>();
	   	if (list != null && !list.isEmpty()) {
	   	     resultMap = list.get(0);
	   	}
	   	
		 return resultMap;      	 
     }
      
     /** jpno(전표번호) 1건의 상세(제품) 목록 조회 (ai101_sale_detail.do) */
     public  List<Map<String, Object>> selectSaleDetailList(Map<String, Object> paramMap)  throws Exception {
	   	 logger.info("grid" + "selectSaleDetailList "+ paramMap );
	   	 List<Map<String, Object>> list = sqlSession.selectList(namespace+ "." + "selectSaleDetailList",paramMap);
	   	 logger.info("grid" + "selectSaleDetailList.List: "+ list );
	   	 
		 return list;        	 
     }
     
     
     @Override
     public List<Map<String, Object>> getMemoList(String custcd) throws Exception {
         Map<String, Object> paramMap = new HashMap<String, Object>();
         paramMap.put("custcd", custcd);
         return sqlSession.selectList(namespace + "." + "selectMemoList", paramMap);
     }

     @SuppressWarnings("unchecked")
     @Override
     public void saveMemo(Map<String, Object> body) throws Exception {
         String custcd = (String) body.get("CUSTCD");
         List<Map<String, Object>> list = (List<Map<String, Object>>) body.get("list");

         if (custcd == null || custcd.trim().isEmpty()) {
             throw new IllegalArgumentException("CUSTCD가 없습니다.");
         }
         if (list == null) {
             return;
         }

         // 신규 행에 채번할 다음 순번 (현재 최대 SEQ + 1부터 순서대로)
         Map<String, Object> seqParam = new HashMap<String, Object>();
         seqParam.put("custcd", custcd);
         Object maxSeqObj = sqlSession.selectOne(namespace + "." + "selectMemoMaxSeq", seqParam);
         int nextSeq = (maxSeqObj != null) ? ((Number) maxSeqObj).intValue() + 1 : 1;

         String today = new SimpleDateFormat("yyyyMMdd").format(new Date());

         for (Map<String, Object> row : list) {
             Object seqObj = row.get("SEQ");
             String memo = (String) row.get("MEMO");
             String plandate = (String) row.get("PLANDATE");
             if (plandate != null) {
                 plandate = plandate.replace("-", "");
             }

             Map<String, Object> paramMap = new HashMap<String, Object>();
             paramMap.put("custcd", custcd);
             paramMap.put("memo", memo);
             paramMap.put("plandate", plandate);

             if (seqObj == null || seqObj.toString().trim().isEmpty()) {
                 // 신규 행
                 paramMap.put("seq", nextSeq);
                 paramMap.put("prdate", today);
                 sqlSession.insert(namespace + "." + "insertMemo", paramMap);
                 nextSeq++;
             } else {
                 // 기존 행 수정
                 paramMap.put("seq", ((Number) seqObj).intValue());
                 sqlSession.update(namespace + "." + "updateMemo", paramMap);
             }
         }
     }

     @Override
     public void deleteMemo(Map<String, Object> body) throws Exception {
         String custcd = (String) body.get("CUSTCD");
         Object seqObj = body.get("SEQ");

         if (custcd == null || seqObj == null) {
             throw new IllegalArgumentException("CUSTCD/SEQ가 없습니다.");
         }

         Map<String, Object> paramMap = new HashMap<String, Object>();
         paramMap.put("custcd", custcd);
         paramMap.put("seq", ((Number) seqObj).intValue());

         sqlSession.delete(namespace + "." + "deleteMemo", paramMap);
     }     
     
     @Override
     public Map<String, Object> getStatementCompany() throws Exception {
         Map<String, Object> paramMap = new HashMap<String, Object>();
         return (Map<String, Object>) sqlSession.selectOne(namespace + "." + "selectStatementCompany", paramMap);
     }
     

     @Override
     public Map<String, Object> getStatementHeader(String jpno) throws Exception {
	   	 logger.info("grid" + "getStatementHeader "+ jpno );
         Map<String, Object> paramMap = new HashMap<String, Object>();
         paramMap.put("jpno", jpno);
         return (Map<String, Object>) sqlSession.selectOne(namespace + "." + "selectStatementHeader", paramMap);
     }

     @Override
     public List<Map<String, Object>> getStatementList(String jpno) throws Exception {
         Map<String, Object> paramMap = new HashMap<String, Object>();
         paramMap.put("jpno", jpno);
         return sqlSession.selectList(namespace + "." + "selectStatementList", paramMap);
     }
     
}

