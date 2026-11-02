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
import java.util.Locale;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.sens.lush.service.ai015_Service;

/**
 * Handles requests for the application home page.
 */
@Controller
//@RestController
public class ai015_Controller {
	
//	private static final Logger logger = LoggerFactory.getLogger(this.class);
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@Resource(name = "ai015_Service")
	private ai015_Service Service;

	
	@RequestMapping(value = "/ai015.do")
//	public String ai015(@RequestParam Map map, Model model) {  //From을 MAP 으로 받기
	public String ai015(HttpServletRequest request,Model model ) throws Exception {

		String sPage = "ai015";
	    logger.info("Start Page: "+ sPage);
		
//		model.addAttribute("CODE", sId);

	    return sPage;
	}
 
	@ResponseBody
    @GetMapping("/ai015grid.do")
    public List<Map<String, Object>> list(@RequestParam(value = "name", required = false) String name) {
        return Service.getList(name);
    }

	@ResponseBody
    @PostMapping("/ai015grid.do")
    public Map<String, Object> save(HttpServletRequest request) throws Exception {
		Map<String, Object> paramMap = getParameterMap(request);
		
//		String mode = request.getParameter("mode");
//        String body = readRequestBody(request);
//	    logger.info("ai015grid" + ".save" +  ".request:" + request);
	    logger.info("ai015grid" + ".save" +  ".paramMap:" + paramMap);
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
        Ai015VO vo = mapper.readValue(body, Ai015VO.class);

        String mode = vo.getMode();

        if ("new".equals(mode)) {
            ai015Service.insertEmployee(vo);
        } else if ("edit".equals(mode)) {
            ai015Service.updateEmployee(vo);
        } else if ("delete".equals(mode)) {
            ai015Service.deleteEmployee(vo.getCodeList());
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
    
	@RequestMapping(value = "/ai015grid.do")
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

	
	@RequestMapping(value = "/ai015save.do")
	public @ResponseBody String dataSave(HttpServletRequest request,HttpServletResponse res) throws Exception {
        
		String sList = request.getParameter("list");
		logger.info("ai015save.list: "+ sList );

		Map<String, Object> mapSos = new ObjectMapper().readValue(sList, HashMap.class);
		HashMap MapUser = (HashMap)request.getSession().getAttribute("USER");
		List<Map> addList = (List<Map>) mapSos.get("addList");
		List<Map> paramList = (List<Map>) mapSos.get("updateList");
		logger.info("ai015save.updateList: "+ paramList );
/*		
		Iterator it;
		HashMap map = new HashMap();
		it = addList.iterator();
		while (it.hasNext())
		{
			map = (HashMap)it.next();
			logger.info("ai015save.iterator: "+ map );
			paramList.add(map);
		}
*/
	    logger.info("ai015save.MapUser: "+ MapUser);
		logger.info("ai015save.addList: "+ addList );
		logger.info("ai015save.update+Add: "+ paramList );
	    

		
		String sRet = Service.dataSave(paramList,MapUser);
		

		
//		Iterator it;
//		HashMap map = new HashMap();
		
//		String sAdd = mapSos.get("addList").toString();
//		String sUpdate = mapSos.get("updateList").toString();
//		String sDelete = mapSos.get("deleteList").toString();

//		logger.info("ai015save.mapSos: "+ mapSos );
//		logger.info("ai015save.sAdd: "+ sAdd );

/*		
		List<Map> addList = (List<Map>) mapSos.get("addList");
		logger.info("ai015save.addList: "+ addList );

		it = addList.iterator();
		
		while (it.hasNext())
		{
			map = (HashMap)it.next();
			logger.info("ai015save.map: "+ map );
			logger.info("ai015save.CUSTCD: "+ map.get("CUSTCD") );
			logger.info("ai015save.MEMO: "+ map.get("MEMO") );
			logger.info("ai015save.PRDATE: "+ map.get("PRDATE") );
//			String sRet = Service.ai015(map);
		}		
*/		
//		List<Map> updateList = (List<Map>) mapSos.get("updateList");
//		logger.info("ai015save.updateList: "+ updateList );
//		String sRet = Service.dataSave(updateList,MapUser);

//		it = updateList.iterator();
		
//		while (it.hasNext())
//		{
//			map = (HashMap)it.next();
//			logger.info("ai015save.map: "+ map );
//			logger.info("ai015save.CUSTCD: "+ map.get("CUSTCD") );
//			logger.info("ai015save.MEMO: "+ map.get("MEMO") );
//			logger.info("ai015save.PRDATE: "+ map.get("PRDATE") );
//			logger.info("ai015save.chk: "+ map.get("chk") );
//			String sRet = Service.ai015Save(map);
//			if (map.get("chk")==null){
//				logger.info("ai015save.update: "+ map.get("chk") );
//			}
//			else{
//				logger.info("ai015save.delete: "+ map.get("chk") );
//				String sRet = Service.custMcMemoDelete(map);
//			}
//		}		
		return "{\"result\": \"success\"}";
	}
	
}
