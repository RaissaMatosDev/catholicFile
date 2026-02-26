package com.catholicFile.catholicFile.entities;


import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "folhetos")

public class Folheto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;


    @OneToMany(mappedBy = "folheto", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SecaoFolheto> secoes = new ArrayList<>();

    public Folheto() {

    }
}
