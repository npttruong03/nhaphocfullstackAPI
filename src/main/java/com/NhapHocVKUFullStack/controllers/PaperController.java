//package com.NhapHocVKUFullStack.controllers;
//
//import java.util.List;
//import java.util.Map;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.ui.ModelMap;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//
//import com.NhapHocVKUFullStack.Service.DocumentItemsService;
//import com.NhapHocVKUFullStack.Service.PaperService;
//import com.NhapHocVKUFullStack.models.Message;
//import com.NhapHocVKUFullStack.models.Student;
//
//@Controller
//@RequestMapping("/admin/statisticsMoneyy")
//public class PaperController {
//	
//	@Autowired
//	private PaperService paperService;
//	
//	
////	@GetMapping()
////	public String Index() {
////		return "statistical";
////	}
//	
//	@GetMapping("/statisticalshow")
//	public String getPaper(Model model) throws Exception {
////	    DocumentItemsService documentItemsService = new DocumentItemsService();
//	    Map<String, Integer> count = paperService.getDataFromAPI();
//	 // Tính tổng số lần xuất hiện
//        int totalCount = count.values().stream().mapToInt(Integer::intValue).sum();
//	    model.addAttribute("totalCount", totalCount);
//	    return "statistical";
//	}
//
//	
//	@GetMapping("/statisticalshow.")
//	public String getPapersinhvien(Model model) throws Exception {
//	 //   Map<String, Student> students = paperService.getDataFromAPIsvdn();
//		List<Student> students = paperService.getDataFromAPIsvdn();
//	    int submittedCount = 0;
//	    int notSubmittedCount = 0;
//
//	    for (Student student : students) {
//	        if (student.getDocumentItems() != null) {
//	            submittedCount++;
//	        } else {
//            notSubmittedCount++;
//	        }
//	    }
//	    model.addAttribute("submittedCount", submittedCount);
//	    model.addAttribute("notSubmittedCount", notSubmittedCount);
//	    return "statistical";
//	}
//}