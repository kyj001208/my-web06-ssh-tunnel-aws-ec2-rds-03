package com.green.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LogController {
	
	
	 	@GetMapping("/login")
	    public String signUpPage() {
	    	return "views/a/b/login"; // 회원가입 페이지의 이름
	    }
	    
	    @GetMapping("/log")
	    public String signUpPage2() {
	    	return "views/a/b/log"; // 회원가입 페이지의 이름
	    }
	    
	  
	
	

}
