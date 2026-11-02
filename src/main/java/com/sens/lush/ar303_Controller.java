package com.sens.lush;
import com.sens.lush.util.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sens.lush.service.ar303_Service;

/**
 * Handles requests for the application home page.
 */
@Controller
public class ar303_Controller {
	
//	private static final Logger logger = LoggerFactory.getLogger(this.class);
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@Resource(name = "ar303_Service")
	private ar303_Service Service;

	
	@RequestMapping(value = "/ar303.do")
//	public String ar303(@RequestParam Map map, Model model) {  //From을 MAP 으로 받기
	public String ar303(HttpServletRequest request,Model model ) throws Exception {

		String sPage = "ar303";
	    logger.info("Start Page: "+ sPage);
		
//		model.addAttribute("CODE", sId);

	    return sPage;
	}

    @RequestMapping(value = "/ar303_data.do", method = RequestMethod.GET)
    @ResponseBody
    public List<Map<String, Object>> data(
            @RequestParam(value = "type", defaultValue = "all") String type,
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "from", required = false) String from,
            @RequestParam(value = "to", required = false) String to) throws Exception {

        String fromDate = (from != null) ? from.replace("-", "") : "";
        String toDate = (to != null) ? to.replace("-", "") : "";

        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("type", type);
        paramMap.put("keyword", keyword);
        paramMap.put("fromDate", fromDate);
        paramMap.put("toDate", toDate);

        return Service.getSalesReport(paramMap);
    }
    
	@RequestMapping(value = "/ar303grid.do")
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

	
	@RequestMapping(value = "/ar303save.do")
	public @ResponseBody String dataSave(HttpServletRequest request,HttpServletResponse res) throws Exception {
        
		String sList = request.getParameter("list");
		logger.info("ar303save.list: "+ sList );

		Map<String, Object> mapSos = new ObjectMapper().readValue(sList, HashMap.class);
		HashMap MapUser = (HashMap)request.getSession().getAttribute("USER");
		List<Map> addList = (List<Map>) mapSos.get("addList");
		List<Map> paramList = (List<Map>) mapSos.get("updateList");
		logger.info("ar303save.updateList: "+ paramList );
/*		
		Iterator it;
		HashMap map = new HashMap();
		it = addList.iterator();
		while (it.hasNext())
		{
			map = (HashMap)it.next();
			logger.info("ar303save.iterator: "+ map );
			paramList.add(map);
		}
*/
	    logger.info("ar303save.MapUser: "+ MapUser);
		logger.info("ar303save.addList: "+ addList );
		logger.info("ar303save.update+Add: "+ paramList );
	    

		
		String sRet = Service.dataSave(paramList,MapUser);
		

		
//		Iterator it;
//		HashMap map = new HashMap();
		
//		String sAdd = mapSos.get("addList").toString();
//		String sUpdate = mapSos.get("updateList").toString();
//		String sDelete = mapSos.get("deleteList").toString();

//		logger.info("ar303save.mapSos: "+ mapSos );
//		logger.info("ar303save.sAdd: "+ sAdd );

/*		
		List<Map> addList = (List<Map>) mapSos.get("addList");
		logger.info("ar303save.addList: "+ addList );

		it = addList.iterator();
		
		while (it.hasNext())
		{
			map = (HashMap)it.next();
			logger.info("ar303save.map: "+ map );
			logger.info("ar303save.CUSTCD: "+ map.get("CUSTCD") );
			logger.info("ar303save.MEMO: "+ map.get("MEMO") );
			logger.info("ar303save.PRDATE: "+ map.get("PRDATE") );
//			String sRet = Service.ar303(map);
		}		
*/		
//		List<Map> updateList = (List<Map>) mapSos.get("updateList");
//		logger.info("ar303save.updateList: "+ updateList );
//		String sRet = Service.dataSave(updateList,MapUser);

//		it = updateList.iterator();
		
//		while (it.hasNext())
//		{
//			map = (HashMap)it.next();
//			logger.info("ar303save.map: "+ map );
//			logger.info("ar303save.CUSTCD: "+ map.get("CUSTCD") );
//			logger.info("ar303save.MEMO: "+ map.get("MEMO") );
//			logger.info("ar303save.PRDATE: "+ map.get("PRDATE") );
//			logger.info("ar303save.chk: "+ map.get("chk") );
//			String sRet = Service.ar303Save(map);
//			if (map.get("chk")==null){
//				logger.info("ar303save.update: "+ map.get("chk") );
//			}
//			else{
//				logger.info("ar303save.delete: "+ map.get("chk") );
//				String sRet = Service.custMcMemoDelete(map);
//			}
//		}		
		return "{\"result\": \"success\"}";
	}
	
}
