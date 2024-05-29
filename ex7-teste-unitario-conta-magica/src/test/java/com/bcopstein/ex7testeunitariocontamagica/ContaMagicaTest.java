package com.bcopstein.ex7testeunitariocontamagica;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ContaMagicaTest {
    private ContaMagica conta;

    @BeforeEach
    // Método que configura as condições iniciais para cada teste.
    void setUp() {
        // Cria uma nova instância da ContaMagica antes de cada teste.
        conta = new ContaMagica("123456-7", "John Doe");
    }

    @Test
    // Teste para verificar as condições iniciais da conta.
    void testaCondicoesIniciais() {
        // Verifica se o nome do correntista está correto.
        assertEquals("John Doe", conta.getNomeCorrentista());
        // Verifica se o número da conta está correto.
        assertEquals("123456-7", conta.getNumeroConta());
        // Verifica se o saldo inicial é 0.
        assertEquals(0.0, conta.getSaldo(), 0.001);
        // Verifica se a categoria inicial é SILVER.
        assertEquals(ContaMagica.SILVER, conta.getStatus());
    }

    @Test
    // Teste para verificar se depósitos válidos são tratados corretamente.
    void testaDepositoValidoSilver() throws INVALID_OPER_EXCEPTION {
        // Verifica se o método de depósito não lança exceção para um valor válido.
        conta.deposito(1000);
        // Verifica se o saldo é atualizado corretamente após o depósito.
        assertEquals(1000, conta.getSaldo(), 0.001);
    }

    @Test
    // Teste para verificar o comportamento do método de depósito com um valor inválido.
    void testaDepositoInvalido() {
        // Verifica se o método de depósito lança exceção para um valor negativo.
        assertThrows(INVALID_OPER_EXCEPTION.class, () -> conta.deposito(-100));
    }

    @Test
    // Teste para verificar se a categoria é atualizada corretamente ao atingir o limite.
    void testaUpgradeParaGold() throws INVALID_OPER_EXCEPTION {
        // Depósito que deveria resultar em uma mudança de categoria para GOLD.
        conta.deposito(50000);
        // Verifica se a categoria foi atualizada para GOLD.
        assertEquals(ContaMagica.GOLD, conta.getStatus());
    }

    @Test
    // Teste para verificar se a exceção de nome inválido é lançada corretamente.
    void testaExcecaoNomeInvalido() {
        // Verifica se a exceção é lançada ao tentar criar uma conta com nome muito curto.
        Exception exception = assertThrows(IllegalNameException.class, () -> {
            new ContaMagica("123456-7", "Jo");
        });
        // Verifica se a mensagem da exceção é a esperada.
        assertEquals("Nome inválido!", exception.getMessage());
    }

    @Test
    // Teste para verificar se a exceção de número de conta inválido é lançada corretamente.
    void testaExcecaoNumeroInvalido() {
        // Verifica se a exceção é lançada ao tentar criar uma conta com número mal formatado.
        Exception exception = assertThrows(IllegalNumberException.class, () -> {
            new ContaMagica("123-456", "John Doe");
        });
        // Verifica se a mensagem da exceção é a esperada.
        assertEquals("Numero de conta invalido", exception.getMessage());
    }

    @Test
    // Teste para verificar se retiradas válidas são tratadas corretamente.
    void testaRetiradaValidaSilver() throws INVALID_OPER_EXCEPTION {
        conta.deposito(500);
        // Verifica se o método de retirada não lança exceção para um valor válido.
        conta.retirada(200);
        // Verifica se o saldo é atualizado corretamente após a retirada.
        assertEquals(300, conta.getSaldo(), 0.001);
    }

    @Test
    // Teste para verificar o comportamento do método de retirada com um valor inválido.
    void testaRetiradaInvalida() {
        // Verifica se o método de retirada lança exceção para um valor negativo.
        assertThrows(INVALID_OPER_EXCEPTION.class, () -> conta.retirada(-100));
    }

    @Test
    // Teste para verificar se a categoria é atualizada corretamente ao atingir o limite Platinum.
    void testaUpgradeParaPlatinum() throws INVALID_OPER_EXCEPTION {
        // Depósito que deveria resultar em uma mudança de categoria para PLATINUM.
        conta.deposito(200000);
        // Verifica se a categoria foi atualizada para PLATINUM.
        assertEquals(ContaMagica.PLATINUM, conta.getStatus());
    }

    @Test
    // Teste para verificar se a categoria é revertida corretamente ao diminuir o saldo.
    void testaDowngradeDeCategoria() throws INVALID_OPER_EXCEPTION {
        conta.deposito(100000);
        // Realiza uma retirada que deve resultar em uma mudança de categoria.
        conta.retirada(80000);
        // Verifica se a categoria foi atualizada para SILVER.
        assertEquals(ContaMagica.SILVER, conta.getStatus());
    }
}