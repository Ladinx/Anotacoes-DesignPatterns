package factorymethod;

public class Produto1 extends ProdutoAbstrato {
    public Produto1() {
        super("produto 1", 100.00);
    }

    public String descricaoAdicional() {
        return "variante 1";
    }
}
