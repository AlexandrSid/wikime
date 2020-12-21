<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="java.util.*" %>
<!DOCTYPE html>
<html>
<body>
<center>
    <h1>
        Articles we have
    </h1>

    <br>List of articles: <br>

    <c:forEach items="${articles}" var="article">
        <div>
            <a href="article?id=${article.getId()}">${article.getHeader()}</a>


        </div>
    </c:forEach>
</center>
</body>
</html>