package com.catholicfile.catholicfile.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.domain.Page;

import java.util.List;

@Schema(description = "Resposta paginada padrão")
public record PageResponseDTO<T>(

        @Schema(description = "Lista de dados da página")
        List<T> content,

        @Schema(description = "Total de elementos", example = "100")
        long totalElements,

        @Schema(description = "Total de páginas", example = "10")
        int totalPages,

        @Schema(description = "Número da página atual (começa em 0)", example = "0")
        int number,

        @Schema(description = "Quantidade de elementos por página", example = "10")
        int size,

        @Schema(description = "Indica se é a primeira página", example = "true")
        boolean first,

        @Schema(description = "Indica se é a última página", example = "false")
        boolean last

) {
    public static <T> PageResponseDTO<T> from(Page<T> page) {
        return new PageResponseDTO<>(
                page.getContent(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.getNumber(),
                page.getSize(),
                page.isFirst(),
                page.isLast()
        );
    }
}
