package com.NhapHocVKUFullStack.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import com.NhapHocVKUFullStack.Service.CurriculumService;
import com.NhapHocVKUFullStack.Service.DocumentItemsService;
import com.NhapHocVKUFullStack.Service.EmailSenderService;
import com.NhapHocVKUFullStack.Service.EthnicService;
import com.NhapHocVKUFullStack.Service.IndexService;
import com.NhapHocVKUFullStack.Service.KhoaService;
import com.NhapHocVKUFullStack.Service.MajorsService;
import com.NhapHocVKUFullStack.Service.MethodService;
import com.NhapHocVKUFullStack.Service.ReligionService;
import com.NhapHocVKUFullStack.Service.StatisticsFeeListServiceMoney;
import com.NhapHocVKUFullStack.Service.StudentService;
import com.NhapHocVKUFullStack.Service.StudentTuitionService;
import com.NhapHocVKUFullStack.Service.TuitionFeeListService;
import com.NhapHocVKUFullStack.config.AppConfig;
import com.NhapHocVKUFullStack.models.Curriculum;
import com.NhapHocVKUFullStack.models.DocumentItems;
import com.NhapHocVKUFullStack.models.Index;
import com.NhapHocVKUFullStack.models.Khoa;
import com.NhapHocVKUFullStack.models.Majors;
import com.NhapHocVKUFullStack.models.Message;
import com.NhapHocVKUFullStack.models.Method;
import com.NhapHocVKUFullStack.models.Student;
import com.NhapHocVKUFullStack.models.StudentTuition;
import com.NhapHocVKUFullStack.models.TuitionFeeList;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/admin/index")
public class IndexController {
	@Autowired
	private IndexService indexService;
	@Autowired
	private StatisticsFeeListServiceMoney statisticsFeeListService;
	@Autowired
	private StudentService studentService;
	@Autowired
	private MajorsService majorsService;
	@Autowired
	private KhoaService khoaService;
	@Autowired
	CurriculumService curriculumService;
	@Autowired
	DocumentItemsService documentItemsService;
	@Autowired
	MethodService methodService;
	@Autowired
	EmailSenderService emailSenderService;
	@Autowired
	TuitionFeeListService tuitionFeeListService;
	@Autowired
	StudentTuitionService studentTuitionService;

	private final AppConfig appConfig;

	@Autowired
	public IndexController(AppConfig appConfig) {
		this.appConfig = appConfig;
	}

	private Message message = null;

	@GetMapping
	public String manageProfile(ModelMap modelMap) throws Exception {
//		List<DocumentItems> documentItems = documentItemsService.getAll();
		List<StudentTuition> tuitionFeeLists = statisticsFeeListService.getDataFromApi();
		List<Student> students = studentService.getAllStudentsFromAPI();
		int submittedCount = 0;
		int notSubmittedCount = 0;

		for (StudentTuition tuitionFeeList : tuitionFeeLists) {
			if (tuitionFeeList.getId() != null && tuitionFeeList.isStatus()) {
				submittedCount++;
			} else {
				notSubmittedCount++;
			}
		}
		int countingadmissions = 0;
		int countrecords = 0;
		int countnumberadmissions = 0;
		for (Student student : students) {
			if (student.getProofOfAdmission() != null) {
				countingadmissions++;
			}
			
			if (student.getDocumentItems() != null && student.getDocumentItems().length()>1 ) {
//				System.out.println("Length: "+ student.getDocumentItems().length());
				countrecords++;
			}
			if (student.getProofOfAdmission() != null
					&& student.getProofOfAdmission().matches(".*")) {
				countnumberadmissions++;
			}
		}
//		String StringDocumentItemIds = "";
//		StringDocumentItemIds = String.join(",",
//				documentItems.stream().map(docItem -> docItem.getId().toString()).collect(Collectors.toList()));
		Map<String, Integer> documentCountMap = documentItemsService.calculateDocumentCount();
		modelMap.addAttribute("documentCountMap", documentCountMap);
		modelMap.addAttribute("countingadmissions", countingadmissions);
		modelMap.addAttribute("countrecords", countrecords);
		modelMap.addAttribute("countnumberadmissions", countnumberadmissions);
		modelMap.addAttribute("submittedCount", submittedCount);
		modelMap.addAttribute("notSubmittedCount", notSubmittedCount);
//		modelMap.addAttribute("StringDocumentItemIds", StringDocumentItemIds);

		modelMap.addAttribute("students", students);
		modelMap.addAttribute("studentTuitionService", studentTuitionService);
		modelMap.addAttribute("khoas", khoaService.getDataFromAPI());
		modelMap.addAttribute("methods", methodService.getDataFromAPI());
		modelMap.addAttribute("majors", majorsService.getDataFromAPI());
		if (message != null) {
			modelMap.addAttribute("message", message);
			message = null;
		}
		return "index";
//		return "student/admin/ProfileManagement/index";
	}
	
