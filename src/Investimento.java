import java.time.LocalDateTime;
import java.util.zip.DataFormatException;

public class Investimento {


    private Ativo ativo;
    private Investidor investidor;
    private double valorInvestido;
    private double quantidade;
    private LocalDateTime dataInvestimento;
    private double valorAtual;

    public Investimento() {
    }

    public Investimento(Ativo ativo, Investidor investidor, double valorInvestido, double quantidade, LocalDateTime dataInvestimento, double valorAtual) {
        this.ativo = ativo;
        this.investidor = investidor;
        this.valorInvestido = valorInvestido;
        this.quantidade = quantidade;
        this.dataInvestimento = dataInvestimento;
        this.valorAtual = valorAtual;
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

        return  1;
    }

    public double calcularRentabilidade(){
        return  1;
    }

    public void atualizarValor(){

    }







}
