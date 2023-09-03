package com.NhapHocVKUFullStack.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.NhapHocVKUFullStack.Service.KhoaService;
import com.NhapHocVKUFullStack.Service.MethodService;
import com.NhapHocVKUFullStack.models.Khoa;
import com.NhapHocVKUFullStack.models.Method;
import com.NhapHocVKUFullStack.models.Religion;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/admin/method")
public class MethodController {
	@Autowired
	private MethodService methodService;
	
	 @GetMapping()
	    public String index(ModelMap modelMap) throws JsonMappingException, JsonProcessingException {
	        modelMap.addAttribute("methods", this.methodService.getDataFromAPI());
	        return "Method/method";
	    }
	 @GetMapping("/create")
	 public String showCreate() {
		 return "Method/createMethod";
	 }
	 
	 @RequestMapping(value = "/create", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	  public String createKhoa(Method method, HttpServletRequest request) throws Exception {
	        methodService.post(method);
	        return "redirect:/admin/method";
	    }
	 
	 
	 @GetMapping("/editMethod/{id}")
	    public String showEdit(@PathVariable int id, ModelMap modelMap) throws Exception {
	        Method method = methodService.getById(id);
	        modelMap.addAttribute("method", method);
	        return "Method/methodEdit";
	    }

	 @PostMapping("/editMethod/{id}")
	 public String editContent(@PathVariable("id") int id, Method method, HttpServletRequest request) throws Exception {
	     method.setId(id);
	     methodService.editMethod(method);
	     return "redirect:/admin/method";
	 }
}
