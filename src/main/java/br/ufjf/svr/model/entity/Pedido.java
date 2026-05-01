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

    @OneToOne
    private Cliente comprador;

    @OneToOne
    private Colaborador vendedor;

    @OneToOne
    private Endereco endEntrega;

    @OneToOne
    private MetodoPagamento pagamento;

}