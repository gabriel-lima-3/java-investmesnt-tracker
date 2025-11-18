public class ListaDuplamenteEncadeada<T> {
    private Node<T> primeiro;
    private Node<T> ultimo;
    private int tamanho;


    public ListaDuplamenteEncadeada() {
        this.primeiro = null;
        this.ultimo = null;
        this.tamanho = 0;
    }

    // 1. Inserir no início
    public void inserirInicio(T dado) {
        Node<T> novoNo = new Node<>(dado);
        if (primeiro == null) {
            primeiro = ultimo = novoNo;
        } else {
            novoNo.next= primeiro;
            primeiro.prev = novoNo;
            primeiro = novoNo;
        }
        tamanho++;
    }

    // 2. Inserir no final
    public void inserirFim(T dado) {
        Node<T> novoNo = new Node<>(dado);
        if (ultimo == null) {
            primeiro = ultimo = novoNo;
        } else {
            ultimo.next = novoNo;
            novoNo.prev = ultimo;
            ultimo = novoNo;
        }
        tamanho++;
    }

    // 3. Remover do início
    public T removerInicio() {
        if (primeiro == null) return null;

        T dadoRemovido = primeiro.data;
        if (primeiro == ultimo) {
            primeiro = ultimo = null;
        } else {
            primeiro = primeiro.next;
            primeiro.prev = null;
        }
        tamanho--;
        return dadoRemovido;
    }

    // 4. Remover do final
    public T removerFim() {
        if (ultimo == null) return null;

        T dadoRemovido = ultimo.data;
        if (primeiro == ultimo) {
            primeiro = ultimo = null;
        } else {
            ultimo = ultimo.prev;
            ultimo.next = null;
        }
        tamanho--;
        return dadoRemovido;
    }

    // 5. Buscar por posição
    public T buscarPorPosicao(int posicao) {
        if (posicao < 0 || posicao >= tamanho) return null;

        Node<T> atual = primeiro;
        for (int i = 0; i < posicao; i++) {
            atual = atual.next;
        }
        return atual.data;
    }

    // 6. Buscar linear (precisa de Comparator)
    public T buscarLinear(T elemento, java.util.Comparator<T> comparador) {
        Node<T> atual = primeiro;
        while (atual != null) {
            if (comparador.compare(atual.data, elemento) == 0) {
                return atual.data;
            }
            atual = atual.next;
        }
        return null;
    }

    // 7. Verificar se está vazia
    public boolean estaVazia() {
        return tamanho == 0;
    }

    // 8. Get tamanho
    public int getTamanho() {
        return tamanho;
    }

    // 9. Imprimir lista (para debug)
    public void imprimirLista() {
        Node<T> atual = primeiro;
        System.out.print("Lista: ");
        while (atual != null) {
            System.out.print(atual.data+ " <-> ");
            atual = atual.next;
        }
        System.out.println("null");
    }

    // 10. Imprimir reverso (para debug)
    public void imprimirReverso() {
        Node<T> atual = ultimo;
        System.out.print("Lista Reversa: ");
        while (atual != null) {
            System.out.print(atual.data + " <-> ");
            atual = atual.prev;
        }
        System.out.println("null");
    }

    // 11. Converter para array (útil para ordenação)
    public Object[] toArray() {
        Object[] array = new Object[tamanho];
        Node<T> atual = primeiro;
        for (int i = 0; i < tamanho; i++) {
            array[i] = atual.data;
            atual = atual.next;
        }
        return array;
    }

    // 12. Limpar lista
    public void limpar() {
        primeiro = ultimo = null;
        tamanho = 0;
    }
}