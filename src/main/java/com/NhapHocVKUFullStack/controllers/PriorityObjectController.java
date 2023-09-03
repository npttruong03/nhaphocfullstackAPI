package com.NhapHocVKUFullStack.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.NhapHocVKUFullStack.Service.PriorityObjectService;
import com.NhapHocVKUFullStack.models.Message;
import com.NhapHocVKUFullStack.models.PriorityObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;



@Controller
@RequestMapping("/admin/priorityObject")
public class PriorityObjectController {
	@Autowired
	private PriorityObjectService priorityObjectService;
	private Message message = null;
	@GetMapping
	public String getAll(ModelMap modelMap) throws Exception{
		modelMap.addAttribute("objects", priorityObjectService.getAll());
		if (message != null)
		{
			modelMap.addAttribute("message", message);
			message = null;
		}
		return "priorityObject/priorityObject";
	}
	
	@GetMapping("/add")
	public String add(ModelMap modelMap) {
		PriorityObject priorityObject = new PriorityObject();
		modelMap.addAttribute("priorityObject", priorityObject);
		if (message != null)
		{
			modelMap.addAttribute("message", message);
			message = null;
		}
		return "priorityObject/priorityObject_add";
	}
	
	@PostMapping("/save")
	public String save(@ModelAttribute("priorityObject") PriorityObject priorityObject) {
		Boolean rs;
		try {
			rs = priorityObjectService.saveObject(priorityObject);
			message = new Message();
			message.setStatus("success");
			message.setMessage("Thêm đối tượng ưu tiên thành công!");
			return "redirect:/admin/priorityObject";
			
		} catch (Exception e) {
			message = new Message();
			message.setStatus("fail");
			message.setMessage("Thêm đối tượng ưu tiên không thành công!");
			return "redirect:/admin/priorityObject/add";
		} 
	}
	
	@PostMapping("/update")
	public String update(@ModelAttribute("priorityObject") PriorityObject priorityObject, ModelMap modelMap) {
		Boolean rs;
		try {
			rs = priorityObjectService.updateObject(priorityObject);
			message = new Message();
			message.setStatus("success");
			message.setMessage("Cập nhật thành công!");
			
			return "redirect:/admin/priorityObject";
			
		} catch (Exception e) {
			message = new Message();
			message.setStatus("fail");
			message.setMessage("Cập nhật không thành công!");
			
			return "redirect:/admin/priorityObject/edit/" + priorityObject.getId();
		}
	}
	
	@GetMapping("/edit/{id}")
	public String edit(@PathVariable int id, ModelMap modelMap) {
		try {
			PriorityObject priorityObject = priorityObjectService.getObject(id);
			modelMap.addAttribute("priorityObject", priorityObject);
			if (message != null)
			{
				modelMap.addAttribute("message", message);
				message = null;
			}
			
			return "priorityObject/priorityObject_edit";
		} catch (Exception e) {
			return "redirect:/admin/priorityObject/add";

		}
		
	}
}
