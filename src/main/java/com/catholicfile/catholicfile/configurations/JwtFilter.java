package com.catholicfile.catholicfile.configurations;

import com.catholicfile.catholicfile.entities.Usuario;
import com.catholicfile.catholicfile.repositories.UsuarioRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
//Este é o "porteiro' de acesso, verifica todas as
// credencias e permite o login e as solicitações http

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final UsuarioRepository usuarioRepository;
    private final JwtUtil jwtUtil;

    public JwtFilter(UsuarioRepository usuarioRepository, JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String path = request.getRequestURI();

        // ignora endpoints publicos
        if (path.startsWith("/v3/api-docs") || path.startsWith("/swagger-ui") ||
                path.equals("/usuarios") || path.equals("/usuarios/login")) {
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");

        //verifica token
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            try {
                if (jwtUtil.validarToken(token)) {
                    String email = jwtUtil.extrairEmail(token);

                    // Busca o usuário e autentica
                    usuarioRepository.findByEmail(email).ifPresent(usuario -> {
                        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                                usuario, null, usuario.getAuthorities());
                        SecurityContextHolder.getContext().setAuthentication(auth);
                    });
                }
            } catch (Exception e) {
                // Se der erro no token, limpa o contexto para garantir o 401 correto
                SecurityContextHolder.clearContext();
            }
        }

        filterChain.doFilter(request, response);
    }
}