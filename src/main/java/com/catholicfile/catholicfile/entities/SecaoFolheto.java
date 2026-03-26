package com.catholicfile.catholicfile.entities;


import com.catholicfile.catholicfile.dtos.SecaoFolhetoDTO;
import com.catholicfile.catholicfile.enums.TempoLit;
import com.catholicfile.catholicfile.enums.TipoSecao;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


//Esta entidade é registrada no banco na tabela secao folhetos,
// e utiliza relacionamento ManytoOne em relação a folheto
// (um folheto contem varias secoes folheto).

@Entity
@Table(name = "secao_folheto",
        uniqueConstraints = @UniqueConstraint(columnNames = {"folheto_id", "tipo"})
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SecaoFolheto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo",nullable = false)
    private TipoSecao tipo;

    @Enumerated(EnumType.STRING)
    @Column(name = "tempo_lit")
    private TempoLit lit;


    @Column(columnDefinition = "TEXT")
    private String titulo;

    @Column(columnDefinition = "TEXT")
    private String conteudo;


    @ManyToMany(mappedBy = "secoes")
    @JsonIgnore
    private List<Folheto> folhetos = new ArrayList<>();


    public SecaoFolheto(SecaoFolhetoDTO dto){
        this.tipo = dto.tipo();
        this.conteudo = dto.conteudo();
    }
}