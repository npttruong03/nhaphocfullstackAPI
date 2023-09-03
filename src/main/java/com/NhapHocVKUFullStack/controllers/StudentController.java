package com.NhapHocVKUFullStack.controllers;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.unit.DataSize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.NhapHocVKUFullStack.Service.AreaService;
import com.NhapHocVKUFullStack.Service.CurriculumService;
import com.NhapHocVKUFullStack.Service.DocumentItemsService;
import com.NhapHocVKUFullStack.Service.EmailSenderService;
import com.NhapHocVKUFullStack.Service.EthnicService;
import com.NhapHocVKUFullStack.Service.KhoaService;
import com.NhapHocVKUFullStack.Service.MajorsService;
import com.NhapHocVKUFullStack.Service.MethodService;
import com.NhapHocVKUFullStack.Service.PriorityObjectService;
import com.NhapHocVKUFullStack.Service.ReligionService;
import com.NhapHocVKUFullStack.Service.StudentService;
import com.NhapHocVKUFullStack.Service.StudentTuitionService;
import com.NhapHocVKUFullStack.Service.SurveyService;
import com.NhapHocVKUFullStack.Service.TuitionFeeListService;
import com.NhapHocVKUFullStack.config.AppConfig;
import com.NhapHocVKUFullStack.models.Curriculum;
import com.NhapHocVKUFullStack.models.DocumentItems;
import com.NhapHocVKUFullStack.models.Khoa;
import com.NhapHocVKUFullStack.models.Majors;
import com.NhapHocVKUFullStack.models.Message;
import com.NhapHocVKUFullStack.models.Method;
import com.NhapHocVKUFullStack.models.Student;
import com.NhapHocVKUFullStack.models.StudentTuition;
import com.NhapHocVKUFullStack.models.Survey;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import cn.apiclub.captcha.Captcha;
import cn.apiclub.captcha.backgrounds.GradiatedBackgroundProducer;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping()
public class StudentController {

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
	@Autowired
	SurveyService surveyService;
	@Autowired
	private EthnicService ethnicService;
	@Autowired
	private ReligionService religionService;

	@Autowired
	private AreaService areaService;

	@Autowired
	private PriorityObjectService priorityObjectService;

	@Autowired
	private AppConfig appConfig;

	@Autowired
	public StudentController(AppConfig appConfig) {
		this.appConfig = appConfig;
	}

	private Message message = null;
	// biến dùng để kiểm tra xem đã đi đến được bước nào rồi
	private static Integer unlocked = 1;
	// biến dùng để xác định đang ở bước mấy
	private Integer index = 1;

	private Integer page = 0;
	private static final String upload_dir = "uploads";

//	private final String apiURL = "http://222.255.130.100:7070/vku/admin/api/document/getAll";
	// --Xếp lớp---
	@GetMapping("/admin/xeplop")
	public String placement(ModelMap modelMap) throws JsonMappingException, JsonProcessingException {
		if (message != null) {
			modelMap.addAttribute("message", message);
			message = null;
		}
		String cookieValue = appConfig.cookieStore().getCookie();
		modelMap.addAttribute("cookieValue", cookieValue);
		modelMap.addAttribute("majors", majorsService.getDataFromAPIsttTrue());
		return "admin/xeplop";
	}

	@PostMapping("/admin/xeplop")
	public String placement(@RequestParam String majorsId, @RequestParam int numberClass) {
//		    String idMajor = (String) formData.get("majorsId");
//		    int numberClass = Integer.parseInt((String) formData.get("numberClass"));
		try {
			studentService.placement(majorsId, numberClass);
			message = new Message();
			message.setStatus("success");
			message.setMessage("Xếp lớp thành công!");
			return "redirect:/admin/xeplop";
		} catch (Exception e) {
			message = new Message();
			message.setStatus("fail");
			message.setMessage("Xếp lớp không thành công!");

			return "redirect:/admin/xeplop";
		}

	}

	// -- End xếp lớp---

