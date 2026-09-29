package checkout;

public class PagamentoPix implements ProcessadorPagamento {
    private static final double DESCONTO = 0.05;

    public String nome() {
        return "Pix";
    }

    public String processar(Pedido pedido) {
        return String.join("\n",
                "Meio: " + nome(),
                "Desconto: " + Moeda.percentual(DESCONTO),
                "Cobrado: " + Moeda.brl(pedido.valor() * (1 - DESCONTO)));
    }
}
