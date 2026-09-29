package checkout;

public interface ProcessadorPagamento {
    String nome();

    String processar(Pedido pedido);
}
