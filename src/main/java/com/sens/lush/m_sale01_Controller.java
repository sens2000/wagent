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
import com.sens.lush.service.m_sale01_Service;
import com.sens.lush.service.codeMgr_Service;


/**
 * Handles requests for the application home page.
 */
@Controller
public class m_sale01_Controller {
	
//	private static final Logger logger = LoggerFactory.getLogger(this.class);
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
	 private static final String UPLOAD_DIR = "/opt/photos";
	 private final ObjectMapper objectMapper = new ObjectMapper();
	 
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@Resource(name = "m_sale01_Service")
	
	@Autowired
	private m_sale01_Service Service;
	@Autowired
	private codeMgr_Service CodeMgrService;

	
	@RequestMapping(
			  value="/getBrand.do",
			  method=RequestMethod.POST,
			  produces="application/json; charset=UTF-8"
			)
			@ResponseBody
			public String getBrand(HttpServletRequest request, Model model, HttpSession session) throws Exception {

		String sId = request.getParameter("ID");
		String sBuseo = request.getParameter("BUSEO");
		
	    logger.info("m_sale01.BUSEO: "+ sBuseo);
		
		HashMap map = new HashMap();
	    Map<String, Object> res = new HashMap<>();
		
	    logger.info("m_sale01.Service: "+ Service);
	    
	    List<Map<String, Object>> listData = null;
	    map.put("BUSEO", sBuseo);
		listData = Service.m_getBrandSearch(map);
				
	    logger.info("m_sale01.listCust: "+ listData);

		res.put("result", "OK");
		res.put("message", "SUCCESS");
		res.put("accessToken", "m_main");
		res.put("data", listData);

		
		return new ObjectMapper().writeValueAsString(res);
	}
    	
	@RequestMapping(
			  value="/getCust.do",
			  method=RequestMethod.POST,
			  produces="application/json; charset=UTF-8"
			)
			@ResponseBody
			public String getCust(HttpServletRequest request, Model model, HttpSession session) throws Exception {
    	
		String sId = request.getParameter("ID");
		String sBuseo = request.getParameter("BUSEO");
		String sMimc = URLDecoder.decode(request.getParameter("MIMC"), StandardCharsets.UTF_8.name());
		String sFind = URLDecoder.decode(request.getParameter("FIND"), StandardCharsets.UTF_8.name());

	    logger.info("m_sale02.BUSEO: "+ sBuseo);
	    logger.info("m_sale02.sMimc: "+ sMimc);
	    logger.info("m_sale02.sFind: "+ sFind);
		
		HashMap map = new HashMap();
	    Map<String, Object> res = new HashMap<>();
		
	    logger.info("m_sale02.Service: "+ Service);
	    
	    map.put("BUSEO", sBuseo);
	    map.put("MIMC", sMimc);
	    map.put("FIND", sFind);
	    

//		List<Map<String, Object>> listCust = CodeMgrService.m_getCodeList(map,"m_CUSTLIST","");
		List<Map<String, Object>> listCust = Service.m_getCodeList(map);
//	    String gSonBrand = JsonUtil.ListToString(listBrand);
				
	    logger.info("m_sale02.listCust: "+ listCust);

		res.put("result", "OK");
		res.put("message", "SUCCESS");
		res.put("accessToken", "m_main");
		res.put("data", listCust);

		
		return new ObjectMapper().writeValueAsString(res);
	}

