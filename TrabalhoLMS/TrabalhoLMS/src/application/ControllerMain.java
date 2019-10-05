package application;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import sintatico.Constants;
import sintatico.ParserConstants;
import sintatico.Pilha;

public class ControllerMain {

	@FXML
	private TextArea Texto;

	@FXML
	private TextArea Error;

	@FXML
	private Button ButtonCarregaArquivo;

	@FXML
	private Button ButtonLexico;

	@FXML
	private Button ButtonSintatico;

	@FXML
	private Button ButtonSemantico;

	@FXML
	private TableView<Tabela> Tabela;

	@FXML
	private TableColumn<Tabela,String> TabelaToken;

	@FXML
	private TableColumn<Tabela,String> TabelaTipo;

	@FXML
	private TableColumn<Tabela,String> TabelaLinha;

	@FXML
	private TableColumn<Tabela,String> TabelaID;

	//Todo o Léxico e o Código serão armazenados neste lugar
	ArrayList<String> Lexico = new ArrayList<String>();
	ArrayList<Integer> LexicoID = new ArrayList<Integer>();
	ArrayList<String> LexicoCodigo = new ArrayList<String>();
	ArrayList<String> LexicoLinha = new ArrayList<String>();

	@FXML 
	private void BSintatico(MouseEvent event)
	{

		System.out.print("Sintático");

//		Error.setText("O Sintático ainda não foi implementado, aguarde novas versões");

		//Inicia a pilha de simbolos
		Pilha symbols = new Pilha();
		Pilha inputStack = new Pilha();

		//popular a pilha se simbolos
		symbols.startSymbolStack();

		for (int i = LexicoID.size()-1; i >= 0; i--){
			inputStack.empilhar(LexicoID.get(i));
		}

		do {
			//pega os primeiros simbolos das pilhas
			Integer topoSimbolos = symbols.exibeUltimoValor();
			Integer topoEntrada = inputStack.exibeUltimoValor();

			System.out.println("topo da pilha de simbolos" + topoSimbolos + "\n");
			System.out.println("topo da pilha de entrada" + topoEntrada + "\n");

			//remove cadeia vazia
			while (topoSimbolos == Constants.EPSILON){
				symbols.desempilhar();
				topoSimbolos = symbols.exibeUltimoValor();
			}

			//se x for terminal ou a pilha estiver vazia
			if(symbols.pilhaVazia() || isTerminal(topoSimbolos)){

				System.out.println(topoEntrada);
				//se x = a e x Ã© $ entÃ£o analise terminada
				if(topoSimbolos.equals(topoEntrada)){
					if(topoEntrada == Constants.DOLLAR){
						//    	                        JOptionPane.showMessageDialog(null,"Analise finalizada");
						Error.setText("Analise finalizada");
						break;
					}
					System.out.println("Desempilhado da matriz de simbolos " + topoSimbolos + " desempilhado da matriz entrada " + topoEntrada);
					symbols.desempilhar();
					inputStack.desempilhar();
				}else{
					//    	                    JOptionPane.showMessageDialog(null,"\"M(X,a) => erro\" + \"X = \" + topoSimbolos + \" a = \" + topoEntrada");
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
					Error.setText("NÃ£o encontrado na matriz de parse " + topoSimbolos + " - " + topoEntrada);
					for (int i = 0; i < symbols.pilha.length; i++){
						System.out.println("  " + symbols.pilha[i] + " - " + inputStack.pilha[i]);
					}
					break;
				}
			}

		} while (!symbols.pilhaVazia());
	}

