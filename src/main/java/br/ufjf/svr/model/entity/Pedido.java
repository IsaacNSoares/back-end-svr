package br.ufjf.svr.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String dataPedido;
    private String status;
    private Float valorTotal;

    @ManyToOne
    private Cliente comprador;

    @ManyToOne
    private Colaborador vendedor;

    @ManyToOne
    private Endereco endEntrega;

    @ManyToOne
    private MetodoPagamento pagamento;

}