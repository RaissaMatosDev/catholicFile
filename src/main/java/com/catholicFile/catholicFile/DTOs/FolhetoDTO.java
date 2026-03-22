package com.catholicFile.catholicFile.DTOs;

import com.catholicFile.catholicFile.entities.Folheto;
import com.catholicFile.catholicFile.entities.SecaoFolheto;
import com.catholicFile.catholicFile.enums.TempoLit;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.util.List;

@Schema(description = "Dados de um folheto")
public record FolhetoDTO(
        @Schema(description = "ID do folheto", example = "1")
        Long id,

        @Schema(description = "Título do folheto", example = "Folheto de Exemplo")
        @NotNull String titulo,

        @Schema(description = "Tempo litúrgico", example = "PASCOA")
        @NotNull TempoLit lit,

        @Schema(description = "IDs das seções que compõem o folheto", example = "[36,38,39]")
        @NotNull List<Long> secoesIds
) {

    public FolhetoDTO(Folheto folheto) {
        this(

                    folheto.getId(),
                    folheto.getTitulo(),
                    folheto.getLit(),
                    folheto.getSecoes()          // List<Secao>
                            .stream()
                            .map(SecaoFolheto::getId)   // pegar o ID de cada Secao
                            .toList()
        );
        }
    }
