package com.catholicFile.catholicFile.entities;


import com.catholicFile.catholicFile.DTOs.FolhetoDTO;
import com.catholicFile.catholicFile.enums.TempoLit;
import com.catholicFile.catholicFile.enums.TipoSecao;
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

    private TempoLit lit;
    @Column(name = "secoes_ids", columnDefinition = "TEXT")
    private String secoesIds;

    @OneToMany(mappedBy = "folheto", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<SecaoFolheto> secoes = new ArrayList<>();

    public void adicionarSecao(SecaoFolheto secao){
        secao.setFolheto(this);
        this.secoes.add(secao);
    }
}

