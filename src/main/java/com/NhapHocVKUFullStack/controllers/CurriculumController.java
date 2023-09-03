package com.NhapHocVKUFullStack.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.NhapHocVKUFullStack.Service.CurriculumService;
import com.NhapHocVKUFullStack.Service.KhoaService;
import com.NhapHocVKUFullStack.Service.MajorsService;
import com.NhapHocVKUFullStack.models.Area;
import com.NhapHocVKUFullStack.models.Curriculum;
import com.NhapHocVKUFullStack.models.Message;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import jakarta.websocket.server.PathParam;

@Controller
@RequestMapping("/admin/curriculum")
public class CurriculumController {
	@Autowired
	private CurriculumService curriculumService;
	
	@Autowired 
	private MajorsService majorsService;
	
	@Autowired
	private KhoaService khoaService;
	Message message = null;
	
	@GetMapping
	public String getAll(ModelMap modelMap) throws Exception{
		modelMap.addAttribute("currys", curriculumService.getAll());
		modelMap.addAttribute("majors", majorsService.getDataFromAPI());
		modelMap.addAttribute("khoas", khoaService.getDataFromAPI());
		if (message != null)
		{
			modelMap.addAttribute("message", message);
			message = null;
		}
		return "curriculum/curriculum";
	}
	
	@GetMapping("/add")
	public String add(ModelMap modelMap) throws Exception {
		Curriculum curry = new Curriculum();
		modelMap.addAttribute("curry", curry);
		modelMap.addAttribute("majors", majorsService.getDataFromAPI());
		modelMap.addAttribute("khoas", khoaService.getDataFromAPI());
		if (message != null)
		{
			modelMap.addAttribute("message", message);
			message = null;
		}
		return "curriculum/curriculum_add";
	}
	
	@PostMapping("/save")
	public String save(@ModelAttribute("curry") Curriculum curry) {
		Boolean rs;
		try {
			rs = curriculumService.saveCurry(curry);
			message = new Message();
			message.setStatus("success");
			message.setMessage("Thêm chương trình học thành công!");
			return "redirect:/admin/curriculum";
			
		} catch (Exception e) {
			message = new Message();
			message.setStatus("fail");
			message.setMessage("Thêm chương trình học không thành công!");
			return "redirect:/admin/curriculum/add";
		}
	}
	
	@PostMapping("/update")
	public String update(@ModelAttribute("curry") Curriculum curry, ModelMap modelMap) {
		Boolean rs;
		try {
			rs = curriculumService.updateCurry(curry);
			message = new Message();
			message.setStatus("success");
			message.setMessage("Cập nhật thành công!");
			
			return "redirect:/admin/curriculum";
			
		} catch (Exception e) {
			message = new Message();
			message.setStatus("fail");
			message.setMessage("Cập nhật không thành công!");
			
			return "redirect:/admin/curriculum/edit/" + curry.getId();
		} 
	}
	
	@GetMapping("/edit/{id}")
	public String edit(@PathVariable int id, ModelMap modelMap) {
		try {
			Curriculum curry = curriculumService.getCurry(id);
			modelMap.addAttribute("curry", curry);
			modelMap.addAttribute("majors", majorsService.getDataFromAPI());
			modelMap.addAttribute("khoas", khoaService.getDataFromAPI());
			if (message != null)
			{
				modelMap.addAttribute("message", message);
				message = null;
			}
			
			return "curriculum/curriculum_edit";
		} catch (Exception e) {
			return "redirect:/admin/curriculum/add";

		}
	}
	
}
