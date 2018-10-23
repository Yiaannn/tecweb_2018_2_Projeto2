package com.tecweb.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

//consultar para quem sabe conseguir puxar verbos http
//https://stackoverflow.com/questions/9521690/how-to-handle-http-options-with-spring-mvc
 
@RestController
public class Home {
	
	private void  authMacro(DAO dao, String authCookie){
		
	}
	
	/*
	@GetMapping("/signUp")
	
	@PostMapping("/signUp")
	
	@GetMapping("/signIn")
	
	@PostMapping("/signIn")
	*/
	
	@GetMapping("/signOut")
	public ModelAndView getSignOut(@CookieValue(value="auth", defaultValue ="empty") String authCookie, HttpServletResponse response) {
		//como o cookie é um recurso do browser, não do server, não faria sentido deletar ele por um delete
		//Posso fazer a deleção no próprio get
		
		//limpar o cookie
		
		if (authCookie.equals("empty")){
			Cookie auth= new Cookie("auth", authCookie);
			auth.setMaxAge(0); //Se desfazer do cookie agora
			response.addCookie(auth);
		}
		
		
		//providenciar a página
		return new ModelAndView("homeUnlogged");
	}
	
 
	@GetMapping("/home")
	public ModelAndView helloWorld(@CookieValue(value="auth", defaultValue ="empty") String authCookie) {
 
		//ler o resource do cookie
		DAO dao= new DAO();
		if (authCookie.equals("empty") || !dao.authenticate(authCookie) ){
			//auth inválido
			
			return new ModelAndView("homeUnlogged");
		}
		
		//auth válido
		
		return new ModelAndView("homeLogged");
	}
	
	//@PostMapping("/home")
}