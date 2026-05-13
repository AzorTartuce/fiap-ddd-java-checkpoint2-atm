package br.fiap.bank.atm.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class ContaPoupanca extends Conta {

    private static final Double RENDIMENTO_MENSAL = 1.1;

    public ContaPoupanca(Cliente cliente, ContaAcesso contaAcesso, Dinheiro saldo) {
        super(cliente, contaAcesso, saldo, RENDIMENTO_MENSAL);
    }

    @Override
    protected void aplicarRegraDeTaxa() {
        // sem taxa no saque
    }

    public void aplicarTaxaMensal() {
        BigDecimal fator = BigDecimal.valueOf(RENDIMENTO_MENSAL / 100.0);
        Dinheiro rendimento = new Dinheiro(
            this.saldo.getValor().multiply(fator).setScale(2, RoundingMode.HALF_UP)
        );
        this.saldo = this.saldo.adicionar(rendimento);
        registrarMovimentacao(rendimento, TipoMovimentacao.RENDIMENTO);
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
