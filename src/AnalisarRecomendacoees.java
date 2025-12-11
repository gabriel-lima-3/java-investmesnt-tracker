public class AnalisarRecomendacoees {

    private ListaDuplamenteEncadeada <Ativo> todosAtivos;

    public AnalisarRecomendacoees() {

    }
    public AnalisarRecomendacoees(ListaDuplamenteEncadeada <Ativo> todosAtivos) {
        this.todosAtivos = todosAtivos;
    }

    public ListaDuplamenteEncadeada<Ativo> getTodosAtivos() {
        return todosAtivos;
    }

    public void setTodosAtivos(ListaDuplamenteEncadeada<Ativo> todosAtivos) {
        this.todosAtivos = todosAtivos;
    }

}
