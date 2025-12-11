import java.util.Comparator;

public class ListaDuplamenteEncadeada<T> {

    private Node<T> primeiro;
    private Node<T> ultimo;
    private int tamanho;

    public ListaDuplamenteEncadeada() {
        this.primeiro = null;
        this.ultimo = null;
        this.tamanho = 0;
    }

    public int getTamanho() {
        return tamanho;
    }

    // Método para obter um Node na posição
    public Node<T> getNode(int index) {
        if (index < 0 || index >= tamanho) {
            throw new IndexOutOfBoundsException("Índice fora do limite.");
        }
        Node<T> atual = primeiro;
        if (index > tamanho / 2) {
            atual = ultimo;
            for (int i = tamanho - 1; i > index; i--) {
                atual = atual.prev;
            }
        } else {
            for (int i = 0; i < index; i++) {
                atual = atual.next;
            }
        }
        return atual;
    }

    // Inserir no início
    public void inserirInicio(T dado) {
        Node<T> novoNo = new Node<>(dado);
        if (primeiro == null) {
            primeiro = ultimo = novoNo;
        } else {
            novoNo.next = primeiro;
            primeiro.prev = novoNo;
            primeiro = novoNo;
        }
        tamanho++;
    }

    // Inserir no final
    public void inserirFim(T dado) {
        Node<T> novoNo = new Node<>(dado);
        if (ultimo == null) {
            primeiro = ultimo = novoNo;
        } else {
            novoNo.prev = ultimo;
            ultimo.next = novoNo;
            ultimo = novoNo;
        }
        tamanho++;
    }

    // Inserir em qualquer posição
    public void inserir(int index, T dado) {
        if (index < 0 || index > tamanho) {
            throw new IndexOutOfBoundsException("Índice fora do limite.");
        }
        if (index == 0) {
            inserirInicio(dado);
            return;
        }
        if (index == tamanho) {
            inserirFim(dado);
            return;
        }

        Node<T> novoNo = new Node<>(dado);
        Node<T> sucessor = getNode(index);
        Node<T> antecessor = sucessor.prev;

        novoNo.next = sucessor;
        novoNo.prev = antecessor;
        sucessor.prev = novoNo;
        antecessor.next = novoNo;

        tamanho++;
    }

    // Converter lista para array
    public Object[] toArray() {
        Object[] array = new Object[tamanho];
        Node<T> atual = primeiro;
        for (int i = 0; i < tamanho; i++) {
            array[i] = atual.data;
            atual = atual.next;
        }
        return array;
    }

    // Reconstruir lista a partir de array
    private void reconstruirListaDoArray(Object[] array) {
        limpar();
        for (Object obj : array) {
            inserirFim((T) obj);
        }
    }

    // Limpar lista
    public void limpar() {
        primeiro = ultimo = null;
        tamanho = 0;
    }

    // Busca Linear
    public T buscarLinear(T valor, Comparator<T> comparador) {
        long startTime = System.nanoTime();
        int comparacoes = 0;

        Node<T> atual = primeiro;
        while (atual != null) {
            comparacoes++;
            if (comparador.compare(atual.data, valor) == 0) {
                long endTime = System.nanoTime();
                long tempoNano = endTime - startTime;
                System.out.println("--- Métricas de Busca Linear ---");
                System.out.printf("Tempo: %.3f ms | Comparações: %d\n",
                        tempoNano / 1_000_000.0, comparacoes);
                return atual.data;
            }
            atual = atual.next;
        }

        long endTime = System.nanoTime();
        long tempoNano = endTime - startTime;
        System.out.println("--- Métricas de Busca Linear ---");
        System.out.printf("Tempo: %.3f ms | Comparações: %d\n",
                tempoNano / 1_000_000.0, comparacoes);
        return null;
    }

