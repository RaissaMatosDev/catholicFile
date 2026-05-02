package com.catholicfile.catholicfile.dtos;

import com.catholicfile.catholicfile.entities.Folheto;
import com.catholicfile.catholicfile.enums.TempoLit;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.util.List;

@Schema(description = "Dados de um folheto")
public record FolhetoDTO(

        Long id,

        @Schema(description = "Título do folheto", example = "Folheto de Exemplo")
        @NotNull String titulo,

        @Schema(description = "Tempo litúrgico", example = "PASCOA")
        @NotNull TempoLit lit,

        @Schema(description = "IDs das seções que compõem o folheto",
                example = "[{\n" +
                "      \"id\": 1\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": 2\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": 3\n" +
                "    }]")
        @NotNull List<com.catholicfile.catholicfile.dtos.SecaoFolhetoDTO> secoesIds
) {

    public FolhetoDTO(Folheto folheto) {
        this(
                folheto.getId(),
                folheto.getTitulo(),
                folheto.getLit(),
                folheto.getSecoes()
                        .stream()
                        .map(secao -> new com.catholicfile.catholicfile.dtos.SecaoFolhetoDTO(
                                secao.getId(),
                                secao.getTipo(),
                                secao.getConteudo(),
                                secao.getLit(),
                                secao.getTitulo()
                        ))
                        .toList()
        );
    }
}
