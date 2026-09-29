package solid;

public class ServicoEmail implements NotificadorEmail {
    public void email(String mensagem) {
        System.out.println("  email: " + mensagem);
    }
}
