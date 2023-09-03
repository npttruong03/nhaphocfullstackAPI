package com.NhapHocVKUFullStack.controllers;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import com.NhapHocVKUFullStack.Service.EthnicService;
import com.NhapHocVKUFullStack.Service.KhoaService;
import com.NhapHocVKUFullStack.Service.MajorsService;
import com.NhapHocVKUFullStack.Service.ReligionService;
import com.NhapHocVKUFullStack.Service.StudentLoginService;
import com.NhapHocVKUFullStack.Service.StudentService;
import com.NhapHocVKUFullStack.Service.TuitionFeeListService;
import com.NhapHocVKUFullStack.models.Khoa;
import com.NhapHocVKUFullStack.models.Majors;
import com.NhapHocVKUFullStack.models.Message;
import com.NhapHocVKUFullStack.models.Student;
import com.NhapHocVKUFullStack.models.TuitionFeeList;

import cn.apiclub.captcha.Captcha;
import cn.apiclub.captcha.backgrounds.GradiatedBackgroundProducer;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping()
public class StudentLoginController {
	@Autowired
	private StudentLoginService studentLoginService;
	@Autowired
	private StudentService studentService;
	@Autowired 
	private TuitionFeeListService tuitionFeeListService;
	@Autowired
	private KhoaService khoaService;
	@Autowired
	private MajorsService majorsService;
	private Message message = null;

	@GetMapping("/login")
	public String showLoginForm(Model model) throws Exception {
		model.addAttribute("student", new Student());
		Khoa khoa = khoaService.getNewestKhoa();
		List<Majors> majors = majorsService.getDataFromAPI();
		for (Majors major : majors) {
			if (major.getMajorsID().equals("7480201"))
			{
				model.addAttribute("hocphicntt", tuitionFeeListService.searchTuitionListByKhoaIdAndMajorsId(khoa.getId(), major.getId()));
			}
			if (major.getMajorsID().equals("7340101EL"))
			{
				model.addAttribute("hocphikt", tuitionFeeListService.searchTuitionListByKhoaIdAndMajorsId(khoa.getId(), major.getId()));
			}
		}
		
		return "student/admission/login";
	}

	// StudentLoginController
	@GetMapping("/captcha-login")
	public void getCaptchaImage(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Captcha captcha = new Captcha.Builder(200, 50).addText().addNoise()
				.addBackground(new GradiatedBackgroundProducer()).build();

		// Save captcha into session
		request.getSession().setAttribute("captcha1", captcha.getAnswer());

		// Send captcha image in the response
		response.setContentType("image/png");
		ServletOutputStream outputStream = response.getOutputStream();
		ImageIO.write(captcha.getImage(), "png", outputStream);
		outputStream.flush();
		outputStream.close();
	}

	@PostMapping("/admission/login")
	public String login(@ModelAttribute("student") Student student, @RequestParam("captchaInput1") String captchaInput,
			Model model, HttpSession session, RedirectAttributesModelMap redirect) {
		// Get the CAPTCHA answer from the session
		String sessionCaptcha = (String) session.getAttribute("captcha1");

		// Validate the CAPTCHA input
		if (captchaInput == null || sessionCaptcha == null || !captchaInput.equals(sessionCaptcha)) {
			redirect.addFlashAttribute("student", student);
			redirect.addFlashAttribute("error", "Thông tin đăng nhập không chính xác hoặc CAPTCHA không đúng!");
			return "redirect:/login"; // Display login page with error message if login or CAPTCHA is incorrect
		}

//        String studentBirthday = studentLoginService.convertToCommonDateFormat(student.getBirthday());
		String studentBirthday = student.getBirthday();

		Student loggedInStudent = studentService.login(studentBirthday, student.getNumberCIC());
		
		if (loggedInStudent != null) {
			LocalDateTime startDate = LocalDateTime.parse(loggedInStudent.getKhoa().getThoiGianBatDau().substring(0,23));
			LocalDateTime endDate = LocalDateTime.parse(loggedInStudent.getKhoa().getThoiGianKetThuc().substring(0,23));
			LocalDateTime currentDate = LocalDateTime.now();
			if (startDate.isBefore(currentDate) && endDate.isAfter(currentDate))
			{
				session.setAttribute("loggedInStudent", loggedInStudent);
				return "redirect:/"; // Redirect to page1 if login is successful
			}
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy");
			redirect.addFlashAttribute("alert", "Thời gian nhập học bắt đầu từ " + startDate.format(formatter) + " đến " + endDate.format(formatter) + ". Vui lòng tiến hành nhập học trong thời gian trên!");
			return "redirect:/login"; // Display login page with error message if login fails 
		} else {
			redirect.addFlashAttribute("student", student);
			redirect.addFlashAttribute("error", "Thông tin đăng nhập không chính xác!");
			return "redirect:/login"; // Display login page with error message if login fails
		}
	}

}
