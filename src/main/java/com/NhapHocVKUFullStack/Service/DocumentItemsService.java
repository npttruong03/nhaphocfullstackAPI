package com.NhapHocVKUFullStack.Service;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.NhapHocVKUFullStack.Utils.Utils;
import com.NhapHocVKUFullStack.config.AppConfig;
import com.NhapHocVKUFullStack.models.Area;
import com.NhapHocVKUFullStack.models.DocumentItems;
import com.NhapHocVKUFullStack.models.Khoa;
import com.NhapHocVKUFullStack.models.Majors;
import com.NhapHocVKUFullStack.models.Method;
import com.NhapHocVKUFullStack.models.StudentTuition;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.multipart.MultipartFile;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

@Service
public class DocumentItemsService {
	private String api = Utils.BASE_URL;
	private AppConfig appConfig;
	private RestTemplate restTemplate = new RestTemplate();
    @Autowired
    private KhoaService khoaService;

    @Autowired
    private MajorsService majorsService;

    @Autowired
    private MethodService methodService;
	
	@Autowired
	public DocumentItemsService(AppConfig appConfig) {
		this.appConfig = appConfig;
	}
	public List<DocumentItems> getAll() throws Exception {
	    String apiURL = api + "/admin/api/document/getAll";

	    HttpHeaders headers = appConfig.cookieStore().getHeaders(); // Get the HttpHeaders with cookies
	    RequestEntity<?> requestEntity = new RequestEntity<>(headers, HttpMethod.GET, URI.create(apiURL));
	    ResponseEntity<String> response = restTemplate.exchange(requestEntity, String.class);
	    String json = response.getBody();

	    ObjectMapper objectMapper = new ObjectMapper();
	    List<DocumentItems> list = objectMapper.readValue(json, new TypeReference<List<DocumentItems>>() {});

	    return list;
	}
	
	public DocumentItems getDocument(int id) throws Exception {
		String apiURL = api+"/admin/api/document/get/" + id;
		RequestEntity<?> requestEntity = new RequestEntity<>(appConfig.cookieStore().getHeaders(), HttpMethod.GET, URI.create(apiURL));
		ResponseEntity<DocumentItems> response = restTemplate.exchange(requestEntity, DocumentItems.class);
		DocumentItems documentItems = response.getBody();

	    return documentItems;
	}

	public Boolean saveDocument(DocumentItems documentItems) {
		String apiURL = api+"/admin/api/document/save";
		RequestEntity<?> requestEntity = new RequestEntity<>(documentItems, appConfig.cookieStore().getHeaders(), HttpMethod.POST, URI.create(apiURL));
		ResponseEntity<String> response = restTemplate.exchange(requestEntity, String.class);
		String json = response.getBody();

	    if (json == "Thêm thất bại") {
	    	return false;
	    }

	    return true;
	}
	
	public Boolean updateDocument(DocumentItems documentItems) throws Exception {
		String apiURL = api+"/admin/api/document/update/" + documentItems.getId();;
		HttpEntity<DocumentItems> requestEntity = new HttpEntity<>(documentItems, appConfig.cookieStore().getHeaders());
		
		ResponseEntity<String> response = restTemplate.exchange(apiURL, HttpMethod.PUT, requestEntity, String.class);
		
		String json = response.getBody();
		
		if (json == "Cập nhật thất bại")
		{
			return false;
		}
		
		return true;
	}
	

	public List<DocumentItems> getForChoose() {
		String apiURL = api+"/admin/api/document/getAllInNewestKhoa";
		RequestEntity<?> requestEntity = new RequestEntity<>(appConfig.cookieStore().getHeaders(), HttpMethod.GET, URI.create(apiURL));
		ResponseEntity<String> response = restTemplate.exchange(requestEntity, String.class);
	    String json = response.getBody();

	    ObjectMapper objectMapper = new ObjectMapper();
	    List<DocumentItems> list;
		try {
			list = objectMapper.readValue(json, new TypeReference<List<DocumentItems>>() {});

		    return list;
		} catch (JsonMappingException e) {
			return null;
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			return null;
		}

	}
	
