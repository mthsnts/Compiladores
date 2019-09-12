package lexico;

import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;
import sintatico.Constants;

public class ConstantesTerminais extends Token {

    private String lexeme = "";

    public static final ConstantesTerminais AND = new ConstantesTerminais("&&", Constants.t_AND, "Sinal AND");
    public static final ConstantesTerminais OR = new ConstantesTerminais("||", Constants.t_OR, "Sinal OR");
    public static final ConstantesTerminais IGUAL = new ConstantesTerminais("=", Constants.t_TOKEN_6, "Sinal de Igualdade");
    public static final ConstantesTerminais DIFERENTE = new ConstantesTerminais("!=", Constants.t_TOKEN_11, "Sinal de Diferenciação");
    public static final ConstantesTerminais MENOR_IGUAL = new ConstantesTerminais("<=", Constants.t_TOKEN_10, "Sinal de comparação menor ou igual");
    public static final ConstantesTerminais MAIOR_IGUAL = new ConstantesTerminais(">=", Constants.t_TOKEN_8, "Sinal de comparação maior ou igual");
    public static final ConstantesTerminais ANOTACAO_TIPO = new ConstantesTerminais(":", Constants.t_TOKEN_13, "Sinal anotação de tipo");

    public static final ConstantesTerminais ATRIBUICAO = new ConstantesTerminais(":=", Constants.t_TOKEN_12, "Sinal atribuição");
    public static final ConstantesTerminais PARENTESES_ABERTO = new ConstantesTerminais("(", Constants.t_TOKEN_17, "Sinal parenteses aberto");
    public static final ConstantesTerminais PARENTESES_FECHADO = new ConstantesTerminais(")", Constants.t_TOKEN_18, "Sinal parenteses fechado");
    public static final ConstantesTerminais VIRGULA = new ConstantesTerminais(",", Constants.t_TOKEN_15, "Sinal vírgula");
    public static final ConstantesTerminais PONTO_VIRGULA = new ConstantesTerminais(";",Constants.t_TOKEN_14, "Sinal ponto vírgula");
    public static final ConstantesTerminais PONTO = new ConstantesTerminais(".", Constants.t_TOKEN_16, "Ponto");

    public static final ConstantesTerminais ASTERISCO = new ConstantesTerminais("*", Constants.t_TOKEN_4, "Sinal multiplicação ou comentário");
    public static final ConstantesTerminais MAIS = new ConstantesTerminais("+", Constants.t_TOKEN_2, "Sinal de soma ou concatenação");
    public static final ConstantesTerminais MENOS = new ConstantesTerminais("-", Constants.t_TOKEN_3, "Sinal de subtração");
    public static final ConstantesTerminais DIVISAO = new ConstantesTerminais("/", Constants.t_TOKEN_5 , "Sinal de divisão");
    public static final ConstantesTerminais MAIOR = new ConstantesTerminais(">", Constants.t_TOKEN_7, "Sinal de comparação maior");
    public static final ConstantesTerminais MENOR = new ConstantesTerminais("<", Constants.t_TOKEN_9, "Sinal de comparação menor");
    public static final ConstantesTerminais FIM_ARQUIVO = new ConstantesTerminais("$", Constants.DOLLAR, "Sinal fim de arquivo");


    public ConstantesTerminais(String s, int tag, String descricao) {
        super(tag);
        lexeme = s;
        this.descricao = descricao;
    }

    public ConstantesTerminais(String s, int tag) {
        super(tag);
        lexeme = s;
    }

    @Override
    public String toString() {
        return "" + lexeme;
    }

    String getLexeme() {
        return toString();
    }
}
