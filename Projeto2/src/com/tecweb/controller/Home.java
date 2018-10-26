package com.tecweb.controller;

import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.tecweb.entidades.User;
import com.tecweb.entidades.Note;

//consultar para quem sabe conseguir puxar verbos http
//https://stackoverflow.com/questions/9521690/how-to-handle-http-options-with-spring-mvc
 
@RestController
public class Home {
	
	/*
	private void  authMacro(DAO dao, String authCookie){
		
	}
	*/
	
	@GetMapping("/note")
	public ModelAndView getNote(@CookieValue(value="auth", defaultValue ="empty") String authCookie){
		//exibir a lista de notas
		
		//ler o resource do cookie
		
		DAO dao= new DAO();
		User user= dao.authenticate(authCookie);
		if (authCookie.equals("empty") || user==null ){
			//auth inválido
			
			return new ModelAndView("unauthorized", HttpStatus.UNAUTHORIZED);
		}
		
		//auth válido
		//montar o recurso que representa a lista de notes
		
		List<Note> notes= dao.getActiveNoteList( user );
		
		ModelAndView mav= new ModelAndView("noteList", HttpStatus.OK);
		mav.addObject("notes", notes);
		return mav;
	}
	
	@GetMapping("/signUp")
	public ModelAndView getSignUp(){
		return new ModelAndView("signUp", HttpStatus.OK);
	}
	
	@PostMapping("/signUp")
	public ModelAndView postSignUp(HttpServletRequest request, HttpServletResponse response){
		DAO dao= new DAO();
		User user= new User();
		
		user.setLoginName(request.getParameter("login"));
		user.setPassHash(dao.hashFromPass( request.getParameter("pass") ));
		user.setDisplayName(user.getLoginName());
		
		//Checar se o par login/pass é válido, devolver 409-Conflict se não
		
		if(dao.checkIfLoginIsAvailable(user)){
			
			dao.addUser(user);
			
			String auth= request.getParameter("login")+"-"+request.getParameter("pass");
			Cookie authCookie= new Cookie("auth", auth);
			authCookie.setMaxAge(-1);
			response.addCookie(authCookie);
			
			return new ModelAndView("homeLogged", HttpStatus.OK);
		}else{
			
			return new ModelAndView("conflict", HttpStatus.CONFLICT);
		}
	}
	
	@GetMapping("/signIn")
	public ModelAndView getSignIn(){
		return new ModelAndView("signIn", HttpStatus.OK);
	}
	
	@PostMapping("/signIn")
	public ModelAndView postSignIn(HttpServletRequest request, HttpServletResponse response){
		DAO dao= new DAO();
		String auth;
		
		auth= request.getParameter("login") + "-" + request.getParameter("pass");
		
		User user= dao.authenticate(auth);
		if( user == null ){
			return new ModelAndView("unauthorized", HttpStatus.UNAUTHORIZED);
			
		}else{
			Cookie authCookie= new Cookie("auth", auth);
			authCookie.setMaxAge(-1);
			response.addCookie(authCookie);
			
			return new ModelAndView("homeLogged", HttpStatus.OK);
		}
	}
	
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
		return new ModelAndView("homeUnlogged", HttpStatus.OK);
	}
	
 
	@GetMapping("/home")
	public ModelAndView helloWorld(@CookieValue(value="auth", defaultValue ="empty") String authCookie) {
 
		//ler o resource do cookie
		
		DAO dao= new DAO();
		User user= dao.authenticate(authCookie);
		if (authCookie.equals("empty") || user==null ){
			//auth inválido
			
			return new ModelAndView("homeUnlogged", HttpStatus.OK);
		}
		
		//auth válido
		
		return new ModelAndView("homeLogged", HttpStatus.OK);
	}
}