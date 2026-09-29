package solid;

public class RegraPercentual implements Regra {
    private final double percentual;

    public RegraPercentual(double percentual) {
        this.percentual = percentual;
    }

    public double aplicar(double total) {
        return total * (1 - percentual);
    }
}
