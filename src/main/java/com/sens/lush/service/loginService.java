package com.sens.lush.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface loginService {
    List<Map<String, Object>>   loginCheck(HashMap paramMap) throws Exception;
    List<Map<String, Object>>   posInfo(HashMap paramMap) throws Exception;
    Map<String, Object>   menuGroup(HashMap paramMap,String sGubun) throws Exception;
    Map<String, Object>   menuAgent(HashMap paramMap,String sGubun) throws Exception;
    List<Map<String, Object>>   gridGroupNews(HashMap paramMap) throws Exception;
    List<Map<String, Object>>   gridComDay(HashMap paramMap) throws Exception;

}


