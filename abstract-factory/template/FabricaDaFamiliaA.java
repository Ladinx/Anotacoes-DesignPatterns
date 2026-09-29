package abstractfactory;

public class FabricaDaFamiliaA implements FabricaAbstrata {
    public Produto1 criarProduto1() {
        return new Produto1DaFamiliaA();
    }

    public Produto2 criarProduto2() {
        return new Produto2DaFamiliaA();
    }
}
