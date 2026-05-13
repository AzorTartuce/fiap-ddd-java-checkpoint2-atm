package br.fiap.bank.atm.model;

public class Cliente extends BaseEntity {

    private String nomeCompleto;

    public Cliente(String nomeCompleto) {
        super();
        if (nomeCompleto == null || nomeCompleto.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome completo é obrigatório.");
        }
        this.nomeCompleto = nomeCompleto;
    }

    public String obterPrimeiroNome() {
        return nomeCompleto.split(" ")[0];
    }

    public String getNomeCompleto() {
        return nomeCompleto;
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
