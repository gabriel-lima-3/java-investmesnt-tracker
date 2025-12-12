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

    // Método otimizado para obter um Node na posição
    public Node<T> getNode(int index) {
        if (index < 0 || index >= tamanho) {
            throw new IndexOutOfBoundsException("Índice fora do limite.");
        }
        Node<T> atual = primeiro;
        // Otimização: busca do final se o índice for maior que a metade
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

    // Limpar lista
    public void limpar() {
        primeiro = ultimo = null;
        tamanho = 0;
    }

    // Método auxiliar para obter o dado em um índice (útil para a Main)
    public T get(int index) {
        return getNode(index).data;
    }

    // --- ALGORITMOS DE BUSCA ---

    public T buscarLinear(T valor, Comparator<T> comparador) {
        long startTime = System.nanoTime();
        int comparacoes = 0;

        Node<T> atual = primeiro;
        while (atual != null) {
            comparacoes++;
            if (comparador.compare(atual.data, valor) == 0) {
                long endTime = System.nanoTime();
                exibirMetricas("Busca Linear", endTime - startTime, comparacoes, 0);
                return atual.data;
            }
            atual = atual.next;
        }

        long endTime = System.nanoTime();
        exibirMetricas("Busca Linear", endTime - startTime, comparacoes, 0);
        return null;
    }

    public T buscaBinaria(T elemento, Comparator<T> comparador) {
        System.out.println("\n=== BUSCA BINÁRIA (NA LISTA) ===");
        System.out.println("Nota: A lista deve estar ordenada pelo critério de busca.");
        
        long startTime = System.nanoTime();
        int comparacoes = 0;

        int inicio = 0;
        int fim = tamanho - 1;

        while (inicio <= fim) {
            comparacoes++;
            int meio = (inicio + fim) / 2;
            
            // Navega até o nó do meio (sem usar array)
            Node<T> nodeMeio = getNode(meio);
            
            int resultado = comparador.compare(nodeMeio.data, elemento);

            if (resultado == 0) {
                long endTime = System.nanoTime();
                exibirMetricas("Busca Binária", endTime - startTime, comparacoes, 0);
                return nodeMeio.data;
            } else if (resultado < 0) {
                // Se elemento do meio é menor que o buscado, busca na direita
                inicio = meio + 1;
            } else {
                // Se elemento do meio é maior, busca na esquerda
                fim = meio - 1;
            }
        }

        long endTime = System.nanoTime();
        exibirMetricas("Busca Binária", endTime - startTime, comparacoes, 0);
        return null;
    }

    // --- ALGORITMOS DE ORDENAÇÃO ---

    // Insertion Sort
    public MetricasOrdenacao insertionSort(Comparator<T> comparador) {
        long startTime = System.nanoTime();
        int comparacoes = 0;
        int trocas = 0;

        if (tamanho <= 1) return new MetricasOrdenacao(0,0,0);

        Node<T> atual = primeiro.next;
        while (atual != null) {
            Node<T> j = atual;
            while (j.prev != null) {
                comparacoes++;
                if (comparador.compare(j.prev.data, j.data) > 0) {
                    // Troca os dados
                    T temp = j.data;
                    j.data = j.prev.data;
                    j.prev.data = temp;
                    trocas++;
                    j = j.prev;
                } else {
                    break;
                }
            }
            atual = atual.next;
        }

        long endTime = System.nanoTime();
        MetricasOrdenacao m = new MetricasOrdenacao(endTime - startTime, comparacoes, trocas);
        return m;
    }

    // Merge Sort
    public MetricasOrdenacao mergeSort(Comparator<T> comparador) {
        long startTime = System.nanoTime();
        MetricasContainer mc = new MetricasContainer();

        if (primeiro != null && primeiro.next != null) {
            this.primeiro = mergeSortRecursivo(this.primeiro, mc, comparador);
            // Corrige referência do último após ordenação
            Node<T> temp = primeiro;
            while (temp.next != null) temp = temp.next;
            this.ultimo = temp;
        }

        long endTime = System.nanoTime();
        return new MetricasOrdenacao(endTime - startTime, mc.comparacoes, mc.trocas);
    }

    private Node<T> mergeSortRecursivo(Node<T> head, MetricasContainer mc, Comparator<T> comparador) {
        if (head == null || head.next == null) return head;

        Node<T> mid = getMid(head);
        Node<T> nextToMid = mid.next;
        mid.next = null; // Quebra a lista em duas
        if (nextToMid != null) nextToMid.prev = null;

        Node<T> left = mergeSortRecursivo(head, mc, comparador);
        Node<T> right = mergeSortRecursivo(nextToMid, mc, comparador);

        return merge(left, right, mc, comparador);
    }

    private Node<T> getMid(Node<T> head) {
        if (head == null) return null;
        Node<T> slow = head, fast = head;
        while (fast.next != null && fast.next.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }
        return slow;
    }

    private Node<T> merge(Node<T> a, Node<T> b, MetricasContainer mc, Comparator<T> comparador) {
        if (a == null) return b;
        if (b == null) return a;

        Node<T> result;
        mc.comparacoes++;
        if (comparador.compare(a.data, b.data) <= 0) {
            result = a;
            result.next = merge(a.next, b, mc, comparador);
            if (result.next != null) result.next.prev = result;
        } else {
            result = b;
            result.next = merge(a, b.next, mc, comparador);
            if (result.next != null) result.next.prev = result;
        }
        return result;
    }

    // Quick Sort (Sem Array - Partition com troca de dados)
    public MetricasOrdenacao quickSort(Comparator<T> comparador) {
        long startTime = System.nanoTime();
        MetricasContainer mc = new MetricasContainer();
        
        // Passa o último nó como referência inicial do fim
        quickSortRecursivo(this.primeiro, this.ultimo, comparador, mc);
        
        long endTime = System.nanoTime();
        return new MetricasOrdenacao(endTime - startTime, mc.comparacoes, mc.trocas);
    }

    private void quickSortRecursivo(Node<T> inicio, Node<T> fim, Comparator<T> comparador, MetricasContainer mc) {
        if (inicio != null && fim != null && inicio != fim && inicio != fim.next) {
            // Partition retorna o nó pivô já na posição correta
            Node<T> pivo = partition(inicio, fim, comparador, mc);
            
            // Ordena antes do pivô
            if (pivo.prev != null)
                quickSortRecursivo(inicio, pivo.prev, comparador, mc);
            
            // Ordena depois do pivô
            if (pivo.next != null)
                quickSortRecursivo(pivo.next, fim, comparador, mc);
        }
    }

    private Node<T> partition(Node<T> inicio, Node<T> fim, Comparator<T> comparador, MetricasContainer mc) {
        T pivoValor = fim.data;
        Node<T> i = inicio.prev; // Índice do menor elemento

        // Percorre de inicio até fim.prev
        for (Node<T> j = inicio; j != fim; j = j.next) {
            mc.comparacoes++;
            if (comparador.compare(j.data, pivoValor) <= 0) {
                // i++
                i = (i == null) ? inicio : i.next;
                
                // Swap dados (i, j)
                T temp = i.data;
                i.data = j.data;
                j.data = temp;
                mc.trocas++;
            }
        }
        
        // Coloca o pivô na posição correta (i + 1)
        i = (i == null) ? inicio : i.next;
        T temp = i.data;
        i.data = fim.data;
        fim.data = temp;
        mc.trocas++;
        
        return i; // Retorna o nó onde o pivô ficou
    }

    // --- UTILITÁRIOS ---
    
    public void imprimirListaFormatada() {
        Node<T> atual = primeiro;
        int i = 1;
        System.out.println("------------------------------------------------");
        while (atual != null) {
            System.out.println(i + ". " + atual.data.toString());
            atual = atual.next;
            i++;
        }
        System.out.println("------------------------------------------------");
    }

    private void exibirMetricas(String algoritmo, long tempoNano, int comp, int trocas) {
        System.out.printf("[%s] Tempo: %.4f ms | Comparações: %d | Trocas: %d\n", 
            algoritmo, tempoNano / 1_000_000.0, comp, trocas);
    }

    // Classe auxiliar interna para passar contadores por referência no recursivo
    private class MetricasContainer {
        int comparacoes = 0;
        int trocas = 0;
    }
}
