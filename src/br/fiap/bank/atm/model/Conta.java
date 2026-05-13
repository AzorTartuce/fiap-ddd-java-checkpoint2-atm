package br.fiap.bank.atm.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public abstract class Conta extends BaseEntity {

    protected Cliente cliente;
    protected Dinheiro saldo;
    protected Double taxa;
    protected StatusConta status;
    protected LocalDate dataAbertura;
    protected ContaAcesso contaAcesso;
    protected List<Movimentacao> movimentacoes;

    public Conta(Cliente cliente, ContaAcesso contaAcesso, Dinheiro saldo, Double taxa) {
        super();
        this.cliente = cliente;
        this.contaAcesso = contaAcesso;
        this.saldo = saldo;
        this.taxa = taxa;
        this.status = StatusConta.ATIVA;
        this.dataAbertura = LocalDate.now();
        this.movimentacoes = new ArrayList<>();
    }

    public void realizarSaque(Dinheiro valor) {
        sacar(valor);
        aplicarRegraDeTaxa();
    }

    public void realizarDeposito(Dinheiro valor) {
        depositar(valor);
    }

    private void depositar(Dinheiro valor) {
        if (valor == null || valor.menorOuIgualQue(new Dinheiro("0"))) {
            throw new IllegalArgumentException("Valor de depósito deve ser maior que zero.");
        }
        this.saldo = this.saldo.adicionar(valor);
        registrarMovimentacao(valor, TipoMovimentacao.DEPOSITO);
    }

    private void sacar(Dinheiro valor) {
        if (valor == null || valor.menorOuIgualQue(new Dinheiro("0"))) {
            throw new IllegalArgumentException("Valor de saque deve ser maior que zero.");
        }
        if (valor.maiorQue(this.saldo)) {
            throw new IllegalArgumentException("Saldo insuficiente para realizar o saque.");
        }
        this.saldo = this.saldo.subtrair(valor);
        registrarMovimentacao(valor, TipoMovimentacao.SAQUE);
    }

    protected abstract void aplicarRegraDeTaxa();

    protected void registrarMovimentacao(Dinheiro valor, TipoMovimentacao tipo) {
        movimentacoes.add(new Movimentacao(LocalDateTime.now(), valor, tipo));
    }

    public Dinheiro getSaldo() {
        return saldo;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public LocalDate getDataAbertura() {
        return dataAbertura;
    }

    public StatusConta getStatus() {
        return status;
    }

    public ContaAcesso getContaAcesso() {
        return contaAcesso;
    }

    public List<Movimentacao> getMovimentacoes() {
        return movimentacoes;
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
