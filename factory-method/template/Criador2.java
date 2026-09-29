package factorymethod;

public class Criador2 extends CriadorAbstrato {
    protected ProdutoAbstrato criarProduto() {
        return new Produto2();
    }
}
