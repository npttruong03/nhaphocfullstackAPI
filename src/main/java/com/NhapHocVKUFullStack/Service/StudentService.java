package com.NhapHocVKUFullStack.Service;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
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
import com.NhapHocVKUFullStack.controllers.StudentTuitionController;
import com.NhapHocVKUFullStack.models.Area;
import com.NhapHocVKUFullStack.models.DocumentItems;
import com.NhapHocVKUFullStack.models.Method;
import com.NhapHocVKUFullStack.models.Student;
import com.NhapHocVKUFullStack.models.StudentTuition;
import com.NhapHocVKUFullStack.models.TuitionFeeList;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class StudentService {
	private AppConfig appConfig;
//	private final static String apiURL = "http://222.255.130.100:7070/vku/api/";
	private final static String apiURL = Utils.BASE_URL + "/api/";
	RestTemplate restTemplate = new RestTemplate();
//	private final static String apiURL = "http://222.255.130.100:7070/vku/api/";
	private StudentTuitionService studentTuitionService;
	private StudentTuitionController studentTuitionController;

	@Autowired
	public StudentService(AppConfig appConfig) {
		this.appConfig = appConfig;
	}

//		Lấy danh sách những sinh viên(bô trả về) nhập học năm hiện tại
	public List<Student> getAllStudentsFromAPI() throws JsonMappingException, JsonProcessingException {
		RequestEntity<?> requestEntity = new RequestEntity<>(appConfig.cookieStore().getHeaders(), HttpMethod.GET,
				URI.create(apiURL + "admin/currentAdmissionStudents"));
		ResponseEntity<String> response = restTemplate.exchange(requestEntity, String.class);
		String json = response.getBody();

		ObjectMapper objectMapper = new ObjectMapper();
		List<Student> listStudent = objectMapper.readValue(json, new TypeReference<List<Student>>() {
		});

		return listStudent;
	}

	public List<Student> getStudentsbyMethod(int methodID) throws Exception {
		List<Student> students = getAllStudentsFromAPI();
		ArrayList<Student> MethodStudents = new ArrayList<>();
		for (Student student : students) {
	        Method method = student.getMethod(); // Lấy đối tượng Method từ student
	        if (method != null&&student.getTrangThaiNhapHoc()!=true) { // Kiểm tra nếu method và method.getId() không null
	            if (method.getId() == methodID && student.isStatus() == true) {
	                MethodStudents.add(student);
	            }
	        } else {
	            // Xử lý trường hợp method hoặc method.getId() là null (nếu cần)
	        }
	    }
		return MethodStudents;
	}

	public List<Student> getAllComfirmedStudents() throws JsonMappingException, JsonProcessingException {
		RequestEntity<?> requestEntity = new RequestEntity<>(appConfig.cookieStore().getHeaders(), HttpMethod.GET,
				URI.create(apiURL + "admin/studentConfirmation"));
		ResponseEntity<String> response = restTemplate.exchange(requestEntity, String.class);
		String json = response.getBody();

		ObjectMapper objectMapper = new ObjectMapper();
		List<Student> listStudent = objectMapper.readValue(json, new TypeReference<List<Student>>() {
		});

		return listStudent;
	}

	public List<Student> searchByOptions(String khoaId, String majorsId, String methodId, String keyword)
			throws JsonMappingException, JsonProcessingException {
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> response = restTemplate.getForEntity(apiURL + "admin/list/search?keyWord=" + keyword
				+ "&idMethod=" + methodId + "&idKhoa=" + khoaId + "&idMajor=" + majorsId, String.class);
		String json = response.getBody();

		ObjectMapper objectMapper = new ObjectMapper();
		List<Student> listStudent = objectMapper.readValue(json, new TypeReference<List<Student>>() {
		});

		return listStudent;
	}

//	public void updateDocumentItemSubmitForStudent(String id, String documentItems) {
//		Map<String, String> requestBody = new HashMap<>();
//		requestBody.put("documentItems", documentItems);
//		HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, appConfig.cookieStore().getHeaders());
//		System.out.println(requestEntity);
//		restTemplate.exchange(apiURL + "admin/studentDocumentManagement/" + id, HttpMethod.PUT, requestEntity);
////		restTemplate.put(, requestEntity);
//
//	}
	public void updateDocumentItemSubmitForStudent(String id, String documentItems, String note) {
		documentItemsUpdate update = new documentItemsUpdate();
		update.setListDocumentItems(documentItems);
		update.setNote(note);
		HttpHeaders headers = appConfig.cookieStore().getHeaders(); // Assuming this returns the required headers
		String url = apiURL + "admin/studentDocumentManagement/" + id;
		RequestEntity<?> requestEntity = new RequestEntity<>(update, headers, HttpMethod.PUT, URI.create(url));
		ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, String.class);
	}

	public void updateDocumentItemSubmitForStudent(String id, String documentItems) {
		Map<String, String> requestBody = new HashMap<>();
		requestBody.put("documentItems", documentItems);
		HttpHeaders headers = appConfig.cookieStore().getHeaders(); // Assuming this returns the required headers
		HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);
		String url = apiURL + "admin/studentDocumentManagement/" + id;

		ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, String.class);

