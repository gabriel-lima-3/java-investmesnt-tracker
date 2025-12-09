public class MetricasOrdenacao {
    private long tempoExecucao;
    private int comparacoes;
    private int trocas;

    public MetricasOrdenacao() {
        this.tempoExecucao = 0;
        this.comparacoes = 0;
        this.trocas = 0;
    }

    public MetricasOrdenacao(long tempoExecucao, int comparacoes, int trocas) {
        this.tempoExecucao = tempoExecucao;
        this.comparacoes = comparacoes;
        this.trocas = trocas;
    }

    public long getTempoExecucao() {
        return tempoExecucao;
    }

    public double getTempo() {
        return tempoExecucao / 1_000_000.0;
    }

    public int getComparacoes() {
        return comparacoes;
    }

    public int getTrocas() {
        return trocas;
    }

    public void imprimirMetricas() {
        System.out.printf("Tempo: %.3f ms | Comparações: %d | Trocas: %d\n",
                getTempo(), comparacoes, trocas);
    }

}