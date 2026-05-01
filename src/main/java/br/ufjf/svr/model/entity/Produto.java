package br.ufjf.svr.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String sku;
    private String nome;
    private String descricao;
    private String urlFoto;
    private Float precoVarejo;
    private Float precoAtacado;

    @OneToOne
    private Fornecedor fornecedor;

}