package com.NhapHocVKUFullStack.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.NhapHocVKUFullStack.Service.DocumentItemsService;
import com.NhapHocVKUFullStack.Service.StatisticsFeeListServiceMoney;
import com.NhapHocVKUFullStack.Service.StudentService;
import com.NhapHocVKUFullStack.Service.StudentTuitionService;
import com.NhapHocVKUFullStack.Service.TuitionFeeListService;
import com.NhapHocVKUFullStack.models.DocumentItems;
import com.NhapHocVKUFullStack.models.Student;
import com.NhapHocVKUFullStack.models.StudentTuition;

@Controller
@RequestMapping("/admin/statistical")
public class StatisticalController {
	@Autowired
	private StatisticsFeeListServiceMoney statisticsFeeListService;
	@Autowired
	private TuitionFeeListService tuitionFeeListService;
	@Autowired
	private StudentService studentService;
	@Autowired
	 private  DocumentItemsService documentItemsService;
	@Autowired
	 private  StudentTuitionService studentTuitionService;
	@GetMapping
	public String index(ModelMap modelMap) throws Exception {
		  List<StudentTuition> tuitionFeeLists = statisticsFeeListService.getDataFromApi();
		    int submittedCount = 0;
		    int notSubmittedCount = 0;
		
		    for (StudentTuition tuitionFeeList : tuitionFeeLists) {
		        if (tuitionFeeList.getId() != null) {
		            submittedCount++;
		        } else {
		        notSubmittedCount++;
		        }
		    }
		    
		    Map<String, Integer> documentItemCountMap = documentItemsService.calculateDocumentItemCount();
	        modelMap.addAttribute("documentItemCountMap", documentItemCountMap);
	        Map<String, Integer> documentCountMap = documentItemsService.calculateDocumentCount();
	        modelMap.addAttribute("documentCountMap", documentCountMap);
		    modelMap.addAttribute("submittedCount", submittedCount);
		    modelMap.addAttribute("notSubmittedCount", notSubmittedCount);
		    Map<String, Integer> tuitionByType = tuitionFeeListService.calculateTuitionByType();
	        modelMap.addAttribute("tuitionByType", tuitionByType);
	        Map<String, Integer> tuitionByTypenganh = tuitionFeeListService.calculateTuitionByTypenganh();
	        modelMap.addAttribute("tuitionByTypenganh", tuitionByTypenganh);
	        Map<String, Integer> tuitionByMajor = studentTuitionService.calculateTotalTuitionByMajor();
	        modelMap.addAttribute("tuitionByMajor", tuitionByMajor);
	        Map<String, Integer> tuitionByDay = statisticsFeeListService.calculateTuitionByTypengay();
	        modelMap.addAttribute("tuitionByDay", tuitionByDay);
	        Map<String, Integer> tuitionByNgayThu = statisticsFeeListService.calculateTuitionByNgayThu();
	        modelMap.addAttribute("tuitionByNgayThu", tuitionByNgayThu);
	        List<DocumentItems> documentItems = documentItemsService.getAll();
	        studentService.getAllStudentsFromAPI();
	        Map<String, Integer> tuitionFeeCount = tuitionFeeListService.countTuitionNames();
	        modelMap.addAttribute("tuitionFeeCount", tuitionFeeCount);
//	        Map<String, Integer> documentItemCountMap = statisticsFeeListService.calculateDocumentItemsCount();
//	       
	//
//	        modelMap.addAttribute("documentItemCountMap", documentItemCountMap);
	        
	        Student students = new Student();
	        int greenCount = 0;
	        for (DocumentItems docItem : documentItems) {
	            if (students.getId() !=null  && students.getDocuItems().getId().equals(docItem.getId())){
	                // Check if the student's document items contain the current docItem's ID
	                // Assuming getDocumentItems() returns a list, but getId() is for a single item
	                greenCount++;
	            }
	        }

	        int totalSubmission = 0;
	        int totalNotSubmitted = 0;

	        for (Integer document : documentCountMap.values()) {
	            if (document != null && document > 0) {
	                totalSubmission += 1;
	            } else {
	                totalNotSubmitted += 1;
	            }
	        }
	        modelMap.addAttribute("greenCount", greenCount);
	        modelMap.addAttribute("totalSubmission", totalSubmission);
	        modelMap.addAttribute("totalNotSubmitted", totalNotSubmitted);
		return "statistical";
	}
	
}