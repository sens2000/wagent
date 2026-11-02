package com.sens.lush;
import com.sens.lush.util.*;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sens.lush.service.pos01_Service;
import com.sens.lush.service.codeMgr_Service;

/**
 * Handles requests for the application home page.
 */
@Controller
//@RestController
public class pos01_Controller {
	
//	private static final Logger logger = LoggerFactory.getLogger(this.class);
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@Resource(name = "pos01_Service")
	private pos01_Service Service;
	@Autowired
	private codeMgr_Service CodeMgrService;

	
	@RequestMapping(value = "/pos01.do")
//	public String pos01(@RequestParam Map map, Model model) {  //From을 MAP 으로 받기
	public String pos01(HttpServletRequest request,Model model ) throws Exception {

		String sPage = "pos01";
	    logger.info("Start Page: "+ sPage);
		
//		model.addAttribute("CODE", sId);

	    return sPage;
	}
 
//  @RequestMapping(value = "pos/get_posno", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
  @RequestMapping(value = "pos/get_posno", method = RequestMethod.POST)
  
  @ResponseBody
//	public String get_posno(@RequestBody String jsonBody,HttpServletResponse response) throws Exception {    	
 	public void get_posno(@RequestBody String jsonBody,HttpServletResponse response) throws Exception {    	
      response.setCharacterEncoding("UTF-8");
      response.setContentType("application/json; charset=UTF-8");

      logger.info("get_posno" + ".control" + ".jsonBody: "+ jsonBody);
      
      ObjectMapper mapper = new ObjectMapper();
      HashMap<String, Object> map = mapper.readValue(jsonBody, new TypeReference<HashMap<String, Object>>() {});
      String posno   = (String) map.get("posno");
      logger.info("get_posno" + ".control" + ".posno: "+ posno);
      
	  List<Map<String, Object>> list = Service.getPosno(map);
	    if (list == null) {
	        list = new ArrayList<>();
	    }
	    
      // TODO: 필요하면 여기서 파싱
      // ObjectMapper mapper = new ObjectMapper();
      // List<Map<String,Object>> rows =
      //     mapper.readValue(jsonBody, new TypeReference<List<Map<String,Object>>>(){});
	    String code="0";
	    String msg="OK";
	    int count = list.size();
	    
	    Map<String, Object> result = new LinkedHashMap<>();
	    result.put("code", code);
	    result.put("msg", msg);
	    result.put("count", count);
	    result.put("data", list);
	    
//	    String gSonCode = JsonUtil.ListToString(list);
//		return gSonCode;
	    
	    String gSonCode = mapper.writeValueAsString(result);
		response.setContentType("application/json; charset=UTF-8");

	    PrintWriter out = response.getWriter();
	    logger.info("get_posno" + ".control" + ".gSonCode: "+ gSonCode);
	    
	    out.print(gSonCode);
	    out.flush();
	    
//      return "{\"result\":\"OK\"}";
  }

  
@RequestMapping(value = "pos/get_user_info", method = RequestMethod.POST)
@ResponseBody
	public void get_user_info(@RequestBody String jsonBody,HttpServletResponse response) throws Exception {    	
    response.setCharacterEncoding("UTF-8");
    response.setContentType("application/json; charset=UTF-8");

    logger.info("get_user_info" + ".control" + ".jsonBody: "+ jsonBody);
    
    ObjectMapper mapper = new ObjectMapper();
    HashMap<String, Object> map = mapper.readValue(jsonBody, new TypeReference<HashMap<String, Object>>() {});
    String posno   = (String) map.get("posno");
    logger.info("get_user_info" + ".control" + ".posno: "+ posno);
    
    List<Map<String, Object>> list = Service.getUserinfo(map);
    if (list == null) {
        list = new ArrayList<>();
    }
	    
	    String code="0";
	    String msg="OK";
	    int count = list.size();
	    
	    Map<String, Object> result = new LinkedHashMap<>();
	    result.put("code", code);
	    result.put("msg", msg);
	    result.put("count", count);
	    result.put("data", list);
	    
	    String gSonCode = mapper.writeValueAsString(result);
		response.setContentType("application/json; charset=UTF-8");

	    PrintWriter out = response.getWriter();
	    logger.info("get_user_info" + ".control" + ".gSonCode: "+ gSonCode);
	    
	    out.print(gSonCode);
	    out.flush();
	    
}
	
@RequestMapping(value = "pos/get_user_mst", method = RequestMethod.POST)
@ResponseBody
	public void get_user_mst(@RequestBody String jsonBody,HttpServletResponse response) throws Exception {    	
    response.setCharacterEncoding("UTF-8");
    response.setContentType("application/json; charset=UTF-8");

    logger.info("get_user_mst" + ".control" + ".jsonBody: "+ jsonBody);
    
    ObjectMapper mapper = new ObjectMapper();
    HashMap<String, Object> map = mapper.readValue(jsonBody, new TypeReference<HashMap<String, Object>>() {});
    String posno   = (String) map.get("posno");
    logger.info("get_user_mst" + ".control" + ".posno: "+ posno);
    
    List<Map<String, Object>> list = Service.getUsermst(map);
    if (list == null) {
        list = new ArrayList<>();
    }
	    
	    String code="0";
	    String msg="OK";
	    int count = list.size();
	    
	    Map<String, Object> result = new LinkedHashMap<>();
	    result.put("code", code);
	    result.put("msg", msg);
	    result.put("count", count);
	    result.put("data", list);
	    
	    String gSonCode = mapper.writeValueAsString(result);
		response.setContentType("application/json; charset=UTF-8");

	    PrintWriter out = response.getWriter();
	    logger.info("get_user_mst" + ".control" + ".gSonCode: "+ gSonCode);
	    
	    out.print(gSonCode);
	    out.flush();
	    
}
	
@RequestMapping(value = "pos/get_jepm_mst", method = RequestMethod.POST)
@ResponseBody
	public void get_jepm_mst(@RequestBody String jsonBody,HttpServletResponse response) throws Exception {    	
    response.setCharacterEncoding("UTF-8");
    response.setContentType("application/json; charset=UTF-8");

    logger.info("get_jepm_mst" + ".control" + ".jsonBody: "+ jsonBody);
    
    ObjectMapper mapper = new ObjectMapper();
    HashMap<String, Object> map = mapper.readValue(jsonBody, new TypeReference<HashMap<String, Object>>() {});
    String posno   = (String) map.get("posno");
    logger.info("get_jepm_mst" + ".control" + ".posno: "+ posno);
    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
    String sDatetime = sdf.format(new Date());
    map.put("downkey", sDatetime);
    
    Service.setJepmdownkey(map);
    
    List<Map<String, Object>> list = Service.getJepmmst(map);
    if (list == null) {
        list = new ArrayList<>();
    }
	    
    String code="0";
    String msg="OK";
    int count = list.size();
    
    Map<String, Object> result = new LinkedHashMap<>();
    result.put("code", code);
    result.put("msg", sDatetime);
    result.put("count", count);
    result.put("data", list);
    
    String gSonCode = mapper.writeValueAsString(result);
	response.setContentType("application/json; charset=UTF-8");

    PrintWriter out = response.getWriter();
    logger.info("get_jepm_mst" + ".control" + ".gSonCode: "+ gSonCode);
    
    out.print(gSonCode);
    out.flush();
	    
}

@RequestMapping(value = "pos/get_jepm_jepmcd", method = RequestMethod.POST)
@ResponseBody
	public void get_jepm_jepmcd(@RequestBody String jsonBody,HttpServletResponse response) throws Exception {    	
    response.setCharacterEncoding("UTF-8");
    response.setContentType("application/json; charset=UTF-8");

    logger.info("get_jepm_jepmcd" + ".control" + ".jsonBody: "+ jsonBody);
    
    ObjectMapper mapper = new ObjectMapper();
    HashMap<String, Object> map = mapper.readValue(jsonBody, new TypeReference<HashMap<String, Object>>() {});
    String posno   = (String) map.get("posno");
    logger.info("get_jepm_jepmcd" + ".control" + ".posno: "+ posno);
    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
    String sDatetime = sdf.format(new Date());
    map.put("downkey", sDatetime);
    
    Service.setJepmdownkey(map);
    
    List<Map<String, Object>> list = Service.getJepmjepmcd(map);
    if (list == null) {
        list = new ArrayList<>();
    }
	    
    String code="0";
    String msg="OK";
    int count = list.size();
    
    Map<String, Object> result = new LinkedHashMap<>();
    result.put("code", code);
    result.put("msg", sDatetime);
    result.put("count", count);
    result.put("data", list);
    
    String gSonCode = mapper.writeValueAsString(result);
	response.setContentType("application/json; charset=UTF-8");

    PrintWriter out = response.getWriter();
    logger.info("get_jepm_jepmcd" + ".control" + ".gSonCode: "+ gSonCode);
    
    out.print(gSonCode);
    out.flush();
	    
}
	
@RequestMapping(value = "pos/get_jepm_find", method = RequestMethod.POST)
@ResponseBody
	public void get_jepm_find(@RequestBody String jsonBody,HttpServletResponse response) throws Exception {    	
    response.setCharacterEncoding("UTF-8");
    response.setContentType("application/json; charset=UTF-8");

    logger.info("get_jepm_find" + ".control" + ".jsonBody: "+ jsonBody);
    
    ObjectMapper mapper = new ObjectMapper();
    HashMap<String, Object> map = mapper.readValue(jsonBody, new TypeReference<HashMap<String, Object>>() {});
    String posno   = (String) map.get("posno");
    logger.info("get_jepm_find" + ".control" + ".posno: "+ posno);
    
    List<Map<String, Object>> list = Service.getJepmfind(map);
    if (list == null) {
        list = new ArrayList<>();
    }
	    
    String code="0";
    String msg="OK";
    int count = list.size();
    
    Map<String, Object> result = new LinkedHashMap<>();
    result.put("code", code);
    result.put("count", count);
    result.put("data", list);
    
    String gSonCode = mapper.writeValueAsString(result);
	response.setContentType("application/json; charset=UTF-8");

    PrintWriter out = response.getWriter();
    logger.info("get_jepm_find" + ".control" + ".gSonCode: "+ gSonCode);
    
    out.print(gSonCode);
    out.flush();
	    
}
	
@RequestMapping(value = "pos/get_member_find", method = RequestMethod.POST)
@ResponseBody
	public void get_member_find(@RequestBody String jsonBody,HttpServletResponse response) throws Exception {    	
    response.setCharacterEncoding("UTF-8");
    response.setContentType("application/json; charset=UTF-8");

    logger.info("get_member_find" + ".control" + ".jsonBody: "+ jsonBody);
    
    ObjectMapper mapper = new ObjectMapper();
    HashMap<String, Object> map = mapper.readValue(jsonBody, new TypeReference<HashMap<String, Object>>() {});
    String posno   = (String) map.get("posno");
    logger.info("get_member_find" + ".control" + ".posno: "+ posno);
    
    List<Map<String, Object>> list = Service.getMemberfind(map);
    if (list == null) {
        list = new ArrayList<>();
    }
	    
    String code="0";
    String msg="OK";
    int count = list.size();
    
    Map<String, Object> result = new LinkedHashMap<>();
    result.put("code", code);
    result.put("count", count);
    result.put("data", list);
    
    String gSonCode = mapper.writeValueAsString(result);
	response.setContentType("application/json; charset=UTF-8");

    PrintWriter out = response.getWriter();
    logger.info("get_member_find" + ".control" + ".gSonCode: "+ gSonCode);
    
    out.print(gSonCode);
    out.flush();
	    
}
	

@RequestMapping(value = "pos/send_mc20_mst", method = RequestMethod.POST)
@ResponseBody
	public void send_mc20_mst(@RequestBody String jsonBody,HttpServletResponse response) throws Exception {    	
    response.setCharacterEncoding("UTF-8");
    response.setContentType("application/json; charset=UTF-8");

    logger.info("send_mc20_mst" + ".control" + ".jsonBody: "+ jsonBody);
    
    ObjectMapper mapper = new ObjectMapper();
    HashMap<String, Object> map = mapper.readValue(jsonBody, new TypeReference<HashMap<String, Object>>() {});
    String shopno   = (String) map.get("shopno");
    String posno   = (String) map.get("posno");
    String proc   = (String) map.get("proc");
    logger.info("send_mc20_mst" + ".control" + ".posno: "+ posno);
    logger.info("send_mc20_mst" + ".control" + ".shopno: "+ shopno);
    logger.info("send_mc20_mst" + ".control" + ".proc: "+ proc);
    logger.info("send_mc20_mst" + ".control" + ".map: "+ map);

    List<Map<String, Object>> dataList = (List<Map<String, Object>>) map.get("data");

    for (Map<String, Object> item : dataList) {
    	item.put("shopno", shopno);
    	item.put("posno", posno);
    	Service.insertMc20mst(item);
//        logger.info("send_mc20_mst" + ".control" + ".item: "+ item);
    }
    
    String code="0";
    String msg="OK";
    int count = 0;
    
    Map<String, Object> result = new LinkedHashMap<>();
    result.put("code", code);
    result.put("msg", "ok");
    result.put("count", count);
    
    String gSonCode = mapper.writeValueAsString(result);
	response.setContentType("application/json; charset=UTF-8");

    PrintWriter out = response.getWriter();
    logger.info("send_mc20_mst" + ".control" + ".gSonCode: "+ gSonCode);
    
    out.print(gSonCode);
    out.flush();
	    
}
	
@RequestMapping(value = "pos/send_mc20_dtl", method = RequestMethod.POST)
@ResponseBody
	public void send_mc20_dtl(@RequestBody String jsonBody,HttpServletResponse response) throws Exception {    	
    response.setCharacterEncoding("UTF-8");
    response.setContentType("application/json; charset=UTF-8");

    logger.info("send_mc20_dtl" + ".control" + ".jsonBody: "+ jsonBody);
    
    ObjectMapper mapper = new ObjectMapper();
    HashMap<String, Object> map = mapper.readValue(jsonBody, new TypeReference<HashMap<String, Object>>() {});
    String shopno   = (String) map.get("shopno");
    String posno   = (String) map.get("posno");
    String proc   = (String) map.get("proc");
    logger.info("send_mc20_dtl" + ".control" + ".posno: "+ posno);
    logger.info("send_mc20_dtl" + ".control" + ".shopno: "+ shopno);
    logger.info("send_mc20_dtl" + ".control" + ".proc: "+ proc);
    logger.info("send_mc20_dtl" + ".control" + ".map: "+ map);

    List<Map<String, Object>> dataList = (List<Map<String, Object>>) map.get("data");

    for (Map<String, Object> item : dataList) {
    	item.put("shopno", shopno);
    	item.put("posno", posno);
    	Service.insertMc20dtl(item);
//        logger.info("send_mc20_dtl" + ".control" + ".item: "+ item);
    }
    
    String code="0";
    String msg="OK";
    int count = 0;
    
    Map<String, Object> result = new LinkedHashMap<>();
    result.put("code", code);
    result.put("msg", "ok");
    result.put("count", count);
    
    String gSonCode = mapper.writeValueAsString(result);
	response.setContentType("application/json; charset=UTF-8");

    PrintWriter out = response.getWriter();
    logger.info("send_mc20_dtl" + ".control" + ".gSonCode: "+ gSonCode);
    
    out.print(gSonCode);
    out.flush();
	    
}
	
@RequestMapping(value = "pos/send_jepm_mst", method = RequestMethod.POST)
@ResponseBody
	public void send_jepm_mst(@RequestBody String jsonBody,HttpServletResponse response) throws Exception {    	
    response.setCharacterEncoding("UTF-8");
    response.setContentType("application/json; charset=UTF-8");

    logger.info("send_jepm_mst" + ".control" + ".jsonBody: "+ jsonBody);
    
    ObjectMapper mapper = new ObjectMapper();
    HashMap<String, Object> map = mapper.readValue(jsonBody, new TypeReference<HashMap<String, Object>>() {});
    String shopno   = (String) map.get("shopno");
    String posno   = (String) map.get("posno");
    String proc   = (String) map.get("proc");
    logger.info("send_jepm_mst" + ".control" + ".posno: "+ posno);
    logger.info("send_jepm_mst" + ".control" + ".shopno: "+ shopno);
    logger.info("send_jepm_mst" + ".control" + ".proc: "+ proc);
    logger.info("send_jepm_mst" + ".control" + ".map: "+ map);

    List<Map<String, Object>> dataList = (List<Map<String, Object>>) map.get("data");

    for (Map<String, Object> item : dataList) {
    	item.put("shopno", shopno);
//    	item.put("posno", posno);
    	Service.insertJepmmst(item);
//        logger.info("send_jepm_mst" + ".control" + ".item: "+ item);
    }
    
    String code="0";
    String msg="OK";
    int count = 0;
    
    Map<String, Object> result = new LinkedHashMap<>();
    result.put("code", code);
    result.put("msg", "ok");
    result.put("count", count);
    
    String gSonCode = mapper.writeValueAsString(result);
	response.setContentType("application/json; charset=UTF-8");

    PrintWriter out = response.getWriter();
    logger.info("send_jepm_mst" + ".control" + ".gSonCode: "+ gSonCode);
    
    out.print(gSonCode);
    out.flush();
	    
}
	
@RequestMapping(value = "pos/send_member", method = RequestMethod.POST)
@ResponseBody
	public void send_member(@RequestBody String jsonBody,HttpServletResponse response) throws Exception {    	
    response.setCharacterEncoding("UTF-8");
    response.setContentType("application/json; charset=UTF-8");

    logger.info("send_member" + ".control" + ".jsonBody: "+ jsonBody);
    
    ObjectMapper mapper = new ObjectMapper();
    HashMap<String, Object> map = mapper.readValue(jsonBody, new TypeReference<HashMap<String, Object>>() {});
    String shopno   = (String) map.get("shopno");
    String posno   = (String) map.get("posno");
    String proc   = (String) map.get("proc");
    logger.info("send_member" + ".control" + ".posno: "+ posno);
    logger.info("send_member" + ".control" + ".shopno: "+ shopno);
    logger.info("send_member" + ".control" + ".proc: "+ proc);
    logger.info("send_member" + ".control" + ".map: "+ map);

    List<Map<String, Object>> dataList = (List<Map<String, Object>>) map.get("data");

    for (Map<String, Object> item : dataList) {
    	item.put("shopno", shopno);
//    	item.put("posno", posno);
    	Service.insertMember(item);
//        logger.info("send_jepm_mst" + ".control" + ".item: "+ item);
    }
    
    String code="0";
    String msg="OK";
    int count = 0;
    
    Map<String, Object> result = new LinkedHashMap<>();
    result.put("code", code);
    result.put("msg", "ok");
    result.put("count", count);
    
    String gSonCode = mapper.writeValueAsString(result);
	response.setContentType("application/json; charset=UTF-8");

    PrintWriter out = response.getWriter();
    logger.info("send_member" + ".control" + ".gSonCode: "+ gSonCode);
    
    out.print(gSonCode);
    out.flush();
	    
}
		
	
	
