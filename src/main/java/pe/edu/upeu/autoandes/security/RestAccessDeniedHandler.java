package pe.edu.upeu.autoandes.security;
@org.springframework.stereotype.Component
public class RestAccessDeniedHandler implements org.springframework.security.web.access.AccessDeniedHandler {
private final com.fasterxml.jackson.databind.ObjectMapper mapper=new com.fasterxml.jackson.databind.ObjectMapper();
public void handle(jakarta.servlet.http.HttpServletRequest req,jakarta.servlet.http.HttpServletResponse res,org.springframework.security.access.AccessDeniedException ex) throws java.io.IOException { res.setStatus(403);res.setContentType("application/json;charset=UTF-8");mapper.writeValue(res.getOutputStream(),pe.edu.upeu.autoandes.exception.GlobalExceptionHandler.error(403,"No tiene permiso para esta operación",req.getRequestURI(),null)); }
}
