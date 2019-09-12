package lexico;

import sintatico.Constants;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;


public class Lexico {
    public static int line = 1;

    private static String PALAVRA_RESERVADA = "Palavra Reservada";
    private char ch = ' ';
    private FileReader file = null;

    private Hashtable words = new Hashtable();

    private void reserve(ConstantesTerminais w) {
        words.put(w.getLexeme(), w);
    }

    public Lexico(String fileName) throws FileNotFoundException {

        try {
            file = new FileReader(fileName);
        } catch (FileNotFoundException e) {
            System.out.println("Arquivo não encontrado");
            throw e;
        }

        // Palavras reservadas
        reserve(new ConstantesTerminais("PROGRAM", Constants.t_PROGRAM, PALAVRA_RESERVADA));
        reserve(new ConstantesTerminais("VAR", Constants.t_VAR, PALAVRA_RESERVADA));
        reserve(new ConstantesTerminais("PROCEDURE", Constants.t_PROCEDURE, PALAVRA_RESERVADA));
        reserve(new ConstantesTerminais("END", Constants.t_END, PALAVRA_RESERVADA));
        reserve(new ConstantesTerminais("AND", Constants.t_AND, PALAVRA_RESERVADA));
        reserve(new ConstantesTerminais("OR", Constants.t_OR, PALAVRA_RESERVADA));
        reserve(new ConstantesTerminais("BEGIN", Constants.t_BEGIN, PALAVRA_RESERVADA));
        reserve(new ConstantesTerminais("INTEGER", Constants.t_INTEGER, PALAVRA_RESERVADA));
        reserve(new ConstantesTerminais("LIT", Constants.t_LITERAL, PALAVRA_RESERVADA));
        reserve(new ConstantesTerminais("IF", Constants.t_IF, PALAVRA_RESERVADA));
        reserve(new ConstantesTerminais("THEN", Constants.t_THEN, PALAVRA_RESERVADA));
        reserve(new ConstantesTerminais("ELSE", Constants.t_ELSE, PALAVRA_RESERVADA));
        reserve(new ConstantesTerminais("DO", Constants.t_DO, PALAVRA_RESERVADA));
        reserve(new ConstantesTerminais("CALL", Constants.t_CALL, PALAVRA_RESERVADA));
        reserve(new ConstantesTerminais("WHILE", Constants.t_WHILE, PALAVRA_RESERVADA));
        reserve(new ConstantesTerminais("READLN", Constants.t_READLN, PALAVRA_RESERVADA));
        reserve(new ConstantesTerminais("WRITELN", Constants.t_WRITELN, PALAVRA_RESERVADA));
        reserve(new ConstantesTerminais("NOT", Constants.t_NOT, PALAVRA_RESERVADA));
        reserve(new ConstantesTerminais("AND", Constants.t_AND, PALAVRA_RESERVADA));
        reserve(new ConstantesTerminais("REPEAT", Constants.t_REPEAT, PALAVRA_RESERVADA));
        reserve(new ConstantesTerminais("UNTIL", Constants.t_UNTIL, PALAVRA_RESERVADA));
        reserve(new ConstantesTerminais("FOR", Constants.t_FOR, PALAVRA_RESERVADA));
        reserve(new ConstantesTerminais("TO", Constants.t_TO, PALAVRA_RESERVADA));
        reserve(new ConstantesTerminais("CASE", Constants.t_CASE, PALAVRA_RESERVADA));
        reserve(new ConstantesTerminais("OF", Constants.t_OF, PALAVRA_RESERVADA));
        reserve(new ConstantesTerminais("CONST", Constants.t_CONST, PALAVRA_RESERVADA));
    }

    private void readch() throws IOException {
        ch = (char) file.read();
    }

    private boolean readch(char c) throws IOException {
        readch();
        if (ch != c) return false;
        ch = ' ';
        return true;
    }

    public List<Token> scanAll() throws IOException {
        List <Token> tokens = new ArrayList<>();
        StringBuilder texto = new StringBuilder();
        System.out.println("|---Código---|---------Token---------|------------Descrição------------|");

        int linhaAtual = 1;
        do {
            Token tok = scan();
            System.out.println("Código: " + tok.tag + "\t\t\tToken:" + tok.toString() + (tok.descricao != null ? "\t\t\t\tDescrição: " + tok.descricao : ""));

            if (linhaAtual != line) {
                texto.append("\n");
                linhaAtual = line;
            }

            tokens.add(tok);

            if (tok.tag == Codigo.FIM_ARQUIVO.value || tok.tag == Codigo.CARACTERE_INVALIDO.value) { break;
            }

        } while (true);

        return tokens;
    }

    private Token erroUnexEOF(String tipo) {
        JOptionPane.showMessageDialog(null, new ConstantesTerminais("Final de arquivo inesperado (" + tipo + ") - Linha: " + line, Codigo.FIM_ARQUIVO_INESPERADO.value));
        return new ConstantesTerminais("Final de arquivo inesperado (" + tipo + ") - Linha: " + line, Codigo.FIM_ARQUIVO_INESPERADO.value);
    }

