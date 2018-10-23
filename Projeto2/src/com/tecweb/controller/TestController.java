package com.tecweb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
 
 
@Controller
public class TestController {
 
	@RequestMapping("/")
	public String helloWorld() {
		System.out.println("Testar se consigo capturar o root");
		return "welcome";
	}
	@RequestMapping("adicionaNota")
	public String adiciona() {
		return "formNota";
		
	}
}