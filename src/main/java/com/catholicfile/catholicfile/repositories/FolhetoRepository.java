package com.catholicfile.catholicfile.repositories;

import com.catholicfile.catholicfile.entities.Folheto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface FolhetoRepository extends JpaRepository<Folheto, Long> {
    @Query("SELECT f FROM Folheto f LEFT JOIN FETCH f.secoes WHERE f.id = :id")
    Optional<Folheto> findByIdComSecoes(@Param("id") Long id);}