	@RequestMapping(
			  value="/saveCustSK.do",
			  method=RequestMethod.POST,
			  produces="application/json; charset=UTF-8"
			)
			@ResponseBody
			public String m_sale03(HttpServletRequest request, Model model, HttpSession session) throws Exception {
  	
		String sId = request.getParameter("ID");
		String sBuseo = request.getParameter("BUSEO");
		String sCustcd = request.getParameter("CUSTCD");
		String sSKDate = request.getParameter("SKDATE");
		sSKDate = sSKDate.replace("-", "");
		String sAmount = request.getParameter("AMOUNT");
		String sRemk = URLDecoder.decode(request.getParameter("REMK"), StandardCharsets.UTF_8.name());

	    logger.info("m_sale03.BUSEO: "+ sBuseo);
	    logger.info("m_sale03.CUSTCD: "+ sCustcd);
	    logger.info("m_sale03.SKDATE: "+ sSKDate);
	    logger.info("m_sale03.AMOUNT: "+ sAmount);
	    logger.info("m_sale03.REMK: "+ sRemk);
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
	    String sDatetime = sdf.format(new Date());
	    
	    String sToday = sDatetime.substring(0, 8);
	    String year = sDatetime.substring(3, 4);        // "2024"
	    String month = sDatetime.substring(4, 6);       // "03"
	    String sMM="";
	    if (month.equals("10")) {
	        sMM = "A";
	    } else if (month.equals("11")) {
	        sMM = "B";
	    } else if (month.equals("12")) {
	        sMM = "C";
	    } else {
	        sMM = sDatetime.substring(5, 6);;
	    }
	    
	    String day = sDatetime.substring(6, 8);         // "24"
	    String hour = sDatetime.substring(8, 10);       // "15"
	    String minute = sDatetime.substring(10, 12);    // "30"
	    String second = sDatetime.substring(12, 14);    // "45"
	    long lNo = Long.parseLong(hour) * 3600 + Long.parseLong(minute) * 3600 + Long.parseLong(second);
	    String sNo = String.format("%05d", lNo);
	    String sJpno = sBuseo.substring(3, 4) + year + sMM + day + sNo;

	    logger.info("m_sale03.sJpno: "+ sJpno);
	    
		HashMap map = new HashMap();
	    Map<String, Object> res = new HashMap<>();
		
	    logger.info("m_sale03.Service: "+ Service);
	    logger.info("m_sale03.CodeMgrService: "+ CodeMgrService);
	    
	    map.put("BUSEO", sBuseo);
	    map.put("JPNO", sJpno);
	    map.put("KJSKDATE", sSKDate);
	    map.put("CUSTCD", sCustcd);
	    map.put("CASHAMT", sAmount);
	    map.put("PRDATE", sToday);
	    map.put("PRUSER", sId);
	    map.put("REMK", sRemk);
	    logger.info("m_sale03.map: "+ map);
        
		String sRet = Service.m_dataSKSave(map);
	    
		res.put("result", "OK");
		res.put("message", "SUCCESS");
		res.put("accessToken", "m_main");
//		res.put("data", listCust);

		
		return new ObjectMapper().writeValueAsString(res);
	}

