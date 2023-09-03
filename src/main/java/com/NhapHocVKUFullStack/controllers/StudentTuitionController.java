package com.NhapHocVKUFullStack.controllers;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.poi.ss.formula.functions.IfFunc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.NhapHocVKUFullStack.Service.CurriculumService;
import com.NhapHocVKUFullStack.Service.KhoaService;
import com.NhapHocVKUFullStack.Service.MajorsService;
import com.NhapHocVKUFullStack.Service.StudentService;
import com.NhapHocVKUFullStack.Service.StudentTuitionService;
import com.NhapHocVKUFullStack.Service.TuitionFeeListService;
import com.NhapHocVKUFullStack.config.AppConfig;
import com.NhapHocVKUFullStack.models.Message;
import com.NhapHocVKUFullStack.models.Student;
import com.NhapHocVKUFullStack.models.StudentTuition;
import com.NhapHocVKUFullStack.models.TuitionFeeList;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/admin/studentTuition")
public class StudentTuitionController {
	private Message message = null;
	@Autowired
	private StudentTuitionService studentTuitionService;

	@Autowired
	private TuitionFeeListService tuitionFeeListService;

	@Autowired
	private MajorsService majorsService;

	@Autowired
	private StudentService studentService;

	@Autowired
	private KhoaService khoaService;

	@Autowired
	private CurriculumService curriculumService;

	private final Lock lock = new ReentrantLock();
//	private final AppConfig appConfig;

//    @Autowired
//    public StudentTuitionController(AppConfig appConfig) {
//        this.appConfig = appConfig;
//    }

	private ArrayList<TuitionFeeList> tuitionFeeLists;

	@GetMapping
	public String index(ModelMap modelMap) throws Exception {
		if (message != null) {
			modelMap.addAttribute("message", message);
			message = null;
		}
		modelMap.addAttribute("tuitions", this.tuitionFeeListService.getDataStt());
		modelMap.addAttribute("stFees", this.studentTuitionService.getDataStt());
		modelMap.addAttribute("majors", this.majorsService.getDataFromAPI());
		modelMap.addAttribute("khoas", this.khoaService.getDataFromAPI());
		modelMap.addAttribute("curries", this.curriculumService.getAllSttTrue());
//		String cookieValue = appConfig.cookieStore().getCookie();
//		modelMap.addAttribute("cookieValue", cookieValue);
		List<TuitionFeeList> tuitionFeeLists = this.tuitionFeeListService.getDataFromApi();
		String Stringtuitionfee = "";

		for (int i = 0; i < tuitionFeeLists.size(); i++) {
			Stringtuitionfee += tuitionFeeLists.get(i).getId().toString();
			if (i < tuitionFeeLists.size() - 1) {
				Stringtuitionfee += ",";
			}
		}
		modelMap.addAttribute("TuitionFeeList", Stringtuitionfee);
		return "studentTuition/index";
	}

	@GetMapping("/detail/{id}")
	public String detail(ModelMap modelMap, @Validated @PathVariable("id") int id) throws Exception {
		lock.lock(); // Acquire the lock
		try {
			StudentTuition stTuition = studentTuitionService.getById(id);
			modelMap.addAttribute("stFee", stTuition);

			String listFees = stTuition.getTuitionFeeList();
			tuitionFeeLists = new ArrayList<TuitionFeeList>();
			if (listFees != null && !listFees.isEmpty()) {
				String fee[] = listFees.split(",");
				for (int i = 0; i < fee.length; i++) {
					int tuition = Integer.parseInt(fee[i]);
					TuitionFeeList tuitionFeeList = tuitionFeeListService.getById(tuition);
					tuitionFeeLists.add(tuitionFeeList);
				}
			}
			modelMap.addAttribute("fees", tuitionFeeLists);
		} finally {
			lock.unlock(); // Release the lock even if an exception occurs
		}
		return "studentTuition/detail";
	}

	@GetMapping("/print/{id}")
	public String print(ModelMap modelMap, @Validated @PathVariable("id") int id) throws Exception {
		lock.lock(); // Acquire the lock
		try {
			StudentTuition stTuition = studentTuitionService.getById(id);
			modelMap.addAttribute("stFee", stTuition);
			String listFees = stTuition.getTuitionFeeList();
			tuitionFeeLists = new ArrayList<TuitionFeeList>();
			if (listFees != null && !listFees.isEmpty()) {
				String fee[] = listFees.split(",");
				for (int i = 0; i < fee.length; i++) {
					int tuition = Integer.parseInt(fee[i]);
					TuitionFeeList tuitionFeeList = tuitionFeeListService.getById(tuition);
					tuitionFeeLists.add(tuitionFeeList);
				}
			}
			modelMap.addAttribute("fees", tuitionFeeLists);
		} finally {
			lock.unlock(); // Release the lock even if an exception occurs
		}
		return "studentTuition/print";
	}