	@FXML 
	private void BLexico(MouseEvent event)
	{

		Lexico.clear();
		LexicoID.clear();
		LexicoCodigo.clear();
		LexicoLinha.clear();

		// Define a STRING do código inteiro léxico
		String Codigo = Texto.getText();

		// Define a quantia de letras do código léxico
		int Tamanho = Codigo.length();

		// Determina o atual token que esta
		String Token = ""; //Servirá para analizar qual as palavras do token

		// Determina um Token de Identificador
		String Identificador = ""; //Servirá para analizar qual as palavras do token

		// Definirá a letra atual para comparar se o token acabou ou não
		char Letra ;

		// Ignorar letras pois é complemento de um token
		int ignorar = 0;

		// Serve para quando achar um Token não ler como Identificador ou Número Inteiro
		boolean achou = false;

		int Linha = 1;

		//O sistema irá procurar letra por letra no código inteiro

		for (int i= 0;i<Tamanho;i++) {

			if (ignorar == 0) {

				Error.setText("Nenhum erro encontrado");

				achou = false;
				Token = "";
				Letra = Codigo.charAt(i);

				if (Letra == '$') {

					achou = true;
					Lexico.add("$");
					LexicoID.add(Constants.DOLLAR);
					LexicoCodigo.add("Dolar");
					LexicoLinha.add(Linha+"");
				}
				if (Letra == ',' || Letra == ';' || Letra == '.' || Letra == ':' || Letra == '(' || Letra == ')') {

					achou = true;

					if ( Letra == ',') {
						Lexico.add(",");
						LexicoID.add(Constants.t_TOKEN_5);
						LexicoCodigo.add("Símbolos Especiais");
						LexicoLinha.add(Linha+"");
					}

					if ( Letra == ';') {
						Lexico.add(";");
						LexicoID.add(Constants.t_TOKEN_14);
						LexicoCodigo.add("Símbolos Especiais");
						LexicoLinha.add(Linha+"");
					}

					if ( Letra == '.') {
						Lexico.add(".");
						LexicoID.add(Constants.t_TOKEN_16);
						LexicoCodigo.add("Símbolos Especiais");
						LexicoLinha.add(Linha+"");
					}

					if ( Letra == ':') {
						if( Codigo.charAt(i+1) == '=') {
							ignorar++;
							Lexico.add(":=");
							LexicoID.add(Constants.t_TOKEN_12);
							LexicoCodigo.add("Símbolos Especiais");
							LexicoLinha.add(Linha+"");
						}else {
							Lexico.add(":");
							LexicoID.add(Constants.t_TOKEN_13);
							LexicoCodigo.add("Símbolos Especiais");
							LexicoLinha.add(Linha+"");
						}

					}

					if ( Letra == '(' && '*' != Codigo.charAt(i+1)) {
						Lexico.add("(");
						LexicoID.add(Constants.t_TOKEN_17);
						LexicoCodigo.add("Símbolos Especiais");
						LexicoLinha.add(Linha+"");
					}

					if ( Letra == ')') {
						Lexico.add(")");
						LexicoID.add(Constants.t_TOKEN_18);
						LexicoCodigo.add("Símbolos Especiais");
						LexicoLinha.add(Linha+"");
					}

					//virgula (,); pontovirgula (;); ponto (.); doispontos(:);
					//atribuição(:=); abrepar((); fechapar()) ;

				}

				if (Letra >= 'A' && Letra <= 'Z') {

					if(Codigo.charAt(i) == 'A' && i < Tamanho - 3) {
						if(Codigo.charAt(i+1) == 'N') {
							if(Codigo.charAt(i+2) == 'D'){
								//AND
								achou = true;
								ignorar= 2;
								Lexico.add("AND");
								LexicoID.add(Constants.t_AND);
								LexicoCodigo.add("Palavras Reservadas");
								LexicoLinha.add(Linha+"");
							}
						}
					}

					if(Codigo.charAt(i) == 'B' && i < Tamanho - 5) {
						if(Codigo.charAt(i+1) == 'E') {
							if(Codigo.charAt(i+2) == 'G'){
								if(Codigo.charAt(i+3) == 'I'){
									if(Codigo.charAt(i+4) == 'N'){
										//BEGIN
										achou = true;
										ignorar= 4;
										Lexico.add("BEGIN");
										LexicoID.add(Constants.t_BEGIN);
										LexicoCodigo.add("Palavras Reservadas");
										LexicoLinha.add(Linha+"");
									}
								}
							}
						}
					}

					if(Codigo.charAt(i) == 'C' && i < Tamanho - 2){
						if(Codigo.charAt(i+1) == 'O' && i < Tamanho - 5){
							if(Codigo.charAt(i+2) == 'N'){
								if(Codigo.charAt(i+3) == 'S'){
									if(Codigo.charAt(i+4) == 'T'){
										//CONST
										achou = true;
										ignorar= 4;
										Lexico.add("CONST");
										LexicoID.add(Constants.t_CONST);
										LexicoCodigo.add("Palavras Reservadas");
										LexicoLinha.add(Linha+"");
									}
								}
							}
						}

						if(Codigo.charAt(i+1) == 'A' && i < Tamanho - 4){
							if(Codigo.charAt(i+2) == 'S'){
								if(Codigo.charAt(i+3) == 'E'){
									//CASE
									achou = true;
									ignorar= 3;
									Lexico.add("CASE");
									LexicoID.add(Constants.t_CASE);
									LexicoCodigo.add("Palavras Reservadas");
									LexicoLinha.add(Linha+"");
								}
							}

							if(Codigo.charAt(i+2) == 'L'){
								if(Codigo.charAt(i+3) == 'L'){
									//CALL
									achou = true;
									ignorar= 3;
									Lexico.add("CALL");
									LexicoID.add(Constants.t_CALL);
									LexicoCodigo.add("Palavras Reservadas");
									LexicoLinha.add(Linha+"");
								}
							}
						}
					}

					if(Codigo.charAt(i) == 'D' && i < Tamanho - 2){
						if(Codigo.charAt(i+1) == 'O'){
							//DO
							achou = true;
							ignorar= 1;
							Lexico.add("DO");
							LexicoID.add(Constants.t_DO);
							LexicoCodigo.add("Palavras Reservadas");
							LexicoLinha.add(Linha+"");
						}
					}

					if(Codigo.charAt(i) == 'E' && i < Tamanho - 3){
						if(Codigo.charAt(i+1) == 'N'){
							if(Codigo.charAt(i+2) == 'D'){
								//END
								achou = true;
								ignorar= 2;
								Lexico.add("END");
								LexicoID.add(Constants.t_END);
								LexicoCodigo.add("Palavras Reservadas");
								LexicoLinha.add(Linha+"");
							}
						}

						if(Codigo.charAt(i+1) == 'L' && i < Tamanho - 4){
							if(Codigo.charAt(i+2) == 'S'){
								if(Codigo.charAt(i+3) == 'E'){
									//ELSE
									achou = true;
									ignorar= 3;
									Lexico.add("ELSE");
									LexicoID.add(Constants.t_ELSE);
									LexicoCodigo.add("Palavras Reservadas");
									LexicoLinha.add(Linha+"");
								}
							}
						}
					}

					if(Codigo.charAt(i) == 'F' && i < Tamanho - 3){
						if(Codigo.charAt(i+1) == 'O'){
							if(Codigo.charAt(i+2) == 'R'){
								//FOR
								achou = true;
								ignorar= 2;
								Lexico.add("FOR");
								LexicoID.add(Constants.t_FOR);
								LexicoCodigo.add("Palavras Reservadas");
								LexicoLinha.add(Linha+"");
							}
						}
					}

					if(Codigo.charAt(i) == 'I' && i < Tamanho - 2){
						if(Codigo.charAt(i+1) == 'N' && i < Tamanho - 7){
							if(Codigo.charAt(i+2) == 'T'){
								if(Codigo.charAt(i+3) == 'E'){
									if(Codigo.charAt(i+4) == 'G'){
										if(Codigo.charAt(i+5) == 'E'){
											if(Codigo.charAt(i+6) == 'R'){
												//INTEGER
												achou = true;
												ignorar= 6;
												Lexico.add("INTEGER");
												LexicoID.add(Constants.t_INTEGER);
												LexicoCodigo.add("Palavras Reservadas");
												LexicoLinha.add(Linha+"");
											}
										}
									}
								}
							}
						}

						if(Codigo.charAt(i+1) == 'F'){
							//IF
							achou = true;
							ignorar= 1;
							Lexico.add("IF");
							LexicoID.add(Constants.t_IF);
							LexicoCodigo.add("Palavras Reservadas");
							LexicoLinha.add(Linha+"");
						}
					}

					if(Codigo.charAt(i) == 'N' && i < Tamanho - 3){
						if(Codigo.charAt(i+1) == 'O'){
							if(Codigo.charAt(i+2) == 'T'){
								//NOT
								achou = true;
								ignorar= 2;
								Lexico.add("NOT");
								LexicoID.add(Constants.t_NOT);
								LexicoCodigo.add("Palavras Reservadas");
								LexicoLinha.add(Linha+"");
							}
						}
					}

					if(Codigo.charAt(i) == 'O' && i < Tamanho - 2){
						if(Codigo.charAt(i+1) == 'F'){
							//OF
							achou = true;
							ignorar= 1;
							Lexico.add("OF");
							LexicoID.add(Constants.t_OF);
							LexicoCodigo.add("Palavras Reservadas");
							LexicoLinha.add(Linha+"");
						}

						if(Codigo.charAt(i+1) == 'R'){
							//OR
							achou = true;
							ignorar= 1;
							Lexico.add("OR");
							LexicoID.add(Constants.t_OR);
							LexicoCodigo.add("Palavras Reservadas");
							LexicoLinha.add(Linha+"");
						}

					}

					if(Codigo.charAt(i) == 'P' && i < Tamanho - 4){
						if(Codigo.charAt(i+1) == 'R'){
							if(Codigo.charAt(i+2) == 'O'){
								if(Codigo.charAt(i+3) == 'G' && i < Tamanho - 7){
									if(Codigo.charAt(i+4) == 'R'){
										if(Codigo.charAt(i+5) == 'A'){
											if(Codigo.charAt(i+6) == 'M'){
												//PROGRAM
												achou = true;
												ignorar= 6;
												Lexico.add("PROGRAM");
												LexicoID.add(Constants.t_PROGRAM);
												LexicoCodigo.add("Palavras Reservadas");
												LexicoLinha.add(Linha+"");
											}
										}
									}
								}

								if(Codigo.charAt(i+3) == 'C'  && i < Tamanho - 9){
									if(Codigo.charAt(i+4) == 'E'){
										if(Codigo.charAt(i+5) == 'D'){
											if(Codigo.charAt(i+6) == 'U'){
												if(Codigo.charAt(i+7) == 'R'){
													if(Codigo.charAt(i+8) == 'E'){
														//PROCEDURE
														achou = true;
														ignorar= 8;
														Lexico.add("PROCEDURE");
														LexicoID.add(Constants.t_PROCEDURE);
														LexicoCodigo.add("Palavras Reservadas");
														LexicoLinha.add(Linha+"");
													}
												}
											}
										}
									}
								}
							}
						}
					}

					if(Codigo.charAt(i) == 'R'  && i < Tamanho - 3){
						if(Codigo.charAt(i+1) == 'E'){

							if(Codigo.charAt(i+2) == 'A' && i < Tamanho - 6){
								if(Codigo.charAt(i+3) == 'D'){
									if(Codigo.charAt(i+4) == 'L'){
										if(Codigo.charAt(i+5) == 'N'){
											//READLN
											achou = true;
											ignorar= 5;
											Lexico.add("READLN");
											LexicoID.add(Constants.t_READLN);
											LexicoCodigo.add("Palavras Reservadas");
											LexicoLinha.add(Linha+"");
										}
									}
								}
							}

							if(Codigo.charAt(i+2) == 'P' && i < Tamanho - 6){
								if(Codigo.charAt(i+3) == 'E'){
									if(Codigo.charAt(i+4) == 'A'){
										if(Codigo.charAt(i+5) == 'T'){
											//REPEAT
											achou = true;
											ignorar= 5;
											Lexico.add("REPEAT");
											LexicoID.add(Constants.t_REPEAT);
											LexicoCodigo.add("Palavras Reservadas");
											LexicoLinha.add(Linha+"");
										}
									}
								}
							}
						}
					}

					if(Codigo.charAt(i) == 'T' && i < Tamanho - 2){

						if(Codigo.charAt(i+1) == 'O'){
							//TO
							achou = true;
							ignorar= 1;
							Lexico.add("TO");
							LexicoID.add(Constants.t_TO);
							LexicoCodigo.add("Palavras Reservadas");
							LexicoLinha.add(Linha+"");
						}

						if(Codigo.charAt(i+1) == 'H' && i < Tamanho - 4){
							if(Codigo.charAt(i+2) == 'E'){
								if(Codigo.charAt(i+3) == 'N'){
									//THEN
									achou = true;
									ignorar= 3;
									Lexico.add("THEN");
									LexicoID.add(Constants.t_THEN);
									LexicoCodigo.add("Palavras Reservadas");
									LexicoLinha.add(Linha+"");
								}
							}
						}
					}

					if(Codigo.charAt(i) == 'U' && i < Tamanho - 5){
						if(Codigo.charAt(i+1) == 'N'){
							if(Codigo.charAt(i+2) == 'T'){
								if(Codigo.charAt(i+3) == 'I'){
									if(Codigo.charAt(i+4) == 'L'){
										//UNTIL
										achou = true;
										ignorar= 4;
										Lexico.add("UNTIL");
										LexicoID.add(Constants.t_UNTIL);
										LexicoCodigo.add("Palavras Reservadas");
										LexicoLinha.add(Linha+"");
									}
								}
							}
						}
					}

					if(Codigo.charAt(i) == 'V' && i < Tamanho - 3){
						if(Codigo.charAt(i+1) == 'A'){
							if(Codigo.charAt(i+2) == 'R'){
								//VAR
								achou = true;
								ignorar= 2;
								Lexico.add("VAR");
								LexicoID.add(Constants.t_VAR);
								LexicoCodigo.add("Palavras Reservadas");
								LexicoLinha.add(Linha+"");
							}
						}
					}

					if(Codigo.charAt(i) == 'W' && i < Tamanho - 2){

						if(Codigo.charAt(i+1) == 'R' && i < Tamanho - 7){
							if(Codigo.charAt(i+2) == 'I'){
								if(Codigo.charAt(i+3) == 'T'){
									if(Codigo.charAt(i+4) == 'E'){
										if(Codigo.charAt(i+5) == 'L'){
											if(Codigo.charAt(i+6) == 'N'){
												//WRITELN
												achou = true;
												ignorar= 6;
												Lexico.add("WRITELN");
												LexicoID.add(Constants.t_WRITELN);
												LexicoCodigo.add("Palavras Reservadas");
												LexicoLinha.add(Linha+"");
											}
										}
									}
								}
							}
						}

						if(Codigo.charAt(i+1) == 'H' && i < Tamanho - 5){
							if(Codigo.charAt(i+2) == 'I'){
								if(Codigo.charAt(i+3) == 'L'){
									if(Codigo.charAt(i+4) == 'E'){
										//WHILE
										achou = true;
										ignorar= 4;
										Lexico.add("WHILE");
										LexicoID.add(Constants.t_WHILE);
										LexicoCodigo.add("Palavras Reservadas");
										LexicoLinha.add(Linha+"");
									}
								}
							}
						}
					}

					//AND – BEGIN – CALL – CASE – CONST – DO – ELSE – END – FOR – IF – INTEGER – NOT - OF
					//- OR - AND - PROCEDURE - PROGRAM – READLN - REPEAT - THEN - TO - UNTIL - VAR
					//- WHILE - WRITELN


					//Alguns símbolos não estavam inclusos mas eu adicionei também

					if(Codigo.charAt(i) == 'I' && i < Tamanho - 2){
						if(Codigo.charAt(i+1) == 'D'){
							//ID
							achou = true;
							ignorar= 1;
							Lexico.add("ID");
							LexicoID.add(Constants.t_IDENT);
							LexicoCodigo.add("Identificador");
							LexicoLinha.add(Linha+"");
						}
						if(Codigo.charAt(i+1) == 'N' && i < Tamanho - 7){
							if(Codigo.charAt(i+2) == 'T'){
								if(Codigo.charAt(i+3) == 'E'){
									if(Codigo.charAt(i+4) == 'I'){
										if(Codigo.charAt(i+5) == 'R'){
											if(Codigo.charAt(i+6) == 'O'){
												//WRITELN
												achou = true;
												ignorar= 6;
												Lexico.add("INTEGER");
												LexicoID.add(Constants.t_INTEGER);
												LexicoCodigo.add("Inteiro");
												LexicoLinha.add(Linha+"");
											}
										}
									}
								}
							}
						}
					}

					if(Codigo.charAt(i) == 'L' && i < Tamanho - 3){
						if(Codigo.charAt(i+1) == 'I'){
							if(Codigo.charAt(i+2) == 'T'){
								//ID
								achou = true;
								ignorar= 2;
								Lexico.add("LIT");
								LexicoID.add(Constants.t_LITERAL);
								LexicoCodigo.add("Literal");
								LexicoLinha.add(Linha+"");
							}
						}
					}
				}

				// - - -

				if ( '"' == Codigo.charAt(i) ) {

					achou = true;

					//Para saber se o código finalizou sem as " no final
					boolean FIM = false;

					int Atual = i+1;
					Token = Token + '"';

					LexicoLinha.add(Linha+"");

					while (FIM == false && (Codigo.charAt(Atual) != '"')) {

						if (Letra == '\n') {

							Linha++;
						}

						Letra = Codigo.charAt(Atual);
						Token = Token + Letra;
						Atual++;

						if ( Atual == Tamanho - 1 && Codigo.charAt(Tamanho-1) != '"') {

							FIM = true;
							Error.setText("Linha : "+Linha+" - Erro Encontrado, literal não foi finalizada");

							Letra = Codigo.charAt(Tamanho-1);
							Token = Token + Letra;



						}

					}

					Token = Token + '"';
					ignorar = Atual - i;

					Lexico.add(Token);
					LexicoID.add(Constants.t_LITERAL);
					LexicoCodigo.add("Literal");


					// Literal
				} 

				if (Letra == '+' || Letra == '-' || Letra == '*' || Letra == '/' || Letra == '<' || Letra == '>' || Letra == '=') {

					// AND e NOT estão nas Palavras reservadas ( O código será como um operador ) 

					achou = true;

					if ( Letra == '+') {
						Lexico.add("+");
						LexicoID.add(Constants.t_TOKEN_2);
						LexicoCodigo.add("Operador");
						LexicoLinha.add(Linha+"");
					}

					if ( Letra == '-') {
						Lexico.add("-");
						LexicoID.add(Constants.t_TOKEN_3);
						LexicoCodigo.add("Operador");
						LexicoLinha.add(Linha+"");
					}

					if ( Letra == '*') {
						Lexico.add("*");
						LexicoID.add(Constants.t_TOKEN_4);
						LexicoCodigo.add("Operador");
						LexicoLinha.add(Linha+"");
					}

					if ( Letra == '/') {
						Lexico.add("/");
						LexicoID.add(Constants.t_TOKEN_5);
						LexicoCodigo.add("Operador");
						LexicoLinha.add(Linha+"");
					}

					if ( Letra == '<') {
						if( Codigo.charAt(i+1) == '=') {
							ignorar++;
							Lexico.add("<=");
							LexicoID.add(Constants.t_TOKEN_10);
							LexicoCodigo.add("Operador");
							LexicoLinha.add(Linha+"");
						}else {
							if( Codigo.charAt(i+1) == '>') {
								ignorar++;
								Lexico.add("<>");
								LexicoID.add(Constants.t_TOKEN_11);
								LexicoCodigo.add("Operador");
								LexicoLinha.add(Linha+"");
							}else {
								Lexico.add("<");
								LexicoID.add(Constants.t_TOKEN_9);
								LexicoCodigo.add("Operador");
								LexicoLinha.add(Linha+"");
							}
						}

					}

					if ( Letra == '>') {
						if( Codigo.charAt(i+1) == '=') {
							ignorar++;
							Lexico.add(">=");
							LexicoID.add(Constants.t_TOKEN_8);
							LexicoCodigo.add("Operador");
							LexicoLinha.add(Linha+"");
						}else {
							Lexico.add(">");
							LexicoID.add(Constants.t_TOKEN_8);
							LexicoCodigo.add("Operador");
							LexicoLinha.add(Linha+"");
						}

					}

					if ( Letra == '=') {
						Lexico.add("=");
						LexicoID.add(Constants.t_TOKEN_6);
						LexicoCodigo.add("Operador");
						LexicoLinha.add(Linha+"");
					}

					// Operadores

				}

				if ( Letra == ' ' || Letra == '\n' || Letra == '\t' ) {

					achou = true;

					// Isso serve para não entrar como identificador/número ou outro

					// Não vai fazer nada

					// Delimitadores

				}

				if ( '(' == Codigo.charAt(i) && '*' == Codigo.charAt(i+1)) {

					achou = true;

					int Atual = i+2;
					Token = Token + "(*";
					boolean FIM = false;

					LexicoLinha.add(Linha+"");

					while (FIM == false && (Codigo.charAt(Atual) != '*' || Codigo.charAt(Atual+1) != ')')) {

						if (Letra == '\n') {

							Linha++;
						}

						Letra = Codigo.charAt(Atual);
						Token = Token + Letra;
						Atual++;



						if ( Atual == Tamanho - 2 && (Codigo.charAt(Tamanho-2) != '*' && Codigo.charAt(Tamanho-1) != ')')) {

							if (Atual == Tamanho -2) {
								FIM = true;
							}

							Error.setText("Linha : "+LexicoLinha.get(LexicoLinha.size()-1)+" - Erro Encontrado, comentário não foi finalizado");

							Letra = Codigo.charAt(Tamanho-2);
							Token = Token + Letra;
							Letra = Codigo.charAt(Tamanho-1);
							Token = Token + Letra;



						}

					}            		

					Token = Token + "*)";            		
					ignorar = Atual - i + 1;

					Lexico.add(Token);
					LexicoID.add(Constants.EPSILON);
					LexicoCodigo.add("Comentário");


					// Comentários
				}       	

				if (achou == false) {

					// Identificador + Número Inteiro

					Identificador = Identificador + Letra;

					if(i == Tamanho-1) {
						Lexico.add(Lexico.size(),Identificador);
						LexicoID.add(Constants.t_IDENT);
						LexicoCodigo.add(LexicoCodigo.size(),"Identificador");
						LexicoLinha.add(Linha+"");
						Error.setText("Linha : "+Linha+" - Caso o último token não seja um Identificador,por favor, finalize o token");

					}

				}else {

					if (Identificador.contentEquals("") ) {

					}else {

						int Valor = 0;

						//Verificar se ele adicionou um novo código ou não para adicionar antes
						if(Letra == ' ' || Letra == '\n' || Letra == '\t' || i == Tamanho) {
							Valor = 0;
						}else {
							Valor = 1;
						}

						//Verifica cada letra do Identificador para saber se é inteiro ou não
						boolean VerificaInteiro = true;

						for (int j=0;j<Identificador.length();j++) {
							if (Identificador.charAt(j) >= '0' && Identificador.charAt(j) <= '9') {
							}else {
								VerificaInteiro = false;
							}
						}

						if (VerificaInteiro == true) {
							Lexico.add(Lexico.size()-Valor,Identificador);
							LexicoID.add(LexicoID.size()-Valor,Constants.t_INTEIRO);
							LexicoCodigo.add(LexicoCodigo.size()-Valor,"Numeros Inteiros");
							LexicoLinha.add(Linha+"");
						}else {
							Lexico.add(Lexico.size()-Valor,Identificador);
							LexicoID.add(LexicoID.size()-Valor, Constants.t_IDENT);
							LexicoCodigo.add(LexicoCodigo.size()-Valor,"Identificador");
							LexicoLinha.add(Linha+"");
						}

						Identificador = "";
					}
				}

				if (Letra == '\n') {
					Linha++;
				}

			}else {

				ignorar--;

			}
		}

		//Error.setText("Token : "+Lexico+"\n"+"Codigo : "+LexicoCodigo);

		TabelaToken.setCellValueFactory(
				new PropertyValueFactory<>("token"));
		TabelaTipo.setCellValueFactory(
				new PropertyValueFactory<>("tipo"));
		TabelaLinha.setCellValueFactory(
				new PropertyValueFactory<>("linha"));
		TabelaID.setCellValueFactory(
				new PropertyValueFactory<>("ID"));

		ObservableList<Tabela> Test = FXCollections.observableArrayList();
		for (int i=0;i<Lexico.size();i++) {
			Test.add(new Tabela(Lexico.get(i),LexicoCodigo.get(i),LexicoLinha.get(i),LexicoID.get(i)+""));

		}

		Tabela.setItems(Test);

	}

