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
import com.sens.lush.service.is0002_Service;
import com.sens.lush.service.codeMgr_Service;

/**
 * Handles requests for the application home page.
 */
@Controller
public class is0002_Controller {
	
//	private static final Logger logger = LoggerFactory.getLogger(this.class);
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@Resource(name = "is0002_Service")
	
	@Autowired
	private is0002_Service Service;
	@Autowired
	private codeMgr_Service CodeMgrService;
	
	@RequestMapping(value = "/is0002.do")
	public String is0002(HttpServletRequest request,Model model ) throws Exception {

		String sPage = "is0002";
	    logger.info("Start Page: "+ sPage);

		HashMap map = new HashMap();
		
		HashMap MapUser = (HashMap)request.getSession().getAttribute("USER");
	    logger.info("getAcctGrpName.MapUser: "+ MapUser);
		
		map.put("USER", MapUser);
	    logger.info("getAcctGrpName.Service: "+ Service);
	    logger.info("getAcctGrpName.CodeMgrService: "+ CodeMgrService);
		
		List<Map<String, Object>> listCode = CodeMgrService.getCodeList(map,"code_mst","입출");
		model.addAttribute("comboINOUT", listCode);

	    String gSonCode = JsonUtil.ListToString(listCode);
		model.addAttribute("INOUT", gSonCode);
		
		List<Map<String, Object>> listAcctGrp = CodeMgrService.getCodeList(map,"ACCTGRPNAME","");
	    String gSon = JsonUtil.ListToString(listAcctGrp);
		model.addAttribute("ACCTGRPNAME", gSon);
		
	    logger.info("getAcctGrpName.gSon: "+ gSon);
		
	    return sPage;
	}

	@RequestMapping(value = "/is0002grid.do")
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

	@RequestMapping(value = "/is0002save.do")
	public @ResponseBody String dataSave(HttpServletRequest request,HttpServletResponse res) throws Exception {
        
		String sList = request.getParameter("list");
		logger.info("is0002save.list: "+ sList );

		Map<String, Object> mapSos = new ObjectMapper().readValue(sList, HashMap.class);
		HashMap MapUser = (HashMap)request.getSession().getAttribute("USER");
	    logger.info("gridCustList.MapUser: "+ MapUser);
		
		List<Map> updateList = (List<Map>) mapSos.get("updateList");
		logger.info("is0002save.updateList: "+ updateList );
		String sRet = Service.dataSave(updateList,MapUser);

		return "{\"result\": \"success\"}";
	}
	
	
}
