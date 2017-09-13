<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>    
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%> 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Agregar Componentes</title>
</head>
<body>
	<h4>${mensaje} : ${usuarioForm.nombre}</h4>
</body>

<h4>Agregar Componente</h4>
<c:url var="agregarComponentes" value="agregarComponentes.html"/>
<form:form id="agregar" modelAttribute="componenteForm" method="post" action="${agregarComponentes}">
	<table width="400px" height="150px">
		<tr>
		<td><form:label path="nombre">Nombre</form:label></td>
		<td><form:input  path="nombre"/></td>
		</tr>
		<tr>
		<td><form:label path="version">Versión</form:label></td>
		<td><form:input  path="version"/></td>
		</tr>	
		<tr>
		<td><form:label path="tipo">Tipo</form:label></td>
		<td><form:input  path="tipo"/></td>
		</tr>
		<tr>
		<td><form:label path="extension">Extensión</form:label></td>
		<td><form:input  path="extension"/></td>
		</tr>
		
		<tr><td></td><td>
		<input type="submit" value="Agregar" />
		</td></tr>
	</table>
</form:form>

</html>