package checkout;

import java.util.List;

public record Relatorio(String pedido, String pais, String fiscal, String pagamento, String etiqueta) {
    public String formatar() {
        return String.join("\n",
                "Pedido " + pedido + " (" + pais + ")",
                "",
                fiscal,
                "",
                pagamento,
                "",
                etiqueta);
    }

    public List<String> artefatos() {
        return List.of(fiscal, pagamento, etiqueta);
    }
}
