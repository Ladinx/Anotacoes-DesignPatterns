package factorymethod;

public class CriadorJogoPromocional extends CriadorJogo {
    @Override
    protected void preparar(Produto produto) {
        System.out.println("preparando " + produto.nome() + " com cupom");
        produto.aplicarCupom(50);
    }
}
