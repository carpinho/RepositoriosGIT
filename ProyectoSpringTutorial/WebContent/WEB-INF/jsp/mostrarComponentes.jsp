<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Lista de Componentes</title>
</head>
<body>
<center>
<br><br><br><br><br><br>
<div style="color: teal;font-size: 30px">Lista de Componentes</div>
<br><br>
<c:if test="${!empty listaComponentes}">
<table border="1" bgcolor="black" width="600px">
<tr style="background-color: teal;color: white;text-align: center;" height="40px">
<td>Nombre</td>
<td>Version</td>
<td>Tipo</td>
<td>Extensión</td>
</tr>
<c:forEach items="${listaComponentes}" var="componente">
<tr style="background-color:white;color: black;text-align: center;" height="30px" >
<td><c:out value="${componente.nombre}"/></td>
<td><c:out value="${componente.version}"/></td>
<td><c:out value="${componente.tipo}"/></td>
<td><c:out value="${componente.extension}"/></td>
</tr>
</c:forEach>
</table>
</c:if>
<br>
<a href="volverComponente.html" >Agrega un nuevo componente</a>
</center>
</body>
</html>