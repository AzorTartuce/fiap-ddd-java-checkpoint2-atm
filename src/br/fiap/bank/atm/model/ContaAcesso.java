package br.fiap.bank.atm.model;

public class ContaAcesso {

    public static final Integer MAXIMO_TENTATIVAS = 3;

    private String senha;
    private Integer tentativas;
    private Boolean bloqueado;

    public ContaAcesso(String senha) {
        this.senha = senha;
        this.tentativas = 0;
        this.bloqueado = Boolean.FALSE;
    }

    public Boolean validarSenha(String senhaInformada) {
        if (bloqueado) {
            return Boolean.FALSE;
        }
        if (this.senha.equals(senhaInformada)) {
            resetarTentativas();
            return Boolean.TRUE;
        }
        tentativas++;
        if (tentativas >= MAXIMO_TENTATIVAS) {
            bloqueado = Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    public Boolean isBloqueado() {
        return bloqueado;
    }

    public void resetarTentativas() {
        this.tentativas = 0;
        this.bloqueado = Boolean.FALSE;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ContaAcesso that = (ContaAcesso) obj;
        return this.senha.equals(that.senha);
    }
}
