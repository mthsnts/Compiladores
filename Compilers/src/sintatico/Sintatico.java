package sintatico;

import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;
import lexico.Token;

import javax.swing.*;
import java.util.Collections;
import java.util.List;

public class Sintatico {

    //Inicia a pilha de simbolos
    Pilha symbols = new Pilha();
    //Inicia a pilha de entrada
    Pilha inputStack = new Pilha();


    public void analyze(List<Token> tokensList){
        symbols.startSymbolStack();
        populateInputStack(tokensList);

        do {
            int stackTop = symbols.exibeUltimoValor();
            int inputTop = inputStack.exibeUltimoValor();

            while (stackTop == Constants.EPSILON){
                symbols.desempilhar();
                stackTop = symbols.exibeUltimoValor();
            }


            if(symbols.pilhaVazia() || isTerminal(stackTop)){
                if(stackTop == inputTop){
                    if(stackTop == Constants.DOLLAR){
                        System.out.println("ANÁLISE SINTATICA FINALIZADA COM SUCESSO");
                        break;
                    }
                    System.out.println("Desempilhado da matriz de simbolos " + stackTop + " desempilhado da matriz entrada " + inputTop);
                    symbols.desempilhar();
                    inputStack.desempilhar();
                }else{
                    System.out.println("M(X,a) => erro" + "X = " + stackTop + " a = " + inputTop);
                    System.out.println(ParserConstants.PARSER_ERROR[stackTop]);
                    break;
                }

            }else {
                if(isInParserMatrix(stackTop, inputTop)){
                    symbols.desempilhar();
                    int [] productionRules = getProductionRules(getParserMatrix(stackTop, inputTop));

                    for (int i = productionRules.length - 1; i >= 0; i--){
                        symbols.empilhar(productionRules[i]);
                    }
                }else {
                    for (int i = 0; i < symbols.pilha.length; i++){
                        System.out.println("  " + symbols.pilha[i] + " - " + inputStack.pilha[i]);
                    }
                    System.out.println("Não encontrado na matriz de parse " + stackTop + " - " + inputTop);
                    System.out.println(ParserConstants.PARSER_ERROR[stackTop]);
                    break;
                }
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

    public void populateInputStack(List<Token> tokensList){
        for (int i = tokensList.size() - 1; i >= 0; i--){
            inputStack.empilhar(tokensList.get(i).tag);
        }
    }


}