//	    HttpStatus statusCode = responseEntity.getStatusCode();
//	    if (statusCode == HttpStatus.OK) {
//	        System.out.println("Document item for student with ID " + id + " updated successfully.");
//	    } else {
//	        System.err.println("Failed to update document item for student with ID " + id + ". Status code: " + statusCode);
//	    }
	}

	public ResponseEntity<String> placement(String idMajors, int numberClass) {

		String api = apiURL + "admin/placementNormal?idNganh=" + idMajors + "&numberClass=" + numberClass;

		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<TuitionFeeList> requestEntity = new HttpEntity<>(appConfig.cookieStore().getHeaders());
		ResponseEntity<String> responseEntity = restTemplate.exchange(api, HttpMethod.PUT, requestEntity, String.class);
		return responseEntity;
	}

	// Mã sv
	public ResponseEntity<String> IDStudent(String idMajors) {
		String api = apiURL + "admin/idStudent?idNganh=" + idMajors;
//		String api = "http://localhost:2222/api/" + "admin/idStudent?idNganh="+idMajors;
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<TuitionFeeList> requestEntity = new HttpEntity<>(appConfig.cookieStore().getHeaders());
		ResponseEntity<String> responseEntity = restTemplate.exchange(api, HttpMethod.PUT, requestEntity, String.class);
		return responseEntity;
	}

	public Student getStudentById(String id) {
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<Student> response = restTemplate.getForEntity(apiURL + "admin/list/" + id, Student.class);
		return response.getBody();
	}

//	public String addStudent(StudentService Student) {
//		RestTemplate rest = new RestTemplate();
//		ResponseEntity<String> response = rest.postForEntity(apiURL + "/create", Student, String.class);
//		return response.getBody();
//	}

//	public Student  login(String birthday, String numberCIC) {
//		RestTemplate restTemplate = new RestTemplate();
//
//		ResponseEntity<Student> student = restTemplate.getForEntity(apiURL + "students?numberCIC=" + numberCIC+"&birthday=" + birthday, Student.class);
////		System.out.println(student.toString());
//		return student.getBody();
//	}
	public Student login(String birthday, String numberCIC) {
		RestTemplate restTemplate = new RestTemplate();

		// Tạo một đối tượng Student với các thuộc tính được đặt giá trị từ birthday và
		// numberCIC
		Student student = new Student();
		student.setBirthday(birthday);
		student.setNumberCIC(numberCIC);

		// Định nghĩa header cho yêu cầu POST
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		// Tạo một yêu cầu POST với dữ liệu JSON được đóng gói trong requestEntity
		HttpEntity<Student> requestEntity = new HttpEntity<>(student, headers);
		Student response = restTemplate.postForObject(apiURL + "students/login", requestEntity, Student.class);
		return response;
	}

	public String updateStudent(Student Student) {
		RestTemplate rest = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<Student> requestEntity = new HttpEntity<>(Student, headers);
		ResponseEntity<String> response = rest.exchange(apiURL + "students/" + Student.getId(), HttpMethod.PUT,
				requestEntity, String.class);

		return response.getBody();
	}

