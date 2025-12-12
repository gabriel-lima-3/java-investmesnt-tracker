import java.time.LocalDateTime;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ListaDuplamenteEncadeada<Ativo> listaAtivos = new ListaDuplamenteEncadeada<>();
        ListaDuplamenteEncadeada<Investidor> listaInvestidores = new ListaDuplamenteEncadeada<>();

        inicializarDados(listaAtivos, listaInvestidores);
        AnalisarRecomendacoees analisador = new AnalisarRecomendacoees(listaAtivos);

        int opcao = 0;
        while (opcao != 9) {
            System.out.println("\n#############################################");
            System.out.println("# SISTEMA INTELIGENTE DE INVESTIMENTOS v1.0 #");
            System.out.println("#############################################");
            System.out.println("1. Listar Ativos Disponíveis");
            System.out.println("2. Buscar Ativo por Nome (Linear)");
            System.out.println("3. Buscar Ativo por Código (Binária)");
            System.out.println("4. Comparar Algoritmos de Ordenação");
            System.out.println("5. Ver Carteira do Investidor");
            System.out.println("6. Gerar Recomendações (IA Simbólica)");
            System.out.println("7. Relatório Top 5 Rentabilidade");
            System.out.println("9. Sair");
            System.out.print("Escolha uma opção: ");

            try {
                String input = scanner.nextLine();
                opcao = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                opcao = 0;
            }

            switch (opcao) {
                case 1:
                    listaAtivos.imprimirListaFormatada();
                    break;
                case 2:
                    System.out.print("Digite o nome do ativo: ");
                    String nome = scanner.nextLine();
                    Ativo buscaNome = new Ativo();
                    buscaNome.setNome(nome);
                    Ativo resLinear = listaAtivos.buscarLinear(buscaNome, Comparador.porNome());
                    if (resLinear != null) System.out.println("ENCONTRADO: " + resLinear);
                    else System.out.println("Não encontrado.");
                    break;
                case 3:
                    System.out.print("Digite o código (ex: PETR4): ");
                    String cod = scanner.nextLine();
                    Ativo buscaCod = new Ativo();
                    buscaCod.setCodigo(cod);
                    
                    // IMPORTANTE: Busca binária exige ordenação prévia
                    System.out.println("Ordenando lista por código para permitir busca binária...");
                    listaAtivos.quickSort(Comparador.porCodigo());
                    
                    Ativo resBin = listaAtivos.buscaBinaria(buscaCod, Comparador.porCodigo());
                    if (resBin != null) System.out.println("ENCONTRADO: " + resBin);
                    else System.out.println("Não encontrado.");
                    break;
                case 4:
                    System.out.println("\n--- COMPARATIVO DE PERFORMANCE ---");
                    System.out.println(">> Insertion Sort (Por Rentabilidade):");
                    MetricasOrdenacao m1 = listaAtivos.insertionSort(Comparador.porRentabilidade());
                    m1.imprimirMetricas();
                    
                    System.out.println(">> Merge Sort (Por Variação Acumulada):");
                    MetricasOrdenacao m2 = listaAtivos.mergeSort(Comparador.porVariacaoAcumulada());
                    m2.imprimirMetricas();
                    
                    System.out.println(">> Quick Sort (Por Valor Atual):");
                    MetricasOrdenacao m3 = listaAtivos.quickSort(Comparador.porValorAtual());
                    m3.imprimirMetricas();
                    break;
                case 5:
                    Investidor inv = listaInvestidores.get(0); // Pega o primeiro para teste
                    System.out.println("Investidor: " + inv.getNome());
                    System.out.println("Capital Livre: R$ " + inv.getCapitalDisponivel());
                    System.out.println("--- CARTEIRA ---");
                    if (inv.getCarteira().getTamanho() == 0) System.out.println("Vazia.");
                    else inv.getCarteira().imprimirListaFormatada();
                    break;
                case 6:
                    Investidor cliente = listaInvestidores.get(0);
                    ListaDuplamenteEncadeada<Ativo> recs = analisador.gerarRecomendacoes(cliente);
                    System.out.println("\n=== RECOMENDAÇÕES PARA " + cliente.getNome() + " (" + cliente.getPerfilRisco() + ") ===");
                    if (recs.getTamanho() == 0) System.out.println("Nenhum ativo recomendado no momento.");
                    else recs.imprimirListaFormatada();
                    break;
                case 7:
                    analisador.relatorioTop5Rentabilidade();
                    break;
                case 9:
                    System.out.println("Encerrando sistema...");
                    break;
                default:
                    System.out.println("Opção inválida.");
            }
        }
        scanner.close();
    }

    private static void inicializarDados(ListaDuplamenteEncadeada<Ativo> ativos, ListaDuplamenteEncadeada<Investidor> investidores) {
        // Criando históricos fictícios
        ListaDuplamenteEncadeada<Double> histPositivo = new ListaDuplamenteEncadeada<>();
        histPositivo.inserirFim(1.5); histPositivo.inserirFim(2.0); histPositivo.inserirFim(0.5);

        ListaDuplamenteEncadeada<Double> histNegativo = new ListaDuplamenteEncadeada<>();
        histNegativo.inserirFim(-1.0); histNegativo.inserirFim(-2.5); histNegativo.inserirFim(-0.5);

        // Ativos
        // Ativo(codigo, nome, tipo, risco, rentabilidade, valorAtual, variacaoAcum, historico)
        ativos.inserirFim(new Ativo("PETR4", "Petrobras", "Ação", "Alto", 15.0, 34.50, 10.0, histPositivo));
        ativos.inserirFim(new Ativo("VALE3", "Vale", "Ação", "Alto", 12.0, 68.00, -5.0, histNegativo)); // Deve ser penalizado
        ativos.inserirFim(new Ativo("SELIC", "Tesouro Selic", "Renda Fixa", "Baixo", 13.75, 1000.0, 1.0, histPositivo));
        ativos.inserirFim(new Ativo("MXRF11", "Maxi Renda", "Fundo", "Medio", 1.1, 10.50, 2.0, histPositivo));
        ativos.inserirFim(new Ativo("BTC", "Bitcoin", "Cripto", "Alto", 45.0, 300000.0, 20.0, histPositivo));

        // Investidor
        ListaDuplamenteEncadeada<Investimento> carteira = new ListaDuplamenteEncadeada<>();
        // Adiciona um investimento inicial para testar regra de 80%
        // Investimento(ativo, investidor, valorInv, qtd, data, valorAtual)
        Investimento inv1 = new Investimento(ativos.get(0), null, 5000, 100, LocalDateTime.now(), 5500);
        carteira.inserirFim(inv1);

        Investidor joao = new Investidor("João Silva", 30, "Moderado", 50000.0, carteira);
        inv1.setInvestidor(joao); // Atualiza referência

        investidores.inserirFim(joao);
    }
}