package com.bcopstein.ex7testeunitariocontamagica;

public class IllegalNameException extends RuntimeException{
    public IllegalNameException(){
        super("Nome inválido!");
    }
}
