package tabeladesimbolos;

public class Tabela {

    private final int tableSize = 25147; //first prime after 25143
    int index, collisions;
    Simbolo[] hashtable = new Simbolo[tableSize];

    public int hash(String key, int tableSize) {
        int hashVal = 0; //uses Horner’s method to evaluate a polynomial
        for (int i = 0; i < key.length(); i++)
            hashVal = 37 * hashVal + key.charAt(i);
        hashVal %= tableSize;
        if (hashVal < 0)
            hashVal += tableSize; //needed if hashVal is negative
        return hashVal;
    }

    public void adiciona(Simbolo simbolo) {
        index = this.hash(simbolo.getNome(), tableSize);
        if (isNullIndex(index))
            this.hashtable[index] = simbolo;
        else {
            Simbolo auxSimbolo = this.hashtable[index];
            while (this.hasProximo(auxSimbolo)) {
                auxSimbolo = auxSimbolo.getProximo();
            }
            auxSimbolo.setProximo(simbolo);
        }
    }

    public void remove(String simbolo) {
        index = this.hash(simbolo, tableSize);
        Simbolo simboloAtual = this.hashtable[index];

        if (simboloAtual.getNome().equals(simbolo)) {
            this.hashtable[index] = simboloAtual.getProximo();
        } else {

        }
    }

    public boolean remover(Simbolo simbolo) {
        int indexSimbolo = this.hash(simbolo.getNome(), this.tableSize);

        Simbolo atual = hashtable[indexSimbolo];
        Simbolo proximo = atual.getProximo();

        if (proximo == null && atual.getNome().equals(simbolo.getNome())) {
            hashtable[indexSimbolo] = null;
            return true;

        } else if (proximo == null && !atual.getNome().equals(simbolo.getNome())) {
            return false;

        } else {
            do {

                if (proximo != null && proximo.getNome().equals(simbolo.getNome())) {
                    atual.setProximo(proximo.getProximo());
                    return true;
                }
            } while (hasProximo(proximo));

            return false;
        }
    }

    public Simbolo buscar(Simbolo simbolo) {
        int indexSimbolo = this.hash(simbolo.getNome(), this.tableSize);

        Simbolo busca = hashtable[indexSimbolo];

        if (busca.getNome().equals(simbolo.getNome())) {
            return busca;
        } else {
            do {
                busca = busca.getProximo();
            } while (hasProximo(busca));

            return busca;
        }
    }

    public void atualizar(Simbolo simbolo, Simbolo novoSimbolo) {
        int index = this.hash(simbolo.getNome(), tableSize);
        if (hashtable[index] != null) {
            Simbolo atual = hashtable[index];
            if (atual == simbolo && !hasProximo(atual)) {
                hashtable[index] = novoSimbolo;
            } else if (atual == simbolo) {
                novoSimbolo.setProximo(atual.getProximo());
                hashtable[index] = novoSimbolo;
            } else {
                Simbolo anterior = atual;
                atual = atual.getProximo();
                while (atual != simbolo) {
                    anterior = atual;
                    atual = atual.getProximo();
                }
                if (atual != null) {
                    anterior.setProximo(novoSimbolo);
                    novoSimbolo.setProximo(atual.getProximo());
                }
            }
        }
    }


    public boolean isNullIndex(int index) {
        return this.hashtable[index] == null;
    }

    public boolean hasProximo(Simbolo simbolo) {
        return simbolo.getProximo() != null;
    }
}
