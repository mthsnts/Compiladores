package lexico;

import sintatico.Constants;

public class Numero extends Token {
    
    public final int value;
    
    public Numero(int value) {
        super(Constants.t_INTEIRO, "Número");
        this.value = value;
    }
    
    public String toString(){
        return "" + value;
    }
}
