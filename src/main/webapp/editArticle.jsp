<%--
  Created by IntelliJ IDEA.
  User: alex
  Date: 23.12.2020
  Time: 14:33
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<h1>editArticle.jsp</h1>
<jsp:useBean id="article" type="model.Article" />
<% out.print(article);%>
<form method="post" >
    <label>Title:
        <input type="text" name="title" placeholder="${article.header}"><br />
        <%= out.print(article.getHeader())%>
    </label>

    <label>Tags (enter tags, separated by comma ","):
        <input type="text" name="tags" value=""><br />
    </label>


    <label>Text of article
        <input type="text" name="text"><br />
    </label>
    <button type="submit">Save</button>
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
