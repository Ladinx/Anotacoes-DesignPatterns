package solid;

public class PagamentoBoleto implements PagamentoReembolsavel {
    public void pagar(double valor) {
        System.out.println("  boleto gerado para " + valor);
    }

    public void reembolsar(double valor) {
        System.out.println("  boleto estornado para " + valor);
    }
}
