public class AnalisarRecomendacoees {

    private ListaDuplamenteEncadeada<Ativo> todosAtivos;

    public AnalisarRecomendacoees(ListaDuplamenteEncadeada<Ativo> todosAtivos) {
        this.todosAtivos = todosAtivos;
    }

    // --- MÉTODOS DE RECOMENDAÇÃO ---
    
    public ListaDuplamenteEncadeada<Ativo> gerarRecomendacoes(Investidor investidor) {
        ListaDuplamenteEncadeada<Ativo> recomendados = new ListaDuplamenteEncadeada<>();
        String perfil = investidor.getPerfilRisco();

        for (int i = 0; i < todosAtivos.getTamanho(); i++) {
            Ativo ativo = todosAtivos.get(i);
            boolean compativel = false;

            if (perfil.equalsIgnoreCase("Conservador")) {
                if (ativo.getRisco().equalsIgnoreCase("Baixo")) compativel = true;
            } else if (perfil.equalsIgnoreCase("Moderado")) {
                if (!ativo.getRisco().equalsIgnoreCase("Alto")) compativel = true;
            } else { 
                compativel = true;
            }

            if (temPrejuizoConsecutivo(ativo)) compativel = false;

            if (ativo.getRisco().equalsIgnoreCase("Alto") && compativel) {
                if (!validaLimiteRiscoAlto(investidor)) compativel = false;
            }

            if (compativel) recomendados.inserirFim(ativo);
        }
        recomendados.insertionSort(Comparador.porRentabilidade());
        return recomendados;
    }

    private boolean temPrejuizoConsecutivo(Ativo ativo) {
        ListaDuplamenteEncadeada<Double> hist = ativo.getHistoricoRentavel();
        if (hist == null || hist.getTamanho() < 3) return false;
        double v1 = hist.get(hist.getTamanho() - 1);
        double v2 = hist.get(hist.getTamanho() - 2);
        double v3 = hist.get(hist.getTamanho() - 3);
        return (v1 < 0 && v2 < 0 && v3 < 0);
    }

    private boolean validaLimiteRiscoAlto(Investidor investidor) {
        ListaDuplamenteEncadeada<Investimento> carteira = investidor.getCarteira();
        
        double patrimonioTotal = investidor.getCapitalDisponivel();
        double totalAltoRisco = 0;

        if (carteira != null) {
            for (int i = 0; i < carteira.getTamanho(); i++) {
                Investimento inv = carteira.get(i);
                patrimonioTotal += inv.getValorAtual();
                
                if (inv.getAtivo().getRisco().equalsIgnoreCase("Alto")) {
                    totalAltoRisco += inv.getValorAtual();
                }
            }
        }

        if (patrimonioTotal == 0) return true;
        
        return (totalAltoRisco / patrimonioTotal) < 0.80;
    }

    // --- NOVOS RELATÓRIOS ---

    // 1. Top 5 Rentabilidade
    public void relatorioTop5Rentabilidade() {
        System.out.println("\n--- TOP 5 MAIORES RENTABILIDADES ---");
        todosAtivos.insertionSort(Comparador.porRentabilidade());
        imprimirTop(5);
    }

    // 2. Top 5 Risco x Retorno
    public void relatorioTop5RiscoRetorno() {
        System.out.println("\n--- TOP 5 MELHOR RELAÇÃO RISCO X RETORNO ---");
        todosAtivos.insertionSort(Comparador.porRiscoRetorno());
        imprimirTop(5);
    }

    // 3. Ranking Histórico (Variação Acumulada)
    public void relatorioRankingHistorico() {
        System.out.println("\n--- RANKING DE DESEMPENHO HISTÓRICO (VARIAÇÃO ACUMULADA) ---");
        todosAtivos.mergeSort(Comparador.porVariacaoAcumulada());
        
        for (int i = 0; i < todosAtivos.getTamanho(); i++) {
            Ativo a = todosAtivos.get(i);
            System.out.printf("%dº. %s | Variação: %.2f%%\n", (i+1), a.getNome(), a.getVariacaoAcumulada());
        }
    }

    // 4. Distribuição Percentual da Carteira
    public void relatorioDistribuicaoCarteira(Investidor investidor) {
        System.out.println("\n--- DISTRIBUIÇÃO DA CARTEIRA POR TIPO ---");
        ListaDuplamenteEncadeada<Investimento> carteira = investidor.getCarteira();
        
        if (carteira.getTamanho() == 0) {
            System.out.println("Carteira vazia.");
            return;
        }

        double valorTotalCarteira = 0;
        for (int i = 0; i < carteira.getTamanho(); i++) {
            valorTotalCarteira += carteira.get(i).getValorAtual();
        }

        ListaDuplamenteEncadeada<String> tiposProcessados = new ListaDuplamenteEncadeada<>();

        for (int i = 0; i < carteira.getTamanho(); i++) {
            String tipoAtual = carteira.get(i).getAtivo().getTipo();
            boolean jaProcessou = false;
            for (int k = 0; k < tiposProcessados.getTamanho(); k++) {
                if (tiposProcessados.get(k).equalsIgnoreCase(tipoAtual)) {
                    jaProcessou = true;
                    break;
                }
            }

            if (!jaProcessou) {
                double totalTipo = 0;
                for (int j = 0; j < carteira.getTamanho(); j++) {
                    if (carteira.get(j).getAtivo().getTipo().equalsIgnoreCase(tipoAtual)) {
                        totalTipo += carteira.get(j).getValorAtual();
                    }
                }
                
                double percentual = (totalTipo / valorTotalCarteira) * 100;
                System.out.printf("Tipo: %-10s | Total: R$ %9.2f | Parte: %.1f%%\n", 
                        tipoAtual, totalTipo, percentual);
                
                tiposProcessados.inserirFim(tipoAtual);
            }
        }
        System.out.printf("VALOR TOTAL: R$ %.2f\n", valorTotalCarteira);
    }

    private void imprimirTop(int n) {
        int limite = Math.min(n, todosAtivos.getTamanho());
        for (int i = 0; i < limite; i++) {
            Ativo a = todosAtivos.get(i);
            System.out.printf("%d. %s (%s) | Rent: %.2f%% | Risco: %s\n", 
                i+1, a.getNome(), a.getTipo(), a.getRentabilidade(), a.getRisco());
        }
    }
}