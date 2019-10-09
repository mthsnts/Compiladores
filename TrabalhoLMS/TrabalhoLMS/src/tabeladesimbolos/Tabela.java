package tabeladesimbolos;

public class Tabela {

    private final int tableSize = 25147; //first prime after 25143
    int index,collisions;
    Simbolo[] hashtable = new Simbolo[tableSize];

    public int hash(String key, int tableSize){
        int hashVal = 0; //uses Horner’s method to evaluate a polynomial
        for( int i = 0; i < key.length( ); i++ )
            hashVal = 37 * hashVal + key.charAt( i );
        hashVal %= tableSize;
        if( hashVal < 0 )
            hashVal += tableSize; //needed if hashVal is negative
        return hashVal;
    }

    public void adiciona(Simbolo simbolo){
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

    public void remove(String simbolo){
        index = this.hash(simbolo, tableSize);
        Simbolo simboloAtual = this.hashtable[index];

        if (simboloAtual.getNome().equals(simbolo)){
            this.hashtable[index] = simboloAtual.getProximo();
        }else {

        }
    }


    public boolean isNullIndex(int index){
        return this.hashtable[index] == null;
    }

    public boolean hasProximo(Simbolo simbolo){
        return simbolo.getProximo() != null;
    }
}
