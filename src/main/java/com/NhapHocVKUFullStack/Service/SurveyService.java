package com.NhapHocVKUFullStack.Service;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.poi.ss.formula.functions.IfFunc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.NhapHocVKUFullStack.Utils.Utils;
import com.NhapHocVKUFullStack.config.AppConfig;
import com.NhapHocVKUFullStack.models.Majors;
import com.NhapHocVKUFullStack.models.Survey;
import com.NhapHocVKUFullStack.models.TuitionFeeList;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class SurveyService {
	private String apiURL = Utils.BASE_URL + "/admin/api/survey";

	private RestTemplate restTemplate = new RestTemplate();
	private AppConfig appConfig;
	private HttpHeaders headers = new HttpHeaders();

	
	@Autowired
	public SurveyService(AppConfig appConfig)
	{
		this.appConfig = appConfig;
	}
	
	public List<Survey> getDataFromApi() throws Exception{

//		RequestEntity<?> requestEntity = new RequestEntity<>(headers, HttpMethod.GET, URI.create(apiURL));

	    RequestEntity<?> requestEntity = new RequestEntity<>(appConfig.cookieStore().getHeaders(), HttpMethod.GET, URI.create(apiURL));

	    ResponseEntity<String> response = restTemplate.exchange(requestEntity, String.class);
	    String json = response.getBody();
	    ObjectMapper objectMapper = new ObjectMapper();
	    List<Survey> list = objectMapper.readValue(json, new TypeReference<List<Survey>>() {});
	return list;
	
	}
	
	public List<Survey> getDataStt()throws Exception{
		ArrayList<Survey> list = new ArrayList<>();
		for (Survey survey : getDataFromApi()) {
			if(survey.isStatus()==true)
				list.add(survey);
		}
		return list;
	}
	
	public List<Survey> searchDataFromAPI(Integer khoaId,
    		String majorsId) throws Exception{
		String api=apiURL+"/searchByKey?khoaId="+khoaId+"&majorsId="
            +majorsId;
	    RequestEntity<?> requestEntity = new RequestEntity<>(appConfig.cookieStore().getHeaders(), HttpMethod.GET, URI.create(api));
	    ResponseEntity<String> response = restTemplate.exchange(requestEntity, String.class);
	    String json = response.getBody();
		
	    ObjectMapper objectMapper = new ObjectMapper();
	    List<Survey> list = objectMapper.readValue(json, new TypeReference<List<Survey>>() {});
	return list;
	}
	public List<Survey> searchDataFromAPISTTtrue(Integer khoaId,
    		String majorsId) throws Exception{
		ArrayList<Survey> list = new ArrayList<>();
		for (Survey Survey : searchDataFromAPI(khoaId,majorsId)) {
			if(Survey.isStatus()==true) {
				list.add(Survey);
			}
		}
	return list;
	}
	


	
	public Survey getById(int id) throws Exception {
		
		for(Survey Survey : getDataFromApi() ) {
			if(Survey.getId()==id) {
				System.out.println(Survey.toString());
				return Survey;
				}
		}
		return null;
	}

	public ResponseEntity<Survey> post(Survey Survey) {
		System.out.println(Survey);
	    String api = apiURL+"/create";
	    System.out.println(appConfig.cookieStore().getHeaders());
//		headers.setContentType(MediaType.APPLICATION_JSON);
		RequestEntity<?> requestEntity = new RequestEntity<>(Survey,appConfig.cookieStore().getHeaders(), HttpMethod.POST, URI.create(api));
		ResponseEntity<Survey> response = restTemplate.exchange(requestEntity, Survey.class);
	    return response;
	}
	
	  public ResponseEntity<String> Update(Survey Survey) {
	        String api = apiURL+"/edit/" + Survey.getId();
			headers.setContentType(MediaType.APPLICATION_JSON);
	        HttpEntity<Survey> requestEntity = new HttpEntity<>(Survey, appConfig.cookieStore().getHeaders());
	        ResponseEntity<String> responseEntity = restTemplate.exchange(api, HttpMethod.PUT, requestEntity, String.class);
	        return responseEntity;
	    }
	  public ResponseEntity<String> Update(Survey Survey,int id) {
	        String api = apiURL+"/edit/" + id;
			headers.setContentType(MediaType.APPLICATION_JSON);
	        HttpEntity<Survey> requestEntity = new HttpEntity<>(Survey, appConfig.cookieStore().getHeaders());
	        ResponseEntity<String> responseEntity = restTemplate.exchange(api, HttpMethod.PUT, requestEntity, String.class);
	        return responseEntity;
	    }

	  //updatedata
	  public ResponseEntity<String> UpdatesttFalse() {
	        String api = apiURL+"/editStatus";
			headers.setContentType(MediaType.APPLICATION_JSON);
	        HttpEntity<Survey> requestEntity = new HttpEntity<>(appConfig.cookieStore().getHeaders());
	        ResponseEntity<String> responseEntity = restTemplate.exchange(api, HttpMethod.PUT, requestEntity, String.class);
	        return responseEntity;
	    }
	  
	  public List<Integer> ids(String name, String url) throws Exception{
		  List<Survey> surveys = getDataStt();
		  ArrayList<Integer> list = new ArrayList<>();
		  for (Survey survey : surveys) {
			if(survey.getShared()==1) {
				if(survey.getUrl().equals(url)&&survey.getName().equals(name)) {
					list.add(survey.getId());
				}
			}
		}
		  return list;
	  }
		public Majors getMajorsById(int id) throws Exception {
			
			for(Survey Survey : getDataFromApi() ) {
				if(Survey.getId()==id) {
					System.out.println(Survey.getMajors());
					return Survey.getMajors();
					}
			}
			return null;
		}
	  
	  


	
}
