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

        // Primeiro criei o cliente com nome completo e a senha de acesso dele.
        // Usei a classe Dinheiro no lugar de double porque aprendi que double
        // tem problemas de precisão com dinheiro (tipo 0.1 + 0.2 != 0.3).
        Cliente cliente = new Cliente("João da Silva");
        ContaAcesso contaAcesso = new ContaAcesso("1234");
        Dinheiro saldoInicial = new Dinheiro("0.00");

        // Aqui usei o padrão Factory para criar a conta em vez de chamar
        // new ContaCorrente() direto. A ideia é que o Main não precisa saber
        // os detalhes de como cada tipo de conta é criado, só pede pra factory fazer.
        // O getInstance() é o padrão Singleton, garante que só existe uma factory no sistema.
        ContaFactory factory = ContaFactory.getInstance();
        Conta conta = factory.criarContaCorrente(cliente, contaAcesso, saldoInicial);

        // O repository serve para guardar as contas em memória (usei HashMap internamente).
        // Em um sistema real isso seria um banco de dados, mas por enquanto
        // a memória resolve para testar o funcionamento.
        ContaRepository repository = new ContaRepository();
        repository.salvar(conta);

        // Separei as responsabilidades em dois serviços:
        // ContaService cuida das operações financeiras (saque, depósito, saldo).
        // AutorizacaoService cuida só da autenticação (validar senha, checar bloqueio).
        // Fiz assim porque misturar tudo em um serviço só ficaria bagunçado.
        ContaService contaService = new ContaService(conta);
        AutorizacaoService autorizacaoService = new AutorizacaoService(conta);

        // O terminal é a camada de apresentação, é ela que conversa com o usuário.
        // Passei os dois serviços para ela porque o terminal precisa tanto
        // autenticar quanto executar as operações.
        TerminalBancarioController terminal = new TerminalBancarioController(contaService, autorizacaoService);
        terminal.iniciar();
    }
}
