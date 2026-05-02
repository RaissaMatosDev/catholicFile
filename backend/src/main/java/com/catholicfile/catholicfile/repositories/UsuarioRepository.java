package com.catholicfile.catholicfile.repositories;

import com.catholicfile.catholicfile.entities.Usuario;
import com.catholicfile.catholicfile.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByEmail(String email);

    boolean existsByRole(UserRole role);
}
