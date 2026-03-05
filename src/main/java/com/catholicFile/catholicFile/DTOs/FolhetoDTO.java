package com.catholicFile.catholicFile.DTOs;

import com.catholicFile.catholicFile.entities.Folheto;
import com.catholicFile.catholicFile.entities.SecaoFolheto;

import java.util.List;

public record FolhetoDTO(
        Long id,
        String titulo,
        List<Long> secoesIds
) {

    public FolhetoDTO(Folheto folheto) {
        this(
                folheto.getId(),
                folheto.getTitulo(),
                folheto.getSecoes()
                        .stream()
                        .map(SecaoFolheto::getId)
                        .toList()
        );
    }
}