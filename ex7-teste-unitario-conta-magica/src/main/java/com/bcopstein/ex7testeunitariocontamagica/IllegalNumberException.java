package com.bcopstein.ex7testeunitariocontamagica;

public class IllegalNumberException extends RuntimeException {
    public IllegalNumberException(){
        super("Numero de conta invalido");
    }
}
