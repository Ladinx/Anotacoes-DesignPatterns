package checkout;

public class FabricaEstadosUnidos implements FabricaCheckout {
    public DocumentoFiscal criarDocumentoFiscal() {
        return new SalesInvoice();
    }

    public ProcessadorPagamento criarProcessadorPagamento() {
        return new PagamentoCartao();
    }

    public GeradorEtiqueta criarGeradorEtiqueta() {
        return new EtiquetaUsps();
    }
}
