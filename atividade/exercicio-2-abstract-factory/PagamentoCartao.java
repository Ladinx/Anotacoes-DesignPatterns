package checkout;

public class PagamentoCartao implements ProcessadorPagamento {
    public String nome() {
        return "Credit card";
    }

    public String processar(Pedido pedido) {
        boolean avsAprovado = pedido.cepEntrega().equalsIgnoreCase(pedido.cepCobranca());
        return String.join("\n",
                "Meio: " + nome(),
                "AVS (billing zip " + pedido.cepCobranca() + "): " + (avsAprovado ? "aprovado" : "recusado"),
                "Cobrado: " + Moeda.usd(pedido.valor()));
    }
}
