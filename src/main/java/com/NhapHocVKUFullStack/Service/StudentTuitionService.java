package com.NhapHocVKUFullStack.Service;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.swing.event.SwingPropertyChangeSupport;

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
import com.NhapHocVKUFullStack.models.Student;
import com.NhapHocVKUFullStack.models.StudentTuition;
import com.NhapHocVKUFullStack.models.TuitionFeeList;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.services.sheets.v4.model.Sheet;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;

@Service
public class StudentTuitionService {
	private String apiURL = Utils.BASE_URL + "/admin/api/studentTuition";
//	private String apiURL = Utils.LOCAL_STRING+"/admin/api/studentTuition";
	private RestTemplate restTemplate = new RestTemplate();
	private AppConfig appConfig;
	private HttpHeaders headers = new HttpHeaders();

	@Autowired
	public StudentTuitionService(AppConfig appConfig) {
		this.appConfig = appConfig;
	}

	private List<StudentTuition> listStudentTuitions;
	List<TuitionFeeList> tuitionFeeList;

	public List<StudentTuition> getDataFromApi() throws Exception {
		long startTime = System.currentTimeMillis();
		RequestEntity<?> requestEntity = new RequestEntity<>(appConfig.cookieStore().getHeaders(), HttpMethod.GET,
				URI.create(apiURL));
		ResponseEntity<String> response = restTemplate.exchange(requestEntity, String.class);
		String json = response.getBody();
		ObjectMapper objectMapper = new ObjectMapper();
		listStudentTuitions = objectMapper.readValue(json, new TypeReference<List<StudentTuition>>() {
		});

		// Kết thúc đếm thời gian
		long endTime = System.currentTimeMillis();

		// Tính toán thời gian đã trôi qua
		long elapsedTime = endTime - startTime;

		// Hiển thị thời gian đã trôi qua ở đơn vị mili giây
		System.out.println("Thời gian thực thi api: " + elapsedTime + " ms");
		return listStudentTuitions;
	}
	
	public List<StudentTuition> getDataFromApiforMajor(String id) throws Exception {
		List<StudentTuition> listStudentTuitions;
		String api = apiURL +"/searchOption?idMajor=" + id;
		RequestEntity<?> requestEntity = new RequestEntity<>(appConfig.cookieStore().getHeaders(), HttpMethod.GET,
				URI.create(apiURL));
		ResponseEntity<String> response = restTemplate.exchange(requestEntity, String.class);
		String json = response.getBody();
		ObjectMapper objectMapper = new ObjectMapper();
		listStudentTuitions = objectMapper.readValue(json, new TypeReference<List<StudentTuition>>() {
		});
		return listStudentTuitions;
	}

	public List<StudentTuition> getDataStt() throws Exception {
		// Bắt đầu đếm thời gian
		long startTime = System.currentTimeMillis();
		ArrayList<StudentTuition> list = new ArrayList<>();
		for (StudentTuition sTuition : getDataFromApi()) {
			if (sTuition.isStatus() == true)
				list.add(sTuition);
		}

		// Kết thúc đếm thời gian
		long endTime = System.currentTimeMillis();

		// Tính toán thời gian đã trôi qua
		long elapsedTime = endTime - startTime;

		// Hiển thị thời gian đã trôi qua ở đơn vị mili giây
		System.out.println("Thời gian thực thi stt: " + elapsedTime + " ms");
		return list;
	}

