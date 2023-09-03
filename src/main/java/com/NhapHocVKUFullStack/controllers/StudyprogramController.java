package com.NhapHocVKUFullStack.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/studyprogram")
public class StudyprogramController {
		
	@GetMapping
	public String Index() {
		return "studyprogram";
		
	}
}
