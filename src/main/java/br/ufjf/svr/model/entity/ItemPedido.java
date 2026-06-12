package br.ufjf.svr.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor

public class ItemPedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private Float precoUnitario;
    private Integer quantidade;

    @ManyToOne
    private Loja lojaOrigem;

    @ManyToOne
    private Produto produto;

    @ManyToOne
    private Pedido pedido;
}