	public Boolean selectRequire(String listId) throws Exception {
		String apiURL = api+"/admin/api/document/selectRequire";
		appConfig.cookieStore().getHeaders().setContentType(MediaType.TEXT_PLAIN);
		HttpEntity<String> requestEntity = new HttpEntity<>(listId, appConfig.cookieStore().getHeaders());
		
		ResponseEntity<String> response = restTemplate.exchange(apiURL, HttpMethod.POST, requestEntity, String.class);
		
		String json = response.getBody();
		
		if (json != "OK")
		{
			return false;
		}
		
		return true;
	}
	public List<DocumentItems> getAllDocumentRequire() throws Exception {
		String apiURL = api+"/admin/api/document/getAllRequire";
		RequestEntity<?> requestEntity = new RequestEntity<>(appConfig.cookieStore().getHeaders(), HttpMethod.GET, URI.create(apiURL));
		ResponseEntity<String> response = restTemplate.exchange(requestEntity, String.class);
	    String json = response.getBody();

	    ObjectMapper objectMapper = new ObjectMapper();
	    List<DocumentItems> list = objectMapper.readValue(json, new TypeReference<List<DocumentItems>>() {});

	    return list;
	} 
	
	
	public List<DocumentItems> getforMethod(int methodsId) throws Exception {
	    List<DocumentItems> documentItems = getAll();
	    ArrayList<DocumentItems> list = new ArrayList<DocumentItems>();
	    for (DocumentItems documentItem : documentItems) {
	        if(documentItem.getMethod().getId()== methodsId && documentItem.getStatus()== true 
	        		&&documentItem.getMajors().getMajorsID().equals("7480201")) {
	            list.add(documentItem);
	        }
	    }

	    return list;
	}
//	---------------------------------statistical--------------------------
	
	 public Map<String, Integer> countStudentsByKhoa() throws Exception {
	        List<DocumentItems> DocumentItemsList = getAll();
	        Map<String, Integer> countByKhoa = new HashMap<>();
	        for (DocumentItems DocumentItemsList1 : DocumentItemsList) {
	            Khoa khoa = DocumentItemsList1.getKhoa();
//	            int soluong = DocumentItemsList1.getSoLuong();
	            if (khoa != null) {
	                String khoaName = khoa.getKhoa();
	                countByKhoa.put(khoaName, countByKhoa.getOrDefault(khoaName, 0) + 1);
	            }
	        }
	        return countByKhoa;
	    }
	 
	 public Map<String, Integer> calculateDocumentItemCount() throws Exception{
    	 List<DocumentItems> documentItemsList = getAll();
        Map<String, Integer> documentItemCountMap = new HashMap<>();
        int totalStudents = 0;

        for (DocumentItems documentItems : documentItemsList) {
            String documentType = documentItems.getDocumentType();
            int count = documentItems.getSoLuong();
            documentItemCountMap.put(documentType, documentItemCountMap.getOrDefault(documentType, 0) + count);
            totalStudents += count;
        }
        documentItemCountMap.put("Tổng số thí sinh", totalStudents);

        return documentItemCountMap;
    }
	
	 public Map<String, Integer> calculateDocumentCount() throws Exception{
    	 List<DocumentItems> documentItemsList = getAll();
        Map<String, Integer> documentItemCountMap = new HashMap<>();

        int totalStudents = 0;

        for (DocumentItems documentItems : documentItemsList) {
//        	if(documentItems.get)
            String documentType = documentItems.getDocumentType();
            int count = documentItems.getSoLuong();
            documentItemCountMap.put(documentType, documentItemCountMap.getOrDefault(documentType, 0) + count);
           
        }

        // Thêm tổng số thí sinh vào map

        return documentItemCountMap;
    }
	 //import file
	 public ResponseEntity<String> importDocumentItemsFromExcel(MultipartFile file) throws Exception {
	       
	            InputStream inputStream = file.getInputStream();
	            Workbook workbook = new XSSFWorkbook(inputStream);
	            Sheet sheet = workbook.getSheetAt(0);

	            for (Row row : sheet) {
	                if (row.getRowNum() == 0) {
	                    continue; // Skip header row
	                }

	                DocumentItems documentItem = new DocumentItems();
	                documentItem.setDocumentType(row.getCell(0).getStringCellValue());
	                String soluong= row.getCell(1).getStringCellValue();
	                documentItem.setNote(row.getCell(2).getStringCellValue());
	                String statuString = row.getCell(3).getStringCellValue();
	                documentItem.setStatus(Boolean.parseBoolean(statuString));
	                documentItem.setSoLuong(Integer.parseInt(soluong));
	               
	                documentItem.setKhoa(khoaService.getNewestKhoa());

	                String majorsId = row.getCell(4).getStringCellValue();
	                Majors majors = majorsService.getId(majorsId);
	                documentItem.setMajors(majors);

	                String methodId = row.getCell(5).getStringCellValue();
	                Method method = methodService.getByIdMethod(methodId);
	                documentItem.setMethod(method);
	                // Xử lý ràng buộc cho Khoa, Majors, Method ở đây

	                // Call your saveDocumentItem method to add the imported document item
	                if (!saveDocument(documentItem)) {
	                    return ResponseEntity.badRequest().body("Import failed");
	                }
	            }

	            workbook.close();
	            return ResponseEntity.ok("Import successful");
	        
	    }
}
