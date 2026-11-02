package com.sens.lush.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface is0001_Service {
    List<Map<String, Object>>   gridList(HashMap paramMap) throws Exception;
    String   dataSave(List<Map> paramMap,HashMap MapUser) throws Exception;
}


