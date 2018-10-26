<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<style> <%@include file="/css/mystyle.css"%> </style>
<title>Insert title here</title>
</head>
<body>

<p>${message}</p>
<div class="boxwrapper">
	<div class="centerbox">
		<div class="centerboxchild">
			<details>
				<summary>Nova Nota</summary>
				<div class="centerboxchild">
				<form id="postNote" action='/Projeto1/Notes' method='post' >
					<input type="hidden" name="_method" value="POST">
					<textarea name="messageBody" placeholder='Corpo da Mensagem' cols="40" rows="5"></textarea>
					<h2>Prioridade</h2>
					<input type="range" name="priority" min="0" max="9" value="0" step="1" />
					<h2>Segurar até</h2>
					<input type="date" name="expiryDate" />
				</form>
				</div>
				<div class="centerboxchild">
					
				</div>
				<div class="centerboxchild">
					<button type="submit" form="postNote">Confirmar</button>
				</div>
			</details>
		</div>
	</div>

	<c:forEach items="${notes}" var="note">
		<div class="centerbox">
			<div class="centerboxchild">
				<img src="http://identicon-1132.appspot.com/${note.getID()}salt?s=14&p=6&f=png" alt="Ícone identificador da nota">
			</div>
			<div class="centerboxchild">
				<h1>Prioridade: ${note.getDecoratedPriorityLevel()}</h1>
			</div>
			<div class="centerboxchild">
				<p class="messageBody">${note.getMessageBody()}</p>
			</div>
			<div class="centerboxchild">
				<p>Criado em ${note.getCreationDate()}</p>
			</div>
			<div class="centerboxchild">
				<p>${note.getDecoratedExpiryDate()}</p>
			</div>
			<div class="centerboxchild">
				<form id="deleteNote" action='/Projeto1/Notes' method='post'>
					<input type="hidden" name="_method" value="DELETE">
				</form>
				<button type="submit" form="deleteNote" name="target" value="${note.getID()}">Apagar</button>
			</div>
		</div>
	</c:forEach>
	
	<div class="centerbox">
		<div class="centerboxchild">
			<form id="voltar" action='/Projeto1/' method='get'></form>
			<button type="submit" form="voltar">Voltar</button>
		</div>
	</div>
</div>

</body>
</html>