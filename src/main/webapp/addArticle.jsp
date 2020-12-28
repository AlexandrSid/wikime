<%--
  Created by IntelliJ IDEA.
  User: alex
  Date: 23.12.2020
  Time: 13:38
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>New Article</title>
</head>
<body>
<form method="post" >
    <label>Title:
        <input type="text" name="title"><br />
    </label>

    <label>Tags (enter tags, separated by comma ","):
        <input type="text" name="tags"><br />
    </label>


    <label>Text of article
        <input type="text" name="text"><br />
    </label>
    <button type="submit">Save</button>
</form>
</body>
</html>