	@GetMapping("/create/{id}")
	public String showCreate(ModelMap modelMap, @Validated @PathVariable("id") String id) throws Exception {
		lock.lock(); // Acquire the lock
		try {
			Student student = studentService.getStudentById(id);

			if (message != null) {
				modelMap.addAttribute("message", message);
				message = null;
			}

			if (this.studentTuitionService.getByIdStudent().contains(student) == false) {

				LocalDate currentDate = LocalDate.now();
				int day = currentDate.getDayOfMonth();
				int month = currentDate.getMonthValue();
				int year = currentDate.getYear();

				String formattedDate = String.format("%02d/%02d/%04d", day, month, year);
				modelMap.addAttribute("daycreate", formattedDate);
				System.out.println(formattedDate);

				modelMap.addAttribute("st", student);
				StudentTuition studentTuition = new StudentTuition();
				studentTuition.setIdStudent(student);
				modelMap.addAttribute("stFee", studentTuition);
				int khoaId = student.getKhoa().getId();
				String majorsId = student.getMajors().getId();
				int curriculumId = student.getCurriculum().getId();
				modelMap.addAttribute("fees",
						tuitionFeeListService.searchDataFromAPISTTtrue(khoaId, majorsId, curriculumId));
				// modelMap.addAttribute("fees", tuitionFeeListService.getDataFromApi());

				return "studentTuition/create";
			} else {
				int idstTuition = studentTuitionService.getIdByStudent(id);
				StudentTuition stTuition = studentTuitionService.getById(idstTuition);
				modelMap.addAttribute("stFee", stTuition);
				String listFees = stTuition.getTuitionFeeList();
				tuitionFeeLists = new ArrayList<TuitionFeeList>();
				if (listFees != null && !listFees.isEmpty()) {
					String fee[] = listFees.split(",");
					for (int i = 0; i < fee.length; i++) {
						int tuition = Integer.parseInt(fee[i]);
						TuitionFeeList tuitionFeeList = tuitionFeeListService.getById(tuition);
						tuitionFeeLists.add(tuitionFeeList);
					}
				}
				modelMap.addAttribute("fees", tuitionFeeLists);

				return "studentTuition/detail";
			}
		} finally {
			lock.unlock(); // Release the lock even if an exception occurs
		}

	}

	@GetMapping("/edit/{id}")
	public String HomeEdit(ModelMap modelMap, @Validated @PathVariable("id") int id) throws Exception {
		lock.lock(); // Acquire the lock
		try {
		StudentTuition stTuition = this.studentTuitionService.getById(id);
		if (message != null) {
			modelMap.addAttribute("message", message);
			message = null;
		}

		LocalDate currentDate = LocalDate.now();
		int day = currentDate.getDayOfMonth();
		int month = currentDate.getMonthValue();
		int year = currentDate.getYear();

		String formattedDate = String.format("%02d/%02d/%04d", day, month, year);
		modelMap.addAttribute("dayupdate", formattedDate);

		System.out.println(formattedDate);
		modelMap.addAttribute("stFee", stTuition);
		Student student = stTuition.getIdStudent();
		int khoaId = student.getKhoa().getId();
		String majorsId = student.getMajors().getId();
		int curriculumId = student.getCurriculum().getId();
		// hiện ra các checkbox mà thí sinh có thể nộp
		modelMap.addAttribute("fees", tuitionFeeListService.searchDataFromAPISTTtrue(khoaId, majorsId, curriculumId));
		// modelMap.addAttribute("fees", tuitionFeeListService.getDataFromApi());

		// Checked các checkbox mà thí sinh đã nộp
		String listFees = stTuition.getTuitionFeeList();
		tuitionFeeLists = new ArrayList<TuitionFeeList>();
		if (listFees != null && !listFees.isEmpty()) {
			String fee[] = listFees.split(",");
			for (int i = 0; i < fee.length; i++) {
				int tuition = Integer.parseInt(fee[i]);
				TuitionFeeList tuitionFeeList = tuitionFeeListService.getById(tuition);
				tuitionFeeLists.add(tuitionFeeList);
			}
			modelMap.addAttribute("feest", tuitionFeeLists);
		}
		 } finally {
		        lock.unlock(); // Release the lock even if an exception occurs
		    }
		return "studentTuition/edit";
	}

