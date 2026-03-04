package com.catholicFile.catholicFile.DTOs;

import com.catholicFile.catholicFile.entities.Folheto;
import com.catholicFile.catholicFile.enums.TipoSecao;
import jakarta.validation.constraints.NotNull;

public record FolhetoDTO(
                        Long id,

                        @NotNull
                        TipoSecao tipoSecao,

                        @NotNull
                        String conteudo) {

    public FolhetoDTO(Folheto folheto) {
        this (
                folheto.getId(),
                folheto.getTipoSecao(),
                folheto.getConteudo()
        );


    }
}
