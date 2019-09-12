package sample;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import lexico.Lexico;
import lexico.Token;
import sintatico.Constants;
import sintatico.ParserConstants;
import sintatico.Pilha;
import sintatico.Sintatico;

import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

public class Controller {


    @FXML
    private TextArea Error;

    @FXML
    private Button BLexico;

    public void BLexico(MouseEvent mouseEvent) throws IOException {

        FileChooser fileChooser = new FileChooser();
        String currentPath = Paths.get(".").toAbsolutePath().normalize().toString();
        fileChooser.setInitialDirectory(new File(currentPath));
        File selectedFile = fileChooser.showOpenDialog(new Stage());
        fileChooser.getInitialFileName();
        Lexico lex = new Lexico(selectedFile.getName());

        sintatico.Sintatico sintatico = new Sintatico();


          this.analyze(lex.scanAll());

    }

    //Inicia a pilha de simbolos
    Pilha symbols = new Pilha();
    Pilha inputStack = new Pilha();


    public void analyze(List<Token> tokensList){
        //popular a pilha se imbolos
        symbols.startSymbolStack();
        //popula a pilha de entrada
        populateInputStack(tokensList);

        do {
            //pega os primeiros simbolos das pilhas
            Integer topoSimbolos = symbols.pilha[0];
            Integer topoEntrada = inputStack.pilha[0];

            //remove cadeia vazia
            while (topoSimbolos == Constants.EPSILON){
                symbols.desempilhar();
                topoSimbolos = symbols.pilha[0];
            }

            //se x for terminal ou a pilha estiver vazia
            if(symbols.pilhaVazia() || isTerminal(topoSimbolos)){
                //se x = a e x é $ então analise terminada
                if(topoSimbolos.equals(topoEntrada)){
                    if(topoEntrada == Constants.DOLLAR){
//                        JOptionPane.showMessageDialog(null,"Analise finalizada");
                        Error.setText("Analise finalizada");
                        break;
                    }
                    Error.setText("Desempilhado da matriz de simbolos " + topoSimbolos + " desempilhado da matriz entrada " + topoEntrada);
                    symbols.desempilhar();
                    inputStack.desempilhar();
                }else{
//                    JOptionPane.showMessageDialog(null,"\"M(X,a) => erro\" + \"X = \" + topoSimbolos + \" a = \" + topoEntrada");
                    Error.setText("\"M(X,a) => erro\" + \"X = \" + topoSimbolos + \" a = \" + topoEntrada");
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
//                    JOptionPane.showMessageDialog(null, "Não encontrado na matriz de parse " + topoSimbolos + " - " + topoEntrada);
                    Error.setText("Não encontrado na matriz de parse " + topoSimbolos + " - " + topoEntrada);
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