	@ResponseBody
    @GetMapping("/pos01grid.do")
    public List<Map<String, Object>> list(@RequestParam(value = "name", required = false) String name) {
        return Service.getList(name);
    }

	@ResponseBody
    @PostMapping("/pos01grid.do")
    public Map<String, Object> save(HttpServletRequest request) throws Exception {
		Map<String, Object> paramMap = getParameterMap(request);
		
//		String mode = request.getParameter("mode");
//        String body = readRequestBody(request);
//	    logger.info("pos01grid" + ".save" +  ".request:" + request);
	    logger.info("pos01grid" + ".save" +  ".paramMap:" + paramMap);
	    String mode = (String) paramMap.get("mode");

	    if ("new".equals(mode)) {
	    	Service.Insert(paramMap);
	    } else if ("edit".equals(mode)) {
	    	Service.Update(paramMap);
	    } else if ("delete".equals(mode)) {
	    	Service.Delete(paramMap);
	    } else {
	        throw new IllegalArgumentException("알 수 없는 mode 입니다: " + mode);
	    }

/*		
        String body = readRequestBody(request);

        ObjectMapper mapper = new ObjectMapper();
        pos01VO vo = mapper.readValue(body, pos01VO.class);

        String mode = vo.getMode();

        if ("new".equals(mode)) {
            pos01Service.insertEmployee(vo);
        } else if ("edit".equals(mode)) {
            pos01Service.updateEmployee(vo);
        } else if ("delete".equals(mode)) {
            pos01Service.deleteEmployee(vo.getCodeList());
        } else {
            throw new IllegalArgumentException("알 수 없는 mode 입니다: " + mode);
        }
*/
        return Collections.singletonMap("result", "success");
    }

