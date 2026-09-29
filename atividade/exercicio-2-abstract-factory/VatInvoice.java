package checkout;

public class VatInvoice implements DocumentoFiscal {
    private static final double UST_NORMAL = 0.19;
    private static final double UST_ESSENCIAL = 0.07;
    private static final String VAT_ID = "DE123456789";

    public String nome() {
        return "VAT invoice";
    }

    public String emitir(Pedido pedido) {
        double ust = pedido.essencial() ? UST_ESSENCIAL : UST_NORMAL;
        return String.join("\n",
                "Document: " + nome(),
                "Seller VAT-ID: " + VAT_ID,
                "Umsatzsteuer: " + Moeda.percentual(ust) + (pedido.essencial() ? " (essencial)" : ""),
                "Net: " + Moeda.eur(pedido.valor()),
                "Total: " + Moeda.eur(pedido.valor() * (1 + ust)));
    }
}
