import java.util.Date;

public class Transacao {
    private String tipo;
    private Investidor investidor;
    private Ativo ativo;
    private double valor;
    private Date date;
    private double lucroPrejuizo;

    public Transacao() {

    }

    public Transacao(String tipo, Investidor investidor, Ativo ativo, double valor, Date date, double lucroPrejuizo) {
        this.tipo = tipo;
        this.investidor = investidor;
        this.ativo = ativo;
        this.valor = valor;
        this.date = date;
        this.lucroPrejuizo = lucroPrejuizo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Investidor getInvestidor() {
        return investidor;
    }

    public void setInvestidor(Investidor investidor) {
        this.investidor = investidor;
    }

    public Ativo getAtivo() {
        return ativo;
    }

    public void setAtivo(Ativo ativo) {
        this.ativo = ativo;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getLucroPrejuizo() {
        return lucroPrejuizo;
    }

    public void setLucroPrejuizo(double lucroPrejuizo) {
        this.lucroPrejuizo = lucroPrejuizo;
    }
}
