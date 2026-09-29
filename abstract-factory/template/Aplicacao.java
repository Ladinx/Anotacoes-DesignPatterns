package abstractfactory;

public class Aplicacao {
    private final FabricaAbstrata fabrica;

    public Aplicacao(FabricaAbstrata fabrica) {
        this.fabrica = fabrica;
    }

    public void executar() {
        Produto1 produto1 = fabrica.criarProduto1();
        Produto2 produto2 = fabrica.criarProduto2();
        produto1.executar();
        System.out.println("  " + produto2.resumo());
    }
}
