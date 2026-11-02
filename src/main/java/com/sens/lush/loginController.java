package com.sens.lush;
import com.sens.lush.util.*;
//import com.sens.lush.vo.loginVO;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sens.lush.service.codeMgr_Service;
import com.sens.lush.service.loginService;
import com.sens.lush.service.RememberMeService;
import com.sens.lush.filter.AutoLoginFilter;
/**
 * Handles requests for the application home page.
 */
@Controller
public class loginController {
	
//	private static final Logger logger = LoggerFactory.getLogger(this.class);
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
		logger.info("Welcome home! The client locale is {}.", locale);
		
		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
		
		String formattedDate = dateFormat.format(date);
		
		model.addAttribute("serverTime", formattedDate );
		System.out.println("startPage: "+ "0" );
		
		return "relogin";
	}
    
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@Resource(name = "loginService")
	private loginService Service;
	@Autowired
	private codeMgr_Service CodeMgrService;
	@Autowired
	private RememberMeService rememberMeService;

    private final ObjectMapper objectMapper = new ObjectMapper();
    
//    @RequestMapping(value = "/member_search", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @RequestMapping(value = "/member_search", method = RequestMethod.POST)
    
    @ResponseBody
//	public String member_search(@RequestBody String jsonBody,HttpServletResponse response) throws Exception {    	
   	public void member_search(@RequestBody String jsonBody,HttpServletResponse response) throws Exception {    	
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=UTF-8");

        System.out.println("[member_search] received: " + jsonBody);

        ObjectMapper mapper = new ObjectMapper();
        HashMap<String, Object> map = mapper.readValue(jsonBody, new TypeReference<HashMap<String, Object>>() {});
        String name   = (String) map.get("name");
        String handno = (String) map.get("handno");
        
        System.out.println("[member_search] name: " + name);
        System.out.println("[member_search] handno: " + handno);
        
        
        // TODO: 필요하면 여기서 파싱
        // ObjectMapper mapper = new ObjectMapper();
        // List<Map<String,Object>> rows =
        //     mapper.readValue(jsonBody, new TypeReference<List<Map<String,Object>>>(){});

		List<Map<String, Object>> listBrand = CodeMgrService.getCodeList(map,"TEST","");

	    String gSonCode = JsonUtil.ListToString(listBrand);
//		return gSonCode;
		
		response.setContentType("application/json; charset=UTF-8");

	    PrintWriter out = response.getWriter();
	    out.print(gSonCode);
	    out.flush();
	    
//        return "{\"result\":\"OK\"}";
    }
    
    @RequestMapping(value = "/powerbuilder_test", method = RequestMethod.POST)
    @ResponseBody
    public String powerbuilder_test1(@RequestBody String jsonBody,
                                    HttpServletResponse response) throws IOException {
    	
	    logger.info("jsonBody: "+ jsonBody);

	    response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=UTF-8");

	    logger.info("jsonBody: "+ jsonBody);
        
        if (jsonBody == null || jsonBody.trim().isEmpty()) {
            return "{\"result\":\"FAIL\",\"message\":\"empty body\"}";
        }

        try {
            // 평면 객체 배열로 파싱 - 컬럼명은 PB DataWindow 컬럼명과 동일하게 넘어옵니다.
            List<Map<String, Object>> rows =
                objectMapper.readValue(jsonBody, new TypeReference<List<Map<String, Object>>>() {});

    	    logger.info("List.rows: "+ rows);

    	    // TODO: 여기서 rows 를 순회하며 DAO/Mapper 호출해 DB에 저장
             for (Map<String, Object> row : rows) {
            	 logger.info("List.row: "+ row);
//                 posSalesDao.insertSale(row);
             }

            return "{\"result\":\"OK\",\"count\":" + rows.size() + "}";

        } catch (Exception e) {
            // 파싱 실패 시 원본을 로그로 남겨서 PB 쪽 JSON 생성 문제를 바로 확인할 수 있게
            e.printStackTrace();
            return "{\"result\":\"FAIL\",\"message\":\"json parse error: "
                    + e.getMessage().replace("\"", "'") + "\"}";
        }
    }



	@RequestMapping(value = "/loginOk.do")
	public String loginOk(HttpServletRequest request,Model model, HttpSession session) throws Exception {

		String sId = request.getParameter("ID");
		String sPass = request.getParameter("PASS");
		
		System.out.println("loginOk.logIn: "+ sId );
		System.out.println("loginOk.sPass: "+ sPass );
		String sPage = "";
		if (sId==null || sPass==null){
			sPage = "login";
		}
		else {
			HashMap map = new HashMap();
			map.put("SID", sId);
			map.put("SPASS", sPass);
			List<Map<String, Object>> listLogin = Service.loginCheck(map);
 

//		    List<Map> temp = (List<Map>) list.get("data");
		    if (listLogin.size()==0){
			    logger.info("temp.size 0: " );
				sPage = "relogin";
				model.addAttribute("msg", "아이디 또는 암호가 틀립니다");
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
			    
/*
				model.addAttribute("USERID", sId);
				int iGgrade = Integer.parseInt(mapUser.get("GRADE").toString());
				loginVO login = new loginVO();
				
				login.setUSERID(sId);
				login.setGROUPCD(mapUser.get("GROUPCD").toString());
				login.setGROUPNAME(mapUser.get("GROUPNAME").toString());
				login.setCOMCD(mapUser.get("COMCD").toString());
				login.setCOMNAME(mapUser.get("COMNAME").toString());
				login.setUSERNAME(mapUser.get("USERNAME").toString());
				login.setGRADE(iGgrade);
				login.setGUBUN(mapUser.get("GUBUN").toString());

				session.setAttribute("SYS", login);
			    logger.info("login info: "+ login);
 */
				
/*
				session.setAttribute("SYS_USERID", sId);
				session.setAttribute("SYS_GROUPCD", mapUser.get("GROUPCD"));
				session.setAttribute("SYS_GROUPNAME", mapUser.get("GROUPNAME"));
				session.setAttribute("SYS_COMCD", mapUser.get("COMCD"));
				session.setAttribute("SYS_COMNAME", mapUser.get("COMNAME"));
				session.setAttribute("SYS_USERNAME", mapUser.get("USERNAME"));
				session.setAttribute("SYS_GRADE", mapUser.get("GRADE"));
				session.setAttribute("SYS_GUBUN", mapUser.get("GUBUN"));
*/				
				session.setAttribute("USER", mapUser);
				sPage = "main";
		    }
		    logger.info("sPage: "+ sPage);
		    
				
		}
	    return sPage;
	}

	@RequestMapping(value = "/s_main.do")
	public String s_main() {
	    return "s_main"; // ViewResolver가 /WEB-INF/(views 등 설정된 경로)/s_main.jsp 로 알아서 매핑
	}
	
	@RequestMapping(value = "/s_dashboard.do")
	public String s_dashboard() {
	    return "s_dashboard"; // ViewResolver가 /WEB-INF/(views 등 설정된 경로)/s_main.jsp 로 알아서 매핑
	}
	
