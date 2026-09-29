package solid;

public class RegraValorFixo implements Regra {
    private final double valor;

    public RegraValorFixo(double valor) {
        this.valor = valor;
    }

    public double aplicar(double total) {
        return Math.max(0, total - valor);
    }
}
