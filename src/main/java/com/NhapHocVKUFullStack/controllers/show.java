package com.NhapHocVKUFullStack.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping()
public class show {
	   @GetMapping("/show")
	    public String showCreate(ModelMap modelMap) {
	        
	        return "show";
	    }
}