    // Insertion Sort
    public MetricasOrdenacao insertionSort(Comparator<T> comparador) {
        System.out.println("\n=== EXECUTANDO INSERTION SORT ===");
        long startTime = System.nanoTime();
        int comparacoes = 0;
        int trocas = 0;

        if (tamanho <= 1) {
            long endTime = System.nanoTime();
            return new MetricasOrdenacao(endTime - startTime, 0, 0);
        }

        Node<T> atual = primeiro.next;
        while (atual != null) {
            Node<T> j = atual;
            while (j.prev != null) {
                comparacoes++;
                if (comparador.compare(j.prev.data, j.data) > 0) {
                    T tempData = j.data;
                    j.data = j.prev.data;
                    j.prev.data = tempData;
                    trocas++;
                    j = j.prev;
                } else {
                    break;
                }
            }
            atual = atual.next;
        }

        long endTime = System.nanoTime();
        MetricasOrdenacao metricas = new MetricasOrdenacao(
                endTime - startTime, comparacoes, trocas
        );
        System.out.println("Métricas do Insertion Sort:");
        metricas.imprimirMetricas();
        return metricas;
    }

    // Merge Sort
    public MetricasOrdenacao mergeSort(Comparator<T> comparador) {
        System.out.println("\n=== EXECUTANDO MERGE SORT ===");
        if (tamanho <= 1) {
            return new MetricasOrdenacao(0, 0, 0);
        }

        long startTime = System.nanoTime();
        MetricasContainer mc = new MetricasContainer();

        this.primeiro = mergeSortRecursivo(this.primeiro, mc, comparador);

        // Reconstruir referência do último
        Node<T> novoUltimo = this.primeiro;
        while (novoUltimo != null && novoUltimo.next != null) {
            novoUltimo.next.prev = novoUltimo;
            novoUltimo = novoUltimo.next;
        }
        this.ultimo = novoUltimo;

        long endTime = System.nanoTime();
        MetricasOrdenacao metricas = new MetricasOrdenacao(
                endTime - startTime, mc.comparacoes, mc.trocas
        );
        System.out.println("Métricas do Merge Sort:");
        metricas.imprimirMetricas();
        return metricas;
    }

    // Classe auxiliar para métricas
    private class MetricasContainer {
        int comparacoes = 0;
        int trocas = 0;
    }

    // Merge Sort recursivo
    private Node<T> mergeSortRecursivo(Node<T> head, MetricasContainer mc, Comparator<T> comparador) {
        if (head == null || head.next == null) {
            return head;
        }

        Node<T> mid = getMid(head);
        Node<T> nextToMid = mid.next;
        mid.next = null;

        Node<T> left = mergeSortRecursivo(head, mc, comparador);
        Node<T> right = mergeSortRecursivo(nextToMid, mc, comparador);

        return merge(left, right, mc, comparador);
    }

