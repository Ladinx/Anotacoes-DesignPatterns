package factorymethod;

public class Criador1 extends CriadorAbstrato {
    protected ProdutoAbstrato criarProduto() {
        return new Produto1();
    }
}
