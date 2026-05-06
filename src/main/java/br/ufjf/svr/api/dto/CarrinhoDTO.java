package br.ufjf.svr.api.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class CarrinhoDTO {

    private long id;
    private long idCliente;
    private long idColaborador;

}