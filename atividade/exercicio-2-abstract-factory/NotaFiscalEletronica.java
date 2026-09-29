package checkout;

public class NotaFiscalEletronica implements DocumentoFiscal {
    private static final double ICMS_INTERESTADUAL = 0.12;
    private static final double ICMS_INTERNO = 0.18;
    private static final String CFOP_INTERNO = "5.102";
    private static final String CFOP_INTERESTADUAL = "6.102";

    public String nome() {
        return "Nota fiscal eletrônica";
    }

    public String emitir(Pedido pedido) {
        double icms = pedido.interestadual() ? ICMS_INTERESTADUAL : ICMS_INTERNO;
        double total = pedido.valor() * (1 + icms);
        return String.join("\n",
                "Documento: " + nome(),
                "Chave de acesso: " + chave(pedido),
                "CFOP: " + (pedido.interestadual() ? CFOP_INTERESTADUAL : CFOP_INTERNO),
                "ICMS: " + Moeda.percentual(icms),
                "Base: " + Moeda.brl(pedido.valor()),
                "Total: " + Moeda.brl(total));
    }

    private String chave(Pedido pedido) {
        long semente = Math.abs((long) pedido.id().hashCode()) * 2654435761L + (long) pedido.valor();
        return String.format("%044d", semente % 9_000_000_000_000_000_000L);
    }
}