	// Mã SV-----
	@GetMapping("/admin/maSV")
	public String idStudentDisplay(ModelMap modelMap) throws JsonMappingException, JsonProcessingException {
		if (message != null) {
			modelMap.addAttribute("message", message);
			message = null;
		}
		String cookieValue = appConfig.cookieStore().getCookie();
		modelMap.addAttribute("cookieValue", cookieValue);
		modelMap.addAttribute("majors", majorsService.getDataFromAPIsttTrue());
		return "admin/masv";
	}

	@PostMapping("/admin/maSV")
	public String idStudent(@RequestParam String majorsId) {
		try {
			studentService.IDStudent(majorsId);
			message = new Message();
			message.setStatus("success");
			message.setMessage("Tạo mã sinh viên thành công!");
			return "redirect:/admin/maSV";
		} catch (Exception e) {
			message = new Message();
			message.setStatus("fail");
			message.setMessage("Tạo mã sinh viên không thành công!");

			return e.toString();
		}

	}

	@GetMapping("/admin/managingStudentDocument")
	public String manageStudentDocument(ModelMap modelMap) throws Exception {

//
//		List<Khoa> khoas = khoaService.getDataFromAPI();
//		List<Majors> majors = majorsService.getDataFromAPI();
//		List<Method> methods = methodService.getDataFromAPI();
//		modelMap.addAttribute("students",
//				studentService.searchByOptions(khoas.get(0).getId().toString(), majors.get(0).getId(), "", ""));
//		String cookieValue = appConfig.cookieStore().getCookie();
//		modelMap.addAttribute("cookieValue", cookieValue);
//		modelMap.addAttribute("documentItemsJson", documentItemsJson);
//
//		modelMap.addAttribute("StringDocumentItemIds", StringDocumentItemIds);
//		modelMap.addAttribute("documentItems",
//				documentItems.stream()
//						.filter(docItem -> docItem.getKhoa().getId().equals(khoas.get(0).getId())
//								&& docItem.getMajors().getId().equals(majors.get(0).getId()))
//						.collect(Collectors.toList()));
//		modelMap.addAttribute("khoas", khoas);
//		modelMap.addAttribute("methods", methods);
//		modelMap.addAttribute("majors", majors);
//		if (message != null) {
//			modelMap.addAttribute("message", message);
//			message = null;
//		}
		List<DocumentItems> documentItems = documentItemsService.getAll();

		List<Khoa> khoas = khoaService.getDataFromAPI();
		List<Majors> majors = majorsService.getDataFromAPI();
		List<Method> methods = methodService.getDataFromAPI();
		modelMap.addAttribute("documentItems", documentItems.stream()
				.filter(docItem -> docItem.getKhoa().getId().equals(khoas.get(0).getId())
						&& docItem.getMajors().getId().equals(majors.get(0).getId())
						&& docItem.getMethod().getId().equals(methods.get(0).getId()))
				.collect(Collectors.toList()));
		modelMap.addAttribute("students", studentService.searchByOptions(khoaService.getNewestKhoa().getId().toString(),
				majors.get(0).getId(), methods.get(0).getId().toString(), ""));
		modelMap.addAttribute("khoas", khoas);
		modelMap.addAttribute("methods", methods);
		modelMap.addAttribute("majors", majors);
		if (message != null) {
			modelMap.addAttribute("message", message);
			message = null;
		}
		return "student/admin/ManagingStudentDocument/index";

	}

	@GetMapping("/admin/managingStudentDocument/edit/{id}")
	public String updateStudentDocument(ModelMap modelMap, @PathVariable("id") String id) throws Exception {
		Student student = studentService.getStudentById(id);
		List<DocumentItems> documentItems = documentItemsService.getAll();
		modelMap.addAttribute("student", student);
//		modelMap.addAttribute("cookie", documentItemsService.getCookie());
		modelMap.addAttribute("documentItems",
				documentItems.stream()
						.filter(docItem -> docItem.getKhoa().getId().equals(student.getKhoa().getId())
								&& docItem.getMajors().getId().equals(student.getMajors().getId())
								&& (student.getMethod() != null
										? docItem.getMethod().getId().equals(student.getMethod().getId())
										: true))
						.collect(Collectors.toList()));
		if (message != null) {
			modelMap.addAttribute("message", message);
			message = null;
		}
		return "student/admin/ManagingStudentDocument/update";
	}

