<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<title>Wikime -> Articles</title>

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
    <%--    <%= request.getAttribute("message")%>--%>


    <table>
        <tr>
            <td>
                <form action="/wikime">
                    <button type="submit">Main</button>
                </form>
            </td>
            <td>
            </td>
            <td>
                <form method="get" action="/wikime/new">
                    <button type="submit">New</button>
                </form>
            </td>
        </tr>
    </table>
</center>
</body>
</html>