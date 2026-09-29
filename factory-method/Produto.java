package factorymethod;

import java.util.Locale;

public abstract class Produto {
    private double preco;
    private boolean comCupom;

    protected Produto(double preco) {
        this.preco = preco;
    }

    public abstract String nome();

    public void aplicarCupom(double valor) {
        this.preco -= valor;
        this.comCupom = true;
    }

    public double preco() {
        return preco;
    }

    public String descrever() {
        return nome() + " por " + String.format(Locale.ROOT, "%.2f", preco) + (comCupom ? " com cupom" : "");
    }
}