	@RequestMapping(
			  value="/getCustSaleQuery.do",
			  method=RequestMethod.POST,
			  produces="application/json; charset=UTF-8"
			)
			@ResponseBody
			public String getCustSaleQuery(HttpServletRequest request, Model model, HttpSession session) throws Exception {
  	
		String sId = request.getParameter("ID");
		String sBuseo = request.getParameter("BUSEO");
		String sCustcd = request.getParameter("CUSTCD");
		String sCdate = request.getParameter("CDATE");
		sCdate = sCdate.replace("-", "") + "01";
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
		LocalDate date = LocalDate.parse(sCdate, formatter);
		LocalDate previousMonth = date.minusMonths(1);
		String sYYMM = previousMonth.format(formatter);
		sYYMM = sYYMM.substring(0, 6);
		
	    logger.info("m_sale04.BUSEO: "+ sBuseo);
	    logger.info("m_sale04.sCustcd: "+ sCustcd);
	    logger.info("m_sale04.sYYMM: "+ sCdate);
		
		HashMap map = new HashMap();
	    Map<String, Object> res = new HashMap<>();
		
	    logger.info("m_sale02.Service: "+ Service);
	    
	    map.put("BUSEO", sBuseo);
	    map.put("CUSTCD", sCustcd);
	    map.put("CDATE", sCdate);
	    map.put("YYMM", sYYMM);
	    

		List<Map<String, Object>> listData = Service.m_getCustMCSK(map);
				
	    logger.info("m_sale04.listCust: "+ listData);

		res.put("result", "OK");
		res.put("message", "SUCCESS");
		res.put("accessToken", "m_main");
		res.put("data", listData);

		
		return new ObjectMapper().writeValueAsString(res);
	}

		
	@RequestMapping(
			  value="/getJepum.do",
			  method=RequestMethod.POST,
			  produces="application/json; charset=UTF-8"
			)
			@ResponseBody
			public String getJepum(HttpServletRequest request, Model model, HttpSession session) throws Exception {
	
		String sId = request.getParameter("ID");
		String sBuseo = request.getParameter("BUSEO");
		String sGubun = URLDecoder.decode(request.getParameter("GUBUN"), StandardCharsets.UTF_8.name());
		String sFind = URLDecoder.decode(request.getParameter("FIND"), StandardCharsets.UTF_8.name());
		
	    logger.info("m_sale05.BUSEO: "+ sBuseo);
	    logger.info("m_sale05.sGubun: "+ sGubun);
	    logger.info("m_sale05.sFind: "+ sFind);
		
		HashMap map = new HashMap();
	    Map<String, Object> res = new HashMap<>();
		
	    logger.info("m_sale05.Service: "+ Service);
	    
	    List<Map<String, Object>> listData = null;
	    map.put("BUSEO", sBuseo);
    	map.put("GUBUN", sGubun);
    	map.put("FIND", sFind);
		listData = Service.m_getJepumSearch(map);
				
	    logger.info("m_sale05.listCust: "+ listData);

		res.put("result", "OK");
		res.put("message", "SUCCESS");
		res.put("accessToken", "m_main");
		res.put("data", listData);

		
		return new ObjectMapper().writeValueAsString(res);
	}

	@RequestMapping(
			  value="/OrderSave.do",
			  method=RequestMethod.POST,
			  produces="application/json; charset=UTF-8"
			)
			@ResponseBody
			public String OrderSave(HttpServletRequest request, Model model, HttpSession session) throws Exception {
	
		String sId = request.getParameter("ID");
		String sBuseo = request.getParameter("BUSEO");
		String sMcsk = URLDecoder.decode(request.getParameter("MCSK"), StandardCharsets.UTF_8.name());
		String sProc = request.getParameter("PROC");
		String sOldjpno = request.getParameter("JPNO");
		String sCustcd = request.getParameter("CUSTCD");
		String sDate = request.getParameter("CDATE");
		sDate = sDate.replace("-", "");
		String sTotqty = request.getParameter("TOTQTY");
		String sTotamt = request.getParameter("TOTAMT");
		String sData = URLDecoder.decode(request.getParameter("DATA"), StandardCharsets.UTF_8.name());
		
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
	    String sDatetime = sdf.format(new Date());
	    
	    String sToday = sDatetime.substring(0, 8);
	    String year = sDatetime.substring(3, 4);        // "2024"
	    String month = sDatetime.substring(4, 6);       // "03"
	    String sMM="";
	    if (month.equals("10")) {
	        sMM = "A";
	    } else if (month.equals("11")) {
	        sMM = "B";
	    } else if (month.equals("12")) {
	        sMM = "C";
	    } else {
	        sMM = sDatetime.substring(5, 6);;
	    }
	    
	    String day = sDatetime.substring(6, 8);         // "24"
	    String hour = sDatetime.substring(8, 10);       // "15"
	    String minute = sDatetime.substring(10, 12);    // "30"
	    String second = sDatetime.substring(12, 14);    // "45"
	    long lNo = Long.parseLong(hour) * 3600 + Long.parseLong(minute) * 3600 + Long.parseLong(second);
	    String sNo = String.format("%05d", lNo);
	    String sNewpno = sBuseo.substring(3, 4) + year + sMM + day + sNo;
	    
	    logger.info("m_sale06.BUSEO: "+ sBuseo);
	    logger.info("m_sale06.sGubun: "+ sCustcd);
	    logger.info("m_sale06.sDate: "+ sDate);
	    logger.info("m_sale06.sTotqty: "+ sTotqty);
	    logger.info("m_sale06.sTotqty: "+ sTotqty);
	    logger.info("m_sale06.sData: "+ sData);

        Map<String, Object>[] mapSos = new ObjectMapper().readValue(sData, Map[].class);
	        
        System.out.println("=== 배열 크기 ===");
        System.out.println("총 " + mapSos.length + "개");

/*        
        System.out.println("\n=== 모든 항목 ===");
        for (int i = 0; i < mapSos.length; i++) {
            System.out.println("[" + (i+1) + "] " + mapSos[i]);
        }
*/	        
	        
	    
		HashMap map = new HashMap();
	    Map<String, Object> res = new HashMap<>();
		
	    logger.info("m_sale06.Service: "+ Service);
	    String sYYMM = sDate.substring(0, 6);
	    map.put("BUSEO", sBuseo);
	    map.put("MCSK", sMcsk);
	    map.put("PROC", sProc);
	    map.put("OLDJPNO", sOldjpno);
	    map.put("JPNO", sNewpno);
	    map.put("CUSTCD", sCustcd);
	    map.put("YYMM", sYYMM);
	    map.put("CDATE", sDate);
	    map.put("TOTQTY", sTotqty);
	    map.put("TOTAMT", sTotamt);
	    map.put("USERID", sId);
	    
	    logger.info("m_sale06.map: "+ map);

		String sRet = Service.m_dataMCSave(mapSos,map);
	    
		res.put("result", "OK");
		res.put("message", sRet);
		res.put("accessToken", "m_main");
		
		return new ObjectMapper().writeValueAsString(res);
	}

