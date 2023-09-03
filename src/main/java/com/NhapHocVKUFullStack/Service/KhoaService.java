package com.NhapHocVKUFullStack.Service;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
import com.NhapHocVKUFullStack.models.Content;
import com.NhapHocVKUFullStack.models.Khoa;
import com.NhapHocVKUFullStack.models.Religion;
import com.NhapHocVKUFullStack.models.TuitionFeeList;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class KhoaService {

//	private String apiURL = "http://222.255.130.100:7070/vku/admin/api/khoa";

	private String apiURL = Utils.BASE_URL+"/admin/api/khoa";
//	private String apiURL = Utils.LOCAL_STRING+"/admin/api/khoa";

	private RestTemplate restTemplate = new RestTemplate();
	private AppConfig appConfig;
	private HttpHeaders headers = new HttpHeaders();
	@Autowired
	public KhoaService(AppConfig appConfig)
	{
		this.appConfig = appConfig;
	}
	public List<Khoa> getDataFromAPI() throws JsonMappingException, JsonProcessingException {
		 RequestEntity<?> requestEntity = new RequestEntity<>(appConfig.cookieStore().getHeaders(), HttpMethod.GET, URI.create(apiURL));
		    ResponseEntity<String> response = restTemplate.exchange(requestEntity, String.class);
		    String json = response.getBody();
		    ObjectMapper objectMapper = new ObjectMapper();
		    List<Khoa> listKhoa = objectMapper.readValue(json, new TypeReference<List<Khoa>>() {});

		    return listKhoa;
	}
	public List<Khoa> getDatasttKhoatrue() throws JsonMappingException, JsonProcessingException {

	    RequestEntity<?> requestEntity = new RequestEntity<>(appConfig.cookieStore().getHeaders(), HttpMethod.GET, URI.create(apiURL));

		headers.add("Cookie", "nhaphocvku=" + appConfig.cookieStore().getCookie());
	    System.out.println("reuqest ben khoa: " + requestEntity);

	    ResponseEntity<String> response = restTemplate.exchange(requestEntity, String.class);
	    String json = response.getBody();
		    ObjectMapper objectMapper = new ObjectMapper();
		    List<Khoa> listKhoa = objectMapper.readValue(json, new TypeReference<List<Khoa>>() {});
		    ArrayList<Khoa> liststtKhoatrue = new ArrayList();
		    for (Khoa khoa : listKhoa) {
				if(khoa.isStatus()==true)
					liststtKhoatrue.add(khoa);
			}
		    return liststtKhoatrue;
	}

	public String post(Khoa khoa) {
	    String api = apiURL+"/create";
		headers.setContentType(MediaType.APPLICATION_JSON);
	    RequestEntity<?> requestEntity = new RequestEntity<>(khoa,appConfig.cookieStore().getHeaders(), HttpMethod.POST, URI.create(api));
		ResponseEntity<String> response = restTemplate.exchange(requestEntity, String.class);
	     return response.getBody();
	}
	
	public Khoa getById(int id) throws Exception {
		for(Khoa Khoa : getDataFromAPI() ) {
			if(Khoa.getId()==id)
				return Khoa;
		}
		return null;
	}

	 public ResponseEntity<String> callApi(Khoa khoa, int id) {
	        String apiUrl = apiURL+"/edit/" + id;
			headers.setContentType(MediaType.APPLICATION_JSON);
	        HttpEntity<Khoa> requestEntity = new HttpEntity<>(khoa, appConfig.cookieStore().getHeaders());
	        ResponseEntity<String> responseEntity = restTemplate.exchange(apiUrl, HttpMethod.PUT, requestEntity, String.class);
	        return responseEntity;
	    }
	 
	public List<Khoa> searchDataFromAPI(String keyword) throws JsonMappingException, JsonProcessingException {
		String api = apiURL+"/search?keyword="+keyword;
	    RequestEntity<?> requestEntity = new RequestEntity<>(appConfig.cookieStore().getHeaders(), HttpMethod.GET, URI.create(api));
	    ResponseEntity<String> response = restTemplate.exchange(requestEntity, String.class);
	    String json = response.getBody();

		    ObjectMapper objectMapper = new ObjectMapper();
		    List<Khoa> listKhoa = objectMapper.readValue(json, new TypeReference<List<Khoa>>() {});

		    return listKhoa;
	}

	
	public Khoa getNewestKhoa() throws Exception, JsonProcessingException {
		String api = apiURL+"/getNewest";
	    RequestEntity<?> requestEntity = new RequestEntity<>(appConfig.cookieStore().getHeaders(), HttpMethod.GET, URI.create(api));
	    ResponseEntity<String> response = restTemplate.exchange(requestEntity, String.class);
	    String json = response.getBody();
	    ObjectMapper objectMapper = new ObjectMapper();
	    Khoa khoa = objectMapper.readValue(json, new TypeReference<Khoa>() {});
	    return khoa;
	}
	
	
	
}