    private Token erroLexico() {
        JOptionPane.showMessageDialog(null, new ConstantesTerminais("Caracter inválido - Linha: " + line, Codigo.CARACTERE_INVALIDO.value));
        return new ConstantesTerminais("Caracter inválido - Linha: " + line, Codigo.CARACTERE_INVALIDO.value);
    }

    /*
     * Justificativa: Realizada modificações para lançar erro ao criar variaveis com numeros na frente de letra
     * Exemplo: VAR 1111x, 1x;
     *
     * Justificativa: Corrigida identificação de sinais, como <> (erro), << (erro), <= (aceito), => (erro)
     *
     * Justificativa: Na criação dos tokens, é gerada a descrição (Palavra reservada, Identificador, Sinal de Atribuição....)
     */
    public Token scan() throws IOException {

        for (; ; readch()) {
            if (ch == ' ' || ch == '\t' || ch == '\r' || ch == '\b') continue;
            else if (ch == '\n') line++;
            else break;
        }
        if (!Character.isLetter(ch) && !Character.isDigit(ch)) {
            switch (ch) {
                // Operadores
                case '|':
                    if (readch('|')) return ConstantesTerminais.OR;
                    else return erroLexico();

                case '.':
                    if (readch('.')) return ConstantesTerminais.PONTO;
                    else return erroLexico();
                case ':':
                    if (readch('='))
                        return ConstantesTerminais.ATRIBUICAO;
                    else
                        return ConstantesTerminais.ANOTACAO_TIPO;
                case '!':
                    if (readch('='))
                        return ConstantesTerminais.DIFERENTE;
                    else
                        return erroLexico();
                case '=':
                    readch();
                    if (!Character.isLetter(ch) && !Character.isDigit(ch))
                        return erroLexico();
                    else
                        return ConstantesTerminais.IGUAL;
                case '<':
                    readch();
                    if (ch == '=') {
                        readch();
                        return ConstantesTerminais.MENOR_IGUAL;
                    } else if (ch == ' ')
                        return ConstantesTerminais.MENOR;
                    else
                        return erroLexico();
                case '>':
                    readch();
                    if (ch == '=') {
                        readch();
                        return ConstantesTerminais.MAIOR_IGUAL;
                    } else if (ch == ' ')
                        return ConstantesTerminais.MAIOR;
                    else
                        return erroLexico();
                case '(':
                    readch();
                    return ConstantesTerminais.PARENTESES_ABERTO;
                case ')':
                    readch();
                    return ConstantesTerminais.PARENTESES_FECHADO;
                case ',':
                    readch();
                    return ConstantesTerminais.VIRGULA;
                case '$':
                    readch();
                    return ConstantesTerminais.FIM_ARQUIVO;
                case ';':
                    readch();
                    return ConstantesTerminais.PONTO_VIRGULA;
                case '*':
                    readch();
                    int done = 0;
                    while (done == 0) {
                        readch();
                        if (ch == '*') {
                            done = 1;
                        }
                        if (ch == Codigo.FIM_ARQUIVO.value) {
                            return erroUnexEOF("Comentário");
                        }
                    }
                    readch();
                    return new ConstantesTerminais("Comentário", Codigo.COMENTARIO.value);
                case '+':
                    readch();
                    return ConstantesTerminais.MAIS;
                case '-':
                    readch();
                    return ConstantesTerminais.MENOS;
                case '‘': {
                    String str = "";
                    str = str + ch;
                    readch();
                    while (ch != '‘') {
                        str = str + ch;
                        readch();
                        if (ch == Codigo.FIM_ARQUIVO.value) {
                            return erroUnexEOF("Aspas");
                        }
                        if (ch == '\n') {
                            return erroLexico();
                        }

                    }
                    str = str + ch;
                    readch();
                    return new ConstantesTerminais(str, Constants.t_LITERAL);
                }
                case '/': {
                    readch();
                    if (ch == '/') {
                        readch();
                        while (ch != '\n')
                            readch();
                        return new ConstantesTerminais("Comentário", Codigo.COMENTARIO.value);
                    } else return ConstantesTerminais.DIVISAO;
                }
                default:
                    return erroLexico();
            }
        }

        // Números
        if (Character.isDigit(ch)) {
            int value = 0;
            do {
                value = 10 * value + Character.digit(ch, 10);
                readch();
            } while (Character.isDigit(ch));
            if (Character.isLetter(ch))
                return erroLexico();
            return new Numero(value);
        }

        // Identificadores
        if (Character.isLetter(ch)) {
            StringBuffer sb = new StringBuffer();
            do {
                sb.append(ch);
                readch();
            } while (Character.isLetterOrDigit(ch));

            String s = sb.toString();
            ConstantesTerminais w = (ConstantesTerminais) words.get(s.toUpperCase());
            if (w != null) return w; // ja existe na hashtable ou é uma palabra reservada
            w = new ConstantesTerminais(s, Constants.t_IDENT, "Identificador");
            words.put(s, w);
            return w;
        }

        // Caracteres não identificados
        if (ch != Codigo.FIM_ARQUIVO.value) {
            readch();
            return erroLexico();
        }

        Token t = new Token(ch);
        ch = ' ';
        return t;
    }
}
