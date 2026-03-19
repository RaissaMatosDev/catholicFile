package com.catholicFile.catholicFile.Configurations;

import com.catholicFile.catholicFile.entities.Usuario;
import com.catholicFile.catholicFile.repositories.UsuarioRepository;
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

        // Ignora endpoints públicos
        String path = request.getRequestURI();
        if (path.equals("/usuarios/cadastrar") || path.equals("/usuarios/login")) {
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);


            if (jwtUtil.validarToken(token)) {
                String email = jwtUtil.extrairEmail(token);

                Usuario usuario = usuarioRepository.findByEmail(email)
                        .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));


                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(
                                usuario,
                                null,
                                usuario.getAuthorities()
                        );

                auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }

        // Se não tiver token válido, o Spring vai tratar a requisição conforme o SecurityChain
        filterChain.doFilter(request, response);
    }
}