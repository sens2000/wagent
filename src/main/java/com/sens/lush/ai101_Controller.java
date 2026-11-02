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
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
import com.sens.lush.service.ai101_Service;

/**
 * Handles requests for the application home page.
 */
@Controller
public class ai101_Controller {
	
//	private static final Logger logger = LoggerFactory.getLogger(this.class);
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@Resource(name = "ai101_Service")
	private ai101_Service Service;

	
	@RequestMapping(value = "/ai101.do")
//	public String ai101(@RequestParam Map map, Model model) {  //From을 MAP 으로 받기
	public String ai101(HttpServletRequest request,Model model ) throws Exception {

		String sPage = "ai101";
	    logger.info("Start Page: "+ sPage);
		
//		model.addAttribute("CODE", sId);

	    return sPage;
	}

    @RequestMapping(value = "/ai101_cust_search.do", method = RequestMethod.GET)
    @ResponseBody
    public List<Map<String, Object>> custSearch(
            @RequestParam(value = "custname", required = false) String custname,
            @RequestParam(value = "owner", required = false) String owner,
            @RequestParam(value = "handno", required = false) String handno,
            @RequestParam(value = "sano", required = false) String sano) throws Exception {

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("custname", custname);
        params.put("owner", owner);
        params.put("handno", handno);
        params.put("sano", sano);

        // Tabulator가 그대로 화면에 뿌릴 수 있도록 컬럼명을 그대로(대문자 key) 반환
        return Service.selectCustList(params);
    }

    @RequestMapping(value = "/ai101_product_search.do", method = RequestMethod.GET)
    @ResponseBody
    public List<Map<String, Object>> jepmSearch(
            @RequestParam(value = "jepmcd", required = false) String jepmcd,
            @RequestParam(value = "barcode", required = false) String barcode,
            @RequestParam(value = "jepmnm", required = false) String jepmnm) throws Exception {

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("jepmcd", jepmcd);
        params.put("barcode", barcode);
        params.put("jepmnm", jepmnm);

        // Tabulator가 그대로 화면에 뿌릴 수 있도록 컬럼명을 그대로(대문자 key) 반환
        return Service.selectJepmList(params);
    }

    @RequestMapping(value = "/ai101_detail_view.do", method = RequestMethod.GET)
    @ResponseBody
    public List<Map<String, Object>> detailView(
            @RequestParam(value = "custcd", required = false) String custcd) throws Exception {

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("custcd", custcd);

        // Tabulator가 그대로 화면에 뿌릴 수 있도록 컬럼명을 그대로(대문자 key) 반환
        return Service.detailView(params);
    }
    
