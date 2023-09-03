package com.NhapHocVKUFullStack.controllers;

import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.NhapHocVKUFullStack.Service.EthnicService;
import com.NhapHocVKUFullStack.models.Ethnic;
import com.NhapHocVKUFullStack.models.Message;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/admin/ethnic")
public class EthnicController {

	@Autowired
	private EthnicService ethnicService;
	private Message message = null;

	@GetMapping
	public String index(ModelMap modelMap) throws JsonMappingException, JsonProcessingException {
		modelMap.addAttribute("ethnics", this.ethnicService.getDataFromAPI());
		if (message != null)
		{
			modelMap.addAttribute("message", message);
			message = null;
		}
		return "ethnic/index";
	}

	@GetMapping("/create")
	public String create(ModelMap modelMap) throws JsonMappingException, JsonProcessingException {
		if (message != null)
		{
			modelMap.addAttribute("message", message);
			message = null;
		}
		return "ethnic/add";
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public String createEthnic(Ethnic ethnic, HttpServletRequest request) throws Exception {
		try {
			ethnic.setCreateTime(new Timestamp(System.currentTimeMillis()));
			ethnic.setUpdateTime(new Timestamp(System.currentTimeMillis()));
			String msg =  ethnicService.addEthnic(ethnic);
			message = new Message();
			message.setStatus("success");
			message.setMessage(msg);
			
			return "redirect:/admin/ethnic";
			
		} catch (Exception e) {
			e.printStackTrace();
			message = new Message();
			message.setStatus("fail");
			message.setMessage("Thêm không thành công!");
			
			return "redirect:/admin/ethnic/create";
		}	
		
	}
	
	
	//
	@GetMapping("/edit/{id}")
	public String editUI(ModelMap modelMap, @PathVariable("id") Integer id) throws Exception {

		modelMap.addAttribute("ethnic", ethnicService.getById(id));
		if (message != null)
		{
			modelMap.addAttribute("message", message);
			message = null;
		}
		return "ethnic/edit";
	}
	
	@PostMapping("/edit")
	public String updateEthnic(Ethnic ethnic, HttpServletRequest request) throws Exception {
		try {
			ethnic.setUpdateTime(new Timestamp(System.currentTimeMillis()));
			String msg = ethnicService.updateEthnic(ethnic);
			message = new Message();
			message.setStatus("success");
			message.setMessage(msg);
			
			return "redirect:/admin/ethnic";
			
		} catch (Exception e) {
			message = new Message();
			message.setStatus("fail");
			message.setMessage("Cập nhật không thành công!");


			return "redirect:/admin/ethnic/edit/" + ethnic.getId();
		}	
//	    return "redirect:/admin/ethnic";
	}
}
