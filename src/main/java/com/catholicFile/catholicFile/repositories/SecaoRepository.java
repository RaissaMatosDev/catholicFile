package com.catholicFile.catholicFile.repositories;

import com.catholicFile.catholicFile.entities.SecaoFolheto;
import com.catholicFile.catholicFile.enums.TipoSecao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SecaoRepository extends JpaRepository<SecaoFolheto, Long> {
    List<SecaoFolheto> findByFolhetoId(Long folhetoId);
    Optional<SecaoFolheto> findByFolhetoIdAndTipo(Long folhetoId, TipoSecao tipo);

}