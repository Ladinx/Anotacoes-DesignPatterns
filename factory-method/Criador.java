package factorymethod;

public abstract class Criador {
    public final Produto criar() {
        Produto produto = criarProduto();
        preparar(produto);
        return produto;
    }

    protected abstract Produto criarProduto();

    protected void preparar(Produto produto) {
        System.out.println("preparando " + produto.nome());
    }
}
