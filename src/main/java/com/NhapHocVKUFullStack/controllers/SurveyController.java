package com.NhapHocVKUFullStack.controllers;

import java.util.List;

import org.apache.poi.ss.formula.functions.IfFunc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
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

import com.NhapHocVKUFullStack.Service.CurriculumService;
import com.NhapHocVKUFullStack.Service.KhoaService;
import com.NhapHocVKUFullStack.Service.MajorsService;
import com.NhapHocVKUFullStack.Service.SurveyService;
import com.NhapHocVKUFullStack.Service.TuitionFeeListService;
import com.NhapHocVKUFullStack.config.AppConfig;
import com.NhapHocVKUFullStack.models.Majors;
import com.NhapHocVKUFullStack.models.Message;
import com.NhapHocVKUFullStack.models.Survey;
import com.NhapHocVKUFullStack.models.TuitionFeeList;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/admin/survey")
public class SurveyController {
	private Message message = null;
	
	@Autowired
	private SurveyService surveyService;
	@Autowired
	private KhoaService khoaService;
	@Autowired
	private MajorsService majorsService;
	@GetMapping()
	public String index(ModelMap modelMap) throws Exception {
		if (message != null)
		{
			modelMap.addAttribute("message", message);
			message = null;
		}
		modelMap.addAttribute("khoas", this.khoaService.getDataFromAPI());
		modelMap.addAttribute("majors", this.majorsService.getDataFromAPIsttTrue());
//		if (khoaId == null&&curriculumId==null &&majorsId==null )
			modelMap.addAttribute("surveys", this.surveyService.getDataFromApi());
		
//			modelMap.addAttribute("tuitions", this.tuitionFeeListService.searchDataFromAPI(khoaId,majorsId,curriculumId));
		return "survey/index";
	}
	
	@GetMapping("/create")
	public String showCreate(ModelMap modelMap)throws Exception {
		Survey Survey = new Survey();
		if (message != null)
		{
			modelMap.addAttribute("message", message);
			message = null;
		}
		modelMap.addAttribute("survey", Survey);
		modelMap.addAttribute("khoas", this.khoaService.getDatasttKhoatrue());
		modelMap.addAttribute("majors", this.majorsService.getDataFromAPIsttTrue());
		return "survey/create";
	}
	@GetMapping("/createShared")
	public String showCreateShare(ModelMap modelMap)throws Exception {
		Survey Survey = new Survey();
		if (message != null)
		{
			modelMap.addAttribute("message", message);
			message = null;
		}
		modelMap.addAttribute("survey", Survey);
		modelMap.addAttribute("khoas", this.khoaService.getDatasttKhoatrue());
		return "survey/createShared";
	}
	@RequestMapping(value = "/createShared", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public String CreateShare(@ModelAttribute("survey") Survey Survey, HttpServletRequest request)throws Exception {
		try {
			for (Majors majors : this.majorsService.getDataFromAPIsttTrue()) {
				Survey.setMajors(majors);
				Survey.setShared(1);
				surveyService.post(Survey);
			}
			message = new Message();
			message.setStatus("success");
			message.setMessage("Thêm khảo sát thành công!");
			return "redirect:/admin/survey";

		} catch (Exception e) {
			message = new Message();
			message.setStatus("fail");
			message.setMessage("Thêm khảo sát không thành công!"+e.toString());
			return "redirect:/admin/survey/createShared";
		}
	}
	
	@RequestMapping(value = "/create", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public String createTuition(@ModelAttribute("survey") Survey Survey, HttpServletRequest request) {
		try {
			surveyService.post(Survey);
			message = new Message();
			message.setStatus("success");
			message.setMessage("Thêm khảo sát thành công!");
			return "redirect:/admin/survey";

		} catch (Exception e) {
			message = new Message();
			message.setStatus("fail");
			message.setMessage("Thêm khảo sát không thành công!"+e.toString());
			return "redirect:/admin/survey/create";
		}
	}
	
	@GetMapping("/edit/{id}")
	public String HomeEdit(ModelMap modelMap, @Validated @PathVariable("id") int id) throws Exception {
		if (message != null)
		{
			modelMap.addAttribute("message", message);
			message = null;
		}
		Survey Survey = surveyService.getById(id);
		modelMap.addAttribute("Survey", Survey);
		modelMap.addAttribute("khoas", this.khoaService.getDatasttKhoatrue());
		if(Survey.getShared()!=1) {
		modelMap.addAttribute("majors", this.majorsService.getDataFromAPIsttTrue());
		}
		return "survey/edit";
	}
	
	@PostMapping("/edit")
	public String edit(@ModelAttribute("Survey") Survey Survey) {
		try {
				if(Survey.getShared()==1) {
					for (Integer ids : surveyService.ids(Survey.getName(), Survey.getUrl())) {
						Survey.setMajors(surveyService.getMajorsById(ids));
						surveyService.Update(Survey,ids);
					}
				}else {
					surveyService.Update(Survey);
				}
			
			message = new Message();
			message.setStatus("success");
			message.setMessage("Cập nhật thành công!");
			return "redirect:/admin/survey";
		} catch (Exception e) {
			message = new Message();
			message.setStatus("fail");
			message.setMessage("Cập nhật không thành công!" +e.toString());

			return "redirect:/admin/survey/edit/" + Survey.getId();
		}
	}
	
	@PostMapping("/editSttFlase")
	public String edit() {
		try {
			surveyService.UpdatesttFalse();
			message = new Message();
			message.setStatus("success");
			message.setMessage("Cập nhật thành công!");
			return "redirect:/admin/survey";
		} catch (Exception e) {
			message = new Message();
			message.setStatus("fail");
			message.setMessage("Cập nhật không thành công!"+e.toString());

			return "redirect:/admin/survey";
		}
	}
	
}
