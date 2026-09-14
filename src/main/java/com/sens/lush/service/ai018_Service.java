package com.sens.lush.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface ai018_Service {
    List<Map<String, Object>>   list(HashMap paramMap) throws Exception;
    List<String>   selectCode(String Codegu) throws Exception;
    void updateStatus(Map<String, Object> body) throws Exception;
    void updateCheckin(Map<String, Object> body) throws Exception;
    void updateCheckout(Map<String, Object> body) throws Exception;
    void updateEquip(Map<String, Object> body) throws Exception;
    void updateapprove(Map<String, Object> body) throws Exception;
    void updatesend(Map<String, Object> body) throws Exception;
    
    String   dataSave(List<Map> paramMap,HashMap MapUser) throws Exception;
}


