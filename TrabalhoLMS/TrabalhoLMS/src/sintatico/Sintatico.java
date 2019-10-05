/*
package sintatico;

import javax.swing.*;

import application.ControllerMain.Tabela;

import java.util.Collections;
import java.util.List;

public class Sintatico {

    //Inicia a pilha de entrada
    //Inicia a pilha de simbolos
    Pilha symbols = new Pilha();
    Pilha inputStack = new Pilha();


    public void analyze(List<Tabela> tokensList){
        symbols.startSymbolStack();
        populateInputStack(tokensList);

        do {
            Integer topoSimbolos = symbols.pilha[0];
             Integer topoEntrada = inputStack.pilha[0];

            while (topoSimbolos == Constants.EPSILON){
                symbols.desempilhar();
                topoSimbolos = symbols.pilha[0];
            }

            if(symbols.pilhaVazia() || isTerminal(topoSimbolos)){
                if(topoSimbolos.equals(topoEntrada)){
                    if(topoEntrada == Constants.DOLLAR){
                        JOptionPane.showMessageDialog(null,"Analise finalizada");
                        break;
                    }
                    System.out.println("Desempilhado da matriz de simbolos " + topoSimbolos + " desempilhado da matriz entrada " + topoEntrada);
                    symbols.desempilhar();
                    inputStack.desempilhar();
                }else{
                    JOptionPane.showMessageDialog(null,"\"M(X,a) => erro\" + \"X = \" + topoSimbolos + \" a = \" + topoEntrada");
                    System.out.println(ParserConstants.PARSER_ERROR[topoSimbolos]);
                    break;
                }

            }else {
                if(isInParserMatrix(topoSimbolos, topoEntrada)){
                    symbols.desempilhar();
                    int [] productionRules = getProductionRules(getParserMatrix(topoSimbolos, topoEntrada));

                    for (int i = productionRules.length - 1; i >= 0; i--){
                        symbols.empilhar(productionRules[i]);
                    }
                }else {
                    JOptionPane.showMessageDialog(null, "Não encontrado na matriz de parse " + topoSimbolos + " - " + topoEntrada);
                    for (int i = 0; i < symbols.pilha.length; i++){
                        System.out.println("  " + symbols.pilha[i] + " - " + inputStack.pilha[i]);
                    }
                    break;
                }
            }

        } while (!symbols.pilhaVazia());
    }

    public boolean isTerminal(int codigo){
        return codigo < ParserConstants.FIRST_NON_TERMINAL;
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
*/
