<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
            <% 
    String user_name=(String)request.getParameter("user_name");
    String user_email=(String)request.getParameter("user_email"); 
    %>

    <%=user_name %>
     <%=user_email %>
    </body>
</html>
