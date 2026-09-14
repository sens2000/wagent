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
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
import com.sens.lush.service.ai018_Service;

/**
 * Handles requests for the application home page.
 */
@Controller
public class ai018_Controller {
	
//	private static final Logger logger = LoggerFactory.getLogger(this.class);
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@Resource(name = "ai018_Service")
	private ai018_Service Service;

	
	@RequestMapping(value = "/ai018.do")
//	public String ai018(@RequestParam Map map, Model model) {  //From을 MAP 으로 받기
	public String ai018(HttpServletRequest request,Model model ) throws Exception {

		String sPage = "ai018";
	    logger.info("Start Page: "+ sPage);
		
//		model.addAttribute("CODE", sId);

	    return sPage;
	}

    //List<Map<String, Object>>   list(HashMap paramMap) throws Exception;
    
    @GetMapping("/api/attendance")
    public @ResponseBody List<Map<String, Object>> list(
            @RequestParam(required = false, defaultValue = "전체") String equip,
            @RequestParam(required = false) String date,
            @RequestParam(required = false, defaultValue = "전체") String pay,
            @RequestParam(required = false, defaultValue = "전체") String status
    )  throws Exception {
		HashMap map = new HashMap();
		map.put("equip", equip);
		map.put("date", date);
		map.put("pay", pay);
		map.put("status", status);
		
		logger.info("attendance.1: " );
		List<Map<String, Object>> list =  Service.list(map);
		logger.info("attendance.2: "+ list );
		return list;
    }
    
    @GetMapping("/api/attendance/status-codes")
    @ResponseBody
    public List<String> statusCodes() throws Exception {
        return Service.selectCode("근태");
    }
    
    @GetMapping("/api/attendance/equipments")
    @ResponseBody
    public List<String> statusMachine() throws Exception {
        return Service.selectCode("설비");
    }
    

    
    @PatchMapping("/api/attendance/{id}/status")
    @ResponseBody
    public String updateStatus(@PathVariable Long id, @RequestBody Map<String, Object> body) throws Exception {
    	body.put("id", id);
    	Service.updateStatus(body);
    	return "{\"result\": \"success\"}";
//        return ResponseEntity.ok().build();
    }
    
    @PatchMapping("/api/attendance/{id}/checkin")
    @ResponseBody
    public String updateCheckin(@PathVariable Long id, @RequestBody Map<String, Object> body) throws Exception {
    	body.put("id", id);
    	Service.updateCheckin(body);
    	return "{\"result\": \"success\"}";
//        return ResponseEntity.ok().build();
    }
    
    @PatchMapping("/api/attendance/{id}/checkout")
    @ResponseBody
    public String updateCheckout(@PathVariable Long id, @RequestBody Map<String, Object> body) throws Exception {
    	body.put("id", id);
    	Service.updateCheckout(body);
    	return "{\"result\": \"success\"}";
//        return ResponseEntity.ok().build();
    }
    
    @PatchMapping("/api/attendance/{id}/equip")
    @ResponseBody
    public String updateEquip(@PathVariable Long id, @RequestBody Map<String, Object> body) throws Exception {
    	body.put("id", id);
    	Service.updateEquip(body);
    	return "{\"result\": \"success\"}";
//        return ResponseEntity.ok().build();
    }

    /** PATCH /api/attendance/{id}/approve  body: { stage } — 순차 결재(확인→반장→과장→부장)만 허용 */
    @PatchMapping("/api/attendance/{id}/approve")
    public String approve(@PathVariable Long id, @RequestBody Map<String, Object> body)throws Exception {
    	body.put("id", id);
    	Service.updateapprove(body);
    	return "{\"result\": \"success\"}";
    }
    
    @PostMapping("/api/attendance/{id}/send")
    public String send(@PathVariable Long id, @RequestBody Map<String, Object> body)throws Exception {
    	body.put("id", id);
		logger.info("control.send.body: " + body);
    	Service.updatesend(body);
    	return "{\"result\": \"success\"}";
    }
    
	@RequestMapping(value = "/ai018save.do")
	public @ResponseBody String dataSave(HttpServletRequest request,HttpServletResponse res) throws Exception {
        
		String sList = request.getParameter("list");
		logger.info("ai018save.list: "+ sList );

		Map<String, Object> mapSos = new ObjectMapper().readValue(sList, HashMap.class);
		HashMap MapUser = (HashMap)request.getSession().getAttribute("USER");
		List<Map> addList = (List<Map>) mapSos.get("addList");
		List<Map> paramList = (List<Map>) mapSos.get("updateList");
		logger.info("ai018save.updateList: "+ paramList );
/*		
		Iterator it;
		HashMap map = new HashMap();
		it = addList.iterator();
		while (it.hasNext())
		{
			map = (HashMap)it.next();
			logger.info("ai018save.iterator: "+ map );
			paramList.add(map);
		}
*/
	    logger.info("ai018save.MapUser: "+ MapUser);
		logger.info("ai018save.addList: "+ addList );
		logger.info("ai018save.update+Add: "+ paramList );
	    

		
		String sRet = Service.dataSave(paramList,MapUser);
		

		
//		Iterator it;
//		HashMap map = new HashMap();
		
//		String sAdd = mapSos.get("addList").toString();
//		String sUpdate = mapSos.get("updateList").toString();
//		String sDelete = mapSos.get("deleteList").toString();

//		logger.info("ai018save.mapSos: "+ mapSos );
//		logger.info("ai018save.sAdd: "+ sAdd );

/*		
		List<Map> addList = (List<Map>) mapSos.get("addList");
		logger.info("ai018save.addList: "+ addList );

		it = addList.iterator();
		
		while (it.hasNext())
		{
			map = (HashMap)it.next();
			logger.info("ai018save.map: "+ map );
			logger.info("ai018save.CUSTCD: "+ map.get("CUSTCD") );
			logger.info("ai018save.MEMO: "+ map.get("MEMO") );
			logger.info("ai018save.PRDATE: "+ map.get("PRDATE") );
//			String sRet = Service.ai018(map);
		}		
*/		
//		List<Map> updateList = (List<Map>) mapSos.get("updateList");
//		logger.info("ai018save.updateList: "+ updateList );
//		String sRet = Service.dataSave(updateList,MapUser);

//		it = updateList.iterator();
		
//		while (it.hasNext())
//		{
//			map = (HashMap)it.next();
//			logger.info("ai018save.map: "+ map );
//			logger.info("ai018save.CUSTCD: "+ map.get("CUSTCD") );
//			logger.info("ai018save.MEMO: "+ map.get("MEMO") );
//			logger.info("ai018save.PRDATE: "+ map.get("PRDATE") );
//			logger.info("ai018save.chk: "+ map.get("chk") );
//			String sRet = Service.ai018Save(map);
//			if (map.get("chk")==null){
//				logger.info("ai018save.update: "+ map.get("chk") );
//			}
//			else{
//				logger.info("ai018save.delete: "+ map.get("chk") );
//				String sRet = Service.custMcMemoDelete(map);
//			}
//		}		
		return "{\"result\": \"success\"}";
	}
	
}