	@RequestMapping(
			  value="/MCSKQuery.do",
			  method=RequestMethod.POST,
			  produces="application/json; charset=UTF-8"
			)
			@ResponseBody
			public String MCSKQuery(HttpServletRequest request, Model model, HttpSession session) throws Exception {
	
		String sId = request.getParameter("ID");
		String sBuseo = request.getParameter("BUSEO");
		String sStartdate = request.getParameter("STARTDATE");
		String sEnddate = request.getParameter("ENDDATE");
		
	    logger.info("m_sale07.BUSEO: "+ sBuseo);
	    logger.info("m_sale07.sStartdate: "+ sStartdate);
	    logger.info("m_sale07.sEnddate: "+ sEnddate);
		
		HashMap map = new HashMap();
	    Map<String, Object> res = new HashMap<>();
		
	    logger.info("m_sale07.Service: "+ Service);
	    
	    map.put("BUSEO", sBuseo);
	    map.put("STARTDATE", sStartdate);
	    map.put("ENDDATE", sEnddate);
	    

		List<Map<String, Object>> listData = Service.m_MCSKQuery(map);
				
	    logger.info("m_sale07.listCust: "+ listData);

		res.put("result", "OK");
		res.put("message", "SUCCESS");
		res.put("accessToken", "m_main");
		res.put("data", listData);

		
		return new ObjectMapper().writeValueAsString(res);
	}

	@RequestMapping(
			  value="/JPNOMCSKQuery.do",
			  method=RequestMethod.POST,
			  produces="application/json; charset=UTF-8"
			)
			@ResponseBody
			public String JPNOMCSKQuery(HttpServletRequest request, Model model, HttpSession session) throws Exception {
	
		String sId = request.getParameter("ID");
		String sBuseo = request.getParameter("BUSEO");
		String sJpno = request.getParameter("JPNO");
		
	    logger.info("m_sale08.BUSEO: "+ sBuseo);
	    logger.info("m_sale08.sJpno: "+ sJpno);
		
		HashMap map = new HashMap();
	    Map<String, Object> res = new HashMap<>();
		
	    logger.info("m_sale08.Service: "+ Service);
	    
	    map.put("BUSEO", sBuseo);
	    map.put("JPNO", sJpno);
	    

		List<Map<String, Object>> listData = Service.m_JPNOMCSKQuery(map);
				
	    logger.info("m_sale08.listCust: "+ listData);

		res.put("result", "OK");
		res.put("message", "SUCCESS");
		res.put("accessToken", "m_main");
		res.put("data", listData);

		
		return new ObjectMapper().writeValueAsString(res);
	}
	
/*		
	saleActivity->ConfirmActivity

	mcskqueryactivity->MCSKDelActivity
	activity_mcsk_del
	item_order_confirm
	MCSKDelActivity
*/	
	@RequestMapping(value = "/m_sale01grid.do")
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

