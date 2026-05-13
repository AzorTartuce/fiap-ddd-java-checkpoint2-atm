# FIAP Bank ATM — Versão Beta

> Checkpoint 2 — Domain Driven Design com Java  
> Disciplina: Domain Driven Design - Java | Professor: Eduardo dos Santos Ramos | Turma: 2ESPG

---

## Sobre o Projeto

Refatoração completa do sistema de Caixa Eletrônico (ATM) do FIAP Bank, evoluindo da versão Alpha (procedural) para a versão Beta orientada a objetos.

O sistema simula um terminal bancário via console, com autenticação por senha, operações financeiras e histórico de movimentações, tudo arquitetado seguindo os princípios de **Orientação a Objetos** e **Domain-Driven Design (DDD)**.

---

## Funcionalidades

- Autenticação com senha (bloqueio após 3 tentativas incorretas)
- Consulta de saldo
- Depósito
- Saque (com cobrança de taxa para Conta Corrente)
- Histórico de movimentações com data/hora, tipo e valor
- Suporte a dois tipos de conta: **Corrente** e **Poupança**

---

## Arquitetura — Camadas DDD

O projeto segue a separação estrita de responsabilidades em 4 camadas dentro do pacote `br.fiap.bank.atm`:

```
presentation  →  application  →  model
                                    ↑
                           infrastructure
```

### `model` — Domínio

Coração da aplicação. Contém todas as regras de negócio. **Nenhuma dependência visual (Scanner, System.out) existe aqui.**

| Classe | Tipo | Responsabilidade |
|---|---|---|
| `BaseEntity` | Abstract | UUID e data de criação para todas as entidades |
| `Cliente` | Entity | Representa o correntista, exige nome no construtor |
| `ContaAcesso` | Value Object | Gerencia senha, tentativas e bloqueio de conta |
| `Dinheiro` | Value Object | Encapsula BigDecimal com operações soma/subtrai/compara |
| `Movimentacao` | Entity | Registro imutável de uma transação (data, tipo, valor) |
| `Conta` | Abstract | Protege saldo, lista de movimentações e define Template Method do saque |
| `ContaCorrente` | Entity | Cobra taxa de R$ 25,00 por saque (registra como TAXA) |
| `ContaPoupanca` | Entity | Sem taxa no saque, possui rendimento mensal de 1,1% (registra como RENDIMENTO) |
| `StatusConta` | Enum | ATIVA, BLOQUEADA, ENCERRADA |
| `TipoMovimentacao` | Enum | DEPOSITO, SAQUE, TAXA, RENDIMENTO |

### `application` — Orquestração

Serviços que conectam a apresentação ao domínio. Sem lógica financeira, sem console.

| Classe | Padrão | Responsabilidade |
|---|---|---|
| `ContaFactory` | Singleton + Factory | Única instância responsável por criar ContaCorrente ou ContaPoupanca |
| `ContaService` | Service | Expõe operações de depósito, saque, saldo e histórico |
| `AutorizacaoService` | Service | Delega a validação de senha ao `ContaAcesso` do domínio |

### `presentation` — Interface com o Usuário

Responsável por toda a interação via console (Scanner, System.out). **Sem regras de negócio.**

| Classe | Responsabilidade |
|---|---|
| `TerminalBancarioController` | Menu principal, captura de entradas, formatação de telas |

### `infrastructure` — Dados

Simula a persistência em memória.

| Classe | Responsabilidade |
|---|---|
| `ContaRepository` | Armazena contas em um HashMap (simula banco de dados) |

---

## Estrutura de Diretórios

```
cp2java/
└── src/
    └── br/
        └── fiap/
            └── bank/
                └── atm/
                    ├── Main.java
                    ├── model/
                    │   ├── BaseEntity.java
                    │   ├── Cliente.java
                    │   ├── ContaAcesso.java
                    │   ├── Conta.java
                    │   ├── ContaCorrente.java
                    │   ├── ContaPoupanca.java
                    │   ├── Dinheiro.java
                    │   ├── Movimentacao.java
                    │   ├── StatusConta.java
                    │   └── TipoMovimentacao.java
                    ├── application/
                    │   ├── ContaFactory.java
                    │   ├── ContaService.java
                    │   └── AutorizacaoService.java
                    ├── presentation/
                    │   └── TerminalBancarioController.java
                    └── infrastructure/
                        └── ContaRepository.java
```

