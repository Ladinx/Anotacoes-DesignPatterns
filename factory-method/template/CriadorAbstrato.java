package factorymethod;

public abstract class CriadorAbstrato {
    public final ProdutoAbstrato criar() {
        ProdutoAbstrato produto = criarProduto();
        preparar(produto);
        return produto;
    }

    protected abstract ProdutoAbstrato criarProduto();

    protected void preparar(ProdutoAbstrato produto) {
        System.out.println("preparando " + produto.descrever());
    }
}