	@PostMapping("/admin/managingStudentDocument/edit/{id}")
	public String updateDocumentItemSubmitForStudent(@PathVariable("id") String id,
			@RequestParam(value = "documentItemId", defaultValue = "") List<Long> selectedDocumentItems,
			@RequestParam(value = "note") String note) {
		// Từ một list id documents selected(id các giấy tờ đã nộp) gộp thành chuỗi
		String documentItemsString = String.join(",",
				selectedDocumentItems.stream().map(String::valueOf).collect(Collectors.toList()));
		try {

			// Cập nhật giá trị của documentItems cho student
			studentService.updateDocumentItemSubmitForStudent(id, documentItemsString, note);
			// Thêm message thông báo cập nhật thành công
			message = new Message("success", "Cập nhật giấy tờ sinh viên thành công");
			return "redirect:/admin/managingStudentDocument/edit/" + id;
		} catch (HttpServerErrorException e) {
//			e.printStackTrace();
			// Trả về modelMap với thông tin lỗi và render view với modelMap này
			message = new Message("fail", "Cập nhật giấy tờ sinh viên thất bại. Bạn không có quyền cập nhập giấy tờ");

			return "redirect:/admin/managingStudentDocument/edit/" + id;

		}
	}

	// Thí sinh

	public String showPage1(ModelMap modelMap, HttpSession session)
			throws JsonMappingException, JsonProcessingException {
		Student loggedInStudent = (Student) session.getAttribute("loggedInStudent");
		if (unlocked < 1) {
			unlocked = 1;
		}
//		index = 1;
		if (loggedInStudent != null) {
			modelMap.addAttribute("loggedInStudent", loggedInStudent);
			modelMap.addAttribute("ethnics", ethnicService.getDataFromAPI());
			modelMap.addAttribute("religions", religionService.getDataFromAPI());
			modelMap.addAttribute("unlocked", unlocked);
			if (message != null) {
				modelMap.addAttribute("message", message);
				message = null;
			}
			return "student/admission/page1";
		} else {
			return "redirect:/login";
		}
	}

