package com.catholicFile.catholicFile.Configurations;


import com.catholicFile.catholicFile.services.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


    @Configuration
    @EnableWebSecurity
    @EnableMethodSecurity

    public class SecurityConfigurations {
        private final JwtFilter jwtFilter;

        public SecurityConfigurations(JwtFilter jwtFilter) {
            this.jwtFilter = jwtFilter;
        }
         @Bean
        public AuthenticationManager authenticationManager(HttpSecurity http, CustomUserDetailsService userDetailsService) throws Exception {
            AuthenticationManagerBuilder authBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
            authBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
            return authBuilder.build();
    }

        @Bean
        public PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            return http
                    .csrf(csrf -> csrf.disable())
                    .authorizeHttpRequests(req -> req
                            .requestMatchers("/usuarios/login","/usuarios" ).permitAll()
                            .anyRequest().authenticated()
                    )
                    .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                    .build();
        }


    }


