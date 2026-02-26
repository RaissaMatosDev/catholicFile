package com.catholicFile.catholicFile.entities;


import com.catholicFile.catholicFile.enums.TipoSecao;
import jakarta.persistence.*;
import lombok.*;
//Esta entidade é registrada no banco na tabela secao folhetos,
// e utiliza relacionamento ManytoOne em relação a folheto
// (um folheto contem varias secoes folheto).
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "secao_folhetos")
public class SecaoFolheto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;

    @Column(columnDefinition = "TEXT")
    private String conteudo;

    private Integer ordem;

    @Enumerated(EnumType.STRING)
    private TipoSecao tipo;

    @ManyToOne
    @JoinColumn(name = "folheto_id")
    private Folheto folheto;
}