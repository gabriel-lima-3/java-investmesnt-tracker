import java.time.LocalDateTime;
import java.util.zip.DataFormatException;

public class Investimento {


    private Ativo ativo;
    private Investidor investidor;
    private double valorInvestido;
    private double quantidade;
    private LocalDateTime dataInvestimento;
    private double valorAtual;
    private ListaDuplamenteEncadeada<Double> historicoVariacoes;

    public Investimento() {
        this.historicoVariacoes = new ListaDuplamenteEncadeada<>();
    }

    public Investimento(Ativo ativo, Investidor investidor, double valorInvestido,
                        double quantidade, LocalDateTime dataInvestimento, double valorAtual) {
        this.ativo = ativo;
        this.investidor = investidor;
        this.valorInvestido = valorInvestido;
        this.quantidade = quantidade;
        this.dataInvestimento = dataInvestimento;
        this.valorAtual = valorAtual;
        this.historicoVariacoes = new ListaDuplamenteEncadeada<>(); // ← ADICIONE ESTA LINHA
    }

    public Ativo getAtivo() {
        return ativo;
    }

    public void setAtivo(Ativo ativo) {
        this.ativo = ativo;
    }

    public Investidor getInvestidor() {
        return investidor;
    }

    public void setInvestidor(Investidor investidor) {
        this.investidor = investidor;
    }

    public double getValorInvestido() {
        return valorInvestido;
    }

    public void setValorInvestido(double valorInvestido) {
        this.valorInvestido = valorInvestido;
    }

    public double getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(double quantidade) {
        this.quantidade = quantidade;
    }

    public LocalDateTime getDataInvestimento() {
        return dataInvestimento;
    }

    public void setDataInvestimento(LocalDateTime dataInvestimento) {
        this.dataInvestimento = dataInvestimento;
    }

    public double getValorAtual() {
        return valorAtual;
    }

    public void setValorAtual(double valorAtual) {
        this.valorAtual = valorAtual;
    }

    public double calcularLucroPrejuizo(){
        return this.valorAtual - this.valorInvestido;
    }

    public double calcularRentabilidade(){
        if (this.valorInvestido == 0) return 0;
        return ((this.valorAtual - this.valorInvestido) / this.valorInvestido) * 100;
    }

    public void atualizarValor(double variacaoPercentual){
        this.historicoVariacoes.inserirFim(variacaoPercentual);
        this.valorAtual = this.valorAtual * (1 + (variacaoPercentual / 100));
    }

    public boolean isAltoRisco() {
        return ativo != null && ativo.getRisco().equalsIgnoreCase("Alto");
    }

    @Override
    public String toString() {
        return String.format("Investimento: %s em %s | Valor: R$%.2f | Rent: %.1f%%",
                investidor != null ? investidor.getNome() : "N/A",
                ativo != null ? ativo.getNome() : "N/A",
                valorAtual,
                calcularRentabilidade());
    }




}
