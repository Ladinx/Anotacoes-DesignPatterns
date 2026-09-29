package solid;

public class PagamentoCartao implements PagamentoReembolsavel {
    public void pagar(double valor) {
        System.out.println("  cartao cobrado " + valor);
    }

    public void reembolsar(double valor) {
        System.out.println("  cartao estornado " + valor);
    }
}
