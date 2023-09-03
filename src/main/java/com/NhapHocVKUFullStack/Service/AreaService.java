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
import org.springframework.web.client.RestTemplate;

import com.NhapHocVKUFullStack.Utils.Utils;
import com.NhapHocVKUFullStack.config.AppConfig;
import com.NhapHocVKUFullStack.models.Area;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpSession;

@Service
public class AreaService {
	private AppConfig appConfig;
	private RestTemplate restTemplate = new RestTemplate();
	@Autowired
	public AreaService(AppConfig appConfig)
	{
		this.appConfig = appConfig;
	}
	public List<Area> getAll() throws Exception {


//		String apiURL = "http://222.255.130.100:7070/vku/admin/api/area/getAll";
		String apiURL = "http://222.255.130.100:7070/vku/admin/api/area/getAll";
//		String apiURL = "http://222.255.130.100/vku/admin/api/area/getAll";

		RequestEntity<?> requestEntity = new RequestEntity<>(appConfig.cookieStore().getHeaders(), HttpMethod.GET, URI.create(apiURL));
		ResponseEntity<String> response = restTemplate.exchange(requestEntity, String.class);
	    String json = response.getBody();
	    
	    ObjectMapper objectMapper = new ObjectMapper();
	    List<Area> listObject = objectMapper.readValue(json, new TypeReference<List<Area>>() {});
	    return listObject;
	}
	
	public Area getArea(int id) throws Exception {
		String apiURL = "http://222.255.130.100:7070/vku/admin/api/area/get/" + id;
		RequestEntity<?> requestEntity = new RequestEntity<>(appConfig.cookieStore().getHeaders(), HttpMethod.GET, URI.create(apiURL));
		ResponseEntity<Area> response = restTemplate.exchange(requestEntity, Area.class);
		Area area = response.getBody();

	    return area;
	}

	public Boolean saveArea(Area area) throws Exception {
		String apiURL = "http://222.255.130.100:7070/vku/admin/api/area/save";
		RequestEntity<?> requestEntity = new RequestEntity<>(area, appConfig.cookieStore().getHeaders(), HttpMethod.POST, URI.create(apiURL));
		ResponseEntity<String> response = restTemplate.exchange(requestEntity, String.class);
		String json = response.getBody();

	    if (json == "Thêm thất bại") {
	    	return false;
	    }

	    return true;
	}
	
	public Boolean updateArea(Area area) throws Exception {
		String api = "http://222.255.130.100:7070/vku/admin/api/area/update/" + area.getId();
		String apiURL = api+"update/" + area.getId();
		RequestEntity<Area> requestEntity = new RequestEntity<>(area, appConfig.cookieStore().getHeaders(), HttpMethod.PUT, URI.create(apiURL));
		
		ResponseEntity<String> response = restTemplate.exchange(requestEntity, String.class);
		
		String json = response.getBody();
		
		if (json == "Cập nhật thất bại")
		{
			return false;
		}
		
		return true;
	}
}
