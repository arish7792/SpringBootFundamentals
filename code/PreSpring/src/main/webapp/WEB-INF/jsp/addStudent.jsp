<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link type="text/css" rel="stylesheet" media="all" 
		href="${pageContext.request.contextPath}/css/larkU.css" >
<title>LarkU</title>
</head>
<body>
<h1>Add a Student</h1>
	<form:form commandName="student" >
	<table border="1">
		<tr>
			<td>Name</td>
			<td><form:input path="name"/></td>
		</tr>
		<tr>
			<td>Phone Number</td>
			<td><form:input path="phoneNumber"/></td>
		</tr>
		<tr>
			<td>Status</td>
			<td><form:select path="status" items="${student.statusList}"/></td>
		</tr>
		<tr>
			<td><input type="submit" name="addStudent" value="Add Student" /></td>
		</tr>
	</table>
	</form:form>
	<br />
	<a href="${pageContext.request.contextPath}/index.jsp">Home</a>
</body>
</html>