	@GetMapping("/StudentConfirmation/{id}")
	public String StudentConfirmation(ModelMap modelMap, @PathVariable("id") String idStudent) throws Exception {

		Student student = studentService.getStudentById(idStudent);

//		student.setStatus(true);
//		studentService.updateStudent(student);
		if (student.getCurriculum() != null && student.getCurriculum().getId() != null) {
			modelMap.addAttribute("tuitionFeeList", tuitionFeeListService.searchDataFromAPI(student.getKhoa().getId(),
					student.getMajors().getId(), student.getCurriculum().getId()));
		} else {
			modelMap.addAttribute("tuitionFeeList", tuitionFeeListService
					.searchTuitionListByKhoaIdAndMajorsId(student.getKhoa().getId(), student.getMajors().getId()));
		}
		modelMap.addAttribute("student", student);
		modelMap.addAttribute("studentTuition", studentTuitionService.getStudentTuitionByIdStudentApi(idStudent));
		List<DocumentItems> documentItems = documentItemsService.getAll().stream()
				.filter(item -> item.getKhoa().getId().equals(student.getKhoa().getId())
						&& item.getMajors().getId().equals(student.getMajors().getId())
						&& (student.getMethod() != null
								? item.getMethod().getId().equals(student.getMethod().getId())
								: true))
				.collect(Collectors.toList());
		modelMap.addAttribute("documentItems", documentItems);

//		if (message != null) {
//			modelMap.addAttribute("message", message);
//			message = null;
//		}
//		return "index";
		return "student/admin/ProfileManagement/StudentConfirmation";
	}

	@GetMapping("/excelctsv")
	public void generateExcelReport(HttpServletResponse response) throws Exception {

		response.setContentType("application/octet-stream");
		String headerket = "Content-Disposition";
		String headerValue = "attachment;" + "filename=DanhSachGiayTo.xls";
		response.setHeader(headerket, headerValue);

		studentService.generateExcelctsv(response);
	}
	@GetMapping("/excel/{id}")
	public void generateExcel(HttpServletResponse response, @PathVariable("id") int methodId) throws Exception {
		System.out.println(methodId);
		response.setContentType("application/octet-stream");
		String headerket = "Content-Disposition";
		String headerValue = "attachment;" + "filename=DanhSachGiayTo.xls";
		response.setHeader(headerket, headerValue);

		studentService.generateExcel(response,methodId);
	}

	@GetMapping("/exceldaotao")
	public void generateExcelReport2(HttpServletResponse response) throws IOException {

		response.setContentType("application/octet-stream");
		String headerket = "Content-Disposition";
		String headerValue = "attachment;" + "filename=Daotao.xls";
		response.setHeader(headerket, headerValue);

		studentService.generateExceldaotao(response);
	}

	@RequestMapping(method = RequestMethod.POST, path = "/importExcel")
	public String importExcel(@RequestBody MultipartFile file) throws Exception {
		// tạo thư mục tạm để lưu file
		String tempDir = System.getProperty("java.io.tmpdir");
		Path filePath = Paths.get(tempDir, file.getOriginalFilename());
		Files.write(filePath, file.getBytes());

		FileInputStream fis = new FileInputStream(filePath.toFile());
		Workbook workbook = WorkbookFactory.create(fis);

		// lấy sheet đầu tiền
		Sheet sheet = workbook.getSheetAt(0);
		// lấy khóa mới nhất
		Khoa khoa = khoaService.getNewestKhoa();

		// tạo danh sách sv
		List<Student> students = new ArrayList<>();
		for (Row row : sheet) {
			if (row.getRowNum() != 0)
			{
					Student student = new Student();
					for (Cell cell : row) {
						switch (cell.getColumnIndex()) {
						case 1: {
							student.setSbd(cell.getStringCellValue());
							break;
						}
						
						case 2: {
							student.setFullName(cell.getStringCellValue());
							break;
						}
						
						case 3: {
							try {
								Majors major = majorsService.getByIdMajor(cell.getStringCellValue());
								student.setMajors(major);
								List<Curriculum> curriculums = curriculumService.searchCurry(khoa.getId(), major.getId());
								student.setCurriculum(curriculums.get(0));
	 						} catch (JsonProcessingException e) {
								message.setMessage("Ngành không tồn tại!");
								message.setStatus("fail");
								e.printStackTrace();
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							break;
						}
						
						case 5: {
							student.setDiemTrungTuyen(cell.getNumericCellValue());
							break;
						}
						
						case 6: {
							List<Student> listStd = studentService.searchByOptions("", "", "", cell.getStringCellValue());
							if (listStd.size() > 0)
							{
								listStd.get(0).setMajors(student.getMajors());
								listStd.get(0).setCurriculum(student.getCurriculum());
								student = listStd.get(0);
							}
							
							student.setNumberCIC(cell.getStringCellValue());
							break;
						}
						
						case 7: {
							student.setPhoneNumber(cell.getStringCellValue());
							break;
						}
						
						case 8: {
							student.setBirthday(cell.getStringCellValue());
							break;
						}
						
						case 9: {
							student.setArea(cell.getStringCellValue());
							break;
						}
						
						case 10: {
							if (!cell.getStringCellValue().isBlank())
							{
								Method method = methodService.getByIdMethod(cell.getStringCellValue());
								student.setMethod(method);
							}
							break;
						}
						
						case 11: {
							student.setPriorityObject(cell.getStringCellValue());
							break;
						}
						
						case 12: {
							student.setClassName(cell.getStringCellValue());
							break;
						}
						
						case 13: {
							student.setIdStudent(cell.getStringCellValue());
							break;
						}
						 
						case 14: {
							student.setEmailvku(cell.getStringCellValue());
							break;
						}
						default:
							
						}

					}
					student.setKhoa(khoa);
					student.setStatus(true);
					if (student.getTrangThaiNhapHoc() == null || student.getTrangThaiNhapHoc() == false)
					{
						student.setTrangThaiNhapHoc(false);
					}
					students.add(student);
				}

		}

		fis.close();
		Files.delete(filePath);
		String rs = indexService.importExcel(students);
		if (rs == "OK") {
			return "redirect:/admin/index";
		}
		return "redirect:/admin/index";
	}
}