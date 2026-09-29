package solid;

public class DescontoFixo implements Desconto {
    private final double valor;

    public DescontoFixo(double valor) {
        this.valor = valor;
    }

    public double aplicar(double total) {
        return Math.max(0, total - valor);
    }
}
