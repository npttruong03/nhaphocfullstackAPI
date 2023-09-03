package com.NhapHocVKUFullStack.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.NhapHocVKUFullStack.Service.AreaService;
import com.NhapHocVKUFullStack.models.Area;
import com.NhapHocVKUFullStack.models.Message;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import jakarta.servlet.http.HttpSession;


@Controller
@RequestMapping("/admin/area")
public class AreaController {
	@Autowired
	private AreaService areaService;
	Message message = null;
	
	@GetMapping
	public String getAll(ModelMap modelMap) {
		try {
			modelMap.addAttribute("areas", areaService.getAll());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (message != null)
		{
			modelMap.addAttribute("message", message);
			message = null;
		}
		return "area/area";
	}
	
	@GetMapping("/add")
	public String add(ModelMap modelMap) {
		Area area = new Area();
		modelMap.addAttribute("area", area);
		if (message != null)
		{
			modelMap.addAttribute("notify", message);
			message = null;
		}
		return "area/area_add";
	}
	
	@PostMapping("/save")
	public String save(@ModelAttribute("area") Area area) {
		Boolean rs;
		try {
			rs = areaService.saveArea(area);
			message = new Message();
			message.setStatus("success");
			message.setMessage("Thêm khu vực thành công!");
			return "redirect:/admin/area";
			
		} catch (Exception e) {
			e.printStackTrace();
			message = new Message();
			message.setStatus("fail");
			message.setMessage("Thêm khu vực không thành công!");
			return "redirect:/admin/area/add";
		}
	}
	
	@PostMapping("/update")
	public String update(@ModelAttribute("area") Area area, ModelMap modelMap) {
		Boolean rs;
		try {
			rs = areaService.updateArea(area);
			message = new Message();
			message.setStatus("success");
			message.setMessage("Cập nhật thành công!");
			
			return "redirect:/admin/area";
			
		} catch (Exception e) {
			message = new Message();
			message.setStatus("fail");
			message.setMessage("Cập nhật không thành công!");
			
			return "redirect:/admin/area/edit/" + area.getId();
		}	
	}
	
	@GetMapping("/edit/{id}")
	public String edit(@PathVariable int id, ModelMap modelMap) {
		try {
			Area area = areaService.getArea(id);
			modelMap.addAttribute("area", area);
			if (message != null)
			{
				modelMap.addAttribute("message", message);
				message = null;
			}
			
			return "area/area_edit";
		} catch (Exception e) {
			return "redirect:/admin/area/add";

		}
		
	}
}
