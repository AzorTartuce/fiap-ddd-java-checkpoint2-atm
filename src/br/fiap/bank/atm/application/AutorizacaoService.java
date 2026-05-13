package br.fiap.bank.atm.application;

import br.fiap.bank.atm.model.Conta;

public class AutorizacaoService {

    private Conta conta;

    public AutorizacaoService(Conta conta) {
        this.conta = conta;
    }

    public Boolean autorizar(String senha) {
        return conta.getContaAcesso().validarSenha(senha);
    }

    public Boolean isContaBloqueada() {
        return conta.getContaAcesso().isBloqueado();
    }
}
