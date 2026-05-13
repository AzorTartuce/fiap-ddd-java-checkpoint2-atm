package br.fiap.bank.atm.model;

import java.time.LocalDateTime;

public class Movimentacao extends BaseEntity {

    private LocalDateTime dataHora;
    private TipoMovimentacao tipo;
    private Dinheiro valor;

    public Movimentacao(LocalDateTime dataHora, Dinheiro valor, TipoMovimentacao tipo) {
        super();
        this.dataHora = dataHora;
        this.valor = valor;
        this.tipo = tipo;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public Dinheiro getValor() {
        return valor;
    }

    public TipoMovimentacao getTipo() {
        return tipo;
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
