package com.NhapHocVKUFullStack.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AppController {
		
	@GetMapping("calendar")
	public String IndexCalendar() {
		return "app/calendar";	
	}
	
	@GetMapping("inboxmail")
	public String IndexMail() {
		return "app/inboxMail";
	}
	@GetMapping("readmail")
	public String IndexReadMail() {
		return "app/readmail";
	}
}
