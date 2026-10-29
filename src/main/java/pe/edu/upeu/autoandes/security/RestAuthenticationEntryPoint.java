package pe.edu.upeu.autoandes.security;
@org.springframework.stereotype.Component
public class RestAuthenticationEntryPoint implements org.springframework.security.web.AuthenticationEntryPoint {
private final com.fasterxml.jackson.databind.ObjectMapper mapper=new com.fasterxml.jackson.databind.ObjectMapper();
public void commence(jakarta.servlet.http.HttpServletRequest req,jakarta.servlet.http.HttpServletResponse res,org.springframework.security.core.AuthenticationException ex) throws java.io.IOException { res.setStatus(401);res.setContentType("application/json;charset=UTF-8");mapper.writeValue(res.getOutputStream(),pe.edu.upeu.autoandes.exception.GlobalExceptionHandler.error(401,"Autenticación requerida o token inválido",req.getRequestURI(),null)); }
}