//	@RequestMapping(value = "/s_boards.do", produces = "application/json;charset=UTF-8")
//	@ResponseBody
//	public String s_boards(HttpServletRequest request,Model model, HttpSession session) throws Exception {
//	public void s_boards(HttpServletRequest request,HttpServletResponse res) throws Exception {		
	@RequestMapping(value = "/s_boards.do")
	public void s_boards(HttpServletRequest request,HttpServletResponse res) throws Exception {		
		String sId = request.getParameter("ID");
		String sPass = request.getParameter("PASS");
		
		System.out.println("s_boards.logIn: "+ sId );
		System.out.println("s_boards.sPass: "+ sPass );

		HashMap map = new HashMap();
		map.put("SID", sId);
		map.put("SPASS", sPass);
		List<Map<String, Object>> newslist = Service.gridGroupNews(map);

	    ObjectMapper mapper = new ObjectMapper();
	    String gSon = mapper.writeValueAsString(newslist);

	    res.setContentType("application/json; charset=UTF-8");

	    PrintWriter out = res.getWriter();
	    out.print(gSon);
	    out.flush();
/*		
		String json = "["
                + "{"
                    + "\"id\":3,"
                    + "\"title\":\"사내 게시판 오픈했습니다\","
                    + "\"writer\":\"관리자\","
                    + "\"regDate\":\"2026-07-28\","
                    + "\"viewCount\":130"
                + "},"
                + "{"
                    + "\"id\":2,"
                    + "\"title\":\"2026년 하계 휴가 일정 공지\","
                    + "\"writer\":\"인사팀\","
                    + "\"regDate\":\"2026-08-05\","
                    + "\"viewCount\":87"
                + "},"
                + "{"
                    + "\"id\":1,"
                    + "\"title\":\"시스템 정기 점검 안내\","
                    + "\"writer\":\"관리자\","
                    + "\"regDate\":\"2026-08-10\","
                    + "\"viewCount\":42"
                + "}"
                + "]";

	    res.setContentType("text/html; charset=UTF-8");

		PrintWriter out = res.getWriter();  	

		out.print(json); 	
*/
	}
	
   @RequestMapping(value = "/s_me.do", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String me(HttpServletRequest request) throws Exception {
 
        HttpSession session = request.getSession(false);
        HashMap mapUser = (session != null) ? (HashMap) session.getAttribute("USER") : null;
 
        logger.info("s_me.do mapUser: " + mapUser);
 
        if (mapUser == null) {
            return "{\"success\":false,\"message\":\"로그인이 필요합니다.\"}";
        }
 
        // {"success":true,"data":{...}} 형태로 조립
        ObjectMapper mapper = new ObjectMapper();
        String userJson = mapper.writeValueAsString(mapUser);
 
        return "{\"success\":true,\"data\":" + userJson + "}";
    }
	   
	
	@RequestMapping(value = "/s_logout.do", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String s_logout(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
	    session.removeAttribute("USER");
	    // REMEMBER_ME 쿠키 삭제 + 저장된 토큰 폐기
	    Cookie[] cookies = request.getCookies();
	    if (cookies != null) {
	        for (Cookie c : cookies) {
	            if ("REMEMBER_ME".equals(c.getName())) {
	                rememberMeService.revoke(c.getValue());   // loginController가 이미 가진 @Autowired 필드

	                Cookie expired = new Cookie("REMEMBER_ME", "");
	                expired.setPath("/");
	                expired.setMaxAge(0);
	                response.addCookie(expired);
	                break;
	            }
	        }
	    }   
	    return "{\"success\":true,\"message\":\"로그아웃되었습니다.\"}";
	}

	@RequestMapping(value = "/s_loginOk.do")
	public String s_loginOk(HttpServletRequest request,HttpServletResponse response,Model model, HttpSession session) throws Exception {

		String sId = request.getParameter("ID");
		String sPass = request.getParameter("PASS");
		String rememberMe = request.getParameter("rememberMe"); // 체크됐으면 "on", 안 됐으면 null
		
		System.out.println("loginOk.logIn: "+ sId );
		System.out.println("loginOk.sPass: "+ sPass );
		String sPage = "";
		if (sId==null || sPass==null){
			sPage = "forward:/sens.jsp";
		}
		else {
			HashMap map = new HashMap();
			map.put("SID", sId);
			map.put("SPASS", sPass);
			List<Map<String, Object>> listLogin = Service.loginCheck(map);
 

//		    List<Map> temp = (List<Map>) list.get("data");
		    if (listLogin.size()==0){
			    logger.info("temp.size 0: " );
				sPage = "forward:/sens.jsp";
				model.addAttribute("msg", "아이디 또는 암호가 틀립니다");
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

				List<Map<String, Object>> newslist = Service.gridGroupNews(map);
				List<Map<String, Object>> daymclist = Service.gridComDay(map);

				Map<String, Object> menu = Service.menuAgent(map, "");
				List<Map<String, Object>> menuList = (List<Map<String, Object>>) menu.get("data");
				ObjectMapper mapper = new ObjectMapper();
				String menuJson = mapper.writeValueAsString(menuList);
				
				
			  	Map<String, Object>  resultMap  = new HashMap<String, Object>();
				resultMap.put("news"     , newslist);
				resultMap.put("day"     , daymclist);
				model.addAttribute("data", resultMap);

				
				
				if ("S".equals(mapUser.get("CHK"))) {
					sPage = "forward:/sens.jsp";
					model.addAttribute("msg", "사용중지된 아이디 입니다.");
				} else {
					session.setAttribute("USER", mapUser);
					session.setAttribute("menuJson", menuJson);
					if ("on".equals(rememberMe)) {
					    String token = rememberMeService.issueToken(mapUser);

					    Cookie cookie = new Cookie("REMEMBER_ME", token);
					    cookie.setPath("/");
					    cookie.setHttpOnly(true);                                  // JS에서 접근 못하게 (XSS 방지)
					    cookie.setMaxAge((int) rememberMeService.getExpireSeconds()); // 30일
					    // cookie.setSecure(true); // HTTPS 환경이라면 반드시 추가 권장

					    response.addCookie(cookie);
					}
					sPage = "s_dashboard";
				}
		    }
		    logger.info("sPage: "+ sPage);
		    
				
		}
	    return sPage;
	}

    @RequestMapping(value = "/s_dashboardStat.do", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String dashboard() throws Exception {

        List<Map<String, Object>> byDepartment = new ArrayList<>();
        byDepartment.add(dept("영업팀", 32));
        byDepartment.add(dept("개발팀", 45));
        byDepartment.add(dept("인사팀", 12));
        byDepartment.add(dept("총무팀", 15));
        byDepartment.add(dept("마케팅팀", 24));

        int total = 0;
        for (Map<String, Object> d : byDepartment) {
            total += (int) d.get("count");
        }

        Map<String, Object> data = new HashMap<>();
        data.put("totalEmployees", total);
        data.put("thisMonthCount", 6);
        data.put("noticeCount", 3);
        data.put("pendingCount", 1);
        data.put("byDepartment", byDepartment);

        return new ObjectMapper().writeValueAsString(data);
    }

    private Map<String, Object> dept(String name, int count) {
        Map<String, Object> map = new HashMap<>();
        map.put("dept", name);
        map.put("count", count);
        return map;
    }
    
	
	@RequestMapping(value = "/m_loginOk.do")
	public String m_loginOk(HttpServletRequest request,Model model, HttpSession session) throws Exception {

		String sId = request.getParameter("ID");
		String sPass = request.getParameter("PASS");
		
		System.out.println("loginOk.logIn: "+ sId );
		System.out.println("loginOk.sPass: "+ sPass );
		String sPage = "";
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
				model.addAttribute("msg", "아이디 또는 암호가 틀립니다");
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

				List<Map<String, Object>> newslist = Service.gridGroupNews(map);
				List<Map<String, Object>> daymclist = Service.gridComDay(map);
				
			  	Map<String, Object>  resultMap  = new HashMap<String, Object>();
				resultMap.put("news"     , newslist);
				resultMap.put("day"     , daymclist);
				model.addAttribute("data", resultMap);

				
				
				Map<String, Object> list = Service.menuGroup(map,"m_M");
				Map<String, Object> sublist = Service.menuGroup(map,"m_P");
				if ("S".equals(mapUser.get("CHK"))) {
					sPage = "m_relogin";
					model.addAttribute("msg", "사용중지된 아이디 입니다.");
				} else {
					session.setAttribute("USER", mapUser);
					session.setAttribute("list", list);
					session.setAttribute("sublist", sublist);
					sPage = "m_main";
				}
		    }
		    logger.info("sPage: "+ sPage);
		    
				
		}
	    return sPage;
	}

	@RequestMapping(
			  value="/m_loginConfirm.do",
			  method=RequestMethod.POST,
			  produces="application/json; charset=UTF-8"
			)
			@ResponseBody
			public String m_loginConfirm(HttpServletRequest request, Model model, HttpSession session) throws Exception {

		String sId = request.getParameter("ID");
		String sPass = request.getParameter("PASS");
		String sPosNo = request.getParameter("BUSEO");
	    Map<String, Object> res = new HashMap<>();
		
		System.out.println("m_loginConfirm.logIn: "+ sId );
		System.out.println("m_loginConfirm.sPass: "+ sPass );
		String sPage = "";
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
				} else {
//					session.setAttribute("USER", mapUser);
					String sBuseo = (String)mapUser.get("BUSEO");
				    logger.info("sBuseo: "+ sBuseo);
					
					map.put("BUSEO", sBuseo);
					List<Map<String, Object>> listPosInfo = Service.posInfo(map);
				    logger.info("listPosInfo: "+ listPosInfo);
					res.put("result", "OK");
					res.put("message", "SUCCESS");
					res.put("accessToken", "m_main");
					res.put("data", listLogin);
					res.put("posinfo", listPosInfo);
			        sPage = "m_main";
				}
		    }
		    logger.info("sPage: "+ sPage);
		    
				
		}
		return new ObjectMapper().writeValueAsString(res);
	}

    @RequestMapping(
            value = "/m_brandSearch.do",
            method = RequestMethod.POST,
            produces = "application/json; charset=UTF-8"
    )
    @ResponseBody
    public String m_productSearch(
            HttpServletRequest request,
            Model model,
            HttpSession session
    ) throws Exception {

        // (선택) 세션 체크: 로그인 필요 시
        // Object loginId = session.getAttribute("LOGIN_ID");
        // if (loginId == null) return ResponseEntity.status(401).body(new ProductSearchRes("FAIL","UNAUTHORIZED", List.of()));

		String keyword = request.getParameter("keyword");
		String category = request.getParameter("category");
		String barcode = request.getParameter("barcode");
	    Map<String, Object> res = new HashMap<>();

        // TODO: 여기서 DB 조회/서비스 호출로 교체
		HashMap map = new HashMap();
		map = JsonUtil.getJsonToMap(barcode);

		List<Map<String, Object>> listBrand = CodeMgrService.getCodeList(map,"JEPMBRANDLIST","");
		model.addAttribute("comboBRAND", listBrand);

	    String gSonCode = JsonUtil.ListToString(listBrand);

		res.put("result", gSonCode);
		return new ObjectMapper().writeValueAsString(res);
    }

	
	@RequestMapping(value = "/main.do")
	public String mainScr(HttpServletRequest request,Model model, HttpSession session) throws Exception {

		HashMap MapUser = (HashMap)request.getSession().getAttribute("USER");
		
		System.out.println("loginOk.MapUser: "+ MapUser );
		String sPage = "main";
		if (MapUser==null){
			sPage = "relogin";
		}
	    logger.info("sPage: "+ sPage);
				
	    return sPage;
	}
				
	@RequestMapping(value = "/m_main.do")
	public String m_mainScr(HttpServletRequest request,Model model, HttpSession session) throws Exception {

		String sId = request.getParameter("ID");
		String sPass = request.getParameter("PASS");
	    logger.info("sId: "+ sId);
	    logger.info("sPass: "+ sPass);
		
		HashMap MapUser = (HashMap)request.getSession().getAttribute("USER");
		
		System.out.println("m_loginOk.MapUser: "+ MapUser );
		String sPage = "m_main";
		
		HashMap map = new HashMap();
		map.put("SID", sId);
		map.put("SPASS", sPass);
		
		if (MapUser==null){
			sPage = "m_relogin";
		}
		else{
			List<Map<String, Object>> newslist = Service.gridGroupNews(map);
			List<Map<String, Object>> daymclist = Service.gridComDay(map);
			
		  	Map<String, Object>  resultMap  = new HashMap<String, Object>();
			resultMap.put("news"     , newslist);
			resultMap.put("day"     , daymclist);
			model.addAttribute("data", resultMap);			
		}
	    logger.info("sPage: "+ sPage);
				
	    return sPage;
	}
				

	@RequestMapping(value = "/gridGroupNews.do")
	public void gridGroupNews(HttpServletRequest request,HttpServletResponse res) throws Exception {

		String sId = request.getParameter("ID");
		String sPass = request.getParameter("PASS");
	    logger.info("grid check: "+ request.getParameter("check"));
	    logger.info("grid custname: "+ request.getParameter("custname"));
		
		HashMap map = new HashMap();
		map.put("SID", sId);
		map.put("SPASS", sPass);

		List<Map<String, Object>> list = Service.gridGroupNews(map);
	    String gSon = JsonUtil.ListToStringData(list);

	    res.setContentType("text/html; charset=UTF-8");

		PrintWriter out = res.getWriter();  	

		out.print(gSon); 		

	}
	
	@RequestMapping(value = "/gridComDay.do")
	public void gridComDay(HttpServletRequest request,HttpServletResponse res) throws Exception {

		String sId = request.getParameter("ID");
		String sPass = request.getParameter("PASS");
	    logger.info("grid check: "+ request.getParameter("check"));
	    logger.info("grid custname: "+ request.getParameter("custname"));
		
		HashMap map = new HashMap();
		map.put("SID", sId);
		map.put("SPASS", sPass);

		List<Map<String, Object>> list = Service.gridComDay(map);
	    String gSon = JsonUtil.ListToStringData(list);

	    res.setContentType("text/html; charset=UTF-8");

		PrintWriter out = res.getWriter();  	

		out.print(gSon); 		

	}

	@RequestMapping(value = "/logout.do")
	public String logOut(HttpServletRequest request,Model model, HttpSession session) throws Exception {

		String sId = request.getParameter("ID");
		String sPass = request.getParameter("PASS");
	    logger.info("logOut: "+ request.getParameter("check"));
	    
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		Date today = new Date();
		String strDate = format.format(today);

		session.removeAttribute("USER");
		
		String sPage = "relogin";
	    return sPage;
	}
				
	@RequestMapping(value = "/m_logout.do")
	public String m_logOut(HttpServletRequest request,Model model, HttpSession session) throws Exception {

		String sId = request.getParameter("ID");
		String sPass = request.getParameter("PASS");
	    logger.info("logOut: "+ request.getParameter("check"));
	    
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		Date today = new Date();
		String strDate = format.format(today);

		session.removeAttribute("USER");
		
		String sPage = "m_relogin";
	    return sPage;
	}
				
	
}
