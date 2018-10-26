<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<style> <%@include file="/css/mystyle.css"%> </style>
<title>Insert title here</title>
</head>
<body>

<form id="signup" action='signUp' method='get'></form>
<form id="signin" action='signIn' method='get'></form>

<p>${param.message}</p>

<div class="boxwrapper">
	<div class="centerbox">
		<div class="centerboxchild">
			<h1>Projeto Notinhas</h1>
		</div>
		
		<div class="centerboxchild">
			<button type="submit" form='signup'>Inscrever-se</button>
			<button type="submit" form="signin">Acessar Perfil</button>
		</div>
	</div>
	
	<div class="centerbox">
		<div class="centerboxchild">
			<h1>Previsão do Dia</h1>
		</div>
		
		<div class="centerboxchild">
			<p>${temperatura}</p>
		</div>
	</div>
</div>

</body>
</html>