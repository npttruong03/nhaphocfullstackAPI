package com.NhapHocVKUFullStack.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/payment")
public class PaymentController {
	@GetMapping
	public String index() {
		return "payment";
	}
}
