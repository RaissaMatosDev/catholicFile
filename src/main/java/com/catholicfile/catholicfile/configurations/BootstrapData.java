package com.catholicfile.catholicfile.configurations;

import com.catholicfile.catholicfile.entities.Usuario;
import com.catholicfile.catholicfile.enums.UserRole;
import com.catholicfile.catholicfile.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

public class BootstrapData {
    @Configuration
    public class DataInitializer {

        @Value("${app.admin.email}")
        private String adminEmail;

        @Value("${app.admin.senha}")
        private String adminSenha;

        @Bean
        CommandLineRunner initAdmin(UsuarioRepository repo, PasswordEncoder encoder) {
            return args -> {
                if (!repo.existsByRole(UserRole.ADMINISTRADOR)) {
                    var admin = new Usuario(
                            "ADMINISTRADOR",
                            adminEmail,
                            encoder.encode(adminSenha),
                            UserRole.ADMINISTRADOR
                    );
                    repo.save(admin);
                }
            };
        }
    }
}
