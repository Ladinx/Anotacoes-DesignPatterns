package checkout;

public class PagamentoBoleto implements ProcessadorPagamento {
    private static final int DIAS_COMPENSACAO = 3;

    public String nome() {
        return "Boleto";
    }

    public String processar(Pedido pedido) {
        return String.join("\n",
                "Meio: " + nome(),
                "Compensação: " + DIAS_COMPENSACAO + " dias úteis",
                "Cobrado: " + Moeda.brl(pedido.valor()));
    }
}
