package com.sens.lush.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface codeMgr_Service {
    List<Map<String, Object>>   getCodeList(HashMap paramMap,String sParm,String sKind) throws Exception;
    List<Map<String, Object>>   m_getCodeList(HashMap paramMap,String sParm,String sKind) throws Exception;
}