	@PostMapping("/admission/page1/{id}")
	public String admissionPage1Submit(@PathVariable("id") String id, HttpServletRequest request,
			RedirectAttributes redirectAttributes) throws Exception {
		Student studentDTO = studentService.getStudentById(id);
		String ethnic = request.getParameter("ethnic");
		String religion = request.getParameter("religion");
		String homeTown = request.getParameter("homeTown");
		String birthplace = request.getParameter("birthplace");
		String dateIssuanceCIC = request.getParameter("dateIssuanceCIC").trim();
		String gender = request.getParameter("gender");
		String email = request.getParameter("email").trim();
		String phoneNbHome = request.getParameter("phoneNbHome");
		String phoneNumber = request.getParameter("phoneNumber");
		String treatmentPolicy = request.getParameter("treatmentPolicy");

		String placeIssuanceCIC = request.getParameter("placeIssuanceCIC").trim();
		String cuTruTinh = request.getParameter("hktinh1");

		String cuTruHuyen = request.getParameter("hkhuyen1");
		String cuTruXa = request.getParameter("hkxa1");
		String cuTruThon = request.getParameter("hkthon").trim();
		String codeBHYT = request.getParameter("codeBHYT");
		try {
			if (isNullOrEmpty(ethnic) || isNullOrEmpty(religion) || isNullOrEmpty(homeTown) || isNullOrEmpty(birthplace)
					|| isNullOrEmpty(dateIssuanceCIC) || isNullOrEmpty(gender) || isNullOrEmpty(email)
					|| isNullOrEmpty(phoneNbHome) || isNullOrEmpty(phoneNumber) || isNullOrEmpty(placeIssuanceCIC)
					|| isNullOrEmpty(cuTruTinh) || isNullOrEmpty(cuTruHuyen) || isNullOrEmpty(cuTruXa)
					|| isNullOrEmpty(cuTruThon)) {
				throw new IllegalArgumentException("Các thông tin còn thiếu. Vui lòng điền đầy đủ!");
			} else {

				studentDTO.setCuTruTinh(cuTruTinh);
				studentDTO.setCuTruQuanHuyen(cuTruHuyen);
				studentDTO.setCuTruXaPhuong(cuTruXa);
				studentDTO.setCuTruToThon(cuTruThon);
				studentDTO.setCodeBHYT(codeBHYT);
				String unionMemberValue = request.getParameter("unionMember");
				boolean unionMember = Boolean.parseBoolean(unionMemberValue != null ? unionMemberValue : "false");
//		System.out.println("member "+unionMember);
				studentDTO.setUnionMember(unionMember);
				studentDTO.setEthnic(ethnic);
				studentDTO.setReligion(religion);
				studentDTO.setPhoneNumber(phoneNumber);
				studentDTO.setPhoneNbHome(phoneNbHome);
				studentDTO.setTreatmentPolicy(treatmentPolicy);
				studentDTO.setHomeTown(homeTown);
				studentDTO.setBirthplace(birthplace);
				studentDTO.setPlaceIssuanceCIC(placeIssuanceCIC);
				studentDTO.setDateIssuanceCIC(dateIssuanceCIC);
				studentDTO.setGender(gender);
				studentDTO.setEmail(email);

//			System.out.println("stuent: " + studentDTO);
				studentService.updateStudent(studentDTO);
				request.getSession().setAttribute("loggedInStudent", studentDTO);
				index = 2;
				return "redirect:/";
			}
		} catch (Exception e) {
//			e.printStackTrace();
			message = new Message();
			message.setMessage(e.getMessage());
			message.setStatus("fail");
			return "redirect:/";

		}

	}

	private boolean isNullOrEmpty(String value) {
		return value == null || value.trim().isEmpty();

	}

	@GetMapping("/admission/page2/{id}")
	public String admissionpage2(ModelMap modelMap, String id) throws Exception {
		Student student = studentService.getStudentById(id);
		if (unlocked < 2) {
			unlocked = 2;
		}
//		index = 2;
		modelMap.addAttribute("student", student);
//		modelMap.addAttribute("khoas", khoaService.getDataFromAPI());
		modelMap.addAttribute("methods", methodService.getDataFromAPI());

		modelMap.addAttribute("unlocked", unlocked);
		modelMap.addAttribute("majors", majorsService.getDataFromAPI());
		if (message != null) {
			modelMap.addAttribute("message", message);
			message = null;
		}
//		return null;	
		return "student/admission/page2";
	}

