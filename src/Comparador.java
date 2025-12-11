import java.util.Comparator;

public class Comparador {

    // Para INSERTION SORT: ordenar por rentabilidade
    public static Comparator<Ativo> porRentabilidade() {
        return new Comparator<Ativo>() {
            @Override
            public int compare(Ativo a1, Ativo a2) {
                // Maior rentabilidade primeiro
                return Double.compare(a2.getRentabilidade(), a1.getRentabilidade());
            }
        };
    }

    // Para MERGE SORT: ordenar por variação acumulada
    public static Comparator<Ativo> porVariacaoAcumulada() {
        return new Comparator<Ativo>() {
            @Override
            public int compare(Ativo a1, Ativo a2) {
                // Maior variação primeiro
                return Double.compare(a2.getVariacaoAcumulada(), a1.getVariacaoAcumulada());
            }
        };
    }

    // Para QUICK SORT: ordenar por valor atual
    public static Comparator<Ativo> porValorAtual() {
        return new Comparator<Ativo>() {
            @Override
            public int compare(Ativo a1, Ativo a2) {
                // Maior valor primeiro
                return Double.compare(a2.getValorAtual(), a1.getValorAtual());
            }
        };
    }

    // Para BUSCA BINÁRIA: ordenar por código
    public static Comparator<Ativo> porCodigo() {
        return new Comparator<Ativo>() {
            @Override
            public int compare(Ativo a1, Ativo a2) {
                return a1.getCodigo().compareTo(a2.getCodigo());
            }
        };
    }

    // Para BUSCA LINEAR por nome
    public static Comparator<Ativo> porNome() {
        return new Comparator<Ativo>() {
            @Override
            public int compare(Ativo a1, Ativo a2) {
                return a1.getNome().compareTo(a2.getNome());
            }
        };
    }

    // Para BUSCA LINEAR por tipo
    public static Comparator<Ativo> porTipo() {
        return new Comparator<Ativo>() {
            @Override
            public int compare(Ativo a1, Ativo a2) {
                return a1.getTipo().compareTo(a2.getTipo());
            }
        };
    }
}