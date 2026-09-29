package solid;

public class PagamentoB implements PagamentoReembolsavel {
    public void pagar(double valor) {
        System.out.println("  meio B: " + valor);
    }

    public void reembolsar(double valor) {
        System.out.println("  meio B: estorno de " + valor);
    }
}
