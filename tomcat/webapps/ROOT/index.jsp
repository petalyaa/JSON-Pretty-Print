<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:useBean id="WebConstants" class="org.pet.jsonprettyprint.web.util.WebConstants" scope="session"/>
<%
String contextPath = request.getContextPath();
%>
<!DOCTYPE html>
<html lang="en">
	<head>
		<title><fmt:message key="application.title" /></title>
		<link rel="stylesheet" type="text/css" href="<%= contextPath %>/css/bootstrap.css">
		<link rel="stylesheet" type="text/css" href="<%= contextPath %>/css/todc-bootstrap.css">
		<link rel="stylesheet" type="text/css" href="<%= contextPath %>/css/style.css">
		<link rel="stylesheet" type="text/css" href="<%= contextPath %>/css/jquery.treeview.css">
		<script type="text/JavaScript" src="<%= contextPath %>/js/jquery-1.10.2.min.js"></script>
		<script type="text/JavaScript" src="<%= contextPath %>/js/bootstrap.min.js"></script>
		<script type="text/JavaScript" src="<%= contextPath %>/js/json2.js"></script>
		<script type="text/JavaScript" src="<%= contextPath %>/js/jquery.treeview.js"></script>
		<script type="text/JavaScript" src="<%= contextPath %>/js/application.js"></script>
		<script type="text/javascript">
			var app = new Application();
			app.loadDiv('input', 'main-container');
		</script>
	</head>
	<body>
		<div class="container" id="main-container">
		</div>
		<div class="footer text-center">
			<div class="footer-top-border"></div>
			<b>Any issue? Feel free to reach me at : petalyaa[at]gmail[dot]com. <a class="pull-right" style="padding-right : 15px;">Credit</a></b>
		</div>
	</body>
</html>