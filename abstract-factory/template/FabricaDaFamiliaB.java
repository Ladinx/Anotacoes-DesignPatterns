package abstractfactory;

public class FabricaDaFamiliaB implements FabricaAbstrata {
    public Produto1 criarProduto1() {
        return new Produto1DaFamiliaB();
    }

    public Produto2 criarProduto2() {
        return new Produto2DaFamiliaB();
    }
}
