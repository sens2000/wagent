package com.sens.lush.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface ai101_Service {
    List<Map<String, Object>>   gridList(HashMap paramMap) throws Exception;
    String   dataSave(List<Map> paramMap,HashMap MapUser) throws Exception;
    List<Map<String, Object>>   selectCustList(Map<String, Object> paramMap) throws Exception;
    List<Map<String, Object>>   selectJepmList(Map<String, Object> paramMap) throws Exception;
    List<Map<String, Object>> selectMisu(Map<String, Object> paramMap) throws Exception;
    List<Map<String, Object>>   detailView(Map<String, Object> paramMap) throws Exception;
    String saveSale(String Stat,Map<String, Object> body) throws Exception;
    /** 기간 + 거래처 조건으로 판매 내역 목록(전표 단위로 그룹)을 조회 (ai101_sale_query.do) */
    List<Map<String, Object>> selectSaleQueryList(Map<String, Object> paramMap) throws Exception ;
     
    /** jpno(전표번호) 1건의 헤더 정보(거래처/일자) 조회 (ai101_sale_detail.do) */
    Map<String, Object> selectSaleHeader(Map<String, Object> paramMap) throws Exception ;
     
    /** jpno(전표번호) 1건의 상세(제품) 목록 조회 (ai101_sale_detail.do) */
    List<Map<String, Object>> selectSaleDetailList(Map<String, Object> paramMap) throws Exception ;
    
    List<Map<String, Object>> getMemoList(String custcd) throws Exception;
    void saveMemo(Map<String, Object> body) throws Exception;
    void deleteMemo(Map<String, Object> body) throws Exception;   
    
    Map<String, Object> getStatementCompany() throws Exception;
    Map<String, Object> getStatementHeader(String jpno) throws Exception;
    List<Map<String, Object>> getStatementList(String jpno) throws Exception;
    
    int addIpsData(Map<String, Object> paramMap) throws Exception;
    List<Map<String, Object>> getSmsList(String custcd) throws Exception;
    Map<String, Object> getIpsSmsCnt() throws Exception;
}


