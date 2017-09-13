<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>    
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>  
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Proyecto Spring Tutorial by Victor Elliott Humala</title>
</head>
<body>
<c:if test="${mensaje !='Usuario Correcto'}">
 	<h4>${mensaje}</h4>
</c:if>

<c:url var="verificarLogin" value="verificarLogin.html"/>
<form:form id="login" modelAttribute="usuarioForm" method="post" action="${verificarLogin}">
	<table width="400px" height="150px">
		<tr>
		<td><form:label path="nombre">Nombre</form:label></td>
		<td><form:input  path="nombre"/></td>
		</tr>
		<tr>
		<td><form:label path="clave">Clave</form:label></td>
		<td><form:input  path="clave"/></td>
		</tr>
		<tr><td></td><td>
		<input type="submit" value="Login" />
		</td></tr>
	</table>
</form:form>

</body>
</html>