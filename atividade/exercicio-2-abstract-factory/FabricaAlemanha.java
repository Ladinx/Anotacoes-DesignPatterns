package checkout;

public class FabricaAlemanha implements FabricaCheckout {
    public DocumentoFiscal criarDocumentoFiscal() {
        return new VatInvoice();
    }

    public ProcessadorPagamento criarProcessadorPagamento() {
        return new PagamentoSepa();
    }

    public GeradorEtiqueta criarGeradorEtiqueta() {
        return new EtiquetaDeutschePost();
    }
}
