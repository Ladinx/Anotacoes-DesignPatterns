package checkout;

public class PagamentoSepa implements ProcessadorPagamento {
    public String nome() {
        return "SEPA Direct Debit";
    }

    public String processar(Pedido pedido) {
        return String.join("\n",
                "Meio: " + nome(),
                "IBAN: DE00 0000 0000 0000 0000 00",
                "Cobrado: " + Moeda.eur(pedido.valor()));
    }
}
