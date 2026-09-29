package factorymethod;

public class Livro extends Produto {
    public Livro() {
        super(49.90);
    }

    public String nome() {
        return "livro";
    }
}
