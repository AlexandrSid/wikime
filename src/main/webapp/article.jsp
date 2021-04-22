<%--
  Created by IntelliJ IDEA.
  User: alex
  Date: 21.12.2020
  Time: 15:17
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="java.org.aleksid.wikime.util.*" %>
<%@ page import="org.aleksid.wikime.model.Article" %>
<html>
<jsp:useBean id="article" scope="request" type="org.aleksid.wikime.model.Article"/>

<head>
    <title>${article.header}</title>
</head>
<body>
<table>
    <tr>
        <td>
            <form action="/wikime">
                <button type="submit">Main</button>
            </form>
        </td>
        <td>
            <form method="get" action="/wikime/articles">
                <button type="submit">Articles</button>
            </form>
        </td>
        <td>
            <form method="get" action="/wikime/edit">
                <input type="hidden" name="id" value="${article.id}">
                ${out.print(article.id)}
                <button type="submit" value="${article.id}">Edit</button>
            </form>
        </td>
        <td>
            <form method="post" name="delete">
                <button type="submit" value="delete">Delete</button>
            </form>
        </td>
    </tr>
</table>
<center>
<h3>${article.header}</h3>
</center>
<c:forEach items="${article.tags}" var="tag">
    ${tag.getTag()}
</c:forEach>
<br>
${article.paragraphs}
</body>
</html>
