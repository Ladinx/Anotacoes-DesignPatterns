package factorymethod;

public class Jogo extends Produto {
    public Jogo() {
        super(199.90);
    }

    public String nome() {
        return "jogo";
    }
}
