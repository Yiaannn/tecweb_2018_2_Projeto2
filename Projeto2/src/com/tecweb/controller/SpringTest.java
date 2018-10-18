package com.tecweb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
 
 
@Controller
public class SpringTest {
 
	@RequestMapping("/welcome")
	public String helloWorld() {
 
		System.out.println("Por favor algo funciona");
		
		return "welcome";
	}
}