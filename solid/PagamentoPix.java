package solid;

public class PagamentoPix implements Pagamento {
    public void pagar(double valor) {
        System.out.println("  pix enviado " + valor);
    }
}
