package solid;

public class DescontoPercentual implements Desconto {
    private final double percentual;

    public DescontoPercentual(double percentual) {
        this.percentual = percentual;
    }

    public double aplicar(double total) {
        return total * (1 - percentual);
    }
}
