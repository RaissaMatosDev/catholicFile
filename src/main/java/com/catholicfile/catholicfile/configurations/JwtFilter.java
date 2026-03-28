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
import java.util.Optional;

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
        System.out.println("=== JWT DEBUG ===");
        System.out.println("PATH: " + path);

        // Rotas públicas
        if (path.startsWith("/v3/api-docs") ||
                path.startsWith("/swagger-ui") ||
                path.equals("/usuarios/login") ||
                path.equals("/usuarios/usuarios") ||
                path.startsWith("/error")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Pega o token do header ou do proxy
        String authHeader = Optional.ofNullable(request.getHeader("Authorization"))
                .or(() -> Optional.ofNullable(request.getHeader("X-Forwarded-Authorization")))
                .orElse(null);
        System.out.println("AUTH HEADER: " + authHeader);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);

        try {
            if (jwtUtil.validarToken(token)) {
                String email = jwtUtil.extrairEmail(token);
                Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(email);

                if (usuarioOpt.isPresent()) {
                    Usuario usuario = usuarioOpt.get();

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
        } catch (Exception e) {
            System.out.println("=== ERRO JWT ===");
            e.printStackTrace();
            SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(request, response);
    }
}