<%@ page import="java.util.*" %>
<!DOCTYPE html>
<html>
<body>
<center>
    <h1>
        Articles we have
    </h1>
        <%List result= (List) request.getAttribute("articles");
            Iterator it = result.iterator();
            out.println("<br>List of articles: <br><br>");
            while(it.hasNext()){
                out.println(it.next()+"<br>");
            }
        %>
</body>
</html>