package com.catholicFile.catholicFile.DTOs;

import com.catholicFile.catholicFile.enums.TipoSecao;

public record SecaoFolhetoDTO(TipoSecao tipoSecao, String conteudo, TipoSecao tipo) {
}
