package com.tecweb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

//consultar para quem sabe conseguir puxar verbos http
//https://stackoverflow.com/questions/9521690/how-to-handle-http-options-with-spring-mvc
 
@RestController
public class Home {
 
	@GetMapping("/home")
	public ModelAndView helloWorld() {
 
		//preciso devolver algo?
		//como eu falo qual view jogar?
		
		return new ModelAndView("homeUnlogged");
	}
	
	//@PostMapping("/home")
}