import java.time.LocalDateTime;

public class AnalisarRecomendacoees {

    private ListaDuplamenteEncadeada<Ativo> todosAtivos;

    public AnalisarRecomendacoees(ListaDuplamenteEncadeada<Ativo> todosAtivos) {
        this.todosAtivos = todosAtivos;
    }

    // Requisito: Gerar recomendações baseadas no perfil
    public ListaDuplamenteEncadeada<Ativo> gerarRecomendacoes(Investidor investidor) {
        ListaDuplamenteEncadeada<Ativo> recomendados = new ListaDuplamenteEncadeada<>();
        String perfil = investidor.getPerfilRisco();
        
        System.out.println("Analisando ativos para perfil: " + perfil);

        // Percorre todos os ativos disponíveis
        for (int i = 0; i < todosAtivos.getTamanho(); i++) {
            Ativo ativo = todosAtivos.get(i);
            boolean compativel = false;

            // 1. Regra de Perfil
            if (perfil.equalsIgnoreCase("Conservador")) {
                if (ativo.getRisco().equalsIgnoreCase("Baixo")) compativel = true;
            } else if (perfil.equalsIgnoreCase("Moderado")) {
                if (!ativo.getRisco().equalsIgnoreCase("Alto")) compativel = true;
            } else { // Arrojado
                compativel = true;
            }

            // 2. Regra de Penalização (3 quedas consecutivas)
            if (temPrejuizoConsecutivo(ativo)) {
                // System.out.println("Ativo " + ativo.getCodigo() + " penalizado por histórico negativo.");
                compativel = false;
            }

            // 3. Regra dos 80% em Risco Alto
            if (ativo.getRisco().equalsIgnoreCase("Alto") && compativel) {
                if (!validaLimiteRiscoAlto(investidor)) {
                    compativel = false; // Bloqueia recomendação de alto risco se já estourou limite
                }
            }

            if (compativel) {
                recomendados.inserirFim(ativo);
            }
        }

        // Ordenar recomendações pela maior rentabilidade (Insertion Sort - estável e simples)
        recomendados.insertionSort(Comparador.porRentabilidade());

        return recomendados;
    }

    // Verifica se o ativo teve rentabilidade negativa nas últimas 3 variações
    private boolean temPrejuizoConsecutivo(Ativo ativo) {
        ListaDuplamenteEncadeada<Double> hist = ativo.getHistoricoRentavel();
        if (hist == null || hist.getTamanho() < 3) return false;

        // Pega os 3 últimos registros
        double v1 = hist.get(hist.getTamanho() - 1);
        double v2 = hist.get(hist.getTamanho() - 2);
        double v3 = hist.get(hist.getTamanho() - 3);

        return (v1 < 0 && v2 < 0 && v3 < 0);
    }

    // Verifica se o investidor já comprometeu 80% ou mais em alto risco
    private boolean validaLimiteRiscoAlto(Investidor investidor) {
        ListaDuplamenteEncadeada<Investimento> carteira = investidor.getCarteira();
        if (carteira == null || carteira.getTamanho() == 0) return true;

        double totalPatrimonio = 0;
        double totalAltoRisco = 0;

        for (int i = 0; i < carteira.getTamanho(); i++) {
            Investimento inv = carteira.get(i);
            totalPatrimonio += inv.getValorAtual();
            if (inv.getAtivo().getRisco().equalsIgnoreCase("Alto")) {
                totalAltoRisco += inv.getValorAtual();
            }
        }

        if (totalPatrimonio == 0) return true;
        
        // Se a proporção for maior ou igual a 80% (0.8), retorna false (bloqueia)
        return (totalAltoRisco / totalPatrimonio) < 0.8;
    }

    public void relatorioTop5Rentabilidade() {
        System.out.println("\n=== TOP 5 ATIVOS POR RENTABILIDADE ===");
        // Ordena a lista principal (ou poderia criar uma cópia se quisesse preservar a ordem original)
        todosAtivos.insertionSort(Comparador.porRentabilidade());
        
        int limite = Math.min(5, todosAtivos.getTamanho());
        for (int i = 0; i < limite; i++) {
            Ativo a = todosAtivos.get(i);
            System.out.printf("%d. %s (%s) - Rentabilidade: %.2f%%\n", 
                i+1, a.getNome(), a.getTipo(), a.getRentabilidade());
        }
    }
}