	// ---
	
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

	
	//
	// Definindo a Tabela Léxica
	//

	public static class Tabela {
		private final SimpleStringProperty token;
		private final SimpleStringProperty tipo;
		private final SimpleStringProperty linha;
		private final SimpleStringProperty ID;

		public Tabela(String token, String tipo, String linha, String id) {
			this.token = new SimpleStringProperty(token);
			this.tipo = new SimpleStringProperty(tipo);
			this.linha = new SimpleStringProperty(linha);
			this.ID = new SimpleStringProperty(id);
		}

		// ---

		public String getToken() {
			return token.get();
		}

		public SimpleStringProperty tokenProperty() {
			return token;
		}

		public void setToken(String token) {
			this.token.set(token);
		}

		// ---

		public String getTipo() {
			return tipo.get();
		}

		public SimpleStringProperty tipoProperty() {
			return tipo;
		}

		public void setTipo(String tipo) {
			this.tipo.set(tipo);
		}

		// ---

		public String getLinha() {
			return linha.get();
		}

		public SimpleStringProperty linhaProperty() {
			return linha;
		}

		public void setLinha(String linha) {
			this.linha.set(linha);
		}

		// ---

		public String getID() {
			return ID.get();
		}

		public SimpleStringProperty IDProperty() {
			return ID;
		}

		public void setID(String id) {
			this.ID.set(id);
		}



	}
}