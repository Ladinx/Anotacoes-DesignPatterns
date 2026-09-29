package checkout;

public class ServicoCheckout {
    private final FabricaCheckout fabrica;

    public ServicoCheckout(FabricaCheckout fabrica) {
        this.fabrica = fabrica;
    }

    public Relatorio finalizar(Pedido pedido) {
        DocumentoFiscal fiscal = fabrica.criarDocumentoFiscal();
        ProcessadorPagamento pagamento = fabrica.criarProcessadorPagamento();
        GeradorEtiqueta etiqueta = fabrica.criarGeradorEtiqueta();
        return new Relatorio(
                pedido.id(),
                fiscal.nome(),
                fiscal.emitir(pedido),
                pagamento.processar(pedido),
                etiqueta.gerar(pedido));
    }
}