	private Map<String, Object> getParameterMap(HttpServletRequest request) throws Exception {
	    ByteArrayOutputStream baos = new ByteArrayOutputStream();
	    try (InputStream is = request.getInputStream()) {
	        byte[] buf = new byte[1024];
	        int len;
	        while ((len = is.read(buf)) != -1) {
	            baos.write(buf, 0, len);
	        }
	    }

	    String body = new String(baos.toByteArray(), "UTF-8");

	    ObjectMapper mapper = new ObjectMapper();
	    return mapper.readValue(body, new TypeReference<Map<String, Object>>() {});
	}
	
    private String readRequestBody(HttpServletRequest request) throws Exception {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = request.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        }
        return sb.toString();
    }
    
	@RequestMapping(value = "/pos01grid.do")
	public void gridList(HttpServletRequest request,HttpServletResponse res) throws Exception {

	    logger.info("gridCustList Start:" );
//	    String param = new String(request.getParameter("param").getBytes("8859_1"), "EUC-KR");
//	    logger.info("param: "+ param);
	    
		String sParam = request.getParameter("param");
	    logger.info("param: "+ sParam);
	    if (sParam != null){		
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
	
			out.print(gSon); 		
	    }
	}

	
	@RequestMapping(value = "/pos01save.do")
	public @ResponseBody String dataSave(HttpServletRequest request,HttpServletResponse res) throws Exception {
        
		String sList = request.getParameter("list");
		logger.info("pos01save.list: "+ sList );

		Map<String, Object> mapSos = new ObjectMapper().readValue(sList, HashMap.class);
		HashMap MapUser = (HashMap)request.getSession().getAttribute("USER");
		List<Map> addList = (List<Map>) mapSos.get("addList");
		List<Map> paramList = (List<Map>) mapSos.get("updateList");
		logger.info("pos01save.updateList: "+ paramList );
/*		
		Iterator it;
		HashMap map = new HashMap();
		it = addList.iterator();
		while (it.hasNext())
		{
			map = (HashMap)it.next();
			logger.info("pos01save.iterator: "+ map );
			paramList.add(map);
		}
*/
	    logger.info("pos01save.MapUser: "+ MapUser);
		logger.info("pos01save.addList: "+ addList );
		logger.info("pos01save.update+Add: "+ paramList );
	    

		
		String sRet = Service.dataSave(paramList,MapUser);
		

		
//		Iterator it;
//		HashMap map = new HashMap();
		
//		String sAdd = mapSos.get("addList").toString();
//		String sUpdate = mapSos.get("updateList").toString();
//		String sDelete = mapSos.get("deleteList").toString();

//		logger.info("pos01save.mapSos: "+ mapSos );
//		logger.info("pos01save.sAdd: "+ sAdd );

/*		
		List<Map> addList = (List<Map>) mapSos.get("addList");
		logger.info("pos01save.addList: "+ addList );

		it = addList.iterator();
		
		while (it.hasNext())
		{
			map = (HashMap)it.next();
			logger.info("pos01save.map: "+ map );
			logger.info("pos01save.CUSTCD: "+ map.get("CUSTCD") );
			logger.info("pos01save.MEMO: "+ map.get("MEMO") );
			logger.info("pos01save.PRDATE: "+ map.get("PRDATE") );
//			String sRet = Service.pos01(map);
		}		
*/		
//		List<Map> updateList = (List<Map>) mapSos.get("updateList");
//		logger.info("pos01save.updateList: "+ updateList );
//		String sRet = Service.dataSave(updateList,MapUser);

//		it = updateList.iterator();
		
//		while (it.hasNext())
//		{
//			map = (HashMap)it.next();
//			logger.info("pos01save.map: "+ map );
//			logger.info("pos01save.CUSTCD: "+ map.get("CUSTCD") );
//			logger.info("pos01save.MEMO: "+ map.get("MEMO") );
//			logger.info("pos01save.PRDATE: "+ map.get("PRDATE") );
//			logger.info("pos01save.chk: "+ map.get("chk") );
//			String sRet = Service.pos01Save(map);
//			if (map.get("chk")==null){
//				logger.info("pos01save.update: "+ map.get("chk") );
//			}
//			else{
//				logger.info("pos01save.delete: "+ map.get("chk") );
//				String sRet = Service.custMcMemoDelete(map);
//			}
//		}		
		return "{\"result\": \"success\"}";
	}
	
}
