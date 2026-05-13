package br.fiap.bank.atm.model;

import java.math.BigDecimal;

public class ContaCorrente extends Conta {

    private static final Double TAXA_MANUTENCAO = 25.00;

    public ContaCorrente(Cliente cliente, ContaAcesso contaAcesso, Dinheiro saldo) {
        super(cliente, contaAcesso, saldo, TAXA_MANUTENCAO);
    }

    @Override
    protected void aplicarRegraDeTaxa() {
        Dinheiro taxaSaque = new Dinheiro(BigDecimal.valueOf(TAXA_MANUTENCAO));
        if (this.saldo.maiorOuIgualQue(taxaSaque)) {
            this.saldo = this.saldo.subtrair(taxaSaque);
            registrarMovimentacao(taxaSaque, TipoMovimentacao.TAXA);
        }
    }

    public void aplicarTaxaMensal() {
        aplicarRegraDeTaxa();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
