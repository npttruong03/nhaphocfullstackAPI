package com.NhapHocVKUFullStack.controllers;

import java.lang.reflect.Array;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.NhapHocVKUFullStack.Service.LoginService;
import com.NhapHocVKUFullStack.config.AppConfig;
import com.NhapHocVKUFullStack.models.Account;
import com.NhapHocVKUFullStack.models.SignupRequest;

import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin/login")
public class LoginController {
	
	private AppConfig appConfig;
	@Autowired
	private LoginService loginService;
	
    @Autowired
    public LoginController(AppConfig appConfig)
    {
    	this.appConfig = appConfig;
    }
	@GetMapping
	public String Login(Model model) {
		String check = loginService.checkToken();
		if(check != null) {
			if (!StringUtils.isEmpty(check)) {
				return "redirect:/admin/index";
			}
		}
		model.addAttribute("account", new Account());
		return "indexLog";
	}
	
	@PostMapping("/signin")
	public String login(@RequestParam("username") String username,
			@RequestParam("password") String password,
			RedirectAttributes redirectAttributes, HttpServletResponse response, HttpSession session) {
        try {
            String token = loginService.loginAndGetToken(username, password);
            // Save the token to the response cookie 	or session as needed
//            HttpHeaders responseHeaders = new HttpHeaders();
//            responseHeaders.set(HttpHeaders., token);
//            Cookie cookie = new Cookie("nhaphocvku", token);
//    	    cookie.setHttpOnly(true);
//    	    cookie.setMaxAge(86400);
//    	    cookie.setPath("/admin/**");
//    	    System.out.println(cookie);
//    	    response.addCookie(cookie);
            // Add other necessary headers or cookie handling as required

//            return "redirect:/admin/tuitionFeeList"; // Redirect to the dashboard or any other page upon successful login
//
            session.setAttribute("roles", appConfig.cookieStore().getRoles());
            session.setAttribute("username", appConfig.cookieStore().getUsername());
            return "redirect:/admin/index"; // Redirect to the dashboard or any other page upon successful login

        } catch (Exception e) {
        	e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Invalid credentials. Please try again.");
            return "redirect:/admin/login"; // Redirect back to the login page with an error message
        }
    }
	
	
	
	
	@PostMapping("/logout")
	public String logout() {
		String rs = loginService.logout();
		if (rs == "OK")
		{
			return "redirect:/admin/login";
		}
		else {
			return "";
		}
	}
	

	
//	@GetMapping("/checkToken")
//	public String check() {
//		String check = loginService.checkToken();
//		System.out.println("dk: "+check);
//		if(check != null) {
//			if (!StringUtils.isEmpty(check)) {
//				return "redirect:/admin/index";
//			}
//		} else {
//			return "redirect:/login";
//		}
//		return "redirect:/login";
//	}
	} 