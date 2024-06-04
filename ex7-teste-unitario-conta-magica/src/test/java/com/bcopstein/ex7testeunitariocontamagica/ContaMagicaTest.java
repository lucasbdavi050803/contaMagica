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
        conta = new ContaMagica("999999-54", "John Doe");
    }

    @Test
    // Teste para verificar as condições iniciais da conta.
    void testaCondicoesIniciais() {
        // Verifica se o nome do correntista está correto.
        assertEquals("John Doe", conta.getNomeCorrentista());
        // Verifica se o número da conta está correto.
        assertEquals("999999-54", conta.getNumeroConta());
        // Verifica se o saldo inicial é 0.
        assertEquals(0.0, conta.getSaldo());
        // Verifica se a categoria inicial é SILVER.
        assertEquals(ContaMagica.SILVER, conta.getStatus());
    }


    @Test
    // Teste para verificar se depósitos válidos são tratados corretamente.
    void testaDepositoValidoSilver() throws INVALID_OPER_EXCEPTION {
        // Verifica se o método de depósito não lança exceção para um valor válido.
        conta.deposito(1000);
        // Verifica se o saldo é atualizado corretamente após o depósito.
        assertEquals(1000, conta.getSaldo());
    }

    @Test
    // Teste para verificar o comportamento do método de depósito com um valor inválido.
    void testaDepositoInvalido() {
        // Verifica se o método de depósito lança exceção para um valor negativo.
        assertThrows(INVALID_OPER_EXCEPTION.class, () -> conta.deposito(-100));
    }

    @Test
    // Teste para verificar se os depositos sao valorizados corretamente para conta tipo silver
    void testaValorizacaoeParaSilver() throws INVALID_OPER_EXCEPTION {
        //conta deposita 1000
        conta.deposito(1000);
        // Verifica se o deposito de mais mil foi valorizado
        assertEquals(1000, conta.getSaldo());
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
    // Teste para verificar se os depositos sao valorizados corretamente para conta tipo gold
    void testaValorizacaoeParaGold() throws INVALID_OPER_EXCEPTION {
        //conta para gold
        conta.deposito(50000);
        conta.deposito(400);
        // Verifica se o deposito de mais mil foi valorizado
        assertEquals(50404, conta.getSaldo());
    }

    @Test
    // Teste para verificar se os depositos sao valorizados corretamente para conta tipo platinum
    void testaValorizacaoeParaPlatinum() throws INVALID_OPER_EXCEPTION {
        //conta para gold
        conta.deposito(50000);
        //conta para platinum
        conta.deposito(200000);
        // este deposito valoriza 2,5%
        conta.deposito(400);
        // Verifica se os depositos foram valorizados corretamente 1% gold, 2,5% platinum
        assertEquals( 252410, conta.getSaldo());
    }


    @Test
    // Teste para verificar se a exceção de nome inválido é lançada corretamente.
    void testaExcecaoNomeInvalido() {
        // Verifica se a exceção é lançada ao tentar criar uma conta com nome muito curto.
        Exception exception = assertThrows(IllegalNameException.class, () -> {
            new ContaMagica("999999-54", "Jo");
        });
        // Verifica se a mensagem da exceção é a esperada.
        assertEquals("Nome inválido!", exception.getMessage());
    }

    @Test
    // Teste para verificar se a exceção de número de conta inválido é lançada corretamente.
    void testaExcecaoNumeroInvalido() {
        // Verifica se a exceção é lançada ao tentar criar uma conta com número mal formatado.
        Exception exception = assertThrows(IllegalNumberException.class, () -> {
            new ContaMagica("123-1", "John Doe");
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
        assertEquals(300, conta.getSaldo());
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
        // Depósito que deveria resultar em uma mudança de categoria para GOLD, ja que nao e possivel pular diretamente para platinum.
        conta.deposito(200000);
        // Depósito que deveria resultar em uma mudança de categoria para PLATINUM
        conta.deposito(20);
        // Verifica se a categoria foi atualizada para PLATINUM.
        assertEquals(ContaMagica.PLATINUM, conta.getStatus());
    }

    @Test
    // Teste para verificar se a categoria é revertida corretamente ao diminuir o saldo.
    void testaDowngradeDeCategoriaGoldParaSilver() throws INVALID_OPER_EXCEPTION {
        conta.deposito(100000);
        // Realiza uma retirada que deve resultar em uma mudança de categoria.
        conta.retirada(80500);
        conta.retirada(10);
        // Verifica se a categoria foi atualizada para SILVER.
        assertEquals(ContaMagica.SILVER, conta.getStatus());
    }

    @Test
    // Teste para verificar se a categoria é revertida corretamente ao diminuir o saldo.
    void testaDowngradeDeCategoriaPlatinumParaGold() throws INVALID_OPER_EXCEPTION {
        conta.deposito(200000);
        conta.deposito(20);
        // Realiza uma retirada que deve resultar em uma mudança de categoria.
        conta.retirada(100500);
        conta.retirada(10);
        // Verifica se a categoria foi atualizada para GOLD
        assertEquals(ContaMagica.GOLD, conta.getStatus());

    }
}
