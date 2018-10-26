package com.tecweb.controller;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.tecweb.entidades.User;

import tk.plogitech.darksky.api.jackson.DarkSkyJacksonClient;
import tk.plogitech.darksky.forecast.APIKey;
import tk.plogitech.darksky.forecast.DarkSkyClient;
import tk.plogitech.darksky.forecast.ForecastRequest;
import tk.plogitech.darksky.forecast.ForecastRequestBuilder;
import tk.plogitech.darksky.forecast.GeoCoordinates;
import tk.plogitech.darksky.forecast.model.Forecast;
import tk.plogitech.darksky.forecast.model.Latitude;
import tk.plogitech.darksky.forecast.model.Longitude;

import com.tecweb.entidades.Note;

//consultar para quem sabe conseguir puxar verbos http
//https://stackoverflow.com/questions/9521690/how-to-handle-http-options-with-spring-mvc
 
@RestController
public class Home {
	
	/*
	private void  authMacro(DAO dao, String authCookie){
		
	}
	*/
	
	/*
	@DeleteMapping("/note/{id}")
	Posso fazer um delete por id depois
	*/
	
	@PostMapping("/note")
	public ModelAndView postNote(@CookieValue(value="auth", defaultValue ="empty") String authCookie, HttpServletRequest request){
		//autenticar
		
		DAO dao= new DAO();
		User user= dao.authenticate(authCookie);
		if (authCookie.equals("empty") || user==null ){
			//auth inválido
			
			return new ModelAndView("unauthorized", HttpStatus.UNAUTHORIZED);
		}
		
		//auth válido
		
		String methodcheck= request.getParameter("_method");
		
		ModelAndView mav= null;
		if(methodcheck.equals("DELETE")){
		
			String target= request.getParameter("target");
			
			dao.disableNote(Integer.parseInt(target));
			
			//deploy
			mav= new ModelAndView("redirect:note", HttpStatus.OK);
		}else if( methodcheck.equals("POST") || methodcheck == null ){
			
			String messageBody= request.getParameter( "messageBody" );
			int priorityLevel= Integer.parseInt( request.getParameter("priority") );
			java.sql.Date creationDate = new java.sql.Date(Calendar.getInstance().getTime().getTime());
			
			java.sql.Date expiryDate= null;
			try{
				String tmp= request.getParameter("expiryDate");
				expiryDate= new java.sql.Date(   (new SimpleDateFormat("yyyy-MM-dd").parse(tmp)).getTime()   );
			}catch(Exception e){
				System.out.println("expiry date inválida, tratando");
			}
			
			Note note= new Note();
			note.setMessageBody(messageBody);
			note.setPriorityLevel(priorityLevel);
			note.setCreationDate(creationDate);
			note.setExpiryDate( expiryDate );
			
			dao.addNote(note, user);
			
			mav= new ModelAndView("redirect:note", HttpStatus.OK);
		}
		return mav;
	}
	
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
		//pegar a previsão do API
		Forecast forecast= null;
		
		ForecastRequest request = new ForecastRequestBuilder()
		        .key(new APIKey("5742709c18aba85136a1c17a34366ad1"))
		        .time(Instant.now())
		        .units(ForecastRequestBuilder.Units.si)
		        //.exdclude(ForecastRequestBuilder.Block.minutely)
		        //.extendHourly()
		        .location(new GeoCoordinates(new Longitude(-46.67643049021903), new Latitude(-23.5987249))).build(); //latitude e longitude do Insper
		
		DarkSkyJacksonClient client = new DarkSkyJacksonClient();
		
		try {
			forecast = client.forecast(request);
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		//pegar o que a gente quer do forecast
		String previsao= "Temperatura de hoje: Difícil dizer";
		
		if(forecast != null) {
			previsao= "Temperatura de hoje: "+ forecast.getCurrently().getTemperature() +"°C";
		}
		System.out.println(previsao);
		
		//ler o resource do cookie
		
		DAO dao= new DAO();
		User user= dao.authenticate(authCookie);
		if (authCookie.equals("empty") || user==null ){
			//auth inválido
			
			ModelAndView mav= new ModelAndView("homeUnlogged", HttpStatus.OK);
			mav.addObject("temperatura", previsao);
			return mav;
		}
		
		//auth válido
		
		ModelAndView mav= new ModelAndView("homeLogged", HttpStatus.OK);
		mav.addObject("temperatura", previsao);
		return mav;
	}
}