---

## Padrões de Projeto Aplicados

### Template Method — Saque
Definido na classe abstrata `Conta`, o algoritmo do saque é fixo:
1. Validar saldo disponível
2. Debitar o valor (registra `SAQUE` no histórico)
3. Aplicar taxa específica (`aplicarRegraDeTaxa()` — método abstrato)

Cada subclasse implementa o passo 3 de forma diferente:
- `ContaCorrente` → desconta R$ 25,00 e registra `TAXA`
- `ContaPoupanca` → não faz nada (sem taxa no saque)

```java
// Conta.java
public void realizarSaque(Dinheiro valor) {
    sacar(valor);           // passo fixo
    aplicarRegraDeTaxa();   // passo variável (polimorfismo)
}

protected abstract void aplicarRegraDeTaxa();
```

### Singleton — ContaFactory
Garante que apenas uma instância da fábrica exista em memória.

```java
public static ContaFactory getInstance() {
    if (instance == null) {
        instance = new ContaFactory();
    }
    return instance;
}
```

### Factory — ContaFactory
Centraliza a criação de contas, permitindo criar `ContaCorrente` ou `ContaPoupanca` através de um tipo abstrato `Conta`.

```java
Conta conta = ContaFactory.getInstance().criarContaCorrente(cliente, contaAcesso, saldo);
```

---

## Regras de Negócio

| Regra | Detalhe |
|---|---|
| Depósito | Valor deve ser maior que zero |
| Saque | Valor deve ser maior que zero e não pode exceder o saldo |
| Taxa de saque (CC) | R$ 25,00 cobrados a cada saque realizado, descontado do saldo |
| Bloqueio de conta | Após 3 senhas incorretas consecutivas, a conta é bloqueada |
| Histórico | Cada operação gera um registro com `LocalDateTime`, tipo e valor |

---

## Restrições Arquiteturais Cumpridas

| Restrição | Status |
|---|---|
| Proibido tipos primitivos (`int`, `double`, `boolean`…) | ✅ Apenas Wrappers (`Integer`, `Double`, `Boolean`, `BigDecimal`) |
| Pasta raiz `src` obrigatória | ✅ |
| Default Package proibido | ✅ Pacote base: `br.fiap.bank.atm` |
| Camada `model` sem Scanner ou System.out | ✅ |
| Camada `application` sem lógica visual | ✅ |
| `equals()` sobrescrito em todas as entidades e VOs | ✅ |
| Fluxo: presentation → application → model | ✅ |

---

## Como Executar

### Pré-requisito
- Java 11 ou superior instalado

### Compilar

```bash
mkdir -p out
javac -d out -sourcepath src $(find src -name "*.java")
```

### Executar

```bash
java -cp out br.fiap.bank.atm.Main
```

### Dados de Acesso (simulados)

| Campo | Valor |
|---|---|
| Cliente | João da Silva |
| Senha | `1234` |
| Tipo de Conta | Corrente |
| Saldo Inicial | R$ 1.000,00 |

---

## Exemplo de Uso

```
============================================
      FIAP BANK - TERMINAL ATM (BETA)
============================================

Digite sua senha: 1234
Acesso autorizado! Bem-vindo, João!

============================================
              MENU PRINCIPAL
============================================
[1] Consultar Saldo
[2] Fazer Depósito
[3] Fazer Saque
[4] Histórico de Movimentações
[5] Sair
============================================
Escolha uma opção: 3

--- Fazer Saque ---
Informe o valor do saque: R$ 100
Saque realizado com sucesso!
Novo saldo: R$ 875,00

(saldo = 1000 - 100 de saque - 25 de taxa = 875)

============================================
Escolha uma opção: 4

--- Histórico de Movimentações ---
Data/Hora              | Tipo         | Valor
-------------------------------------------------------
11/05/2026 14:32:01    | SAQUE        | R$ 100,00
11/05/2026 14:32:01    | TAXA         | R$ 25,00
```

---

## Tecnologias

- **Java** (sem frameworks externos)
- **BigDecimal** para precisão em valores monetários
- **LocalDateTime / LocalDate** para datas e horários
- **UUID** para identificadores únicos de entidades
- **ArrayList / HashMap** para coleções em memória

---

*Desenvolvido para o Checkpoint 2 da disciplina Domain Driven Design - Java — FIAP 2026.*
