package br.fiap.bank.atm.presentation;

import br.fiap.bank.atm.application.AutorizacaoService;
import br.fiap.bank.atm.application.ContaService;
import br.fiap.bank.atm.model.Dinheiro;
import br.fiap.bank.atm.model.Movimentacao;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class TerminalBancarioController {

    private ContaService contaService;
    private AutorizacaoService autorizacaoService;
    private Scanner scanner;

    private static final String SEPARADOR = "============================================";

    public TerminalBancarioController(ContaService contaService, AutorizacaoService autorizacaoService) {
        this.contaService = contaService;
        this.autorizacaoService = autorizacaoService;
        this.scanner = new Scanner(System.in);
    }

    public void iniciar() {
        System.out.println(SEPARADOR);
        System.out.println("      FIAP BANK - TERMINAL ATM (BETA)      ");
        System.out.println(SEPARADOR);

        if (!autenticar()) {
            System.out.println("Sessão encerrada por segurança. Retire seu cartão.");
            return;
        }

        exibirMenuPrincipal();
    }

    private Boolean autenticar() {
        Integer tentativas = 0;
        Integer maxTentativas = 3;

        while (tentativas < maxTentativas) {
            System.out.print("\nDigite sua senha: ");
            String senha = scanner.nextLine().trim();

            if (autorizacaoService.autorizar(senha)) {
                System.out.println("Acesso autorizado! Bem-vindo, " + contaService.obterNomeCliente() + "!");
                return Boolean.TRUE;
            }

            tentativas++;
            Integer restantes = maxTentativas - tentativas;

            if (restantes > 0) {
                System.out.println("Senha incorreta. Tentativa(s) restante(s): " + restantes);
            }
        }

        System.out.println("\nConta bloqueada por excesso de tentativas. Contate seu banco.");
        return Boolean.FALSE;
    }

    public void exibirMenuPrincipal() {
        Boolean continuar = Boolean.TRUE;

        while (continuar) {
            System.out.println("\n" + SEPARADOR);
            System.out.println("              MENU PRINCIPAL               ");
            System.out.println(SEPARADOR);
            System.out.println("[1] Consultar Saldo");
            System.out.println("[2] Fazer Depósito");
            System.out.println("[3] Fazer Saque");
            System.out.println("[4] Histórico de Movimentações");
            System.out.println("[5] Sair");
            System.out.println(SEPARADOR);
            System.out.print("Escolha uma opção: ");

            String opcao = scanner.nextLine().trim();

            switch (opcao) {
                case "1":
                    exibirSaldo();
                    break;
                case "2":
                    realizarDeposito();
                    break;
                case "3":
                    realizarSaque();
                    break;
                case "4":
                    exibirMovimentacoes();
                    break;
                case "5":
                    System.out.println("\nObrigado por utilizar o FIAP Bank ATM. Até logo!");
                    continuar = Boolean.FALSE;
                    break;
                default:
                    System.out.println("Opção inválida. Escolha entre 1 e 5.");
            }
        }
    }

    public void exibirSaldo() {
        System.out.println("\n--- Consulta de Saldo ---");
        System.out.println("Saldo disponível: " + contaService.obterSaldo());
    }

    public void realizarDeposito() {
        System.out.println("\n--- Fazer Depósito ---");
        System.out.print("Informe o valor do depósito: R$ ");
        String entrada = scanner.nextLine().trim().replace(",", ".");

        try {
            BigDecimal valor = new BigDecimal(entrada);
            contaService.realizarDeposito(new Dinheiro(valor));
            System.out.println("Depósito realizado com sucesso!");
            System.out.println("Novo saldo: " + contaService.obterSaldo());
        } catch (NumberFormatException e) {
            System.out.println("Valor inválido. Digite um número válido.");
        } catch (IllegalArgumentException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    public void realizarSaque() {
        System.out.println("\n--- Fazer Saque ---");
        System.out.print("Informe o valor do saque: R$ ");
        String entrada = scanner.nextLine().trim().replace(",", ".");

        try {
            BigDecimal valor = new BigDecimal(entrada);
            contaService.realizarSaque(new Dinheiro(valor));
            System.out.println("Saque realizado com sucesso!");
            System.out.println("Novo saldo: " + contaService.obterSaldo());
        } catch (NumberFormatException e) {
            System.out.println("Valor inválido. Digite um número válido.");
        } catch (IllegalArgumentException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    public void exibirMovimentacoes() {
        System.out.println("\n--- Histórico de Movimentações ---");

        List<Movimentacao> movimentacoes = contaService.obterMovimentacoes();

        if (movimentacoes.isEmpty()) {
            System.out.println("Nenhuma movimentação encontrada.");
            return;
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

        System.out.printf("%-22s | %-12s | %s%n", "Data/Hora", "Tipo", "Valor");
        System.out.println("-------------------------------------------------------");

        for (Movimentacao mov : movimentacoes) {
            System.out.printf("%-22s | %-12s | %s%n",
                    mov.getDataHora().format(formatter),
                    mov.getTipo(),
                    mov.getValor());
        }
    }
}
