package com.NhapHocVKUFullStack.Service;


import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.RestTemplate;

import com.NhapHocVKUFullStack.Utils.Utils;
import com.NhapHocVKUFullStack.config.AppConfig;
import com.NhapHocVKUFullStack.dtos.ContentDTO;
import com.NhapHocVKUFullStack.models.City;
import com.NhapHocVKUFullStack.models.Content;
import com.NhapHocVKUFullStack.models.TuitionFeeList;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class ContentService {

	private String apiURL = Utils.BASE_URL+"/admin/api/content";

	private RestTemplate restTemplate = new RestTemplate();
	private AppConfig appConfig;
	private HttpHeaders headers = new HttpHeaders();
	@Autowired
	public ContentService(AppConfig appConfig)
	{
		this.appConfig = appConfig;
		}
	public List<Content> getDataFromAPI() throws JsonMappingException, JsonProcessingException {
	    RequestEntity<?> requestEntity = new RequestEntity<>(appConfig.cookieStore().getHeaders(), HttpMethod.GET, URI.create(apiURL));
	    System.out.println(requestEntity);
	    ResponseEntity<String> response = restTemplate.exchange(requestEntity, String.class);
	    String json = response.getBody();
	    
		    ObjectMapper objectMapper = new ObjectMapper();
		    List<Content> listContent = objectMapper.readValue(json, new TypeReference<List<Content>>() {});

		    return listContent;
	}
//	public String post(Content content){
//		RestTemplate rest = new RestTemplate();	
//khi api trả về 1 đối tượng
//		ResponseEntity<String> response = rest.postForEntity("http://222.255.130.100:7070/vku/admin/api/content/create", content, Content.class);
//		return response.getBody();;
//	}
	public String post(Content content) {
	    String api = apiURL+"/create";
	    headers.setContentType(MediaType.APPLICATION_JSON);
	    RequestEntity<?> requestEntity = new RequestEntity<>(content,appConfig.cookieStore().getHeaders(), HttpMethod.POST, URI.create(api));
		ResponseEntity<String> response = restTemplate.exchange(requestEntity, String.class);
	   
	    return response.getBody();
	}
	
	public Content getById(int id) throws Exception {
		for(Content cntContent : getDataFromAPI() ) {
			if(cntContent.getId()==id)
				return cntContent;
		}
		return null;
	}
	
	  public ResponseEntity<String> callApi(Content content, int id) {
		  String api = apiURL+"/edit/" + id;
		  headers.setContentType(MediaType.APPLICATION_JSON);
	        HttpEntity<Content> requestEntity = new HttpEntity<>(content, appConfig.cookieStore().getHeaders());
	        ResponseEntity<String> responseEntity = restTemplate.exchange(api, HttpMethod.PUT, requestEntity, String.class);
	        return responseEntity;
	    }
	  
		public List<Content> searchDataFromAPI(String keyword) throws JsonMappingException, JsonProcessingException {
			String api= apiURL+"/search?keyword="+keyword;
		    RequestEntity<?> requestEntity = new RequestEntity<>(appConfig.cookieStore().getHeaders(), HttpMethod.GET, URI.create(api));
		    ResponseEntity<String> response = restTemplate.exchange(requestEntity, String.class);
		    String json = response.getBody();

			    ObjectMapper objectMapper = new ObjectMapper();
			    List<Content> listContent = objectMapper.readValue(json, new TypeReference<List<Content>>() {});

			    return listContent;
		}
	}
	
	
	
