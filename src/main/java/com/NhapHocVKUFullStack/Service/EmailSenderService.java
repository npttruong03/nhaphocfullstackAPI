package com.NhapHocVKUFullStack.Service;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.NhapHocVKUFullStack.models.DocumentItems;
import com.NhapHocVKUFullStack.models.Student;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailSenderService {

	@Autowired
	JavaMailSender javaMailSender;
	@Autowired
	TemplateEngine templateEngine;
	@Autowired
	StudentTuitionService studentTuitionService;
	@Autowired
	TuitionFeeListService tuitionFeeListService;
	@Autowired 
	DocumentItemsService documentItemsService;

	public void sendMail(Student student, String subject, String content, String toEmail) throws MessagingException {

		MimeMessage message = javaMailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);
		helper.setTo(toEmail);
		helper.setSubject(subject);

		helper.setText(content, true);
		javaMailSender.send(message);
	}

	public void sendEmail(String to, String subject, String templateName, Student student) throws Exception {
		MimeMessage message = javaMailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true, StandardCharsets.UTF_8.name());
		helper.setTo(to);
		helper.setSubject(subject);

		// Render trang thành công từ template Thymeleaf
		String renderedPage = renderTemplate(templateName, student);

		// Thiết lập nội dung email
		helper.setText(renderedPage, true); // Đảm bảo content chứa HTML

		javaMailSender.send(message);
	}

	private String renderTemplate(String templateName, Student student) throws Exception {
		Context context = new Context();
		// Cung cấp các dữ liệu cần thiết cho trang thành công
		context.setVariable("student", student);
		context.setVariable("studentTuition", studentTuitionService.getStudentTuitionByIdStudentApi(student.getId()));
		;
		context.setVariable("tuitionFeeList", tuitionFeeListService
				.searchTuitionListByKhoaIdAndMajorsId(student.getKhoa().getId(), student.getMajors().getId()));
		List<DocumentItems> documentItems = documentItemsService.getAll();
		context.setVariable("documentItems",
				documentItems.stream()
						.filter(docItem -> docItem.getKhoa().getId().equals(student.getKhoa().getId())
								&& docItem.getMajors().getId().equals(student.getMajors().getId())
								&& (student.getMethod() != null
										? docItem.getMethod().getId().equals(student.getMethod().getId())
										: true))
						.collect(Collectors.toList()));
		return templateEngine.process(templateName, context);
	}
//	public void sendMailAdmission(Student student) throws MessagingException {
//		String updateTime = student.getUpdateTime(); // Giả sử giá trị trả về là "Fri Aug 18 15:28:23 ICT 2023"
//
//		// Định dạng mong muốn
//		
//		DateTimeFormatter inputFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
//		DateTimeFormatter outputFormat = DateTimeFormatter.ofPattern("H:m:s dd/MM/yyyy");
//		String formattedTime = "";
//		try {
//		    LocalDateTime dateTime = LocalDateTime.parse(updateTime, inputFormat);
//		    formattedTime = dateTime.format(outputFormat);
//		} catch (java.time.format.DateTimeParseException e) {
//			formattedTime = student.getUpdateTime();
//		    e.printStackTrace();
//		}
//	
////		System.out.println("date time" + formattedTime);
//
//		String content = "<html><body style='font-family: Arial, sans-serif;'>";
//		content += "<div style='background-color: #f2f2f2; padding: 20px;'>"; // Thêm phần header cho email
//		content += "<h2 style='text-align: center;'>Đăng ký nhập học thành công</h2>";
//		content += "</div>";
//		content += "<div style='padding: 20px;'>";
//		content += "<p style='font-size: 16px;'>Chào " + student.getFullName() + ",</p>";
//		content += "<p style='font-size: 16px;'>Bạn đã hoàn thành thủ tục nhập học thành công. Thời gian hoàn thành thủ tục nhập học online: "
//				+  formattedTime + "</p>";
//		content += "<div> <h3 style='font-size: 16px;'>Thông tin cơ bản:</h3>";
//		content += "<ul>";
//		content += "<li style='font-size: 16px;'>Họ và tên: " + student.getFullName() + "</li>";
//		content += "<li style='font-size: 16px;'>Ngày sinh: " + student.getBirthday() + "</li>";
//		content += "<li style='font-size: 16px;'>Số CMND/CCCD: " + student.getNumberCIC() + "</li>";
//		content += "<li style='font-size: 16px;'>Giới tính: " + student.getGender() + "</li>";
//		content += "<li style='font-size: 16px;'>Nơi Sinh: " + student.getBirthplace() + "</li>";
//		content += "<li style='font-size: 16px;'>Quê quán: " + student.getHomeTown() + "</li>";
//		content += "<li style='font-size: 16px;'>Dân tộc: " + student.getEthnic() + "</li>";
//		content += "</ul>";
//		content += "</div>";
//		content += "<div> <h3 style='font-size: 16px;'>Thông tin hồ sơ:</h3>";
//		content += "<ul>";
////	    content += "<li style='font-size: 16px;'>Họ và tên: " + student.getFullName() + "</li>";
////	    content += "<li style='font-size: 16px;'>Ngày sinh: " + student.getBirthday() + "</li>";
//		content += "<li style='font-size: 16px;'>Phương thức trúng tuyển: "
//				+ student.getMethod().getTen_phuong_thuc() + "</li>";
//		content += "<li style='font-size: 16px;'>Mã ngành: " + student.getMajors().getMajorsID() + "</li>";
//		content += "<li style='font-size: 16px;'>Tên ngành: " + student.getMajors().getMajorsName() + "</li>";
//		content += "<li style='font-size: 16px;'>Tên khóa: " + student.getKhoa().getKhoa() + "</li>";
//		content += "<li style='font-size: 16px;'>Chương trình học: " + student.getCurriculum().getName()
//				+ "</li>";
//		content += "</ul>";
//		content += "</div>";
//		content += "<div> <h3 style='font-size: 16px;'>Thông tin bổ sung:</h3>";
//		content += "<ul>";
////	    content += "<li style='font-size: 16px;'>Họ và tên: " + student.getFullName() + "</li>";
////	    content += "<li style='font-size: 16px;'>Ngày sinh: " + student.getBirthday() + "</li>";
//		content += "<li style='font-size: 16px;'>Trường THPT lớp 10: " + student.getSchool10() + "</li>";
//		content += "<li style='font-size: 16px;'>Trường THPT lớp 11: " + student.getSchool11() + "</li>";
//		content += "<li style='font-size: 16px;'>Trường THPT lớp 12: " + student.getSchool12() + "</li>";
//		content += "<li style='font-size: 16px;'>Trường THPT tốt nghiệp: " + student.getHighSchool() + "</li>";
//		content += "<li style='font-size: 16px;'>Mã trường: " + student.getIdHighSchool() + "</li>";
//		content += "</ul>";
//		content += "</div>";
//		content += "<div style='background-color: #f2f2f2; padding: 20px;'>"; // Thêm phần footer cho email
//		content += "<p style='font-size: 14px;'>Cảm ơn bạn đã đăng ký nhập học tại trường chúng tôi.</p>";
//		content += "</div>";
//		
//		content += "</body></html>";
////		sendMail(student, "Đăng ký nhập học thành công", content, "vudinhtan.it@gmail.com");
//
//	}

}
