package tabeladesimbolos;

public class Teste {

    public static void main(String[] args) {
        teste();
    }
    public static void teste() {
        Tabela tabela = new Tabela();
        
        Simbolo simbolo = new Simbolo();
        simbolo.setNome("Token");
        simbolo.setCategoria("INTEIRO");
        simbolo.setGeralA(1);
        simbolo.setGeralB(2);
        simbolo.setNivel(3);

        Simbolo simbolo2 = new Simbolo();
        simbolo2.setNome("Token2");
        simbolo2.setCategoria("VARIAVEL");
        simbolo2.setGeralA(3);
        simbolo2.setGeralB(5);
        simbolo2.setNivel(3);

        Simbolo simbolo3 = new Simbolo();
        simbolo3.setNome("Token3");
        simbolo3.setCategoria("IDENTIFICADOR");
        simbolo3.setGeralA(3);
        simbolo3.setGeralB(6);
        simbolo3.setNivel(1);

        Simbolo simbolo4 = new Simbolo();
        simbolo4.setNome("Token");
        simbolo4.setCategoria("VARIAVEL");
        simbolo4.setGeralA(4);
        simbolo4.setGeralB(2);
        simbolo4.setNivel(1);

        Simbolo simbolo5 = new Simbolo();
        simbolo5.setNome("Token5");
        simbolo5.setCategoria("INTEIRO");
        simbolo5.setGeralA(2);
        simbolo5.setGeralB(1);
        simbolo5.setNivel(4);

        Simbolo simbolo6 = new Simbolo();
        simbolo6.setNome("Token");
        simbolo6.setCategoria("VARIAVEL");
        simbolo6.setGeralA(2);
        simbolo6.setGeralB(3);
        simbolo6.setNivel(1);

        Simbolo simbolo7 = new Simbolo();
        simbolo7.setNome("Token7");
        simbolo7.setCategoria("IDENTIFICADOR");
        simbolo7.setGeralA(3);
        simbolo7.setGeralB(2);
        simbolo7.setNivel(2);

        Simbolo simbolo8 = new Simbolo();
        simbolo8.setNome("Token8");
        simbolo8.setCategoria("IDENTIFICADOR");
        simbolo8.setGeralA(2);
        simbolo8.setGeralB(1);
        simbolo8.setNivel(1);

        Simbolo simbolo9 = new Simbolo();
        simbolo9.setNome("Token9");
        simbolo9.setCategoria("VARIAVEL");
        simbolo9.setGeralA(2);
        simbolo9.setGeralB(2);
        simbolo9.setNivel(1);

        Simbolo simbolo10 = new Simbolo();
        simbolo10.setNome("Token10");
        simbolo10.setCategoria("PROCEDURE");
        simbolo10.setGeralA(1);
        simbolo10.setGeralB(2);
        simbolo10.setNivel(3);

        Simbolo simbolo11 = new Simbolo();
        simbolo11.setNome("Token11");
        simbolo11.setCategoria("PROCEDURE");
        simbolo11.setGeralA(1);
        simbolo11.setGeralB(2);
        simbolo11.setNivel(3);

        tabela.adiciona(simbolo);
        tabela.adiciona(simbolo2);
        tabela.adiciona(simbolo3);
        tabela.adiciona(simbolo4);
        tabela.adiciona(simbolo5);
        tabela.adiciona(simbolo6);
        tabela.adiciona(simbolo7);
        tabela.adiciona(simbolo8);
        tabela.adiciona(simbolo9);
        tabela.adiciona(simbolo10);
        tabela.adiciona(simbolo11);

        tabela.mostraConteudoTabela();

        Simbolo novoSimbolo1 = new Simbolo();
        novoSimbolo1.setNome("Token");
        novoSimbolo1.setCategoria("INTEIRO");
        novoSimbolo1.setGeralA(1);
        novoSimbolo1.setGeralB(2);
        novoSimbolo1.setNivel(3);

        Simbolo novoSimbolo2 = new Simbolo();
        novoSimbolo2.setNome("Token");
        novoSimbolo2.setCategoria("INTEIRO");
        novoSimbolo2.setGeralA(1);
        novoSimbolo2.setGeralB(2);
        novoSimbolo2.setNivel(3);

        Simbolo novoSimbolo3 = new Simbolo();
        novoSimbolo3.setNome("Token");
        novoSimbolo3.setCategoria("INTEIRO");
        novoSimbolo3.setGeralA(1);
        novoSimbolo3.setGeralB(2);
        novoSimbolo3.setNivel(3);

        Simbolo novoSimbolo4 = new Simbolo();
        novoSimbolo4.setNome("Token");
        novoSimbolo4.setCategoria("INTEIRO");
        novoSimbolo4.setGeralA(1);
        novoSimbolo4.setGeralB(2);
        novoSimbolo4.setNivel(3);

        Simbolo novoSimbolo5 = new Simbolo();
        novoSimbolo5.setNome("Token");
        novoSimbolo5.setCategoria("INTEIRO");
        novoSimbolo5.setGeralA(1);
        novoSimbolo5.setGeralB(2);
        novoSimbolo5.setNivel(3);

        System.out.println("Atualizando aqruivos");
        tabela.atualizar(simbolo, novoSimbolo1);
        tabela.atualizar(simbolo2, novoSimbolo2);
        tabela.atualizar(simbolo3, novoSimbolo3);
        tabela.atualizar(simbolo4, novoSimbolo4);
        tabela.atualizar(simbolo5, novoSimbolo5);

        System.out.println("Mostrando Conteudo da tabela");
        tabela.mostraConteudoTabela();

        System.out.println("Removendo tokens");

        tabela.remover(simbolo6);
        tabela.remover(simbolo7);
        tabela.remover(simbolo8);

        System.out.println("Mostrando Conteudo da tabela");
        tabela.mostraConteudoTabela();

        System.out.println("Buscando o conteudo da tabela ");

        System.out.println(tabela.buscar(simbolo9).getNome());
        System.out.println(tabela.buscar(simbolo10).getNome());
        System.out.println(tabela.buscar(simbolo11).getNome());

        //  Fazer uma busca por 1 elemento inexistente na tabela.
        //  Mostrar mensagem informando que o elemento não foi encontrado;
        Simbolo simboloNaoExistente = new Simbolo();
        simboloNaoExistente.setNome("Novo símbo que não existe");
        tabela.buscar(simboloNaoExistente);

    }



}