	// Lấy studentTuition từ idStudent
	@GetMapping("/findStudentTuitionByStudentId/{idStudent}")
	@ResponseBody
	public ResponseEntity<StudentTuition> findStudentTuitionByStudentId(@PathVariable("idStudent") String idStudent) {
		try {
			StudentTuition studentTuition = studentTuitionService.getStudentTuitionByIdStudentApi(idStudent);
			if (studentTuition != null) {
				return ResponseEntity.ok(studentTuition);
			} else {
				return null;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	// post

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public String createTuition(@ModelAttribute("stFee") StudentTuition studentTuition,
			@RequestParam("file") MultipartFile file, HttpServletRequest request) {
		if (!file.isEmpty()) {
			try {
				// Lưu trữ tệp tin vào thư mục lưu trữ
//		    	StringBuilder fileName = new StringBuilder();
				String newFileName = UUID.randomUUID().toString() + "-" + file.getOriginalFilename();
				String applicationPath = request.getServletContext().getRealPath("");
				String uploadFilePath = applicationPath + File.separator + "uploads";
				File uploadFolder = new File(uploadFilePath);
				if (!uploadFolder.exists()) {
					uploadFolder.mkdir();
				}

				File imageFile = new File(uploadFilePath + File.separator + newFileName);

				Files.copy(file.getInputStream(), imageFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

				// Cập nhật đường dẫn tệp tin trong đối tượng studentTuition
				studentTuition.setProofOfTuitionPay(newFileName);
			} catch (Exception e) {
				// Xử lý ngoại lệ nếu có lỗi xảy ra
				e.printStackTrace();
				message = new Message();
				message.setStatus("fail");
				message.setMessage("Nộp Học phí không thành công! Hãy kiểm tra lại file của bạn" + e.toString());
				return "redirect:/admin/studentTuition";
			}
		}
		try {
			studentTuitionService.post(studentTuition);

			message = new Message();
			message.setStatus("success");
			message.setMessage("Nộp Học Phí thành công!");
			int id = studentTuitionService.getIdByStudent(studentTuition.getIdStudent().getId());
//			System.out.println(id);
			return "redirect:/admin/studentTuition/detail/" + id;
		} catch (Exception e) {
			e.printStackTrace();
			message = new Message();
			message.setStatus("fail");
			message.setMessage("Nộp Học phí không thành công! Hãy kiểm tra lại quyền của tài khoản" + e.toString());
			return "redirect:/admin/studentTuition";
		}

	}

	@PostMapping("/edit/{id}")
	public String edit(@ModelAttribute("stFee") StudentTuition studentTuition, @PathVariable("id") int id,
			@RequestParam("file") MultipartFile file, HttpServletRequest request) {
		if (!file.isEmpty()) {
			try {
				String newFileName = UUID.randomUUID().toString() + "-" + file.getOriginalFilename();
				String applicationPath = request.getServletContext().getRealPath("");
				String uploadFilePath = applicationPath + File.separator + "uploads";
				File uploadFolder = new File(uploadFilePath);
				if (!uploadFolder.exists()) {
					uploadFolder.mkdir();
				}

				File imageFile = new File(uploadFilePath + File.separator + newFileName);

				Files.copy(file.getInputStream(), imageFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

				// Cập nhật đường dẫn tệp tin trong đối tượng studentTuition
				studentTuition.setProofOfTuitionPay(newFileName);
			} catch (Exception e) {
				// Xử lý ngoại lệ nếu có lỗi xảy ra
				e.printStackTrace();
				message = new Message();
				message.setStatus("fail");
				message.setMessage("Nộp Học phí không thành công! Hãy kiểm tra lại file của bạn" + e.toString());
				return "redirect:/admin/studentTuition/edit/" + id;
			}
		}
		try {
			studentTuitionService.Update(studentTuition, id);
			Student student = studentService.getStudentById(studentTuition.getIdStudent().getId());
			if(studentTuition.isStatus()) {
				if(!student.getTrangThaiNhapHoc()) {
					
					student.setTrangThaiNhapHoc(true);
					studentService.updateStudent(student);
//				System.out.println("test xác nhận: " + student.getTrangThaiNhapHoc());
				}
			}else {
				student.setTrangThaiNhapHoc(false);
				studentService.updateStudent(student);
			}
			message = new Message();
			message.setStatus("success");
			message.setMessage("Cập nhật thành công!");
//			System.out.println("update" + this.studentTuitionService.getById(id).toString());
			return "redirect:/admin/studentTuition/print/" + id;
		} catch (Exception e) {
			message = new Message();
			message.setStatus("fail");
			message.setMessage("Cập nhật không thành công! Hãy kiểm tra lại quyền của tài khoản" + e.toString());

			return "redirect:/admin/studentTuition/edit/" + id;
		}
	}

	@PostMapping("/editSttFlase")
	public String edit() {
		try {
			studentTuitionService.UpdatesttFalse();
			message = new Message();
			message.setStatus("success");
			message.setMessage("Cập nhật thành công!");
			return "redirect:/admin/studentTuition";
		} catch (Exception e) {
			message = new Message();
			message.setStatus("fail");
			message.setMessage("Cập nhật không thành công! Hãy kiểm tra lại quyền của tài khoản" + e.toString());

			return "redirect:/admin/studentTuition";
		}
	}

	@GetMapping("/exportExcel")
	public void exportExcel(HttpServletResponse response) throws Exception {
		response.setContentType("application/octet-stream");
		String headerket = "Content-Disposition";
		String headerValue = "attachment;" + "filename=DanhSachNopHocPhi.xls";
		response.setHeader(headerket, headerValue);
		studentTuitionService.addDataToExcelSheet(response);

	}
}
