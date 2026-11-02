package com.sens.lush.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface ai015_Service {
    List<Map<String, Object>>   gridList(HashMap paramMap) throws Exception;
    String   dataSave(List<Map> paramMap,HashMap MapUser) throws Exception;
    List<Map<String, Object>> getList(String name);
    String   Insert(Map<String, Object> paramMap) throws Exception;
    String   Update(Map<String, Object> paramMap) throws Exception;
    String   Delete(Map<String, Object> paramMap) throws Exception;
}


