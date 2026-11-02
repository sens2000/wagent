package com.sens.lush.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface pos01_Service {
    List<Map<String, Object>>   getPosno(HashMap paramMap) throws Exception;
    List<Map<String, Object>>   getUserinfo(HashMap paramMap) throws Exception;
    List<Map<String, Object>>   getUsermst(HashMap paramMap) throws Exception;
    List<Map<String, Object>>   getJepmmst(HashMap paramMap) throws Exception;
    List<Map<String, Object>>   getJepmjepmcd(HashMap paramMap) throws Exception;
    List<Map<String, Object>>   getJepmfind(HashMap paramMap) throws Exception;
    List<Map<String, Object>>   getMemberfind(HashMap paramMap) throws Exception;
    
    String   setJepmdownkey(Map<String, Object> paramMap) throws Exception;
    String   insertMc20mst(Map<String, Object> paramMap) throws Exception;
    String   insertMc20dtl(Map<String, Object> paramMap) throws Exception;
    String   insertJepmmst(Map<String, Object> paramMap) throws Exception;

    String   insertMember(Map<String, Object> paramMap) throws Exception;
    
    List<Map<String, Object>>   gridList(HashMap paramMap) throws Exception;
    String   dataSave(List<Map> paramMap,HashMap MapUser) throws Exception;
    List<Map<String, Object>> getList(String name);
    String   Insert(Map<String, Object> paramMap) throws Exception;
    String   Update(Map<String, Object> paramMap) throws Exception;
    String   Delete(Map<String, Object> paramMap) throws Exception;
}


