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
import com.sens.lush.service.is0005_Service;
import com.sens.lush.service.codeMgr_Service;

/**
 * Handles requests for the application home page.
 */
@Controller
public class is0005_Controller {
	
//	private static final Logger logger = LoggerFactory.getLogger(this.class);
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    public  final String CNTR_NAME = "is0005";
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@Resource(name = "is0005_Service")
	
	@Autowired
	private is0005_Service Service;
	@Autowired
	private codeMgr_Service CodeMgrService;
	
	@RequestMapping(value = "/is0005.do")
	public String is0005(HttpServletRequest request,Model model ) throws Exception {

		String sPage = "is0005";
	    logger.info("Start Page: "+ sPage);

		HashMap map = new HashMap();
		
		HashMap MapUser = (HashMap)request.getSession().getAttribute("USER");
	    logger.info("is0005.MapUser: "+ MapUser);
		
		map.put("USER", MapUser);
	    logger.info("is0005.Service: "+ Service);
	    logger.info("is0005.CodeMgrService: "+ CodeMgrService);
	    
	    
		List<Map<String, Object>> listBrand = CodeMgrService.getCodeList(map,"JEPMBRANDLIST","");
		model.addAttribute("comboBRAND", listBrand);

	    String gSonCode = JsonUtil.ListToString(listBrand);
		model.addAttribute("BRAND", gSonCode);
		

		List<Map<String, Object>> listKind = CodeMgrService.getCodeList(map,"JEPMKINDLIST","");
		model.addAttribute("comboKIND", listKind);
		
	    String gSon = JsonUtil.ListToString(listKind);
		model.addAttribute("KIND", gSon);
		
	    logger.info("is0005.gSon: "+ gSon);
		
	    return sPage;
	}

	@RequestMapping(value = "/is0005grid.do")
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

	@RequestMapping(value = "/is0005save.do")
	public @ResponseBody String dataSave(HttpServletRequest request,HttpServletResponse res) throws Exception {
        
		String sList = request.getParameter("list");
		logger.info("is0005save.list: "+ sList );

		Map<String, Object> mapSos = new ObjectMapper().readValue(sList, HashMap.class);
		HashMap MapUser = (HashMap)request.getSession().getAttribute("USER");
	    logger.info("gridCustList.MapUser: "+ MapUser);
		
		List<Map> updateList = (List<Map>) mapSos.get("updateList");
		logger.info("is0005save.updateList: "+ updateList );
		String sRet = Service.dataSave(updateList,MapUser);

		return "{\"result\": \"success\"}";
	}
	
	
}