	public StudentTuition getById(int id) throws Exception {

		long startTime = System.currentTimeMillis();
		List<StudentTuition> list = getDataFromApi();
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getId() == id) {
//	            System.out.println(list.get(i).toString());
				return list.get(i);
			}
		}

		// Kết thúc đếm thời gian
		long endTime = System.currentTimeMillis();

		// Tính toán thời gian đã trôi qua
		long elapsedTime = endTime - startTime;

		// Hiển thị thời gian đã trôi qua ở đơn vị mili giây
		System.out.println("Thời gian thực thi getID: " + elapsedTime + " ms");
		return null;
	}

	public List<Student> getByIdStudent() throws Exception {

		long startTime = System.currentTimeMillis();
		ArrayList<Student> students = new ArrayList<>();
		for (StudentTuition studentTuition : getDataStt()) {
			students.add(studentTuition.getIdStudent());
		}

		// Kết thúc đếm thời gian
		long endTime = System.currentTimeMillis();

		// Tính toán thời gian đã trôi qua
		long elapsedTime = endTime - startTime;

		// Hiển thị thời gian đã trôi qua ở đơn vị mili giây
		System.out.println("Thời gian thực thi getByIdStudent: " + elapsedTime + " ms");
		return students;
	}

	public int getIdByStudent(String id) throws Exception {

		long startTime = System.currentTimeMillis();
		for (StudentTuition studentTuition : getDataFromApi()) {
			if (studentTuition.getIdStudent().getId().equals(id)) {
				return studentTuition.getId();
			}
		}

		// Kết thúc đếm thời gian
		long endTime = System.currentTimeMillis();

		// Tính toán thời gian đã trôi qua
		long elapsedTime = endTime - startTime;

		// Hiển thị thời gian đã trôi qua ở đơn vị mili giây
		System.out.println("Thời gian thực thi getIdByStudent: " + elapsedTime + " ms");
		return 0;
	}

	public ResponseEntity<StudentTuition> post(StudentTuition studentTuition) {
		String api = apiURL + "/create";
		headers.setContentType(MediaType.APPLICATION_JSON);
		RequestEntity<?> requestEntity = new RequestEntity<>(studentTuition, appConfig.cookieStore().getHeaders(),
				HttpMethod.POST, URI.create(api));
		ResponseEntity<StudentTuition> response = restTemplate.exchange(requestEntity, StudentTuition.class);
		return response;
	}

	public ResponseEntity<String> Update(StudentTuition studentTuition, int id) {
		String api = apiURL + "/edit/" + id;

		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<StudentTuition> requestEntity = new HttpEntity<>(studentTuition,
				appConfig.cookieStore().getHeaders());
		ResponseEntity<String> responseEntity = restTemplate.exchange(api, HttpMethod.PUT, requestEntity, String.class);
		return responseEntity;
	}

	public ResponseEntity<String> UpdatesttFalse() {
		String api = apiURL + "/editStatus";
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<TuitionFeeList> requestEntity = new HttpEntity<>(appConfig.cookieStore().getHeaders());
		ResponseEntity<String> responseEntity = restTemplate.exchange(api, HttpMethod.PUT, requestEntity, String.class);
		return responseEntity;
	}

	// Lấy thí sinh đã nộp học phí(bảng thisinh_hocphi) bằng id thí sinh (search
	// bằng api)
	public StudentTuition getStudentTuitionByIdStudentApi(String id) throws Exception {
		String api = apiURL + "/searchByIdStudent?idStudent=" + id;
		RequestEntity<?> requestEntity = new RequestEntity<>(HttpMethod.GET, URI.create(api));
		ResponseEntity<StudentTuition> response = restTemplate.exchange(requestEntity, StudentTuition.class);
//		    StudentTuition studentTuition = response.getBody();

		return response.getBody();
	}

	//
	public Map<String, Integer> calculateTuitionByType() throws Exception {
		List<StudentTuition> tuitionFeeList = getDataFromApi(); // Replace with your actual method to get data
		Map<String, Integer> tuitionByType = new HashMap<>();

		for (StudentTuition tuitionFee : tuitionFeeList) {
			String type = tuitionFee.getIdStudent().getId(); // Assuming the 'Student' class has a 'getType()' method
			int tuitionAmount = tuitionFee.getTotal();
			tuitionByType.put(type, tuitionByType.getOrDefault(type, 0) + tuitionAmount);
		}

		return tuitionByType;
	}
	
		 public Map<String, Integer> calculateTotalTuitionByMajor() throws Exception {
			 List<StudentTuition> studentTuitions= getDataFromApi();
		        Map<String, Integer> tuitionByMajor = new HashMap<>();

		        for (StudentTuition tuition : studentTuitions) {
		        	//check have paid
		            if (tuition.isStatus()) { 
		            	  String major = tuition.getIdStudent().getMajors().getMajorsName();// Giả sử có phương thức lấy tên ngành từ đối tượng Student
		                int tuitionAmount = tuition.getTotal();
		                
		                //addd total map
		                tuitionByMajor.put(major, tuitionByMajor.getOrDefault(major, 0) + tuitionAmount);
		            }
		        }

		        return tuitionByMajor;
		    }
		 
//		 public void addDataToExcelSheet(HttpServletResponse response) throws Exception {
//			 	List<StudentTuition> dataList = getDataStt();

