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
	<jsp:include page="/WEB-INF/jsp/larkUHeader.jsp" />
<h2>All Scheduled Classes</h2>
	<table border="1" >
		<tr>
			<td width="50">#</td>
			<td width="50">Id</td>
			<td>Course Code</td>
			<td>Start Date</td>
			<td>End Date</td>
		</tr>
		<c:forEach items="${classes}" var="sClass" varStatus="loopStatus">
		<tr>
			<td>${loopStatus.index}</td>
			<td>${sClass.id}</td>
			<td>${sClass.course.code}</td>
			<td>${sClass.startDate}</td>
			<td>${sClass.endDate}</td>
		</tr>
		</c:forEach>
	</table>
	<br/> <a href="${pageContext.request.contextPath}/index.jsp">Home</a></td>
</body>
</html>