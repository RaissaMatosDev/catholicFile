package com.catholicFile.catholicFile.entities;


import com.catholicFile.catholicFile.enums.TipoSecao;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
//Esta entidade é registrada no banco na tabela secao folhetos,
// e utiliza relacionamento ManytoOne em relação a folheto
// (um folheto contem varias secoes folheto).

@Entity
@Table(name = "secao_folheto")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SecaoFolheto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private TipoSecao tipo;

    @Column(columnDefinition = "TEXT")
    private String conteudo;

    @ManyToOne
    @JoinColumn(name = "folheto_id")
    private Folheto folheto;

    private Integer ordem;
}