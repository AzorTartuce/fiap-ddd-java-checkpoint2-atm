package br.fiap.bank.atm.infrastructure;

import br.fiap.bank.atm.model.Conta;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ContaRepository {

    private Map<UUID, Conta> contas;

    public ContaRepository() {
        this.contas = new HashMap<>();
    }

    public void salvar(Conta conta) {
        contas.put(conta.getId(), conta);
    }

    public Conta buscarPorId(UUID id) {
        return contas.get(id);
    }

    public Boolean existe(UUID id) {
        return contas.containsKey(id);
    }
}
