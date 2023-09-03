package com.NhapHocVKUFullStack.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.NhapHocVKUFullStack.Service.LoginService;
import com.NhapHocVKUFullStack.config.AppConfig;
import com.NhapHocVKUFullStack.models.Account;
import com.NhapHocVKUFullStack.models.Message;
import com.NhapHocVKUFullStack.models.SignupRequest;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin/account")
public class AccountController {
	private AppConfig appConfig;
	Message message = null;
	@Autowired
	private LoginService loginService;
	
	@Autowired
	public AccountController(AppConfig appConfig) {
		this.appConfig = appConfig;
	}
	
	@GetMapping("/siginup")
	public String signup(Model model) {
		String token = loginService.addUsernameAnhRole();
		model.addAttribute("cookie", token);
		model.addAttribute("siginup", new Account());
		if (message != null) {
			model.addAttribute("message", message);
			message = null;
		}
		return "account_add";
	}
	
	@PostMapping("/signup")
	public String siginup(@RequestBody SignupRequest signupRequest,
			RedirectAttributes redirectAttributes, HttpServletResponse response, HttpSession session) throws Exception {

        String rs = loginService.signupAndGetToken(signupRequest.getUsername(), signupRequest.getPassword(), signupRequest.getEmail(), signupRequest.getRole());
        if (rs.equals("OK"))
        {
        	return "redirect:/admin/account/siginup";
        }
		return "redirect:/admin/account/siginup";    
    }
	
//	@GetMapping
//	public String index() {
//		return "account_add";
//	}
	
}
