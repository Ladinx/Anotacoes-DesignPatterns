package checkout;

public interface GeradorEtiqueta {
    String nome();

    String gerar(Pedido pedido);
}