    @PostMapping(value = "/upload/photo", consumes = "multipart/form-data")
    public ResponseEntity<?> uploadPhoto(
            @RequestPart("custCode") String custCode,
            @RequestPart("custName") String custName,
            @RequestPart("photo") MultipartFile photo) {
    	
	    logger.info("uploadPhoto.custCode: "+ custCode);
	    logger.info("uploadPhoto.custName: "+ custName);

	    Map<String, Object> res = new HashMap<>();
    	
        logger.info("이미지 수신 - 고객: {}, 파일명: {}, 크기: {}" +
                custName, photo.getOriginalFilename(), photo.getSize());

        try {
            // 1. 입력값 검증
            if (photo.isEmpty()) {
        		res.put("result", "ERROR");
        		res.put("message", "파일이 비어있습니다");
        		res.put("accessToken", "m_main");
        		return ResponseEntity.badRequest().body(res);
            }

            // 2. 파일 타입 검증
            String contentType = photo.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
        		res.put("result", "ERROR");
        		res.put("message", "이미지 파일만 업로드 가능합니다");
        		res.put("accessToken", "m_main");
        		return ResponseEntity.badRequest().body(res);
            }

            // 3. 파일 크기 검증 (10MB 제한)
            if (photo.getSize() > 10 * 1024 * 1024) {
        		res.put("result", "ERROR");
        		res.put("message", "파일 크기가 너무 큽니다 (최대 10MB)");
        		res.put("accessToken", "m_main");
        		return ResponseEntity.badRequest().body(res);
            }

            // 4. 업로드 디렉토리 생성
            File uploadDir = new File(UPLOAD_DIR);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            // 5. 파일명 생성 (중복 방지)
            String fileName = generateFileName(custCode, custName, photo.getOriginalFilename());
            Path filePath = Paths.get(UPLOAD_DIR, fileName);

            // 6. 파일 저장
            Files.write(filePath, photo.getBytes());

            logger.info("파일 저장 성공: {}", fileName);
            logger.info("파일 name 성공: {}", filePath);
            

    		res.put("result", "OK");
    		res.put("message", "SUCCESS");
    		res.put("fileName", fileName);
    		res.put("accessToken", "m_main");
    		return ResponseEntity.ok(res);
    		
        } catch (IOException e) {
            logger.error("파일 저장 실패: {}", e.getMessage(), e);
    		res.put("result", "ERROR");
    		res.put("message", "파일 저장 중 오류: " + e.getMessage());
    		res.put("accessToken", "m_main");
    		return ResponseEntity.status(500).body(res);
        }
    }

	@RequestMapping(value = "/m_sale01save.do")
	public @ResponseBody String dataSave(HttpServletRequest request,HttpServletResponse res) throws Exception {
        
		String sList = request.getParameter("list");
		logger.info("m_sale01save.list: "+ sList );

		Map<String, Object> mapSos = new ObjectMapper().readValue(sList, HashMap.class);
		HashMap MapUser = (HashMap)request.getSession().getAttribute("USER");
	    logger.info("gridCustList.MapUser: "+ MapUser);
		
		List<Map> updateList = (List<Map>) mapSos.get("updateList");
		logger.info("m_sale01save.updateList: "+ updateList );
		String sRet = Service.dataSave(updateList,MapUser);

		return "{\"result\": \"success\"}";
	}
	
    private String generateFileName(String custCode, String custName, String originalFileName) {
        String extension = originalFileName != null && originalFileName.contains(".") ?
                originalFileName.substring(originalFileName.lastIndexOf(".")) : ".jpg";
        return custCode + "_" + custName + "_" + System.currentTimeMillis() + extension;
    }
    
}
