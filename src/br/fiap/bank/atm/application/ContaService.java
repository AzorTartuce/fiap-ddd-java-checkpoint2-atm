package br.fiap.bank.atm.application;

import br.fiap.bank.atm.model.Conta;
import br.fiap.bank.atm.model.Dinheiro;
import br.fiap.bank.atm.model.Movimentacao;

import java.util.List;

public class ContaService {

    private Conta conta;

    public ContaService(Conta conta) {
        this.conta = conta;
    }

    public void realizarDeposito(Dinheiro valor) {
        conta.realizarDeposito(valor);
    }

    public void realizarSaque(Dinheiro valor) {
        conta.realizarSaque(valor);
    }

    public Dinheiro obterSaldo() {
        return conta.getSaldo();
    }

    public List<Movimentacao> obterMovimentacoes() {
        return conta.getMovimentacoes();
    }

    public String obterNomeCliente() {
        return conta.getCliente().obterPrimeiroNome();
    }
}
