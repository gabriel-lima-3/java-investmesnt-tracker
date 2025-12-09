public class Investidor {


    private String nome;
    private int idade;
    private String perfilRisco; //conservador, moderado e arrojado
    private double capitalDisponivel;
    private ListaDuplamenteEncadeada<Investimento> carteira; // Historico de investimento


    public Investidor() {

    }

    public Investidor(String nome, int idade, String perfilRisco, double capitalDisponivel, ListaDuplamenteEncadeada<Investimento> carteira) {
        this.nome = nome;
        this.idade = idade;
        this.perfilRisco = perfilRisco;
        this.capitalDisponivel = capitalDisponivel;
        this.carteira = carteira;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getIdade() {
        return idade;
    }

    public void setIdade(int idade) {
        this.idade = idade;
    }

    public String getPerfilRisco() {
        return perfilRisco;
    }

    public void setPerfilRisco(String perfilRisco) {
        this.perfilRisco = perfilRisco;
    }

    public double getCapitalDisponivel() {
        return capitalDisponivel;
    }

    public void setCapitalDisponivel(double capitalDisponivel) {
        this.capitalDisponivel = capitalDisponivel;
    }

    public ListaDuplamenteEncadeada<Investimento> getCarteira() {
        return carteira;
    }

    public void setCarteira(ListaDuplamenteEncadeada<Investimento> carteira) {
        this.carteira = carteira;
    }
}