//	-----------------------xuất file công tác sinh viên--------------------------
	public void generateExcel(HttpServletResponse response, int methodID) throws Exception {

		List<Student> students = getStudentsbyMethod(methodID);
		DocumentItemsService documentItemsService = new DocumentItemsService(appConfig);

		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet("Index Info");
		HSSFRow row = sheet.createRow(0);

		row.createCell(0).setCellValue("STT");
		row.createCell(1).setCellValue("Fullname");
		row.createCell(2).setCellValue("Email");
		row.createCell(3).setCellValue("CMND/CCCD");
		row.createCell(4).setCellValue("Ngày Sinh");
		row.createCell(5).setCellValue("Lớp");
		row.createCell(6).setCellValue("Mã sinh viên");
		row.createCell(7).setCellValue("Mã Ngành");
		row.createCell(8).setCellValue("Trạng thái nhập học");
		row.createCell(9).setCellValue("Thông tin khác");
		
		int i = 10;
		for (DocumentItems documentItems : documentItemsService.getforMethod(methodID)) {
		    row.createCell(i).setCellValue(documentItems.getDocumentType());
		    System.out.println(documentItems.getDocumentType());
		    i++;
		}
		System.out.println("bbbb0"+documentItemsService.getforMethod(methodID).toString());
		System.out.println(methodID);

		int dataRowIndex = 1;
		int STT = 1; // Initialize the ID

		for (Student student : students) {
			HSSFRow dataRow = sheet.createRow(dataRowIndex);

			dataRow.createCell(0).setCellValue(STT); // Set the ID
			dataRow.createCell(1).setCellValue(student.getFullName());
			dataRow.createCell(2).setCellValue(student.getEmail()+","+student.getEmailvku());
			dataRow.createCell(3).setCellValue(student.getNumberCIC());
			dataRow.createCell(4).setCellValue(student.getBirthday());
			dataRow.createCell(5).setCellValue(student.getClassName());
			dataRow.createCell(6).setCellValue(student.getIdStudent());
			dataRow.createCell(7).setCellValue(student.getMajors().getMajorsID());
			dataRow.createCell(8).setCellValue(student.getTrangThaiNhapHoc());
			dataRow.createCell(9).setCellValue("");
			

			String listDocument = student.getDocumentItems();
			int j = 10;
			int e = 0;
			if (listDocument != null && !listDocument.isEmpty()) {

				String fee[] = listDocument.split(",");

				for (DocumentItems documentItems : documentItemsService.getforMethod(methodID)) {
					boolean found = false;
					for (int d = 0; d < fee.length; d++) {
						int document = Integer.parseInt(fee[d]);
						String document1 = documentItemsService.getDocument(document).getDocumentType();
						String documentNote = documentItemsService.getDocument(document).getNote();

						if (document1.equals(documentItems.getDocumentType())
								&& documentNote.equals(documentItems.getNote())) {
							found = true;
							dataRow.createCell(j + e).setCellValue("X");
							break;
						}

					}

					if (!found) {
						dataRow.createCell(j + e).setCellValue(" ");

					}
					e++;
				}
			}

			dataRowIndex++;
			STT++; // Increment the ID
		}
		ServletOutputStream ops = response.getOutputStream();
		workbook.write(ops);
		workbook.close();
		ops.close();
	}
