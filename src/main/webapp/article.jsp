<%--
  Created by IntelliJ IDEA.
  User: alex
  Date: 21.12.2020
  Time: 15:17
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="java.util.*" %>
<%@ page import="model.Article" %>
<html>
<jsp:useBean id="article" scope="request" type="model.Article"/>

<head>
    <title>Title</title>
</head>
<body>
<form method="get" action="addArticle.jsp">
    <button type="submit" value="edit">Edit</button>
    <button type="submit" value="delete">Delete</button>
</form>
<h3>${article.header}</h3>
<br>
<c:forEach items="${article.tags}" var="tag">

    ${tag.getTag()}


</c:forEach>

<br>
${article.paragraphs}
</body>
</html>
