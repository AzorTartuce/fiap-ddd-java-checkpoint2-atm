package br.fiap.bank.atm;

import br.fiap.bank.atm.application.AutorizacaoService;
import br.fiap.bank.atm.application.ContaFactory;
import br.fiap.bank.atm.application.ContaService;
import br.fiap.bank.atm.infrastructure.ContaRepository;
import br.fiap.bank.atm.model.Cliente;
import br.fiap.bank.atm.model.Conta;
import br.fiap.bank.atm.model.ContaAcesso;
import br.fiap.bank.atm.model.Dinheiro;
import br.fiap.bank.atm.presentation.TerminalBancarioController;

public class Main {

    public static void main(String[] args) {

        Cliente cliente = new Cliente("João da Silva");
        ContaAcesso contaAcesso = new ContaAcesso("1234");
        Dinheiro saldoInicial = new Dinheiro("1000.00");

        ContaFactory factory = ContaFactory.getInstance();
        Conta conta = factory.criarContaCorrente(cliente, contaAcesso, saldoInicial);

        ContaRepository repository = new ContaRepository();
        repository.salvar(conta);

        ContaService contaService = new ContaService(conta);
        AutorizacaoService autorizacaoService = new AutorizacaoService(conta);

        TerminalBancarioController terminal = new TerminalBancarioController(contaService, autorizacaoService);
        terminal.iniciar();
    }
}
