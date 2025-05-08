package com.example.customer_service.infra.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    // Clave secreta utilizada para firmar los JWT.
    // En producción esta clave debe configurarse de forma segura (por ejemplo, en variables de entorno).
    private final String jwtSecret = "miClaveSecretaMuySeguraYLoEsSuficientementeLarga";


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        // Se obtiene la cabecera Authorization
        String header = request.getHeader("Authorization");

        if (header != null && header.startsWith("Bearer ")) {
            // Extraemos el token eliminando el prefijo "Bearer "
            String token = header.substring(7);
            try {
                // Validamos y parseamos el token usando JJWT
                Claims claims = Jwts.parser()
                        .setSigningKey(jwtSecret.getBytes())
                        .parseClaimsJws(token)
                        .getBody();

                // Obtenemos el username (sujeto) del token
                String username = claims.getSubject();
                if (username != null) {
                    // Extraemos roles; se espera que el token incluya una lista en la clave "roles"
                    List<String> roles = claims.get("roles", List.class);
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(
                                    username,
                                    null,
                                    roles == null ? List.of() : roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList())
                            );
                    // Establecemos en el contexto de seguridad la autenticación del usuario
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            } catch (Exception e) {
                // Si el token es inválido, se limpia el contexto de seguridad para evitar accesos no autorizados.
                SecurityContextHolder.clearContext();
                throw new RuntimeException("Token inválido: " + e.getMessage(), e);
            }
        }
        // Continuamos con la cadena de filtros
        filterChain.doFilter(request, response);
    }
}
