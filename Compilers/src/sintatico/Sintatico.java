package sintatico;

import lexico.Token;

import java.util.List;

public class Sintatico {

    //Inicia a pilha de simbolos
    Pilha symbols = new Pilha();
    //Inicia a pilha de entrada
    Pilha inputStack = new Pilha();


    public void analyze(List<Token> tokensList){
        //Popula a pilha de simbolos com $ e inicio de arquivo
        symbols.startSymbolStack();

        //Popula a pilha de entrada com os tokens gerados pelo analisador léxico
        for (int i = tokensList.size() - 1; i >= 0; i--){
            inputStack.empilhar(tokensList.get(i).tag);
        }


        do {
            //Primeiro simbolo da pilha de simbolos
            int stackTop = symbols.exibeUltimoValor();
            //Primeiro simbolo da pilha de entrada
            int inputTop = inputStack.exibeUltimoValor();

            //Verifica se a pilha de simbolos está vazia ou se o primeiro simbolo é terminal
            if(symbols.pilhaVazia() || isTerminal(stackTop)){
                //Caso true

                if(stackTop == inputTop){
                    System.out.println("Desempilhado da matriz de simbolos" + stackTop + " desempilhado da matriz entrada " + inputTop);

                    symbols.desempilhar();
                    inputStack.desempilhar();
                }else{
                    System.out.println(stackTop);
                    System.out.println(inputTop);
                    System.out.println(ParserConstants.PARSER_ERROR[stackTop]);
                    break;
                }

            }else if (isNotTerminal(stackTop)){
                if(isInParserMatrix(stackTop, inputTop)){
                    symbols.desempilhar();
                    int [] productionRules = getProductionRules(getParserMatrix(stackTop, inputTop));
                    for (int i = productionRules.length - 1; i >= 0; i--){
                        inputStack.empilhar(productionRules[i]);
                    }
                }else {
                    System.out.println(stackTop);
                    System.out.println(inputTop);
                    System.out.println(ParserConstants.PARSER_ERROR[stackTop]);
                    break;
                }
            }else {
                System.out.println(stackTop);
                System.out.println(inputTop);
                System.out.println(ParserConstants.PARSER_ERROR[stackTop]);
                break;
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
