package com.sens.lush.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface m_sale01_Service {
    List<Map<String, Object>>   gridList(HashMap paramMap) throws Exception;
    List<Map<String, Object>>   m_getCustMCSK(HashMap paramMap) throws Exception;
    List<Map<String, Object>>   m_getCodeList(HashMap paramMap) throws Exception;
    List<Map<String, Object>>   m_getJepumSearch(HashMap paramMap) throws Exception;
    List<Map<String, Object>>   m_getBrandSearch(HashMap paramMap) throws Exception;
    List<Map<String, Object>>   m_MCSKQuery(HashMap paramMap) throws Exception;
    List<Map<String, Object>>   m_JPNOMCSKQuery(HashMap paramMap) throws Exception;

    String   m_dataMCSave(Map<String, Object>[] mapData,HashMap mapMst) throws Exception;

    String   m_dataSKSave(HashMap paramMap) throws Exception;
    String   dataSave(List<Map> paramMap,HashMap MapUser) throws Exception;
}


