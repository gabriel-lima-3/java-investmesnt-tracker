import java.time.LocalDateTime;
import java.util.Date;
import java.util.Scanner;

public class Main {
    
    // Listas globais
    private static ListaDuplamenteEncadeada<Ativo> listaAtivos = new ListaDuplamenteEncadeada<>();
    private static ListaDuplamenteEncadeada<Investidor> listaInvestidores = new ListaDuplamenteEncadeada<>();
    private static ListaDuplamenteEncadeada<Transacao> listaTransacoes = new ListaDuplamenteEncadeada<>();
    
    private static Investidor investidorLogado = null;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        inicializarDados();
        
        while (true) {
            if (investidorLogado == null) {
                menuAcesso(scanner);
            } else {
                menuPrincipal(scanner);
            }
        }
    }

    // --- MENU DE ACESSO (LOGIN/CADASTRO) ---
    private static void menuAcesso(Scanner scanner) {
        System.out.println("\n#############################################");
        System.out.println("# BEM-VINDO AO GESTOR DE INVESTIMENTOS      #");
        System.out.println("#############################################");
        System.out.println("1. Cadastrar Novo Investidor");
        System.out.println("2. Selecionar Investidor (Login)");
        System.out.println("3. Listar Todos os Investidores");
        System.out.println("99. Encerrar Sistema");
        System.out.print("Escolha: ");

        String op = scanner.nextLine();

        switch (op) {
            case "1":
                cadastrarInvestidor(scanner);
                break;
            case "2":
                selecionarInvestidor(scanner);
                break;
            case "3":
                listarInvestidores();
                break;
            case "99":
                System.out.println("Encerrando...");
                System.exit(0);
                break;
            default:
                System.out.println("Opção inválida.");
        }
    }

    // --- LÓGICA DE CADASTRO ---
    private static void cadastrarInvestidor(Scanner scanner) {
        System.out.println("\n--- CADASTRO DE NOVO INVESTIDOR ---");
        
        System.out.print("Nome completo: ");
        String nome = scanner.nextLine();
        
        int idade = 0;
        while (true) {
            try {
                System.out.print("Idade: ");
                idade = Integer.parseInt(scanner.nextLine());
                if (idade > 0) break;
                System.out.println("Idade deve ser maior que zero.");
            } catch (Exception e) {
                System.out.println("Digite um número válido.");
            }
        }

        String perfil = "";
        while (true) {
            System.out.println("Perfil de Risco (Conservador / Moderado / Arrojado): ");
            perfil = scanner.nextLine().trim();
            if (perfil.equalsIgnoreCase("Conservador") || 
                perfil.equalsIgnoreCase("Moderado") || 
                perfil.equalsIgnoreCase("Arrojado")) {
                perfil = perfil.substring(0, 1).toUpperCase() + perfil.substring(1).toLowerCase();
                break;
            }
            System.out.println("Erro: Digite exatamente uma das opções acima.");
        }

        double capital = 0;
        while (true) {
            try {
                System.out.print("Capital Inicial disponível para investir (R$): ");
                String capStr = scanner.nextLine().replace(",", ".");
                capital = Double.parseDouble(capStr);
                if (capital >= 0) break;
                System.out.println("O capital não pode ser negativo.");
            } catch (Exception e) {
                System.out.println("Valor inválido.");
            }
        }

        Investidor novo = new Investidor(nome, idade, perfil, capital, new ListaDuplamenteEncadeada<>());
        listaInvestidores.inserirFim(novo);
        
        System.out.println("Investidor cadastrado com sucesso!");
        System.out.println("Deseja entrar com este usuário agora? (S/N)");
        String resp = scanner.nextLine();
        if (resp.equalsIgnoreCase("S")) {
            investidorLogado = novo;
        }
    }

    private static void selecionarInvestidor(Scanner scanner) {
        if (listaInvestidores.getTamanho() == 0) {
            System.out.println("Nenhum investidor cadastrado.");
            return;
        }
        
        listarInvestidores();
        System.out.print("Digite o ID do investidor para logar: ");
        
        try {
            int id = Integer.parseInt(scanner.nextLine());
            Investidor selecionado = listaInvestidores.get(id - 1);
            if (selecionado != null) {
                investidorLogado = selecionado;
                System.out.println("Login efetuado com sucesso: " + investidorLogado.getNome());
            } else {
                System.out.println("Investidor não encontrado.");
            }
        } catch (Exception e) {
            System.out.println("ID inválido.");
        }
    }

    private static void listarInvestidores() {
        System.out.println("\n--- LISTA DE INVESTIDORES ---");
        for (int i = 0; i < listaInvestidores.getTamanho(); i++) {
            Investidor inv = listaInvestidores.get(i);
            System.out.printf("ID %d: %s | %d anos | Perfil: %s | Saldo: R$ %.2f\n", 
                (i + 1), inv.getNome(), inv.getIdade(), inv.getPerfilRisco(), inv.getCapitalDisponivel());
        }
    }

    // --- MENU PRINCIPAL (OPERAÇÕES DO USUÁRIO LOGADO) ---
    private static void menuPrincipal(Scanner scanner) {
        AnalisarRecomendacoees analisador = new AnalisarRecomendacoees(listaAtivos);

        System.out.println("\n#############################################");
        System.out.println("# MENU DO INVESTIDOR: " + investidorLogado.getNome().toUpperCase());
        System.out.println("# Perfil: " + investidorLogado.getPerfilRisco() + " | Saldo: R$ " + String.format("%.2f", investidorLogado.getCapitalDisponivel()));
        System.out.println("#############################################");
        System.out.println("1. Listar Ativos Disponíveis");
        System.out.println("2. Buscar Ativo (Linear/Binária)");
        System.out.println("3. Ver Minha Carteira");
        System.out.println("4. Realizar Investimento (COMPRAR)");
        System.out.println("5. Resgatar Investimento (VENDER)");
        System.out.println("6. Obter Recomendações");
        System.out.println("7. Relatórios de Performance (Ordenação)");
        System.out.println("8. Histórico de Transações");
        System.out.println("0. Deslogar / Trocar Usuário");
        System.out.print("Escolha: ");

        int opcao = 0;
        try {
            opcao = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            opcao = -1;
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
                System.out.println("\n--- RECOMENDAÇÕES PARA " + investidorLogado.getPerfilRisco().toUpperCase() + " ---");
                if (recs.getTamanho() == 0) System.out.println("Nenhuma recomendação compatível encontrada.");
                else recs.imprimirListaFormatada();
                break;
            case 7:
                menuRelatorios(scanner);
                break;
            case 8:
                exibirTransacoesUsuario();
                break;
            case 0:
                System.out.println("Deslogando...");
                investidorLogado = null;
                break;
            default:
                System.out.println("Opção inválida.");
        }
    }

    // --- MÉTODOS AUXILIARES (LÓGICA DE NEGÓCIO) ---

    private static void exibirTransacoesUsuario() {
        System.out.println("\n--- HISTÓRICO DE " + investidorLogado.getNome().toUpperCase() + " ---");
        boolean temTransacao = false;
        
        // Filtra transações apenas do usuário logado
        for(int i=0; i<listaTransacoes.getTamanho(); i++){
            Transacao t = listaTransacoes.get(i);
            if (t.getInvestidor() == investidorLogado || t.getInvestidor().getNome().equals(investidorLogado.getNome())) {
                System.out.printf("[%s] %s - %s: R$ %.2f (L/P: %.2f)\n", 
                    t.getDate().toString(), t.getTipo(), t.getAtivo().getCodigo(), t.getValor(), t.getLucroPrejuizo());
                temTransacao = true;
            }
        }
        
        if (!temTransacao) System.out.println("Nenhuma transação registrada para este usuário.");
    }

    // Lógica para Comprar Ativos

    private static void realizarInvestimento(Scanner scanner, Investidor inv) {
        System.out.println("\n--- NOVO INVESTIMENTO ---");
        System.out.print("Digite o código do ativo para comprar (ex: PETR4): ");
        String codigo = scanner.nextLine();

        listaAtivos.quickSort(Comparador.porCodigo()); 
        
        Ativo ativoBusca = new Ativo();
        ativoBusca.setCodigo(codigo);
        Ativo ativoAlvo = listaAtivos.buscaBinaria(ativoBusca, Comparador.porCodigo());

        if (ativoAlvo == null) {
            System.out.println("ERRO: Ativo não encontrado!");
            return;
        }

        System.out.println("Selecionado: " + ativoAlvo.getNome() + " | Preço Unitário: R$ " + ativoAlvo.getValorAtual());
        System.out.print("Quantas unidades deseja comprar? ");
        double qtd = 0;
        try {
             qtd = Double.parseDouble(scanner.nextLine());
        } catch (Exception e) { System.out.println("Número inválido"); return; }

        double custoTotal = qtd * ativoAlvo.getValorAtual();

        // 1. Valida Saldo
        if (custoTotal > inv.getCapitalDisponivel()) {
            System.out.println("ERRO: Saldo insuficiente. Você tem: R$ " + inv.getCapitalDisponivel());
            return;
        }

        if (ativoAlvo.getRisco().equalsIgnoreCase("Alto")) {
             double patrimonioTotal = inv.getCapitalDisponivel();
             double totalAltoRiscoFuturo = custoTotal;
             
             // Soma o valor de todos os investimentos que já existem
             for(int i=0; i<inv.getCarteira().getTamanho(); i++){
                 Investimento item = inv.getCarteira().get(i);
                 patrimonioTotal += item.getValorAtual();
                 
                 if(item.getAtivo().getRisco().equalsIgnoreCase("Alto")){
                     totalAltoRiscoFuturo += item.getValorAtual();
                 }
             }
             
             if (patrimonioTotal > 0 && (totalAltoRiscoFuturo / patrimonioTotal) > 0.80) {
                 System.out.println("BLOQUEADO: Essa operação deixaria mais de 80% do seu patrimônio total em alto risco.");
                 System.out.printf("Patrimônio Total: %.2f | Limite Alto Risco: %.2f | Projetado: %.2f\n", 
                         patrimonioTotal, (patrimonioTotal * 0.8), totalAltoRiscoFuturo);
                 return;
             }
        }

        // Efetiva a Compra
        inv.setCapitalDisponivel(inv.getCapitalDisponivel() - custoTotal);
        
        Investimento novoInv = new Investimento(ativoAlvo, inv, custoTotal, qtd, LocalDateTime.now(), custoTotal);
        inv.getCarteira().inserirFim(novoInv);

        Transacao t = new Transacao("COMPRA", inv, ativoAlvo, custoTotal, new Date(), 0);
        listaTransacoes.inserirFim(t);

        System.out.println("SUCESSO! Investimento realizado.");
    }

    // Lógica para Vender/Resgatar

    private static void resgatarInvestimento(Scanner scanner, Investidor inv) {
        System.out.println("\n--- RESGATE DE INVESTIMENTO ---");
        if (inv.getCarteira().getTamanho() == 0) {
            System.out.println("Sua carteira está vazia.");
            return;
        }
        
        inv.getCarteira().imprimirListaFormatada();
        System.out.print("Digite o NÚMERO do item na lista para resgatar (ex: 1): ");
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

        // Credita o dinheiro de volta
        inv.setCapitalDisponivel(inv.getCapitalDisponivel() + valorResgate);
        
        // Registra a transação
        Transacao t = new Transacao("VENDA", inv, item.getAtivo(), valorResgate, new Date(), lucro);
        listaTransacoes.inserirFim(t);

        // --- Remove o nó da Lista ---
        inv.getCarteira().remover(index);
        
        System.out.println("Resgate realizado com sucesso!");
        System.out.println("Valor creditado: R$ " + String.format("%.2f", valorResgate));
        System.out.println("Lucro/Prejuízo: R$ " + String.format("%.2f", lucro));
        System.out.println("O ativo foi removido da sua carteira.");
    }

    private static void menuBusca(Scanner scanner) {
        System.out.println("1. Por Nome (Busca Linear)");
        System.out.println("2. Por Código (Busca Binária)");
        String op = scanner.nextLine();
        
        if (op.equals("1")) {
            System.out.print("Nome do ativo: ");
            String nome = scanner.nextLine();
            Ativo busca = new Ativo(); busca.setNome(nome);
            Ativo res = listaAtivos.buscarLinear(busca, Comparador.porNome());
            if (res != null) System.out.println("Encontrado: " + res);
            else System.out.println("Não encontrado.");
        } else {
            System.out.print("Código do ativo: ");
            String cod = scanner.nextLine();
            Ativo busca = new Ativo(); busca.setCodigo(cod);
            
            System.out.println("Ordenando lista por código...");
            listaAtivos.quickSort(Comparador.porCodigo());
            
            Ativo res = listaAtivos.buscaBinaria(busca, Comparador.porCodigo());
            if (res != null) System.out.println("Encontrado: " + res);
            else System.out.println("Não encontrado.");
        }
    }
    
    private static void menuRelatorios(Scanner scanner) {
        AnalisarRecomendacoees relator = new AnalisarRecomendacoees(listaAtivos);
        
        System.out.println("\n--- CENTRAL DE RELATÓRIOS E ANÁLISES ---");
        System.out.println("1. Top 5 Rentabilidade");
        System.out.println("2. Top 5 Risco x Retorno");
        System.out.println("3. Ranking Histórico de Desempenho");
        System.out.println("4. Distribuição da Minha Carteira");
        System.out.println("5. Comparativo Técnico dos Algoritmos de Ordenação");
        System.out.print("Escolha o relatório: ");
        
        String op = scanner.nextLine();

        switch (op) {
            case "1":
                relator.relatorioTop5Rentabilidade();
                break;
            case "2":
                relator.relatorioTop5RiscoRetorno();
                break;
            case "3":
                relator.relatorioRankingHistorico();
                break;
            case "4":
                relator.relatorioDistribuicaoCarteira(investidorLogado);
                break;
            case "5":
                System.out.println("\n>> Insertion Sort (Rentabilidade)...");
                listaAtivos.insertionSort(Comparador.porRentabilidade()).imprimirMetricas();
                
                System.out.println("\n>> Merge Sort (Variação)...");
                listaAtivos.mergeSort(Comparador.porVariacaoAcumulada()).imprimirMetricas();
                
                System.out.println("\n>> Quick Sort (Valor Atual)...");
                listaAtivos.quickSort(Comparador.porValorAtual()).imprimirMetricas();
                break;
            default:
                System.out.println("Opção de relatório inválida.");
        }
    }

    private static void inicializarDados() {
        ListaDuplamenteEncadeada<Double> h1 = new ListaDuplamenteEncadeada<>();
        h1.inserirFim(1.0); h1.inserirFim(2.0); h1.inserirFim(0.5);
        
        ListaDuplamenteEncadeada<Double> hRuim = new ListaDuplamenteEncadeada<>();
        hRuim.inserirFim(-1.0); hRuim.inserirFim(-2.0); hRuim.inserirFim(-1.5);

        listaAtivos.inserirFim(new Ativo("PETR4", "Petrobras", "Ação", "Alto", 15.5, 30.00, 10.0, h1));
        listaAtivos.inserirFim(new Ativo("VALE3", "Vale", "Ação", "Alto", -5.0, 70.00, -10.0, hRuim));
        listaAtivos.inserirFim(new Ativo("ITUB4", "Itaú", "Ação", "Medio", 8.0, 25.00, 5.0, h1));
        listaAtivos.inserirFim(new Ativo("TESOURO", "Tesouro Direto", "Renda Fixa", "Baixo", 12.0, 100.00, 12.0, h1));
        listaAtivos.inserirFim(new Ativo("BTC", "Bitcoin", "Cripto", "Alto", 50.0, 200000.0, 40.0, h1));
    }
}