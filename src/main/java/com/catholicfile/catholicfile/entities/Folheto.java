package com.catholicfile.catholicfile.entities;


import com.catholicfile.catholicfile.enums.TempoLit;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import lombok.*;

@Entity
@Table(name = "folheto")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Folheto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;

    @Enumerated(EnumType.ORDINAL)
    private TempoLit lit;


    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "folheto_secoes", // Nome da nova tabela de ligação
            joinColumns = @JoinColumn(name = "folheto_id"),
            inverseJoinColumns = @JoinColumn(name = "secao_id")
    )
    private List<SecaoFolheto> secoes = new ArrayList<>();

    public void adicionarSecao(SecaoFolheto secao){
        this.secoes.add(secao);

    }
}

