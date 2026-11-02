package com.sens.lush;
import com.sens.lush.util.*;

import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sens.lush.service.pop0001_Service;
import com.sens.lush.service.codeMgr_Service;

/**
 * Handles requests for the application home page.
 */
@Controller
public class pop0001_Controller {
	
//	private static final Logger logger = LoggerFactory.getLogger(this.class);
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@Resource(name = "pop0001_Service")
	
	@Autowired
	private pop0001_Service Service;
	@Autowired
	private codeMgr_Service CodeMgrService;
	
	@RequestMapping(value = "/pop0001.do")
	public String pop0001(HttpServletRequest request,Model model ) throws Exception {

		String sPage = "pop0001";
	    logger.info("Start Page: "+ sPage);

		HashMap map = new HashMap();
		
		HashMap MapUser = (HashMap)request.getSession().getAttribute("USER");
	    logger.info("pop0001.MapUser: "+ MapUser);
		
		map.put("USER", MapUser);
	    logger.info("pop0001.Service: "+ Service);
	    logger.info("pop0001.CodeMgrService: "+ CodeMgrService);
		
	    return sPage;
	}

	@RequestMapping(value = "/pop0001grid.do")
	public void gridList(HttpServletRequest request,HttpServletResponse res) throws Exception {

	    logger.info("gridCustList Start:" );
		String sParam = request.getParameter("param");
	    logger.info("param: "+ sParam);
	    if (sParam != null){ //초기화면		
			HashMap map = new HashMap();
			map = JsonUtil.getJsonToMap(sParam);
			
			HashMap MapUser = (HashMap)request.getSession().getAttribute("USER");
		    logger.info("gridCustList.MapUser: "+ MapUser);
			
			map.put("USER", MapUser);
		    logger.info("gridCustList.map: "+ map);
			
			List<Map<String, Object>> list = Service.gridList(map);
		    String gSon = JsonUtil.ListToStringData(list);
	
		    res.setContentType("text/html; charset=UTF-8");
	
			PrintWriter out = res.getWriter();  	
	
		    logger.info("gridCustList.gSon: "+ gSon);
			out.print(gSon); 		
	    }
	}

	
}
