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


    @OneToMany(mappedBy = "folheto",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER)
    private List<SecaoFolheto> secoes = new ArrayList<>();

    public void adicionarSecao(SecaoFolheto secao){
        secao.setFolheto(this);
        this.secoes.add(secao);
    }
}

