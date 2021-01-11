<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<body>
<center>
    <h1>Articles we have</h1>

    <br>
    List of articles:
    <br>

    <c:forEach items="${articles}" var="article">
        <ls>
            <a href="article?id=${article.getId()}">${article.getHeader()}</a>
            <br>
        </ls>
    </c:forEach>
    <br>
    <%= request.getAttribute("message")%>
</center>
</body>
</html>