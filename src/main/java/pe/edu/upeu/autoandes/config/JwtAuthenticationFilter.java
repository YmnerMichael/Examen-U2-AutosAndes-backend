package pe.edu.upeu.autoandes.config;
import jakarta.servlet.*;import jakarta.servlet.http.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import pe.edu.upeu.autoandes.security.*;
@org.springframework.stereotype.Component @lombok.RequiredArgsConstructor
public class JwtAuthenticationFilter extends org.springframework.web.filter.OncePerRequestFilter {
private final JwtService jwt;private final UsuarioDetailsService usuarios;private final RestAuthenticationEntryPoint entry;
@Override protected boolean shouldNotFilter(HttpServletRequest r) { String p=r.getServletPath();return r.getMethod().equals("OPTIONS")||p.equals("/api/v1/auth/login")||p.startsWith("/swagger-ui")||p.startsWith("/v3/api-docs"); }
@Override protected void doFilterInternal(HttpServletRequest req,HttpServletResponse res,FilterChain chain) throws ServletException,java.io.IOException {
String auth=req.getHeader("Authorization");
if(auth!=null) {
try { if(!auth.startsWith("Bearer ")) throw new IllegalArgumentException(); var u=usuarios.loadUserByUsername(jwt.validar(auth.substring(7)));if(!u.isEnabled()) throw new IllegalArgumentException();SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(u,null,u.getAuthorities())); }
catch(io.jsonwebtoken.JwtException|IllegalArgumentException|org.springframework.security.core.userdetails.UsernameNotFoundException ex) { SecurityContextHolder.clearContext();entry.commence(req,res,new org.springframework.security.authentication.BadCredentialsException("Token inválido"));return; }
}
chain.doFilter(req,res);
}
}
