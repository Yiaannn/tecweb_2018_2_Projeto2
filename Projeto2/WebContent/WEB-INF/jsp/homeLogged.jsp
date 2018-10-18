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

<form id="notes" action='/Projeto1/Notes' method='get'></form>
<form id="signout" action='/Projeto1/SignOut' method='post'></form>

<p>${param.message}</p>

<div class="boxwrapper">
	<div class="centerbox">
		<div class="centerboxchild">
			<h1>Projeto Notinhas</h1>
		</div>
		
		<div class="centerboxchild">
			<button type="submit" form='notes'>Acessar Notas</button>
			<button type="submit" form="signout">Sair</button>
		</div>
	</div>
</div>

</body>
</html>