//	public void addDataToExcelSheet(HttpServletResponse response) throws Exception {
//		
//
//		long startTime = System.currentTimeMillis();
//		// Một số công việc được thực hiện ở đây
//		List<StudentTuition> dataList = getDataStt();
//		
//
//		HSSFWorkbook workbook = new HSSFWorkbook();
//		HSSFSheet sheet = workbook.createSheet("Nộp học phí");
//		HSSFRow row = sheet.createRow(0);
//
//		row.createCell(0).setCellValue("STT");
//		row.createCell(1).setCellValue("Fullname");
//		row.createCell(2).setCellValue("Email");
//		row.createCell(3).setCellValue("CMND/CCCD");
//		row.createCell(4).setCellValue("Ngày Sinh");
//		row.createCell(5).setCellValue("Lớp");
//		row.createCell(6).setCellValue("Mã Ngành");
//		row.createCell(7).setCellValue("Mã sinh viên");
//		row.createCell(8).setCellValue("Học phí HK I (2023 – 2024)");
//		row.createCell(9).setCellValue("Học phí Giáo dục Thể chất");
//		row.createCell(10).setCellValue("Kiểm tra Tiếng Anh đầu vào");
//		row.createCell(11).setCellValue("Phí khám sức khỏe đầu năm");
//		row.createCell(12).setCellValue("Bảo hiểm y tế ( 3 tháng 10,11,12/2023)");
//		row.createCell(13).setCellValue("Bảo hiểm thân thể");
//		row.createCell(14).setCellValue("Tổng tiền");
//		row.createCell(15).setCellValue("HÌnh thức nộp");
//		row.createCell(16).setCellValue("Người thu");
//		row.createCell(17).setCellValue("Ngày nộp");
//		row.createCell(18).setCellValue("Đã nhập học");
//
//		int dataRowIndex = 1;
//		int STT = 1; // Initialize the ID
//
//		for (StudentTuition sttf : dataList) {
//			
//			long startTimes = System.currentTimeMillis();
//			HSSFRow dataRow = sheet.createRow(dataRowIndex);
//
//			dataRow.createCell(0).setCellValue(STT); // Set the ID
//			dataRow.createCell(1).setCellValue(sttf.getIdStudent().getFullName());
//			dataRow.createCell(2).setCellValue(sttf.getIdStudent().getEmail());
//			dataRow.createCell(3).setCellValue(sttf.getIdStudent().getNumberCIC());
//			dataRow.createCell(4).setCellValue(sttf.getIdStudent().getBirthday());
//			dataRow.createCell(5).setCellValue(sttf.getIdStudent().getClassName());
//			dataRow.createCell(6).setCellValue(sttf.getIdStudent().getMajors().getMajorsID());
//			dataRow.createCell(7).setCellValue(sttf.getIdStudent().getIdStudent());
//			String listFees = sttf.getTuitionFeeList();
//			
////			ArrayList<String> tuitionFeeLists = new ArrayList<>();
//			TuitionFeeListService tuitionFeeListService = new TuitionFeeListService(appConfig);
//			if (listFees != null && !listFees.isEmpty()) {
//				String[] feeNames = { "Học phí học kỳ 1 năm học 2023-2024", "Học phí GDTC",
//						"Kiểm tra Tiếng Anh đầu vào", "Khám sức khỏe đầu khóa",
//						"Bảo hiểm y tế bắt buộc (tháng 10-12/2023)", "Bảo hiểm thân thể (tự nguyện, 12 tháng)" };
//				String fee[] = listFees.split(",");
//				for (int j = 0; j < feeNames.length; j++) {
//					boolean found = false;
//
//					for (int i = 0; i < fee.length; i++) {
//						int tuition = Integer.parseInt(fee[i]);
//						String tuitionFeeList = tuitionFeeListService.getByIdinMajor(tuition,sttf.getIdStudent().getKhoa().getId(), sttf.getIdStudent().getMajors().getId() ).getName();
//
//						if (tuitionFeeList.contains(feeNames[j])) {
//							dataRow.createCell(8 + j).setCellValue(tuitionFeeListService.getByIdinMajor(tuition,sttf.getIdStudent().getKhoa().getId(), sttf.getIdStudent().getMajors().getId()).getTuition());
////						                System.out.println(tuitionFeeList);
//							found = true;
//							break;
//						}
//					}
//
//					if (!found) {
//						dataRow.createCell(8 + j).setCellValue("");
//					}
//				}
//
//			}
//
//			dataRow.createCell(14).setCellValue(sttf.getTotal());
//			dataRow.createCell(15).setCellValue(sttf.getPhuongThucThanhToan());
//			dataRow.createCell(16).setCellValue(sttf.getNameCashier());
//			dataRow.createCell(17).setCellValue(sttf.getTuitionDay());
//			dataRow.createCell(18).setCellValue(sttf.getIdStudent().getTrangThaiNhapHoc());
//			dataRowIndex++;
//			STT++; // Increment the ID
//
//			// Kết thúc đếm thời gian
//			long endTimes = System.currentTimeMillis();
//
//			// Tính toán thời gian đã trôi qua
//			long elapsedTimes = endTimes - startTimes;
//
//			// Hiển thị thời gian đã trôi qua ở đơn vị mili giây
//			System.out.println("Thời gian thực thi 1 đối tượng: " + elapsedTimes + " ms");
//		}
//
//		ServletOutputStream ops = response.getOutputStream();
//		workbook.write(ops);
//		workbook.close();
//		ops.close();
//		long endTime = System.currentTimeMillis();
//
//		// Tính toán thời gian đã trôi qua
//		long elapsedTime = endTime - startTime;
//
//		// Hiển thị thời gian đã trôi qua ở đơn vị mili giây
//		System.out.println("Thời gian thực thi xuất file: " + elapsedTime + " ms");
//	}
	public void addDataToExcelSheet(HttpServletResponse response) throws Exception {

		long startTime = System.currentTimeMillis();
		// Một số công việc được thực hiện ở đây
		List<StudentTuition> dataList = getDataStt();

		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet("Nộp học phí");
		HSSFRow headerRow = sheet.createRow(0);

		String[] columnNames = { "Thông tin sinh viên", "Mã Ngành", "Học phí HK I (2023 – 2024)",
				"Học phí Giáo dục Thể chất", "Kiểm tra Tiếng Anh đầu vào", "Phí khám sức khỏe đầu năm",
				"Bảo hiểm y tế (3 tháng 10,11,12/2023)", "Bảo hiểm thân thể", "Tổng tiền",
				"Người thu - HÌnh thức nộp - Ngày nộp - Trạng thái nhhập học" };

		for (int col = 0; col < columnNames.length; col++) {
			headerRow.createCell(col).setCellValue(columnNames[col]);
		}

		int dataRowIndex = 1;
		int STT = 1; // Initialize the ID

		for (StudentTuition sttf : dataList) {

			long startTimes = System.currentTimeMillis();
				HSSFRow dataRow = sheet.createRow(dataRowIndex);
// Set the ID
				dataRow.createCell(0)
						.setCellValue("Họ tên: " + sttf.getIdStudent().getFullName() + "\n - Email: "
								+ sttf.getIdStudent().getEmail() + "\n - CCCD: " + sttf.getIdStudent().getNumberCIC()
								+ "\n - Ngày sinh: " + sttf.getIdStudent().getBirthday());
				sheet.autoSizeColumn(0);
				dataRow.createCell(1).setCellValue(sttf.getIdStudent().getMajors().getMajorsID());
				String listFees = sttf.getTuitionFeeList();

//		ArrayList<String> tuitionFeeLists = new ArrayList<>();
				TuitionFeeListService tuitionFeeListService = new TuitionFeeListService(appConfig);
				if (listFees != null && !listFees.isEmpty()) {
					String[] feeNames = { "Học phí học kỳ 1 năm học 2023-2024", "Học phí GDTC",
							"Kiểm tra Tiếng Anh đầu vào", "Khám sức khỏe đầu khóa",
							"Bảo hiểm y tế bắt buộc (tháng 10-12/2023)", "Bảo hiểm thân thể (tự nguyện, 12 tháng)" };
					String fee[] = listFees.split(",");
					for (int j = 0; j < feeNames.length; j++) {
						boolean found = false;

						for (int i = 0; i < fee.length; i++) {
							int tuition = Integer.parseInt(fee[i]);
							String tuitionFeeList = tuitionFeeListService.getByIdinMajor(tuition,
									sttf.getIdStudent().getKhoa().getId(), sttf.getIdStudent().getMajors().getId())
									.getName();

							if (tuitionFeeList.contains(feeNames[j])) {
								dataRow.createCell(2 + j)
										.setCellValue(tuitionFeeListService
												.getByIdinMajor(tuition, sttf.getIdStudent().getKhoa().getId(),
														sttf.getIdStudent().getMajors().getId())
												.getTuition());
//					                System.out.println(tuitionFeeList);
								found = true;
								break;
							}
						}

						if (!found) {
							dataRow.createCell(2 + j).setCellValue("");
						}
					}

				}

				dataRow.createCell(8).setCellValue(sttf.getTotal());
				dataRow.createCell(9).setCellValue(sttf.getNameCashier() + "\n - " + sttf.getPhuongThucThanhToan()
						+ "\n - " + sttf.getTuitionDay()+"\n - Trạng thái nhập học: "+sttf.getIdStudent().getTrangThaiNhapHoc());
				sheet.autoSizeColumn(9);
				dataRowIndex++;
				STT++; // Increment the ID
			
			// Kết thúc đếm thời gian
			long endTimes = System.currentTimeMillis();

			// Tính toán thời gian đã trôi qua
			long elapsedTimes = endTimes - startTimes;

			// Hiển thị thời gian đã trôi qua ở đơn vị mili giây
			System.out.println("Thời gian thực thi "+STT+" đối tượng: " + elapsedTimes + " ms");
		}

		ServletOutputStream ops = response.getOutputStream();
		workbook.write(ops);
		workbook.close();
		ops.close();
		long endTime = System.currentTimeMillis();

		// Tính toán thời gian đã trôi qua
		long elapsedTime = endTime - startTime;

		// Hiển thị thời gian đã trôi qua ở đơn vị mili giây
		System.out.println("Thời gian thực thi xuất file: " + elapsedTime + " ms");
	}

}