package solid;

public class PagamentoA implements PagamentoReembolsavel {
    public void pagar(double valor) {
        System.out.println("  meio A: " + valor);
    }

    public void reembolsar(double valor) {
        System.out.println("  meio A: estorno de " + valor);
    }
}
