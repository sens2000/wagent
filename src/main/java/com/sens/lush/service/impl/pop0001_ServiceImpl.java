package com.sens.lush.service.impl;

import java.text.DateFormat;
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

import com.sens.lush.pop0001_Controller;
import com.sens.lush.service.pop0001_Service;

//		List result = sqlSession.selectList("sy2073Mapper.findSheetList", paramMap);

@Service("pop0001_Service")
public class pop0001_ServiceImpl implements pop0001_Service {
	private static final Logger logger = LoggerFactory.getLogger(pop0001_Service.class);
    private static String namespace = "pop0001";
    
     @Autowired
     @Resource(name = "sqlSession")
     protected SqlSessionTemplate sqlSession;


     public List<Map<String, Object>>  gridList(HashMap paramMap) throws Exception {
	  	 Map<String, Object>  resultMap  = new HashMap<String, Object>();
	  	 
	   	 logger.info("grid" + "selectMain:param "+ paramMap );
	   	 List<Map<String, Object>> list = sqlSession.selectList(namespace+ "." + "selectMain",paramMap);
	   	 logger.info("grid" + "selectMain.List: "+ list );
	   	 
		 return list;
  }         

}

