package com.catholicfile.catholicfile.repositories;

import com.catholicfile.catholicfile.entities.SecaoFolheto;
import com.catholicfile.catholicfile.enums.TempoLit;
import com.catholicfile.catholicfile.enums.TipoSecao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SecaoRepository extends JpaRepository<SecaoFolheto, Long> {
    List<SecaoFolheto> findByFolhetoId(Long folhetoId);

    Optional<SecaoFolheto> findByFolhetoIdAndTipo(Long folhetoId, TipoSecao tipo);

    @Query("""
    SELECT s FROM SecaoFolheto s
    WHERE (:palavra IS NULL OR :palavra = '' 
        OR LOWER(s.conteudo) LIKE LOWER(CONCAT('%', :palavra, '%'))
        OR LOWER(s.titulo) LIKE LOWER(CONCAT('%', :palavra, '%')))
      AND (:tipo IS NULL OR s.tipo = :tipo)
      AND (:lit IS NULL OR s.lit = :lit)
""")
    Page<SecaoFolheto> filtrar(
            @Param("palavra") String palavra,
            @Param("tipo") TipoSecao tipo,
            @Param("lit") TempoLit lit,
            Pageable pageable
    );
}