//------------------------ctsv-------
	public void generateExcelctsv(HttpServletResponse response) throws Exception {

		List<Student> students = getAllStudentsFromAPI();

		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet("Index Info");
		HSSFRow row = sheet.createRow(0);

		row.createCell(0).setCellValue("STT");
		row.createCell(1).setCellValue("Fullname");
		row.createCell(2).setCellValue("Email cá nhân");
		row.createCell(3).setCellValue("Email VKU");
		row.createCell(4).setCellValue("CMND/CCCD");
		row.createCell(5).setCellValue("Ngày Sinh");
		row.createCell(6).setCellValue("Lớp");
		row.createCell(7).setCellValue("Mã sinh viên");
		row.createCell(8).setCellValue("Loại hồ sơ đã nộp");
		row.createCell(9).setCellValue("Đã nhập học & chưa nhập học bộ");
		row.createCell(10).setCellValue("Đã nhập học & chưa nhập học Trường");

		int dataRowIndex = 1;
		int STT = 1; // Initialize the ID

		for (Student student : students) {
			HSSFRow dataRow = sheet.createRow(dataRowIndex);

			dataRow.createCell(0).setCellValue(STT); // Set the ID
			dataRow.createCell(1).setCellValue(student.getFullName());
			dataRow.createCell(2).setCellValue(student.getEmail());
			dataRow.createCell(3).setCellValue(student.getEmailvku());
			dataRow.createCell(4).setCellValue(student.getNumberCIC());
			dataRow.createCell(5).setCellValue(student.getBirthday());
			dataRow.createCell(6).setCellValue(student.getClassName());
			dataRow.createCell(7).setCellValue(student.getIdStudent());

			String listDocument = student.getDocumentItems();
			ArrayList<String> documentList = new ArrayList<>();
			DocumentItemsService documentItemsService = new DocumentItemsService(appConfig);
			if (listDocument != null && !listDocument.isEmpty()) {
				String fee[] = listDocument.split(",");
				for (int i = 0; i < fee.length; i++) {
					int document = Integer.parseInt(fee[i]);
					String document1 = documentItemsService.getDocument(document).getDocumentType();
					documentList.add(document1);
				}
			}

			// Tạo chuỗi mới với dấu xuống dòng thay thế dấu ","
			String formattedDocumentList = String.join(", ", documentList);

			// Đặt giá trị chuỗi đã được định dạng vào ô dữ liệu
			dataRow.createCell(8).setCellValue(formattedDocumentList);

			// xét điều kiện
			if (student.getProofOfAdmission() != null
					&& student.getProofOfAdmission().matches(".+(jpg|jpeg|png|bmp)$")) {
				dataRow.createCell(9).setCellValue("Đã nhập học bộ");
			} else {
				dataRow.createCell(10).setCellValue("Chưa nhập học bộ");
			}
			dataRow.createCell(9).setCellValue(student.getTrangThaiNhapHoc());

			dataRowIndex++;
			STT++; // Increment the ID
		}
		for (int i = 0; i < 9; i++) {
			sheet.autoSizeColumn(i);
		}
		ServletOutputStream ops = response.getOutputStream();
		workbook.write(ops);
		workbook.close();
		ops.close();
	}

	// ------------------------Phòng Đào
	// tạo------------------------------------------
	public void generateExceldaotao(HttpServletResponse response) throws IOException {

		List<Student> students = getAllStudentsFromAPI();

		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet("Index Info");
		HSSFRow row = sheet.createRow(0);

		row.createCell(0).setCellValue("STT");
		row.createCell(1).setCellValue("SBD");
		row.createCell(2).setCellValue("Họ tên");
		row.createCell(3).setCellValue("Mã ngành trúng tuyển");
		row.createCell(4).setCellValue("Tên ngành trúng tuyển");
		row.createCell(5).setCellValue("Điểm trúng tuyển");
		row.createCell(6).setCellValue("CMND/CCCD");
		row.createCell(7).setCellValue("Điện thoại");
		row.createCell(8).setCellValue("Ngày Sinh");
		row.createCell(9).setCellValue("Khu vực");
		row.createCell(10).setCellValue("Hình thức trúng tuyển");
		row.createCell(11).setCellValue("Đối tượng ưu tiên");
		row.createCell(12).setCellValue("Lớp sinh hoạt");
		row.createCell(13).setCellValue("Mã sinh viên");
		row.createCell(14).setCellValue("Email VKU");
		row.createCell(15).setCellValue("Đã nhập học & chưa nhập học Bộ");
		row.createCell(16).setCellValue("Hoàn thành nhập học");

		int dataRowIndex = 1;
		int STT = 1; // Initialize the ID

		for (Student student : students) {
			HSSFRow dataRow = sheet.createRow(dataRowIndex);

			dataRow.createCell(0).setCellValue(STT); // Set the ID
			dataRow.createCell(1).setCellValue(student.getSbd());
			dataRow.createCell(2).setCellValue(student.getFullName());
			dataRow.createCell(3).setCellValue(student.getMajors().getMajorsID());
			dataRow.createCell(4).setCellValue(student.getMajors().getMajorsName());
			dataRow.createCell(5).setCellValue(student.getDiemTrungTuyen());
			dataRow.createCell(6).setCellValue(student.getNumberCIC());
			dataRow.createCell(7).setCellValue(student.getPhoneNumber());
			dataRow.createCell(8).setCellValue(student.getBirthday());
			dataRow.createCell(9).setCellValue(student.getArea());
			if (student.getMethod() != null) {
				dataRow.createCell(10).setCellValue(student.getMethod().getMa_phuong_thuc());
			} else {
				dataRow.createCell(10).setCellValue("");
			}
			dataRow.createCell(11).setCellValue(student.getPriorityObject());
			dataRow.createCell(12).setCellValue(student.getClassName());
			dataRow.createCell(13).setCellValue(student.getIdStudent());
			dataRow.createCell(14).setCellValue(student.getEmailvku());
			// xét điều kiện
			if (student.getProofOfAdmission() != null
					&& student.getProofOfAdmission().matches(".+(jpg|jpeg|png|bmp)$")) {
				dataRow.createCell(15).setCellValue("Đã nhập học bộ");
			} else {
				dataRow.createCell(15).setCellValue("Chưa nhập học bộ");
			}
			dataRow.createCell(16).setCellValue(student.getTrangThaiNhapHoc());

			dataRowIndex++;
			STT++; // Increment the ID
		}
		for (int i = 0; i < 15; i++) {
			sheet.autoSizeColumn(i);
		}
		ServletOutputStream ops = response.getOutputStream();
		workbook.write(ops);
		workbook.close();
		ops.close();
	}

	static class documentItemsUpdate {
		private String listDocumentItems;
		private String note;

		public String getListDocumentItems() {
			return listDocumentItems;
		}

		public void setListDocumentItems(String listDocumentItems) {
			this.listDocumentItems = listDocumentItems;
		}

		public String getNote() {
			return note;
		}

		public void setNote(String note) {
			this.note = note;
		}

	}
}
