package checkout;

public class FabricaBrasil implements FabricaCheckout {
    private final boolean usarPix;

    public FabricaBrasil(boolean usarPix) {
        this.usarPix = usarPix;
    }

    public DocumentoFiscal criarDocumentoFiscal() {
        return new NotaFiscalEletronica();
    }

    public ProcessadorPagamento criarProcessadorPagamento() {
        return usarPix ? new PagamentoPix() : new PagamentoBoleto();
    }

    public GeradorEtiqueta criarGeradorEtiqueta() {
        return new EtiquetaCorreios();
    }
}
