package factorymethod;

import java.util.Locale;

public abstract class ProdutoAbstrato {
    private String nome;
    private double preco;

    protected ProdutoAbstrato(String nome, double preco) {
        this.nome = nome;
        this.preco = preco;
    }

    public abstract String descricaoAdicional();

    public void ajustarPreco(double fator) {
        this.preco *= fator;
    }

    public String descrever() {
        return nome + " (" + descricaoAdicional() + ") por "
                + String.format(Locale.ROOT, "%.2f", preco);
    }
}
