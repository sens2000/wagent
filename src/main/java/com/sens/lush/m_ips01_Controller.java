package com.sens.lush;
import com.sens.lush.util.*;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import org.apache.ibatis.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

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
import com.sens.lush.service.m_ips01_Service;
import com.sens.lush.service.codeMgr_Service;


/**
 * Handles requests for the application home page.
 */
@Controller
public class m_ips01_Controller {
	
//	private static final Logger logger = LoggerFactory.getLogger(this.class);
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
	 private static final String UPLOAD_DIR = "/opt/photos";
	 private final ObjectMapper objectMapper = new ObjectMapper();
	 
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@Resource(name = "m_ips01_Service")
	private m_ips01_Service Service;
	@Autowired
	private codeMgr_Service CodeMgrService;

	@RequestMapping(
			  value="/getSmsShop.do",
			  method=RequestMethod.POST,
			  produces="application/json; charset=UTF-8"
			)
			@ResponseBody
			public String getSmsShop(HttpServletRequest request, Model model, HttpSession session) throws Exception {
  	
		String sId = request.getParameter("ID");
		String sBuseo = request.getParameter("BUSEO");
		String sMimc = URLDecoder.decode(request.getParameter("MIMC"), StandardCharsets.UTF_8.name());
		String sFind = URLDecoder.decode(request.getParameter("FIND"), StandardCharsets.UTF_8.name());

	    logger.info("m_sale09.BUSEO: "+ sBuseo);
	    logger.info("m_sale09.sMimc: "+ sMimc);
	    logger.info("m_sale09.sFind: "+ sFind);
		
		HashMap map = new HashMap();
	    Map<String, Object> res = new HashMap<>();
		
	    logger.info("m_sale09.Service: "+ Service);
	    
	    map.put("BUSEO", sBuseo);
	    map.put("MIMC", sMimc);
	    map.put("FIND", sFind);
	    

		List<Map<String, Object>> listCust = Service.m_getSmsList(map);
//	    String gSonBrand = JsonUtil.ListToString(listBrand);
				
	    logger.info("m_sale09.listCust: "+ listCust);

		res.put("result", "OK");
		res.put("message", "SUCCESS");
		res.put("accessToken", "m_main");
		res.put("data", listCust);

		
		return new ObjectMapper().writeValueAsString(res);
	}

	@RequestMapping(
			  value="/shopAuth.do",
			  method=RequestMethod.POST,
			  produces="application/json; charset=UTF-8"
			)
			@ResponseBody
			public String shopAuth(HttpServletRequest request, Model model, HttpSession session) throws Exception {
	
		String sId = request.getParameter("ID");
		String sPass = request.getParameter("PASS");
		String sPosNo = request.getParameter("BUSEO");
	    Map<String, Object> res = new HashMap<>();
		
		System.out.println("m_loginConfirm.logIn: "+ sId );
		System.out.println("m_loginConfirm.sPass: "+ sPass );
		String sPage = "";
		String Status = "";
		if (sId==null || sPass==null){
			sPage = "m_relogin";
		}
		else {
			HashMap map = new HashMap();
			map.put("SID", sId);
			map.put("SPASS", sPass);
			List<Map<String, Object>> listLogin = Service.loginCheck(map);

//		    List<Map> temp = (List<Map>) list.get("data");
		    if (listLogin.size()==0){
			    logger.info("temp.size 0: " );
				sPage = "m_relogin";
				res.put("result", "NO");
				res.put("message", "아이디 또는 암호가 틀립니다.");
				res.put("accessToken", "m_relogin");
				Status = "오류";
		    }
		    else{
				SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
				Date today = new Date();
				String strDate = format.format(today);
				map.put("YYMM", strDate.substring(0, 6));
				map.put("YYMMDD", strDate);
			    logger.info("listLogin: "+ listLogin);

				Iterator it = listLogin.iterator();
				HashMap mapUser = new HashMap();
				mapUser = (HashMap)it.next();
			    logger.info("mapUser: "+ mapUser);

				if ("S".equals(mapUser.get("CHK"))) {
					sPage = "m_relogin";
					res.put("result", "NO");
					res.put("message", "사용중지된 아이디 입니다.");
					res.put("accessToken", "m_relogin");
					Status = "중지";
				} else {
//					session.setAttribute("USER", mapUser);
					String sShopno = (String)mapUser.get("SHOPNO");
				    logger.info("sShopno: "+ sShopno);
					
					res.put("result", "OK");
					res.put("message", "SUCCESS");
					res.put("accessToken", "m_main");
					res.put("data", listLogin);
					Status = "정상";
			        sPage = "m_main";
				}
		    }
			map.put("STATUS", Status);
			String sRet = Service.m_LogSave(map);
			logger.info("sRet: "+ sRet);
			logger.info("sPage: "+ sPage);

				
		}
		return new ObjectMapper().writeValueAsString(res);
	}


