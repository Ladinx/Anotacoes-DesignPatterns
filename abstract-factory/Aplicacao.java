package abstractfactory;

public class Aplicacao {
    private final FabricaUi fabrica;

    public Aplicacao(FabricaUi fabrica) {
        this.fabrica = fabrica;
    }

    public void desenhar() {
        fabrica.criarBotao().pintar();
        fabrica.criarCaixa().pintar();
    }
}
