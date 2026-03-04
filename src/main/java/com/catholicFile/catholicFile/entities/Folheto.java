package com.catholicFile.catholicFile.entities;


import com.catholicFile.catholicFile.DTOs.FolhetoDTO;
import com.catholicFile.catholicFile.enums.TipoSecao;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "folhetos")

public class Folheto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private TipoSecao tipoSecao;

    @Column(columnDefinition = "TEXT")
    private String conteudo;


    @OneToMany(mappedBy = "folheto", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SecaoFolheto> secoes = new ArrayList<>();

    public Folheto(FolhetoDTO dto) {
        this.tipoSecao = dto.tipoSecao();
        this.conteudo = dto.conteudo();
    }
    }


