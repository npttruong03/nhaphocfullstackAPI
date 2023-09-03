package com.NhapHocVKUFullStack.Service;

import java.io.IOException;
import java.net.URI;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.NhapHocVKUFullStack.config.AppConfig;
import com.NhapHocVKUFullStack.models.City;
import com.NhapHocVKUFullStack.models.Index;
import com.NhapHocVKUFullStack.models.Student;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
@Service
public class IndexService {
	private AppConfig appConfig;
	private RestTemplate restTemplate = new RestTemplate();
	@Autowired
	public IndexService(AppConfig appConfig) {
		this.appConfig = appConfig;
	}
	public static List<Index> getDataFromAPI() throws JsonMappingException, JsonProcessingException {
		String apiURL = "https://provinces.open-api.vn/api/?depth=1";
		RestTemplate restTemplate = new RestTemplate();
		 ResponseEntity<String> response = restTemplate.getForEntity(apiURL, String.class);
		    String json = response.getBody();

		    ObjectMapper objectMapper = new ObjectMapper();
		    List<Index> listIndex = objectMapper.readValue(json, new TypeReference<List<Index>>() {});
		    return listIndex;
	}
	
	public void generateExcel(HttpServletResponse response) throws IOException {
		
    	List<Index> cityIndex = IndexService.getDataFromAPI();

		
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet("Index Info");
		HSSFRow row = sheet.createRow(0);
		
		row.createCell(0).setCellValue("ID");
		row.createCell(1).setCellValue("Mã tỉnh thành");
		row.createCell(2).setCellValue("Tên tỉnh thành");
		
		int dataRowIndex = 1;
		int idtinhthanh = 1; // Initialize the ID

		for (Index Index1 : cityIndex) {
		    HSSFRow dataRow = sheet.createRow(dataRowIndex);
		    dataRow.createCell(0).setCellValue(idtinhthanh); // Set the ID
		    dataRow.createCell(1).setCellValue(Index1.getMatinhthanh());
		    dataRow.createCell(2).setCellValue(Index1.getTentinhthanh());
		    dataRowIndex++;
		    idtinhthanh++; // Increment the ID
		}

		ServletOutputStream ops=response.getOutputStream();
		workbook.write(ops);
		workbook.close();
		ops.close();
	}
	
	public String importExcel(List<Student> list) {
		String api = "http://222.255.130.100:7070/vku/api/students/addList";
//		String api = "http://222.255.130.100:7070/vku/api/students/addList";

		HttpHeaders headers = appConfig.cookieStore().getHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		RequestEntity<?> requestEntity = new RequestEntity<>(list,headers, HttpMethod.POST, URI.create(api));
		
		ResponseEntity<String> responseEntity = restTemplate.exchange(requestEntity, String.class);
		return responseEntity.getBody();
	}
}
