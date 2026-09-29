package checkout;

import java.util.Map;

public class SalesInvoice implements DocumentoFiscal {
    private static final Map<String, Double> ALIQUOTAS = Map.of(
            "CA", 0.0725,
            "TX", 0.0625,
            "OR", 0.0);
    private static final String EIN = "12-3456789";

    public String nome() {
        return "Sales invoice";
    }

    public String emitir(Pedido pedido) {
        String estado = pedido.destino().toUpperCase();
        Double aliquota = ALIQUOTAS.get(estado);
        if (aliquota == null) {
            throw new IllegalArgumentException("estado sem aliquota cadastrada: " + estado);
        }
        double imposto = pedido.valor() * aliquota;
        return String.join("\n",
                "Document: " + nome(),
                "Seller EIN: " + EIN,
                "Ship to state: " + estado,
                "Sales tax: " + Moeda.percentual(aliquota),
                "Subtotal: " + Moeda.usd(pedido.valor()),
                "Total: " + Moeda.usd(pedido.valor() + imposto));
    }
}
