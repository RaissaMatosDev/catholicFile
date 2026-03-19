package com.catholicFile.catholicFile.DTOs;

import com.catholicFile.catholicFile.enums.TempoLit;
import com.catholicFile.catholicFile.enums.TipoSecao;

public record SecaoFolhetoDTO(TipoSecao tipo, String conteudo, TempoLit lit) {
}
