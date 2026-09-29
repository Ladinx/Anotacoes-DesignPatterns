package factorymethod;

public class CriadorLivro extends Criador {
    protected Produto criarProduto() {
        return new Livro();
    }
}
