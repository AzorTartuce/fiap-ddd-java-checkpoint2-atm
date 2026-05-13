package br.fiap.bank.atm.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Dinheiro {

    private BigDecimal valor;

    public Dinheiro(BigDecimal valor) {
        this.valor = valor.setScale(2, RoundingMode.HALF_UP);
    }

    public Dinheiro(String valor) {
        this(new BigDecimal(valor));
    }

    public Dinheiro adicionar(Dinheiro outro) {
        return new Dinheiro(this.valor.add(outro.valor));
    }

    public Dinheiro subtrair(Dinheiro outro) {
        return new Dinheiro(this.valor.subtract(outro.valor));
    }

    public Boolean maiorQue(Dinheiro outro) {
        return this.valor.compareTo(outro.valor) > 0;
    }

    public Boolean maiorOuIgualQue(Dinheiro outro) {
        return this.valor.compareTo(outro.valor) >= 0;
    }

    public Boolean menorOuIgualQue(Dinheiro outro) {
        return this.valor.compareTo(outro.valor) <= 0;
    }

    public Boolean menorQue(Dinheiro outro) {
        return this.valor.compareTo(outro.valor) < 0;
    }

    public BigDecimal getValor() {
        return valor;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Dinheiro dinheiro = (Dinheiro) obj;
        return this.valor.compareTo(dinheiro.valor) == 0;
    }

    @Override
    public int hashCode() {
        return this.valor.hashCode();
    }

    @Override
    public String toString() {
        return String.format("R$ %.2f", valor);
    }
}
