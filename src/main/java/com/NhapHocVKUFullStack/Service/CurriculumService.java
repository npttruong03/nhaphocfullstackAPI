package com.NhapHocVKUFullStack.Service;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

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

import com.NhapHocVKUFullStack.Utils.Utils;
import com.NhapHocVKUFullStack.config.AppConfig;
import com.NhapHocVKUFullStack.models.Curriculum;
import com.NhapHocVKUFullStack.models.Student;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.apache.http.HttpStatus;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;

@Service
public class CurriculumService {
	private String api = Utils.BASE_URL;

	private AppConfig appConfig;
	@Autowired
	public CurriculumService(AppConfig appConfig)
	{
		this.appConfig = appConfig;
	}
	private RestTemplate restTemplate = new RestTemplate();
	public List<Curriculum> getAll() throws Exception {
		String apiURL = api+"/admin/api/curry/getAll";
		HttpHeaders headers = appConfig.cookieStore().getHeaders();
		RequestEntity<?> requestEntity = new RequestEntity<>(headers, HttpMethod.GET, URI.create(apiURL));
		ResponseEntity<String> response = restTemplate.exchange(requestEntity, String.class);
	    String json = response.getBody();

	    ObjectMapper objectMapper = new ObjectMapper();
	    List<Curriculum> list = objectMapper.readValue(json, new TypeReference<List<Curriculum>>() {});

	    return list;
	}
	
	public List<Curriculum> getAllSttTrue() throws Exception {
		ArrayList<Curriculum> list = new ArrayList<>();
		for (Curriculum curriculum : getAll()) {
			if(curriculum.getStatus()==true) {
				list.add(curriculum);
			}
		}

	    return list;
	}
	
	public Curriculum getCurry(int id) throws Exception {
		String apiURL = api+"/admin/api/curry/get/" + id;
		HttpHeaders headers = appConfig.cookieStore().getHeaders();
		RequestEntity<?> requestEntity = new RequestEntity<>(headers, HttpMethod.GET, URI.create(apiURL));
		ResponseEntity<Curriculum> response = restTemplate.exchange(requestEntity, Curriculum.class);
		Curriculum curry = response.getBody();

	    return curry;
	}

	public Boolean saveCurry(Curriculum curry) {
		String apiURL = api+"/admin/api/curry/save";
		HttpHeaders headers = appConfig.cookieStore().getHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		RequestEntity<?> requestEntity = new RequestEntity<>(curry, headers, HttpMethod.POST, URI.create(apiURL));
		ResponseEntity<String> response = restTemplate.exchange(requestEntity, String.class);
		String json = response.getBody();

	    if (json == "Thêm thất bại") {
	    	return false;
	    }

	    return true;
	}
	
	public Boolean updateCurry(Curriculum curry) throws Exception {
		String apiURL = api+"/admin/api/curry/update/" + curry.getId();;
		HttpHeaders headers = appConfig.cookieStore().getHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		RequestEntity<?> requestEntity = new RequestEntity<>(curry, headers, HttpMethod.PUT, URI.create(apiURL));
		ResponseEntity<String> response = restTemplate.exchange(requestEntity, String.class);
		
		String json = response.getBody();
		
		if (json == "Cập nhật thất bại")
		{
			return false;
		}
		
		return true;
	}
	
	public List<Curriculum> searchCurry(int idKhoa, String idNganh) throws JsonMappingException, JsonProcessingException {
		String apiURL = api+ "/admin/api/curry/search?idKhoa=" + idKhoa + "&idNganh=" + idNganh;
		HttpHeaders headers = appConfig.cookieStore().getHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		RequestEntity<?> requestEntity = new RequestEntity<>(headers, HttpMethod.GET, URI.create(apiURL));
		ResponseEntity<String> response = restTemplate.exchange(requestEntity, String.class);
		
		String json = response.getBody();

	    ObjectMapper objectMapper = new ObjectMapper();
	    List<Curriculum> list = objectMapper.readValue(json, new TypeReference<List<Curriculum>>() {});
	    return list;
	}

	public List<Curriculum> sttCurriculums() throws Exception {
		ArrayList<Curriculum> list = new ArrayList<>();
		for (Curriculum curriculum : getAll()) {
			if(curriculum.getStatus()== true && curriculum.getHienthi()== 1)
				list.add(curriculum);
		}
	    return list;
	}
}