	@RequestMapping(
			  value="/saveCustSms.do",
			  method=RequestMethod.POST,
			  produces="application/json; charset=UTF-8"
			)
			@ResponseBody
			public String saveCustSms(HttpServletRequest request, Model model, HttpSession session) throws Exception {
  	
		String sId = request.getParameter("ID");
		String sBuseo = request.getParameter("BUSEO");
		String sCustcd = request.getParameter("CUSTCD");
		String sSKDate = request.getParameter("SKDATE");
		String sKjDate = request.getParameter("KJDATE");
		sSKDate = sSKDate.replace("-", "");
	    logger.info("m_sale10.BUSEO: "+ sBuseo);
	    logger.info("m_sale10.CUSTCD: "+ sCustcd);
	    logger.info("m_sale10.SKDATE: "+ sSKDate);
		String sAmount = request.getParameter("AMOUNT");
	    logger.info("m_sale10.AMOUNT: "+ sAmount);
	    String sRemk = "";
	    String remkParam = request.getParameter("KJDATE");
        sRemk = URLDecoder.decode(remkParam, StandardCharsets.UTF_8.name());
	    logger.info("m_sale10.sRemk: "+ sRemk);
	    
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
	    String sDatetime = sdf.format(new Date());
	    String sJpno = "01";
	    String sToday = sDatetime.substring(0, 8);

	    sJpno = sJpno + sCustcd + sSKDate + sDatetime.substring(8, 14);

	    logger.info("m_sale03.sJpno: "+ sJpno);
	    
		HashMap map = new HashMap();
	    Map<String, Object> res = new HashMap<>();
		
	    logger.info("m_sale10.Service: "+ Service);
	    
	    map.put("BUSEO", sBuseo);
	    map.put("JPNO", sJpno);
	    map.put("KJSKDATE", sSKDate);
	    map.put("KJDATE", sKjDate);
	    map.put("CUSTCD", sCustcd);
	    map.put("CASHAMT", sAmount);
	    map.put("PRDATE", sToday);
	    map.put("PRUSER", sId);
	    map.put("REMK", sRemk);
        
		String sRet = Service.m_dataSmsSave(map);
	    
		res.put("result", "OK");
		res.put("message", "SUCCESS");
		res.put("accessToken", "m_main");
//		res.put("data", listCust);

		
		return new ObjectMapper().writeValueAsString(res);
	}

	@RequestMapping(
			  value="/shopList.do",
			  method=RequestMethod.POST,
			  produces="application/json; charset=UTF-8"
			)
			@ResponseBody
			public String shopList(HttpServletRequest request, Model model, HttpSession session) throws Exception {
	
		String sId = request.getParameter("ID");
		String sGroupno = request.getParameter("GROUPNO");

	    logger.info("shopList.GROUPNO: "+ sGroupno);
		
		HashMap map = new HashMap();
	    Map<String, Object> res = new HashMap<>();
		
	    logger.info("shopList.Service: "+ Service);
	    
	    map.put("GROUPNO", sGroupno);
	    

		List<Map<String, Object>> listCust = Service.m_getShopList(map);
//	    String gSonBrand = JsonUtil.ListToString(listBrand);
				
	    logger.info("shopList.listCust: "+ listCust);

		res.put("result", "OK");
		res.put("message", "SUCCESS");
		res.put("accessToken", "m_main");
		res.put("data", listCust);

		
		return new ObjectMapper().writeValueAsString(res);
	}

    
}
