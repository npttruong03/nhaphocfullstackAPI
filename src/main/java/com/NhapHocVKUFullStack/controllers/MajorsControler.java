package com.NhapHocVKUFullStack.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.NhapHocVKUFullStack.Service.MajorsService;
import com.NhapHocVKUFullStack.models.Khoa;
import com.NhapHocVKUFullStack.models.Majors;
import com.NhapHocVKUFullStack.models.Message;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/admin/majors")
public class MajorsControler {
	@Autowired
	private MajorsService majorsService;

	 @GetMapping
	    public String index(ModelMap modelMap) throws JsonMappingException, JsonProcessingException {
	        modelMap.addAttribute("majorses", this.majorsService.getDataFromAPI());
	        return "Nganh/nganh";
	    }
	 @GetMapping("/create")
	 public String showCreate() {
		 return "Nganh/nganh_add";
	 }
	 
	 @RequestMapping(value = "create", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	  public String createContent(Majors majors, HttpServletRequest request) throws Exception {
		 majorsService.post(majors);
		 return "redirect:/admin/majors";
	    }
//	 --------------------get and edit---------------------
	 @GetMapping("/edit/{id}")
	    public String showEdit(ModelMap modelMap, @PathVariable("id") String id) throws Exception {
	        Majors majors = this.majorsService.getById(id);
	        if (majors.getId().equals("")) {
	            throw new Exception("Khoa not found with id: " + id);
	        }
	        modelMap.addAttribute("majorses", majors);
	        return "Nganh/nganh_edit";
	    }

	    @PostMapping("/edit/{id}")
	    public String editMajors(@PathVariable("id") String id, @Validated Majors majors) throws Exception {
	        try {
	            majorsService.callApi(majors, id);
	            return "redirect:/admin/majors";
	        } catch (Exception e) {
	        	e.printStackTrace();
	            return "redirect:/admin/majors/edit/" + id;
	        }
	    }

}