package com.catholicfile.catholicfile.dtos;

import com.catholicfile.catholicfile.entities.SecaoFolheto;
import com.catholicfile.catholicfile.enums.TempoLit;
import com.catholicfile.catholicfile.enums.TipoSecao;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Informações de uma seção folheto")
public record SecaoFolhetoDTO(

        @Schema(description = "Id da Seção (gerado automaticamente)")
        Long id,

        @Schema(description = "Tipo da seção", example = "ENTRADA")
        @NotNull TipoSecao tipo,

        @Schema(description = "Conteudo da seção", example = "Letra do Cântico")
        @NotNull String conteudo,

        @Schema(description = "Tempo Liturgico", example = "QUARESMA")
        @NotNull TempoLit lit,

        @Schema(description = "Titulo do cântico", example = "Quando eu cheguei aqui meu senhor já estava...")
        @NotNull String titulo
) {
    public SecaoFolhetoDTO(SecaoFolheto secao) {
        this(
                secao.getId(),
                secao.getTipo(),
                secao.getConteudo(),
                secao.getLit(),
                secao.getTitulo()
        );
    }

}


