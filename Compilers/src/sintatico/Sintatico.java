package sintatico;

import com.sun.org.apache.bcel.internal.classfile.ConstantString;
import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;
import lexico.Token;
import util.Mensagem;
import util.Pilha;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class Sintatico {

    Pilha symbols = new Pilha();
    Pilha inputStack = new Pilha();


    public void analyze(List<Token> tokensList){
        symbols.startSymbolStack();
        for (int i = tokensList.size() - 1; i >= 0; i--){
            inputStack.empilhar(tokensList.get(i).tag);
        }


        do {
            int stackTop = symbols.exibeUltimoValor();
            int inputTop = inputStack.exibeUltimoValor();

            if(symbols.pilhaVazia() || isTerminal(stackTop)){
                if(stackTop == inputTop){
                    symbols.desempilhar();
                    inputStack.desempilhar();
                    System.out.println("Desempilhado da matriz de simbolos" + stackTop + " desempilhado da matriz entrada " + inputTop);
                }else{
                    System.out.println("erro");
                }

            }else if (isNotTerminal(stackTop)){
                if(isInParserMatrix(stackTop, inputTop)){
                    symbols.desempilhar();
                    int [] productionRules = getProductionRules(getParserMatrix(stackTop, inputTop));
                    for (int i = productionRules.length - 1; i >= 0; i--){
                        inputStack.empilhar(productionRules[i]);
                    }
                }else {
                    System.out.println("erro");
                }
            }else {
                System.out.println("erro");
            }


        } while (!symbols.pilhaVazia());
    }

    public boolean isTerminal(int codigo){
        return codigo < ParserConstants.FIRST_NON_TERMINAL && codigo > Constants.DOLLAR;
    }

    public boolean isNotTerminal(int codigo){
        return codigo >= Constants.FIRST_NON_TERMINAL && codigo < Constants.FIRST_SEMANTIC_ACTION;
    }

    public boolean isInParserMatrix(int x, int a){
        return getParserMatrix(x,a) >= 0;
    }

    public int getParserMatrix(int x, int a){
        return Constants.PARSER_TABLE[x - ParserConstants.START_SYMBOL][a - 1];
    }

    public int[] getProductionRules(int matrixResult){
        return ParserConstants.PRODUCTIONS[matrixResult];
    }



}