    @RequestMapping(value = "/ai101_misu.do", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> misu(
            @RequestParam("custcd") String custcd,
            @RequestParam(value = "ipdate", required = false) String ipdate) {
     
        // ipdate가 안 넘어오면 오늘 날짜로 기본값 처리
        if (ipdate == null || ipdate.trim().isEmpty()) {
            ipdate = new SimpleDateFormat("yyyyMMdd").format(new Date());
        }
     
        Map<String, Object> paramDay = new HashMap<String, Object>();
        paramDay.put("CUSTCD", custcd);
        paramDay.put("FRDATE", ipdate);
        paramDay.put("TODATE", ipdate);

        Map<String, Object> paramMon = new HashMap<String, Object>();
        paramMon.put("CUSTCD", custcd);
        paramMon.put("FRDATE", ipdate.substring(0, 6) + "01");
        paramMon.put("TODATE", ipdate);
     
        Map<String, Object> result = new HashMap<String, Object>();
     
        try {
            // 당일(day): 해당 날짜 하루치
        	List<Map<String, Object>> day = Service.selectMisu(paramDay);
        	Map<String, Object> rowday = day.get(0);
            // 당월(month): 해당 날짜가 속한 달 1일부터 해당 날짜까지 누적
        	List<Map<String, Object>> month = Service.selectMisu(paramMon);
        	Map<String, Object> rowmonth = month.get(0);
     
            result.put("day", day != null ? rowday : new HashMap<String, Object>());
            result.put("month", month != null ? rowmonth : new HashMap<String, Object>());
        } catch (Exception e) {
            e.printStackTrace();
            result.put("day", new HashMap<String, Object>());
            result.put("month", new HashMap<String, Object>());
        }
     
        return result;
    }

    @RequestMapping(value = "/ai101_save.do", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> save(@RequestBody Map<String, Object> body) throws Exception {
        // 기존 컨트롤러가 custMapper를 직접 쓰는 스타일이라면 아래 주석 처리된 방식으로,
        // ai101_Service를 통해 쓰는 중이라면 그 아래 방식으로 사용하세요. (둘 중 하나만 사용)
     
        // 방식 1) 서비스 계층을 거치는 경우
    	Service.saveSale("save",body);
     
        // 방식 2) 서비스 없이 컨트롤러에서 바로 처리하는 경우 (둘 중 하나 선택)
        // custMapper.deleteSaleLines(...); // 아래 ai101_ServiceImpl_save_추가분.java 의 로직을 여기로 옮기면 됩니다.
     
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("success", true);
        return result;
    }

    @RequestMapping(value = "/ai101_delete.do", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> delete(@RequestBody Map<String, Object> body) throws Exception {
        // 기존 컨트롤러가 custMapper를 직접 쓰는 스타일이라면 아래 주석 처리된 방식으로,
        // ai101_Service를 통해 쓰는 중이라면 그 아래 방식으로 사용하세요. (둘 중 하나만 사용)
     
        // 방식 1) 서비스 계층을 거치는 경우
    	Service.saveSale("del",body);
     
        // 방식 2) 서비스 없이 컨트롤러에서 바로 처리하는 경우 (둘 중 하나 선택)
        // custMapper.deleteSaleLines(...); // 아래 ai101_ServiceImpl_save_추가분.java 의 로직을 여기로 옮기면 됩니다.
     
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("success", true);
        return result;
    }    
    @RequestMapping(value = "/ai101_sale_query.do", method = RequestMethod.GET)
    @ResponseBody
    public List<Map<String, Object>> saleQuery(
            @RequestParam(value = "fromDate", required = false) String fromDate,
            @RequestParam(value = "toDate", required = false) String toDate,
            @RequestParam(value = "custcd", required = false) String custcd,
            @RequestParam(value = "custname", required = false) String custname) throws Exception {
     
        Map<String, Object> params = new HashMap<String, Object>();
        // 프론트에서 <input type="date">로 "2026-09-01" 형태로 넘어오므로 YYYYMMDD로 변환
        params.put("fromDate", (fromDate == null || fromDate.trim().isEmpty()) ? null : fromDate.replace("-", ""));
        params.put("toDate", (toDate == null || toDate.trim().isEmpty()) ? null : toDate.replace("-", ""));
        params.put("custcd", (custcd == null || custcd.trim().isEmpty()) ? null : custcd);
        params.put("custname", (custname == null || custname.trim().isEmpty()) ? null : custname.trim());
     
        return Service.selectSaleQueryList(params);
    }
     
    @RequestMapping(value = "/ai101_sale_detail.do", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> saleDetail(@RequestParam("jpno") String jpno) throws Exception {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("jpno", jpno);
     
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("header", Service.selectSaleHeader(params));
        result.put("products", Service.selectSaleDetailList(params));
        return result;
    }

    @RequestMapping(value = "/ai101_memo_list.do", method = RequestMethod.GET)
    @ResponseBody
    public List<Map<String, Object>> memoList(@RequestParam("custcd") String custcd) throws Exception {
        return Service.getMemoList(custcd);
    }

    @RequestMapping(value = "/ai101_memo_save.do", method = RequestMethod.POST)
    @ResponseBody
    public void memoSave(@RequestBody Map<String, Object> body) throws Exception {
    	Service.saveMemo(body);
    }

    @RequestMapping(value = "/ai101_memo_delete.do", method = RequestMethod.POST)
    @ResponseBody
    public void memoDelete(@RequestBody Map<String, Object> body) throws Exception {
    	Service.deleteMemo(body);
    }
    
    @RequestMapping(value = "/ai101_statement.do", method = RequestMethod.GET)
    public String statement(@RequestParam("jpno") String jpno,
                             Model model) throws Exception {
        Map<String, Object> header = Service.getStatementHeader(jpno);
        List<Map<String, Object>> products = Service.getStatementList(jpno);
        Map<String, Object> company = Service.getStatementCompany();
        
        String cdate = (header != null && header.get("CDATE") != null) ? header.get("CDATE").toString() : "";
        String custcd = (header != null && header.get("CUSTCD") != null) ? header.get("CUSTCD").toString() : "";

        Map<String, Object> paramDay = new HashMap<String, Object>();
        paramDay.put("CUSTCD", custcd);
        paramDay.put("FRDATE", cdate);
        paramDay.put("TODATE", cdate);

    	List<Map<String, Object>> day = Service.selectMisu(paramDay);
    	Map<String, Object> misu = day.get(0);
        	
        String ipdate = (cdate.length() == 8)
                ? cdate.substring(0, 4) + "-" + cdate.substring(4, 6) + "-" + cdate.substring(6, 8)
                : cdate;

        long totalAmt = 0;
        long totalTax = 0;
        for (Map<String, Object> p : products) {
            Object amtObj = p.get("AMT");
            Object taxObj = p.get("TAXAMT");
            totalAmt += (amtObj != null) ? ((Number) amtObj).longValue() : 0L;
            totalTax += (taxObj != null) ? ((Number) taxObj).longValue() : 0L;
        }

	    logger.info("statement jpno: "+ jpno);
	    logger.info("statement header: "+ header);
	    logger.info("statement company: "+ company);
	    logger.info("statement misu: "+ misu);
        
        model.addAttribute("custHeader", header);
        model.addAttribute("company", company);
        model.addAttribute("products", products);
        model.addAttribute("ipdate", ipdate);
        model.addAttribute("totalAmt", totalAmt);
        model.addAttribute("totalTax", totalTax);
        model.addAttribute("misu", misu);
        return "ai101_statement";
    }
    
    
    @RequestMapping(value = "/IpsDataAdd.do", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> ipsDataAdd(@RequestBody Map<String, Object> paramMap) throws Exception {
        Service.addIpsData(paramMap);

        Map<String, Object> result = new HashMap<String, Object>();
        result.put("success", true);
        return result;
    }
    
    @RequestMapping(value = "/ai101_sms_list.do", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> smsList(@RequestParam("custcd") String custcd) throws Exception {
        List<Map<String, Object>> list = Service.getSmsList(custcd);
        Map<String, Object> cntMap = Service.getIpsSmsCnt();
        Map<String, Object> company = Service.getStatementCompany();
        String telno = (company != null && company.get("TEL") != null) ? company.get("TEL").toString() : "";
        
        long cnt = 0;
        if (cntMap != null && cntMap.get("cnt") != null) {
            cnt = ((Number) cntMap.get("cnt")).longValue();
        }

        Map<String, Object> result = new HashMap<String, Object>();
        result.put("list", list);
        result.put("cnt", cnt);
        result.put("telno", telno);
        
        return result;
    }
    
    
    
    
	@RequestMapping(value = "/ai101grid.do")
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

	
	@RequestMapping(value = "/ai101save.do")
	public @ResponseBody String dataSave(HttpServletRequest request,HttpServletResponse res) throws Exception {
        
		String sList = request.getParameter("list");
		logger.info("ai101save.list: "+ sList );

		Map<String, Object> mapSos = new ObjectMapper().readValue(sList, HashMap.class);
		HashMap MapUser = (HashMap)request.getSession().getAttribute("USER");
		List<Map> addList = (List<Map>) mapSos.get("addList");
		List<Map> paramList = (List<Map>) mapSos.get("updateList");
		logger.info("ai101save.updateList: "+ paramList );
/*		
		Iterator it;
		HashMap map = new HashMap();
		it = addList.iterator();
		while (it.hasNext())
		{
			map = (HashMap)it.next();
			logger.info("ai101save.iterator: "+ map );
			paramList.add(map);
		}
*/
	    logger.info("ai101save.MapUser: "+ MapUser);
		logger.info("ai101save.addList: "+ addList );
		logger.info("ai101save.update+Add: "+ paramList );
	    

		
		String sRet = Service.dataSave(paramList,MapUser);
		

		
//		Iterator it;
//		HashMap map = new HashMap();
		
//		String sAdd = mapSos.get("addList").toString();
//		String sUpdate = mapSos.get("updateList").toString();
//		String sDelete = mapSos.get("deleteList").toString();

//		logger.info("ai101save.mapSos: "+ mapSos );
//		logger.info("ai101save.sAdd: "+ sAdd );

/*		
		List<Map> addList = (List<Map>) mapSos.get("addList");
		logger.info("ai101save.addList: "+ addList );

		it = addList.iterator();
		
		while (it.hasNext())
		{
			map = (HashMap)it.next();
			logger.info("ai101save.map: "+ map );
			logger.info("ai101save.CUSTCD: "+ map.get("CUSTCD") );
			logger.info("ai101save.MEMO: "+ map.get("MEMO") );
			logger.info("ai101save.PRDATE: "+ map.get("PRDATE") );
//			String sRet = Service.ai101(map);
		}		
*/		
//		List<Map> updateList = (List<Map>) mapSos.get("updateList");
//		logger.info("ai101save.updateList: "+ updateList );
//		String sRet = Service.dataSave(updateList,MapUser);

//		it = updateList.iterator();
		
//		while (it.hasNext())
//		{
//			map = (HashMap)it.next();
//			logger.info("ai101save.map: "+ map );
//			logger.info("ai101save.CUSTCD: "+ map.get("CUSTCD") );
//			logger.info("ai101save.MEMO: "+ map.get("MEMO") );
//			logger.info("ai101save.PRDATE: "+ map.get("PRDATE") );
//			logger.info("ai101save.chk: "+ map.get("chk") );
//			String sRet = Service.ai101Save(map);
//			if (map.get("chk")==null){
//				logger.info("ai101save.update: "+ map.get("chk") );
//			}
//			else{
//				logger.info("ai101save.delete: "+ map.get("chk") );
//				String sRet = Service.custMcMemoDelete(map);
//			}
//		}		
		return "{\"result\": \"success\"}";
	}
	
}
