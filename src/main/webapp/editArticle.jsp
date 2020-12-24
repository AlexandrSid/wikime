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

<h3>${article.header}</h3>
<br>
<c:forEach items="${article.tags}" var="tag">

    ${tag.getTag()}


</c:forEach>

<br>
${article.paragraphs}
</body>
</html>
