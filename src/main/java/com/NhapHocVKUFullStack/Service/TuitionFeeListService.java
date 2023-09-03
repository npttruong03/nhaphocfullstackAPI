package com.NhapHocVKUFullStack.Service;

import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.NhapHocVKUFullStack.Utils.Utils;
import com.NhapHocVKUFullStack.config.AppConfig;
import com.NhapHocVKUFullStack.models.Content;
import com.NhapHocVKUFullStack.models.Curriculum;
import com.NhapHocVKUFullStack.models.DocumentItems;
import com.NhapHocVKUFullStack.models.Khoa;
import com.NhapHocVKUFullStack.models.Majors;
import com.NhapHocVKUFullStack.models.Method;
import com.NhapHocVKUFullStack.models.TuitionFeeList;
import com.NhapHocVKUFullStack.models.TuitionFeeList;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class TuitionFeeListService {
	   @Autowired
	    private KhoaService khoaService;
	   @Autowired
	    private CurriculumService curriculumService;

	    @Autowired
	    private MajorsService majorsService;

	//private String apiURL = "http://222.255.130.100:7070/vku/admin/api/tuitionFeeList";

//	private String apiURL = Utils.LOCAL_STRING+"/admin/api/tuitionFeeList";


//	private String apiURL = "http://localhost:2222/admin/api/tuitionFeeList";
	private String apiURL = Utils.BASE_URL + "/admin/api/tuitionFeeList";

	private RestTemplate restTemplate = new RestTemplate();
	private AppConfig appConfig;
	private HttpHeaders headers = new HttpHeaders();

	
	@Autowired
	public TuitionFeeListService(AppConfig appConfig)
	{
		this.appConfig = appConfig;
	}
	
	public List<TuitionFeeList> getDataFromApi() throws Exception{

//		RequestEntity<?> requestEntity = new RequestEntity<>(headers, HttpMethod.GET, URI.create(apiURL));

	    RequestEntity<?> requestEntity = new RequestEntity<>(appConfig.cookieStore().getHeaders(), HttpMethod.GET, URI.create(apiURL));

	    ResponseEntity<String> response = restTemplate.exchange(requestEntity, String.class);
	    String json = response.getBody();
	    ObjectMapper objectMapper = new ObjectMapper();
	    List<TuitionFeeList> list = objectMapper.readValue(json, new TypeReference<List<TuitionFeeList>>() {});
	return list;
	
	}
	public List<TuitionFeeList> getDataStt()throws Exception{
		ArrayList<TuitionFeeList> list = new ArrayList<>();
		for (TuitionFeeList tuition : getDataFromApi()) {
			if(tuition.getStatus()==true)
				list.add(tuition);
		}
		return list;
	}
	
	public List<TuitionFeeList> searchDataFromAPI(Integer khoaId,
    		String majorsId,
            Integer curriculumId) throws Exception{
		String api=apiURL+"/searchByKey?khoaId="+khoaId+"&majorsId="
            +majorsId+"&curriculumId="+curriculumId;
	    RequestEntity<?> requestEntity = new RequestEntity<>(appConfig.cookieStore().getHeaders(), HttpMethod.GET, URI.create(api));
	    ResponseEntity<String> response = restTemplate.exchange(requestEntity, String.class);
	    String json = response.getBody();
		
	    ObjectMapper objectMapper = new ObjectMapper();
	    List<TuitionFeeList> list = objectMapper.readValue(json, new TypeReference<List<TuitionFeeList>>() {});
	return list;
	}
	public List<TuitionFeeList> searchDataFromAPISTTtrue(Integer khoaId,
    		String majorsId,
            Integer curriculumId) throws Exception{
		ArrayList<TuitionFeeList> list = new ArrayList<>();
		for (TuitionFeeList tuitionFeeList : searchDataFromAPI(khoaId,majorsId,curriculumId)) {
			if(tuitionFeeList.getStatus()==true) {
				list.add(tuitionFeeList);
			}
		}
	return list;
	}
	
	public List<TuitionFeeList> searchTuitionListByKhoaIdAndMajorsId(Integer khoaId, String majorsId) throws Exception {
		String api = apiURL + "/searchByKey?khoaId=" + khoaId + "&majorsId=" + majorsId;
		RequestEntity<?> requestEntity = new RequestEntity<>(appConfig.cookieStore().getHeaders(), HttpMethod.GET, URI.create(api));
		ResponseEntity<String> response = restTemplate.exchange(requestEntity, String.class);
		String json = response.getBody();

		ObjectMapper objectMapper = new ObjectMapper();
		List<TuitionFeeList> list = objectMapper.readValue(json, new TypeReference<List<TuitionFeeList>>() {
		});
		return list;
	}
	
	public TuitionFeeList getById(int id) throws Exception {
		for(TuitionFeeList tuitionFeeList : getDataFromApi() ) {
			if(tuitionFeeList.getId()==id) {
//				System.out.println(tuitionFeeList.toString());
				return tuitionFeeList;
				}
		}

		return null;
	}
	public TuitionFeeList getByIdinMajor(int id, int khoa, String majors) throws Exception {

	
		for(TuitionFeeList tuitionFeeList : searchTuitionListByKhoaIdAndMajorsId(khoa, majors) ) {

			if(tuitionFeeList.getId()==id) {
				return tuitionFeeList;
				}

			
		}

		return null;
	}

	public ResponseEntity<TuitionFeeList> post(TuitionFeeList tuitionFeeList) {
//		System.out.println(tuitionFeeList);
	    String api = apiURL+"/create";
//	    System.out.println(appConfig.cookieStore().getHeaders());
//		headers.setContentType(MediaType.APPLICATION_JSON);
		RequestEntity<?> requestEntity = new RequestEntity<>(tuitionFeeList,appConfig.cookieStore().getHeaders(), HttpMethod.POST, URI.create(api));
		ResponseEntity<TuitionFeeList> response = restTemplate.exchange(requestEntity, TuitionFeeList.class);
	    return response;
	}
	
	  public ResponseEntity<String> Update(TuitionFeeList tuitionFeeList, int id) {
	        String api = apiURL+"/edit/" + id;
			headers.setContentType(MediaType.APPLICATION_JSON);
	        HttpEntity<TuitionFeeList> requestEntity = new HttpEntity<>(tuitionFeeList, appConfig.cookieStore().getHeaders());
	        ResponseEntity<String> responseEntity = restTemplate.exchange(api, HttpMethod.PUT, requestEntity, String.class);
	        return responseEntity;
	    }
	  
	  public List<TuitionFeeList> getDataSttTrue() throws Exception{
		    RequestEntity<?> requestEntity = new RequestEntity<>(appConfig.cookieStore().getHeaders(), HttpMethod.GET, URI.create(apiURL+"/searchByStatus"));
		    ResponseEntity<String> response = restTemplate.exchange(requestEntity, String.class);
		    String json = response.getBody();
		    ObjectMapper objectMapper = new ObjectMapper();
		    List<TuitionFeeList> list = objectMapper.readValue(json, new TypeReference<List<TuitionFeeList>>() {});
		return list;
		}
	  
	  public List<TuitionFeeList> getTuitionUpdateStt() throws Exception{
		  ArrayList<TuitionFeeList> sTuitionFeeLists = new ArrayList<>();
		  for (TuitionFeeList tuitionFeeList : getDataFromApi()) {
			if(getDataSttTrue().contains(tuitionFeeList)) {
				
			}else {
				sTuitionFeeLists.add(tuitionFeeList);
			}
		}
			return sTuitionFeeLists;
	  }
	  
	  public ResponseEntity<String> UpdateSTT(List<Integer> id) {
		  String apiUrl = apiURL+"/editStatus/";
		    String idString = id.stream()
		                         .map(String::valueOf)
		                         .collect(Collectors.joining(","));

		    String api = apiUrl + idString;
	        System.out.println(api);
			headers.setContentType(MediaType.APPLICATION_JSON);
	        HttpEntity<TuitionFeeList> requestEntity = new HttpEntity<>(appConfig.cookieStore().getHeaders());
	        ResponseEntity<String> responseEntity = restTemplate.exchange(api, HttpMethod.PUT, requestEntity, String.class);
	        return responseEntity;
	    }
	  
	  public ResponseEntity<String> UpdatesttFalse() {
	        String api = apiURL+"/editStatus";
			headers.setContentType(MediaType.APPLICATION_JSON);
	        HttpEntity<TuitionFeeList> requestEntity = new HttpEntity<>(appConfig.cookieStore().getHeaders());
	        ResponseEntity<String> responseEntity = restTemplate.exchange(api, HttpMethod.PUT, requestEntity, String.class);
	        return responseEntity;
	    }
//	  public void updateStatus(List<Integer> ids) {
//	        String url = "http://222.255.130.100:7070/vku/admin/api/tuitionFeeList/editStatus/{ids}";
//	        restTemplate.put(url, null, ids.toArray());
//	    }
	 
	  public Map<String, Integer> calculateTuitionByType() throws Exception {
		  List<TuitionFeeList> tuitionFeeList = getDataFromApi();
	        Map<String, Integer> tuitionByType = new HashMap<>();

	        for (TuitionFeeList tuitionFee : tuitionFeeList) {
	        	if(tuitionFee.getStatus() != null) {
	            String type = tuitionFee.getKhoa().getKhoa(); // Assuming the 'Khoa' class has a 'getName()' method.
	            int tuitionAmount = tuitionFee.getTuition();
	            tuitionByType.put(type, tuitionByType.getOrDefault(type, 0) + tuitionAmount);
	        }
	        }
	        return tuitionByType;
	    }
	  
	  public Map<String, Integer> calculateTuitionByTypenganh() throws Exception {
		  List<TuitionFeeList> tuitionFeeList = getDataFromApi();
	        Map<String, Integer> tuitionByType = new HashMap<>();

	        for (TuitionFeeList tuitionFee : tuitionFeeList) {
	        if(tuitionFee.getStatus() != false ) {
	            String type = tuitionFee.getMajors().getMajorsName();;
	            // Assuming the 'Khoa' class has a 'getName()' method.
	            int tuitionAmount = tuitionFee.getTuition();
	            tuitionByType.put(type, tuitionByType.getOrDefault(type, 0) + tuitionAmount);
	        }
	        }
	        return tuitionByType;
	    }
	  
	  public Map<String, Integer> countTuitionNames() throws Exception {
		    List<TuitionFeeList> tuitions = getDataFromApi();
		    Map<String, Integer> tuitionNameCount = new HashMap<>();

		    for (TuitionFeeList tuition : tuitions) {
		        Boolean status = tuition.getStatus();
		        if (status != null && status) {
		            String tuitionName = tuition.getName();

		            // Increment the count for the current tuition name
		            tuitionNameCount.put(tuitionName, tuitionNameCount.getOrDefault(tuitionName, 0) + 1);
		        }
		    }

		    return tuitionNameCount;
		}
	  
	  public ResponseEntity<String> importTuitionFeelistFromExcel(MultipartFile file) throws Exception {
	       
          InputStream inputStream = file.getInputStream();
          Workbook workbook = new XSSFWorkbook(inputStream);
          Sheet sheet = workbook.getSheetAt(0);
          for (Row row : sheet) {
              if (row.getRowNum() == 0) {
                  continue; // Skip header row
              }

              TuitionFeeList tuitionFeeList = new TuitionFeeList();
              tuitionFeeList.setName(row.getCell(0).getStringCellValue());
              
              String hocphi= row.getCell(1).getStringCellValue();
              tuitionFeeList.setTuition(Integer.parseInt(hocphi));
              
              
              String majorsId = row.getCell(2).getStringCellValue();
              String curryid = row.getCell(3).getStringCellValue();
              Curriculum curry = curriculumService.getCurry(Integer.parseInt(curryid));
              tuitionFeeList.setCurriculum(curry);

              Majors majors = majorsService.getId(majorsId);
              tuitionFeeList.setMajors(majors);
             
              tuitionFeeList.setKhoa(khoaService.getNewestKhoa());
              

              tuitionFeeList.setStatus(false);
              post(tuitionFeeList);
          }

          workbook.close();
          return ResponseEntity.ok("Import successful");
      
  }



}
