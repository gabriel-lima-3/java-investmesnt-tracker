public class Ativo {

    private String codigo;
    private String nome;
    private String tipo;
    private String risco;
    private double rentabilidade;
    private double valorAtual;
    private double variacaoAcumulada;
    ListaDuplamenteEncadeada<Double> historicoRentavel;

    public Ativo() {

    }

    public Ativo(String codigo, String nome, String tipo, String risco, double rentabilidade, double valorAtual, double variacaoAcumulada, ListaDuplamenteEncadeada<Double> historicoRentavel) {
        this.codigo = codigo;
        this.nome = nome;
        this.tipo = tipo;
        this.risco = risco;
        this.rentabilidade = rentabilidade;
        this.valorAtual = valorAtual;
        this.variacaoAcumulada = variacaoAcumulada;
        this.historicoRentavel = historicoRentavel;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getRisco() {
        return risco;
    }

    public void setRisco(String risco) {
        this.risco = risco;
    }

    public double getRentabilidade() {
        return rentabilidade;
    }

    public void setRentabilidade(double rentabilidade) {
        this.rentabilidade = rentabilidade;
    }

    public double getValorAtual() {
        return valorAtual;
    }

    public void setValorAtual(double valorAtual) {
        this.valorAtual = valorAtual;
    }

    public double getVariacaoAcumulada() {
        return variacaoAcumulada;
    }

    public void setVariacaoAcumulada(double variacaoAcumulada) {
        this.variacaoAcumulada = variacaoAcumulada;
    }

    public ListaDuplamenteEncadeada<Double> getHistoricoRentavel() {
        return historicoRentavel;
    }

    public void setHistoricoRentavel(ListaDuplamenteEncadeada<Double> historicoRentavel) {
        this.historicoRentavel = historicoRentavel;
    }
}