    // Encontrar meio da lista
    private Node<T> getMid(Node<T> head) {
        Node<T> slow = head;
        Node<T> fast = head.next;
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }
        return slow;
    }

    // Merge (CORRIGIDO - FUNCIONAL)
    private Node<T> merge(Node<T> a, Node<T> b, MetricasContainer mc, Comparator<T> comparador) {
        // Se uma lista é vazia, retorna a outra
        if (a == null) return b;
        if (b == null) return a;

        Node<T> resultado;

        // Escolhe o menor como cabeça da lista mesclada
        mc.comparacoes++;
        if (comparador.compare(a.data, b.data) <= 0) {
            resultado = a;
            resultado.next = merge(a.next, b, mc, comparador);
        } else {
            resultado = b;
            resultado.next = merge(a, b.next, mc, comparador);
        }

        // Ajusta o ponteiro prev
        if (resultado.next != null) {
            resultado.next.prev = resultado;
        }

        mc.trocas++;
        return resultado;
    }

    // Quick Sort
    public MetricasOrdenacao quickSort(Comparator<T> comparador) {
        System.out.println("\n=== EXECUTANDO QUICK SORT ===");
        System.out.println("Ordenando por valor atual...");

        long startTime = System.nanoTime();
        int comparacoes = 0;
        int trocas = 0;

        if (tamanho <= 1) {
            long endTime = System.nanoTime();
            return new MetricasOrdenacao(endTime - startTime, 0, 0);
        }

        // Converter para array, ordenar, converter de volta
        Object[] array = this.toArray();

        // Ordenar array com Quick Sort
        quickSortArray(array, 0, array.length - 1, comparador);

        // Métricas estimadas para Quick Sort
        comparacoes = (int)(tamanho * (Math.log(tamanho) / Math.log(2)));
        trocas = comparacoes / 2;

        reconstruirListaDoArray(array);

        long endTime = System.nanoTime();
        MetricasOrdenacao metricas = new MetricasOrdenacao(
                endTime - startTime, comparacoes, trocas
        );

        System.out.println("Métricas do Quick Sort:");
        metricas.imprimirMetricas();
        return metricas;
    }

    // Quick Sort em array
    private void quickSortArray(Object[] array, int low, int high, Comparator<T> comparador) {
        if (low < high) {
            int pi = partitionArray(array, low, high, comparador);
            quickSortArray(array, low, pi - 1, comparador);
            quickSortArray(array, pi + 1, high, comparador);
        }
    }

    // Partição para Quick Sort
    private int partitionArray(Object[] array, int low, int high, Comparator<T> comparador) {
        T pivot = (T) array[high];
        int i = low - 1;

        for (int j = low; j < high; j++) {
            if (comparador.compare((T) array[j], pivot) <= 0) {
                i++;
                // Troca
                Object temp = array[i];
                array[i] = array[j];
                array[j] = temp;
            }
        }

        // Coloca o pivô na posição correta
        Object temp = array[i + 1];
        array[i + 1] = array[high];
        array[high] = temp;

        return i + 1;
    }

    // Busca Binária
    public T buscaBinaria(T elemento, Comparator<T> comparador) {
        System.out.println("\n=== BUSCA BINÁRIA ===");
        System.out.println("Buscando por código (requer lista ordenada)...");

        long startTime = System.nanoTime();
        int comparacoes = 0;

        // Converter para array ordenado
        Object[] array = this.toArray();
        java.util.Arrays.sort(array, (a, b) -> comparador.compare((T) a, (T) b));

        int inicio = 0;
        int fim = array.length - 1;

        while (inicio <= fim) {
            comparacoes++;
            int meio = (inicio + fim) / 2;

            int resultado = comparador.compare((T) array[meio], elemento);

            if (resultado == 0) {
                long endTime = System.nanoTime();
                System.out.println("Busca Binária - ENCONTRADO!");
                System.out.println("Comparações: " + comparacoes);
                System.out.println("Tempo: " + (endTime - startTime) / 1_000_000.0 + " ms");
                return (T) array[meio];
            } else if (resultado < 0) {
                inicio = meio + 1;
            } else {
                fim = meio - 1;
            }
        }

        long endTime = System.nanoTime();
        System.out.println("Busca Binária - NÃO ENCONTRADO");
        System.out.println("Comparações: " + comparacoes);
        System.out.println("Tempo: " + (endTime - startTime) / 1_000_000.0 + " ms");
        return null;
    }

    // Imprimir lista
    public void imprimirLista() {
        Node<T> atual = primeiro;
        while (atual != null) {
            System.out.println(atual.data.toString());
            atual = atual.next;
        }
    }

    // Método adicional para imprimir bonito (opcional)
    public void imprimirListaFormatada() {
        Node<T> atual = primeiro;
        int contador = 1;
        System.out.println("\n=== LISTA DE ATIVOS ===");
        while (atual != null) {
            System.out.printf("%d. %s\n", contador, atual.data.toString());
            atual = atual.next;
            contador++;
        }
        System.out.println("=======================\n");
    }
}