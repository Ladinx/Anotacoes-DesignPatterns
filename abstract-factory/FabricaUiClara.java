package abstractfactory;

public class FabricaUiClara implements FabricaUi {
    public Botao criarBotao() {
        return new BotaoClaro();
    }

    public Caixa criarCaixa() {
        return new CaixaClara();
    }
}
