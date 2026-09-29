package factorymethod;

public class CriadorJogo extends Criador {
    protected Produto criarProduto() {
        return new Jogo();
    }
}