	@PostMapping("/admission/page2/{id}")
	public String admissionPage2Submit(@PathVariable("id") String id, @RequestParam("highSchool") String highSchool,
			@RequestParam("idHighSchool") String idHighSchool, @RequestParam("provinceSchool") String provinceSchool,
			@RequestParam("graduationYear") String graduationYear, @RequestParam("school10") String school10,
			@RequestParam("school11") String school11, HttpSession session,
			@RequestPart(value = "proofOfAdmission", required = false) MultipartFile file,
//			@RequestParam(value = "methodId", required = false) String methodId, 
			RedirectAttributes redirectAttributes, HttpServletRequest request) throws Exception {
		Student studentDTO = studentService.getStudentById(id);

		try {
			String newFileName = null;

			if (!file.isEmpty()) {
				long fileSizeInBytes = file.getSize();
				DataSize maxSize = DataSize.ofMegabytes(60);
				if (fileSizeInBytes > maxSize.toBytes()) {
					throw new Exception("Dung lượng ảnh vượt quá giới hạn cho phép");
				}

				newFileName = UUID.randomUUID().toString() + "-" + file.getOriginalFilename();
				String applicationPath = request.getServletContext().getRealPath("");
				String uploadFilePath = applicationPath + File.separator + upload_dir;
				File uploadFolder = new File(uploadFilePath);
				if (!uploadFolder.exists()) {
					uploadFolder.mkdir();
				}

				File imageFile = new File(uploadFilePath + File.separator + newFileName);
				Files.copy(file.getInputStream(), imageFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
			} else if (studentDTO.getProofOfAdmission() != null && !studentDTO.getProofOfAdmission().isBlank()) {
//				System.out.println("minh chung: " + studentDTO.getProofOfAdmission() );
				newFileName = studentDTO.getProofOfAdmission();
			}
//			studentDTO.setPriorityObject(priorityObject);
			if (isNullOrEmpty(highSchool) || isNullOrEmpty(school10) || isNullOrEmpty(school11)
					|| isNullOrEmpty(idHighSchool) || isNullOrEmpty(graduationYear) || isNullOrEmpty(newFileName)
					|| isNullOrEmpty(provinceSchool)) {
				throw new Exception("Các thông tin không được để trống");
			} else {
				studentDTO.setHighSchool(highSchool);
				studentDTO.setIdHighSchool(idHighSchool);
				studentDTO.setSchool10(school10);
				studentDTO.setSchool11(school11);
				studentDTO.setGraduationYear(graduationYear);
				studentDTO.setProofOfAdmission(newFileName);
				studentDTO.setProvinceSchool(provinceSchool);
				// để tạm thời ít bữa có dữ liệu xóa
				String methodId = request.getParameter("methodId");
				if (methodId != null) {

					Method method = methodService.getByIdMethod(methodId);
					studentDTO.setMethod(method);
				}

				studentService.updateStudent(studentDTO);

				index = 3;
				session.setAttribute("loggedInStudent", studentDTO);

				return "redirect:/";
			}

		} catch (Exception e) {
//			e.printStackTrace();
			message = new Message();
			message.setMessage(e.getMessage());
			message.setStatus("fail");
			return "redirect:/";
		}

	}

	@GetMapping("/admission/page3/{id}")
	public String admissionpage3(ModelMap modelMap, String id) throws Exception {
		if (unlocked < 3) {
			unlocked = 3;
		}
		Student studentDTO = studentService.getStudentById(id);
		modelMap.addAttribute("student", studentDTO);
		modelMap.addAttribute("unlocked", unlocked);
//		modelMap.addAttribute("tuitionFeeList", tuitionFeeListService
//				.searchTuitionListByKhoaIdAndMajorsId(studentDTO.getKhoa().getId(), studentDTO.getMajors().getId()));
		if (studentDTO.getCurriculum() != null && studentDTO.getCurriculum().getId() != null) {
			modelMap.addAttribute("tuitionFeeList", tuitionFeeListService.searchDataFromAPI(
					studentDTO.getKhoa().getId(), studentDTO.getMajors().getId(), studentDTO.getCurriculum().getId()));
		} else {
			modelMap.addAttribute("tuitionFeeList", tuitionFeeListService.searchTuitionListByKhoaIdAndMajorsId(
					studentDTO.getKhoa().getId(), studentDTO.getMajors().getId()));
		}

		StudentTuition studentTuition = studentTuitionService.getStudentTuitionByIdStudentApi(id);
		if (studentTuition != null) {
			modelMap.addAttribute("studentTuition", studentTuition);
		} else {
			StudentTuition newStudentTuition = new StudentTuition();
			newStudentTuition.setIdStudent(studentDTO);
			modelMap.addAttribute("studentTuition", newStudentTuition);
		}
//		System.out.println("get page 3: " + studentTuition );
//		return "success";	
		return "student/admission/page3";
	}

	@RequestMapping(value = "/createTuitionStudent", method = RequestMethod.POST)
	public String createStudentTuition(@ModelAttribute("studentTuition") StudentTuition studentTuition,
//			@PathVariable("idStudent") String studentId,
			HttpServletRequest request, @RequestParam("file") MultipartFile file) {
		if (!studentTuition.isStatus()) {
			String newFileName = null;
			if (!file.isEmpty() && studentTuition.getTuitionFeeList() != null) {
				try {

					// Lưu trữ tệp tin vào thư mục lưu trữ
//			    	StringBuilder fileName = new StringBuilder();
					newFileName = UUID.randomUUID().toString() + "-" + file.getOriginalFilename();
					String applicationPath = request.getServletContext().getRealPath("");
					String uploadFilePath = applicationPath + File.separator + upload_dir;
					File uploadFolder = new File(uploadFilePath);
					if (!uploadFolder.exists()) {
						uploadFolder.mkdir();
					}

					File imageFile = new File(uploadFilePath + File.separator + newFileName);

					Files.copy(file.getInputStream(), imageFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
					// Cập nhật đường dẫn tệp tin trong đối tượng studentTuition
				} catch (Exception e) {
					// Xử lý ngoại lệ nếu có lỗi xảy ra
//					e.printStackTrace();
					message = new Message();
					message.setStatus("fail");
					message.setMessage("Nộp Học phí không thành công! Vui lòng kiểm tra lại file");
					return "redirect:/";
				}
			}
			if (!studentTuition.getTuitionFeeList().isBlank()) {
				try {
					Student student = (Student) request.getSession().getAttribute("loggedInStudent");
					// Lấy thí sinh đã nộp học phí nếu có
					StudentTuition studentPaidTuition = studentTuitionService
							.getStudentTuitionByIdStudentApi(student.getId());
					if (newFileName != null) {
						studentTuition.setProofOfTuitionPay(newFileName);
					} else if (studentPaidTuition.getProofOfTuitionPay().indexOf('.') >= 0) {
						newFileName = studentPaidTuition.getProofOfTuitionPay();
					} else {
						throw new Exception("Bạn chưa upload ảnh!");
					}
					if (studentPaidTuition != null) {
				
						studentPaidTuition.setTuitionDay(new Date(System.currentTimeMillis()).toString());
						studentPaidTuition.setPhuongThucThanhToan("Chuyển khoản");
						studentPaidTuition.setStatus(false);
						studentPaidTuition.setNameCashier("");
						studentPaidTuition.setProofOfTuitionPay(studentTuition.getProofOfTuitionPay());
						studentPaidTuition.setTuitionFeeList(studentTuition.getTuitionFeeList());
						studentPaidTuition.setTotal(studentTuition.getTotal());
						studentTuitionService.Update(studentPaidTuition, studentPaidTuition.getId());
					} else {
						studentTuition.setIdStudent(student);
						studentTuition.setTuitionDay(new Date(System.currentTimeMillis()).toString());
						studentTuition.setPhuongThucThanhToan("Chuyển khoản");
						studentTuition.setStatus(false);
						studentTuitionService.post(studentTuition);
					}
					message = new Message();
					message.setStatus("success");
					message.setMessage("Nộp Học Phí thành công!");
					index = 4;
					return "redirect:/";
				} catch (Exception e) {
//					e.printStackTrace();
					message = new Message();
					message.setStatus("fail");
					message.setMessage("Nộp Học phí không thành công!" + e.getMessage());
					return "redirect:/";
				}
			} else {
				index = 4;
				return "redirect:/";
			}
		} else {
			index = 4;
			return "redirect:/";
		}

	}

	@GetMapping("/admission/page4/{id}")
	public String admissionPage4(ModelMap modelMap, String id, HttpServletRequest request) throws Exception {
		unlocked = 4;
		Student studentDTO = studentService.getStudentById(id);

		modelMap.addAttribute("unlocked", unlocked);
		modelMap.addAttribute("student", studentDTO);
		modelMap.addAttribute("methods", methodService.getDataFromAPI());
		modelMap.addAttribute("majors", majorsService.getDataFromAPI());
		List<Survey> surveys = surveyService
				.searchDataFromAPI(studentDTO.getKhoa().getId(), studentDTO.getMajors().getId()).stream()
				.filter(survey -> survey.isStatus()).collect(Collectors.toList());
		modelMap.addAttribute("surveys", surveys);
		List<DocumentItems> documentItems = documentItemsService.getAll();
		modelMap.addAttribute("documentItems",
				documentItems.stream()
						.filter(docItem -> docItem.getKhoa().getId().equals(studentDTO.getKhoa().getId())
								&& docItem.getMajors().getId().equals(studentDTO.getMajors().getId())
								&& (studentDTO.getMethod() != null
										? docItem.getMethod().getId().equals(studentDTO.getMethod().getId())
										: true))
						.collect(Collectors.toList()));

		if (message != null) {
			modelMap.addAttribute("message", message);
			message = null;
		}
		return "student/admission/page4";
	}

	@GetMapping("/captcha")
	public void getCaptchaImage(HttpServletRequest request, HttpServletResponse response) throws Exception {

		Captcha captcha = new Captcha.Builder(200, 50).addText().addNoise()
				.addBackground(new GradiatedBackgroundProducer()).build();

		// Lưu captcha vào session
		request.getSession().setAttribute("captcha", captcha.getAnswer());

		// Đưa captcha vào response
		response.setContentType("image/png");
		ServletOutputStream outputStream = response.getOutputStream();
		ImageIO.write(captcha.getImage(), "png", outputStream);
		outputStream.flush();
		outputStream.close();
	}

	@PostMapping("/admission/page4/{id}")
	public String completeAdmission(@PathVariable String id, ModelMap modelMap, HttpServletRequest request,
			RedirectAttributes redirectAttributes) throws Exception {
		String curriculumId = request.getParameter("curriculumId");
		Student student = studentService.getStudentById(id);
		if (student != null) {
			// Update status
//			student.setStatus(true);
//			student.setRegisterSession(true);
//			student.setProofOfAdmission("Đã nhập học");
//			System.out.println(curriculumId); 
			String sessionCaptcha = (String) request.getSession().getAttribute("captcha");
			String captcha = request.getParameter("captcha1");

			if (captcha == null || sessionCaptcha == null || !captcha.equals(sessionCaptcha)) {
				// Nếu giá trị Captcha nhập vào không khớp với giá trị lưu trong session, hiển
				// thị thông báo lỗi
				message = new Message();
				message.setMessage("Mã Captcha không đúng");
				message.setStatus("fail");

				redirectAttributes.addFlashAttribute("errCaptcha", message);
//				request.getSession().setAttribute("errCaptcha", "Mã Captcha không đúng");
				return "redirect:/";
			}
			try {
				if (isNullOrEmpty(student.getEmail()) || isNullOrEmpty(student.getProofOfAdmission())) {

					message = new Message();
					message.setStatus("fail");
					message.setMessage("Vui lòng nhập đầy đủ thông tin!");
					if (isNullOrEmpty(student.getEmail())) {
						index = 1;
					} else {
						index = 2;
					}
					return "redirect:/";
				}

				index = 5;
				if (student.getEmailSended() == null || !student.getEmailSended()) {
					student.setEmailSended(true);
					emailSenderService.sendEmail(student.getEmail(), "Đăng ký nhập học thành công",
							"student/admission/successPage", student);
					String success = studentService.updateStudent(student);
//					System.out.println(success+ student.getEmailSended() );

				} else {
					String success = studentService.updateStudent(student);
//					System.out.println("đã gửi");
				}
			} catch (Exception e) {
				index = 4;
				e.printStackTrace();
				message = new Message();
				message.setStatus("fail");
				message.setMessage("Thất bại!" + e.getMessage());
				return "redirect:/";
			}

		}
		return "redirect:/";
	}

	public String successPage(Student student, ModelMap modelMap) throws Exception {
		modelMap.addAttribute("documentItems", null);
		modelMap.addAttribute("student", student);
		modelMap.addAttribute("studentTuition", studentTuitionService.getStudentTuitionByIdStudentApi(student.getId()));
		modelMap.addAttribute("tuitionFeeList", tuitionFeeListService
				.searchTuitionListByKhoaIdAndMajorsId(student.getKhoa().getId(), student.getMajors().getId()));
//		modelMap.addAttribute("", message);
//		index = 1;message
		modelMap.addAttribute("message", message);
		message = null;

		return "student/admission/successPage";
	}

	@GetMapping("/")
	public String admission(ModelMap modelMap, HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		Student student = (Student) session.getAttribute("loggedInStudent");
		// kiểm tra xem đã có sinh viên đăng nhập chưa
		if (student == null) {
			// nếu chưa thì trỏ đến trang login
			return "redirect:/login";
		}

		// nếu có thì lấy thông tin sinh viên
//		int stdTuition = studentTuitionService.getIdByStudent(student.getId());
		String idStd = student.getId();

		// kiểm tra xem có được yêu cầu chuyển đến trang cụ thể không, nếu không thì
		// chuyển đến trang mặc định là trang tiếp theo của trang đã hoàn thành
//		System.out.println("Trứoc: index =  " + index +  "gửi mail" + student.getEmailSended()) ;
		if (page == 0) {
			if (index == 5 && student.getEmailSended() != null) {
				// den trang thanh cong
				return successPage(student, modelMap);
			} else if ((student.getGraduationYear() != null) && (student.getProofOfAdmission() != null)) {
				if (index >= 4) {
					index = 4;
					unlocked = 3;
				} else {
//					System.out.println("vấn đề chỗ này: index=" + index);
					index = 3;
					unlocked = 2;
				}
			} else if ((student.getSchool10() == null || student.getProofOfAdmission() == null)
					&& student.getEmail() == "" && student.getGender() == null) {
				index = 1;
//				unlocked = 1;
			}

		}
//		System.out.println("message: " + message);
//		System.out.println("Sau: index =  " + index + "page= " + page);

		if (index == 1) {
			return showPage1(modelMap, session);
		} else if (index == 2 && student.getEmail() != null) {

			return admissionpage2(modelMap, idStd);
		} else if (index == 3 && !isNullOrEmpty(student.getProofOfAdmission()) && !isNullOrEmpty(student.getEmail())) {

			return admissionpage3(modelMap, idStd);
		} else if (index == 4 && !isNullOrEmpty(student.getProofOfAdmission()) && !isNullOrEmpty(student.getEmail()) ) {

			return admissionPage4(modelMap, idStd, request);
		} else if (index == 5 && student.getEmailSended() != null && !isNullOrEmpty(student.getEmail())) {
			return successPage(student, modelMap);
		} else {
			page =0;
			unlocked=1;
			index=1;
			return showPage1(modelMap, session);
		}

		// return "error";
	}

	@PostMapping("/admission")
	@ResponseBody
	public String admissionPagePost(@RequestBody Map<String, Object> jsonData, ModelMap modelMap, HttpSession session,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		Integer page = (Integer) jsonData.get("page");
		// Xử lý tham số page và trả về kết quả nếu cần thiết
		Student student = (Student) session.getAttribute("loggedInStudent");
//		session.setAttribute("loggedInStudent", student);
//		System.out.println(session.getMaxInactiveInterval());
		String idStd = student.getId();

		if (page != null) {
			this.page = page;
			if (page == 1) {
				index = 1;
				return "OK";
			} else if (page == 2) {
				index = 2;
				return "OK";
			} else if (page == 3) {
				index = 3;
				return "OK";
			} else if (page == 4) {
				index = 4;
				return "OK";
			}
		}
		return idStd;
	}

	@GetMapping("/admission/logout")
	public String LogoutAdmission(HttpSession session) throws Exception {
		index = 1;
		unlocked = 1;
		page = 0;
		session.removeAttribute("loggedInStudent");
		return "redirect:/login";
	}

	@GetMapping("/admission/back")
	public String back() throws Exception {
		--index;
		return "redirect:/";
	}
}