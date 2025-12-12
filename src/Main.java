import java.time.LocalDateTime;
import java.util.Date;
import java.util.Scanner;

public class Main {
    
    // Listas globais para facilitar o acesso nos métodos estáticos
    private static ListaDuplamenteEncadeada<Ativo> listaAtivos = new ListaDuplamenteEncadeada<>();
    private static ListaDuplamenteEncadeada<Investidor> listaInvestidores = new ListaDuplamenteEncadeada<>();
    private static ListaDuplamenteEncadeada<Transacao> listaTransacoes = new ListaDuplamenteEncadeada<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        inicializarDados();
        AnalisarRecomendacoees analisador = new AnalisarRecomendacoees(listaAtivos);

        Investidor investidorLogado = listaInvestidores.get(0);

        int opcao = 0;
        while (opcao != 99) {
            System.out.println("\n#############################################");
            System.out.println("# SISTEMA DE GESTÃO DE INVESTIMENTOS        #");
            System.out.println("#############################################");
            System.out.println("Usuário: " + investidorLogado.getNome() + " | Saldo: R$ " + String.format("%.2f", investidorLogado.getCapitalDisponivel()));
            System.out.println("---------------------------------------------");
            System.out.println("1. Listar Ativos Disponíveis");
            System.out.println("2. Buscar Ativo (Linear/Binária)");
            System.out.println("3. Ver Minha Carteira");
            System.out.println("4. Realizar Investimento (COMPRAR)");
            System.out.println("5. Resgatar Investimento (VENDER)");
            System.out.println("6. Obter Recomendações (IA)");
            System.out.println("7. Relatórios de Performance (Ordenação)");
            System.out.println("8. Histórico de Transações");
            System.out.println("99. Sair");
            System.out.print("Escolha: ");

            try {
                opcao = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                opcao = 0;
            }

            switch (opcao) {
                case 1:
                    listaAtivos.imprimirListaFormatada();
                    break;
                case 2:
                    menuBusca(scanner);
                    break;
                case 3:
                    System.out.println("\n--- CARTEIRA DE " + investidorLogado.getNome().toUpperCase() + " ---");
                    if (investidorLogado.getCarteira().getTamanho() == 0) System.out.println("Sua carteira está vazia.");
                    else investidorLogado.getCarteira().imprimirListaFormatada();
                    break;
                case 4:
                    realizarInvestimento(scanner, investidorLogado);
                    break;
                case 5:
                    resgatarInvestimento(scanner, investidorLogado);
                    break;
                case 6:
                    ListaDuplamenteEncadeada<Ativo> recs = analisador.gerarRecomendacoes(investidorLogado);
                    System.out.println("\n--- RECOMENDAÇÕES PERSONALIZADAS ---");
                    recs.imprimirListaFormatada();
                    break;
                case 7:
                    menuRelatorios();
                    break;
                case 8:
                    System.out.println("\n--- HISTÓRICO DE TRANSAÇÕES ---");
                    if (listaTransacoes.getTamanho() == 0) System.out.println("Nenhuma transação registrada.");
                    else {
                        for(int i=0; i<listaTransacoes.getTamanho(); i++){
                            Transacao t = listaTransacoes.get(i);
                            System.out.printf("[%s] %s - %s: R$ %.2f\n", t.getDate().toString(), t.getTipo(), t.getAtivo().getCodigo(), t.getValor());
                        }
                    }
                    break;
                case 99:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção inválida.");
            }
        }
    }

    // --- MÉTODOS DE NEGÓCIO ---

    private static void realizarInvestimento(Scanner scanner, Investidor inv) {
        System.out.println("\n--- NOVO INVESTIMENTO ---");
        System.out.print("Digite o código do ativo (ex: PETR4): ");
        String codigo = scanner.nextLine();

        // Busca o ativo
        listaAtivos.quickSort(Comparador.porCodigo());
        Ativo ativoBusca = new Ativo();
        ativoBusca.setCodigo(codigo);
        Ativo ativoAlvo = listaAtivos.buscaBinaria(ativoBusca, Comparador.porCodigo());

        if (ativoAlvo == null) {
            System.out.println("Ativo não encontrado!");
            return;
        }

        System.out.println("Ativo selecionado: " + ativoAlvo.getNome() + " | Preço: R$ " + ativoAlvo.getValorAtual());
        System.out.print("Digite a quantidade a comprar: ");
        double qtd = 0;
        try {
             qtd = Double.parseDouble(scanner.nextLine());
        } catch (Exception e) { System.out.println("Valor inválido"); return; }

        double custoTotal = qtd * ativoAlvo.getValorAtual();

        // Validações
        if (custoTotal > inv.getCapitalDisponivel()) {
            System.out.println("ERRO: Saldo insuficiente. Disponível: R$ " + inv.getCapitalDisponivel());
            return;
        }

        // Validação de Risco (80% em Alto Risco)
        if (ativoAlvo.getRisco().equalsIgnoreCase("Alto")) {
             double totalPatrimonio = custoTotal;
             double totalAltoRisco = custoTotal;
             
             for(int i=0; i<inv.getCarteira().getTamanho(); i++){
                 Investimento carteiraItem = inv.getCarteira().get(i);
                 totalPatrimonio += carteiraItem.getValorAtual();
                 if(carteiraItem.getAtivo().getRisco().equalsIgnoreCase("Alto")){
                     totalAltoRisco += carteiraItem.getValorAtual();
                 }
             }
             
             if ((totalAltoRisco / totalPatrimonio) > 0.80) {
                 System.out.println("BLOQUEADO: Investimento excede limite de 80% em alto risco.");
                 return;
             }
        }

        // Efetivação
        inv.setCapitalDisponivel(inv.getCapitalDisponivel() - custoTotal);
        
        Investimento novoInv = new Investimento(ativoAlvo, inv, custoTotal, qtd, LocalDateTime.now(), custoTotal);
        inv.getCarteira().inserirFim(novoInv);

        Transacao t = new Transacao("COMPRA", inv, ativoAlvo, custoTotal, new Date(), 0);
        listaTransacoes.inserirFim(t);

        System.out.println("Investimento realizado com sucesso!");
    }

    private static void resgatarInvestimento(Scanner scanner, Investidor inv) {
        System.out.println("\n--- RESGATE DE INVESTIMENTO ---");
        if (inv.getCarteira().getTamanho() == 0) {
            System.out.println("Carteira vazia.");
            return;
        }
        
        inv.getCarteira().imprimirListaFormatada();
        System.out.print("Digite o número do item na lista para resgatar (ex: 1): ");
        int index = -1;
        try {
            index = Integer.parseInt(scanner.nextLine()) - 1;
        } catch(Exception e) {}

        if (index < 0 || index >= inv.getCarteira().getTamanho()) {
            System.out.println("Índice inválido.");
            return;
        }

        Investimento item = inv.getCarteira().get(index);
        double valorResgate = item.getQuantidade() * item.getAtivo().getValorAtual();
        double lucro = valorResgate - item.getValorInvestido();

        inv.setCapitalDisponivel(inv.getCapitalDisponivel() + valorResgate);
        
        Transacao t = new Transacao("VENDA", inv, item.getAtivo(), valorResgate, new Date(), lucro);
        listaTransacoes.inserirFim(t);

        item.setQuantidade(0); 
        item.setValorAtual(0);
        System.out.println("Resgate realizado! Valor creditado: R$ " + valorResgate + " (Lucro/Prej: " + lucro + ")");
        System.out.println("Nota: O item permanece no histórico da carteira com valor zerado.");
    }

    private static void menuBusca(Scanner scanner) {
        System.out.println("1. Por Nome (Linear)");
        System.out.println("2. Por Código (Binária)");
        String op = scanner.nextLine();
        
        if (op.equals("1")) {
            System.out.print("Nome: ");
            String nome = scanner.nextLine();
            Ativo busca = new Ativo(); busca.setNome(nome);
            Ativo res = listaAtivos.buscarLinear(busca, Comparador.porNome());
            if (res != null) System.out.println("Encontrado: " + res);
            else System.out.println("Não encontrado.");
        } else {
            System.out.print("Código: ");
            String cod = scanner.nextLine();
            Ativo busca = new Ativo(); busca.setCodigo(cod);
            listaAtivos.quickSort(Comparador.porCodigo());
            Ativo res = listaAtivos.buscaBinaria(busca, Comparador.porCodigo());
            if (res != null) System.out.println("Encontrado: " + res);
            else System.out.println("Não encontrado.");
        }
    }
    
    private static void menuRelatorios() {
        System.out.println("\n--- RELATÓRIOS DE PERFORMANCE ---");
        System.out.println("Ordenando por Rentabilidade (Insertion Sort)...");
        MetricasOrdenacao m1 = listaAtivos.insertionSort(Comparador.porRentabilidade());
        m1.imprimirMetricas();
        
        System.out.println("\nOrdenando por Variação (Merge Sort)...");
        MetricasOrdenacao m2 = listaAtivos.mergeSort(Comparador.porVariacaoAcumulada());
        m2.imprimirMetricas();
        
        System.out.println("\nOrdenando por Valor (Quick Sort)...");
        MetricasOrdenacao m3 = listaAtivos.quickSort(Comparador.porValorAtual());
        m3.imprimirMetricas();
    }

    private static void inicializarDados() {
        // Históricos
        ListaDuplamenteEncadeada<Double> h1 = new ListaDuplamenteEncadeada<>();
        h1.inserirFim(1.0); h1.inserirFim(2.0); h1.inserirFim(0.5);
        
        ListaDuplamenteEncadeada<Double> hRuim = new ListaDuplamenteEncadeada<>();
        hRuim.inserirFim(-1.0); hRuim.inserirFim(-2.0); hRuim.inserirFim(-1.5);

        // Ativos
        listaAtivos.inserirFim(new Ativo("PETR4", "Petrobras", "Ação", "Alto", 15.5, 30.00, 10.0, h1));
        listaAtivos.inserirFim(new Ativo("VALE3", "Vale", "Ação", "Alto", -5.0, 70.00, -10.0, hRuim));
        listaAtivos.inserirFim(new Ativo("ITUB4", "Itaú", "Ação", "Medio", 8.0, 25.00, 5.0, h1));
        listaAtivos.inserirFim(new Ativo("TESOURO", "Tesouro Direto", "Renda Fixa", "Baixo", 12.0, 100.00, 12.0, h1));
        listaAtivos.inserirFim(new Ativo("BTC", "Bitcoin", "Cripto", "Alto", 50.0, 200000.0, 40.0, h1));

        // Investidor
        Investidor inv = new Investidor("Maria Souza", 30, "Moderado", 100000.00, new ListaDuplamenteEncadeada<>());
        listaInvestidores.inserirFim(inv);
    }
}