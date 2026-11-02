package com.sens.lush.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface m_ips01_Service {
    List<Map<String, Object>>   m_getSmsList(HashMap paramMap) throws Exception;
    String   m_dataSmsSave(HashMap paramMap) throws Exception;
    String   m_LogSave(HashMap paramMap) throws Exception;
    List<Map<String, Object>>   loginCheck(HashMap paramMap) throws Exception;
    List<Map<String, Object>>   posInfo(HashMap paramMap) throws Exception;
    List<Map<String, Object>>   m_getShopList(HashMap paramMap) throws Exception;
    

}


