package com.bcopstein.ex7testeunitariocontamagica;

public class ContaMagica {
    public static final int SILVER = 0;
    public static final int GOLD = 1;
    public static final int PLATINUM = 2;
    
    private String numero;
    private String nomeCorrentista;
    private double saldo;
    private int status;

    public ContaMagica(String numero, String nomeCorrentista) {
        this.numero = numero;
        this.nomeCorrentista = nomeCorrentista;
        this.saldo = 0.0; 
        this.status = SILVER;
        verificaNome(nomeCorrentista);
        verificaNroConta(numero);
    }

    public String getNumeroConta(){
        return numero;
    }

    public String getNomeCorrentista(){
        return nomeCorrentista;
    }

    public double getSaldo(){
        return saldo;
    }

    public int getStatus(){
        return status;
    }

    public void deposito(int valor) throws INVALID_OPER_EXCEPTION {

        if (valor <= 0){
            throw new INVALID_OPER_EXCEPTION();
        }
        if (status == SILVER && saldo < 50000){
            saldo += valor;
        } else if (status == GOLD && saldo >= 50000 && saldo < 200000){
            saldo += valor * 1.01; // BONUS de 1% para GOLD
        } else if (status == PLATINUM){
            saldo += valor * 1.025; // BONUS de 2% para PLATINUM
        }
        //caso seja gold vai para platinum 
        if(getSaldo() >= 200000 && getStatus() == GOLD) {
        this.status = 2;
        }
        //caso seja silver vai para gold
        if(getSaldo() >= 50000 && getStatus() == SILVER ) {
                this.status = 1;
        }
    }

    public void retirada(int valor) throws INVALID_OPER_EXCEPTION {
        if (valor <= 0){
            throw new INVALID_OPER_EXCEPTION();
        }
        if (status == PLATINUM && saldo > 100000 && saldo - valor >= 0){
            saldo -= valor;
        } else if (status == PLATINUM && saldo < 100000 && saldo - valor >= 0){
            saldo -= valor;
            status = GOLD;
        } else if (status == GOLD && saldo > 25000 && saldo - (valor * 1.025) >= 0){
            saldo -= valor * 1.025; // TAXA de 2.5% para GOLD
        } else if (status == GOLD && saldo <= 25000 && saldo - (valor * 1.01) >= 0){
            saldo -= valor * 1.01; // TAXA de 1% para GOLD
            status = SILVER;
        } else if (status == SILVER && saldo - valor >= 0){
            saldo -= valor;
        } else {
            throw new INVALID_OPER_EXCEPTION();
        }
    }

    private void verificaNroConta(String numero){
        int posTraco = numero.indexOf('-');
        String nroStr = numero.substring(0, posTraco);
        int nroConta = Integer.parseInt(nroStr);
        int verificador = Integer.parseInt(numero.substring(posTraco + 1));

        if (nroConta < 99999 || nroConta > 999999){
            throw new IllegalNumberException();
        }
        int soma = 0;
        for (int i = 0; i < nroStr.length(); i++){
            soma += (Integer.parseInt(nroStr.charAt(i) + ""));
        }
        if (soma != verificador){
            throw new IllegalNumberException();
        }
    }

    @Override
    public String toString() {
        return "ContaMagica [status=" + status + ", nomeCorrentista=" + nomeCorrentista + ", numero=" + numero
                + ", saldo=" + saldo + "]";
    }

    private void verificaNome(String nome){ 
        if (nome.length() < 3){
            throw new IllegalNameException();
        }
    }
}

class INVALID_OPER_EXCEPTION extends Exception {
    public INVALID_OPER_EXCEPTION() {
        super("Operação inválida!");
    }
}
