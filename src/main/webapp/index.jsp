<%@ page import="org.springframework.security.authentication.UsernamePasswordAuthenticationToken" %>
<%@ page import="org.springframework.security.core.Authentication" %>
<%@ page import="org.springframework.security.core.context.SecurityContext" %>
<%@ page import="org.springframework.security.core.context.SecurityContextHolder" %>
<%@ page import="org.springframework.security.authentication.AuthenticationManager" %>
<%@ page import="org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder" %>
<html>
<body>
<%
    UsernamePasswordAuthenticationToken authReq
            = new UsernamePasswordAuthenticationToken("Timur", "parolyaNet0");
    AuthenticationManager authenticationManager = new AuthenticationManagerBuilder()
    Authentication auth = SecurityContextHolder.getContext().setAuthentication();
    response.sendRedirect("user");
%>
</body>
</html>
