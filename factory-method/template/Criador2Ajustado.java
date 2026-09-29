package factorymethod;

public class Criador2Ajustado extends Criador2 {
    @Override
    protected void preparar(ProdutoAbstrato produto) {
        System.out.println("preparando com ajuste");
        produto.ajustarPreco(0.9);
    }
}
