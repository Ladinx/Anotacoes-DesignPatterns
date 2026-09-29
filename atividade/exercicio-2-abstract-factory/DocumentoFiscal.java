package checkout;

public interface DocumentoFiscal {
    String nome();

    String emitir(Pedido pedido);
}
