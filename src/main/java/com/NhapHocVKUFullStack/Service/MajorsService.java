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
import com.NhapHocVKUFullStack.models.Majors;
import com.NhapHocVKUFullStack.models.Religion;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
@Service
public class MajorsService {
//	private String apiURL = "http://localhost:2222/admin/api/majors";
//	private String apiURL = "http://222.255.130.100:7070/vku/admin/api/majors";
	private String apiURL = Utils.BASE_URL +"/admin/api/majors";

	private RestTemplate restTemplate = new RestTemplate();
	private AppConfig appConfig;
	private HttpHeaders headers = new HttpHeaders();
	@Autowired
	public MajorsService(AppConfig appConfig)
	{
		this.appConfig = appConfig;

	}
	public List<Majors> getDataFromAPI() throws JsonMappingException, JsonProcessingException {
	    RequestEntity<?> requestEntity = new RequestEntity<>(appConfig.cookieStore().getHeaders(), HttpMethod.GET, URI.create(apiURL));
	    ResponseEntity<String> response = restTemplate.exchange(requestEntity, String.class);
	    String json = response.getBody();

		    ObjectMapper objectMapper = new ObjectMapper();
		    List<Majors> listMajors = objectMapper.readValue(json, new TypeReference<List<Majors>>() {});

		    return listMajors;
	}
	public List<Majors> getDataFromAPIsttTrue() throws JsonMappingException, JsonProcessingException {
	   ArrayList<Majors>listMajors = new ArrayList<>();
	   for (Majors majors : getDataFromAPI()) {
		   if(majors.getStatus()==true) {
			   listMajors.add(majors);
		   }
	   }

		    return listMajors;
	}
	public String post(Majors majors) {
		String api = apiURL+"/create";
		headers.setContentType(MediaType.APPLICATION_JSON);
		RequestEntity<?> requestEntity = new RequestEntity<>(majors,appConfig.cookieStore().getHeaders(), HttpMethod.POST, URI.create(api));
		ResponseEntity<String> response = restTemplate.exchange(requestEntity, String.class);
	    return response.getBody();
	}
	
//	--------------------get và edit----------------------------
	public Majors getById(String id) throws Exception {
	    List<Majors> majors = getDataFromAPI();
	    Optional<Majors> optionalmajors = majors.stream()
	            .filter(religion -> religion.getId().equals(id))
	            .findFirst();
	    if (optionalmajors.isPresent()) {
	        return optionalmajors.get();
	    } else {
	        throw new Exception("Religion not found with ID: " + id);
	    }
	}
	public Majors getId(String id) throws Exception {
	    List<Majors> majors = getDataFromAPI();
	    Optional<Majors> optionalmajors = majors.stream()
	            .filter(religion -> religion.getMajorsID().equals(id))
	            .findFirst();
	    if (optionalmajors.isPresent()) {
	        return optionalmajors.get();
	    } else {
	        throw new Exception("Religion not found with ID: " + id);
	    }
	}

	
	public ResponseEntity<String> callApi(Majors majors, String id) {
		String api = apiURL+"/edit/" + id;
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<Majors> requestEntity = new HttpEntity<>(majors, appConfig.cookieStore().getHeaders());
        ResponseEntity<String> responseEntity = restTemplate.exchange(api, HttpMethod.PUT, requestEntity, String.class);
        return responseEntity;
    }
	
	public Majors getByIdMajor(String id) throws Exception, JsonProcessingException 
	{
		String api = apiURL + "/getByIdMajors?idMajor=" + id;
		RequestEntity<?> requestEntity = new RequestEntity<>(appConfig.cookieStore().getHeaders(), HttpMethod.GET, URI.create(api));
		ResponseEntity<Majors> response = restTemplate.exchange(requestEntity, Majors.class);
	    Majors major = response.getBody();
	    return major;
	}
	
	
}
