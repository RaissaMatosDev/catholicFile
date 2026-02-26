package com.catholicFile.catholicFile.repositories;

import com.catholicFile.catholicFile.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository <Usuario, Long> {
}
