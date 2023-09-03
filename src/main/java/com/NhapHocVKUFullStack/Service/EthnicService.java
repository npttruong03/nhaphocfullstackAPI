package com.NhapHocVKUFullStack.Service;

import java.net.URI;
import java.sql.Timestamp;
import java.util.List;

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
import com.NhapHocVKUFullStack.models.Ethnic;
import com.NhapHocVKUFullStack.models.Student;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class EthnicService {
	private AppConfig appConfig;
//	private final static String apiURL = "http://222.255.130.100:7070/vku/api/";
	private final static String apiURL = Utils.BASE_URL + "/admin/api/ethnic";
	RestTemplate restTemplate = new RestTemplate();
//	private final static String apiURL = "http://222.255.130.100:7070/vku/api/";

	@Autowired
	public EthnicService(AppConfig appConfig) {
		this.appConfig = appConfig;
	}

	public List<Ethnic> getDataFromAPI() throws JsonMappingException, JsonProcessingException {
		RequestEntity<?> requestEntity = new RequestEntity<>(appConfig.cookieStore().getHeaders(), HttpMethod.GET,
				URI.create(apiURL));
		 ResponseEntity<String> response = restTemplate.exchange(requestEntity, String.class);
		    String json = response.getBody();
		    
		    ObjectMapper objectMapper = new ObjectMapper();
		    List<Ethnic> listEthnic = objectMapper.readValue(json, new TypeReference<List<Ethnic>>() {});

		    return listEthnic;
	}

	public Ethnic getById(Integer id) throws Exception {
		for(Ethnic cntEthnic : getDataFromAPI() ) {
			if(cntEthnic.getId()==id)
				return cntEthnic;
		}
		return null;
	}

	public String addEthnic(Ethnic ethnic) {
//	    ResponseEntity<String> response = restTemplate.postForEntity(apiURL+"/create", ethnic, String.class);
//	    return response.getBody();
		HttpEntity<Ethnic> requestEntity = new HttpEntity<>(ethnic, appConfig.cookieStore().getHeaders());
		ResponseEntity<String> response = restTemplate.exchange(apiURL+ "/create", HttpMethod.POST, requestEntity, String.class);
		return response.getBody();
	}

	public String updateEthnic(Ethnic ethnic) {
		RequestEntity<?> requestEntity = new RequestEntity<>(ethnic, appConfig.cookieStore().getHeaders(), HttpMethod.PUT, URI.create(apiURL + "/edit/" + ethnic.getId()));
		ResponseEntity<String> response = restTemplate.exchange(requestEntity, String.class);

//		return response.getBody();
	    return response.getBody();
	}
}
