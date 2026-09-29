package factorymethod;

public class Produto2 extends ProdutoAbstrato {
    public Produto2() {
        super("produto 2", 200.00);
    }

    public String descricaoAdicional() {
        return "variante 2";
    }
}
