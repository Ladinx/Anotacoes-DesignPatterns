package abstractfactory;

public class FabricaUiEscura implements FabricaUi {
    public Botao criarBotao() {
        return new BotaoEscuro();
    }

    public Caixa criarCaixa() {
        return new CaixaEscura();
    }
}
