package com.catholicFile.catholicFile.repositories;

import com.catholicFile.catholicFile.entities.SecaoFolheto;
import com.catholicFile.catholicFile.enums.TempoLit;
import com.catholicFile.catholicFile.enums.TipoSecao;
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
                WHERE (LOWER(s.conteudo) LIKE LOWER(CONCAT('%', :palavra, '%'))
                   OR LOWER(s.titulo) LIKE LOWER(CONCAT('%', :palavra, '%')))
                  AND (:tipo IS NULL OR s.tipo = :tipo)
                  AND (:lit IS NULL OR s.lit = :lit)
            """)
    List<SecaoFolheto> filtrarSecoes(
            @Param("palavra") String palavra,
            @Param("tipo") TipoSecao tipo,
            @Param("lit") TempoLit lit
    );
}