package solid;

public class ServicoNotificadorA implements NotificadorA {
    public void enviarA(String mensagem) {
        System.out.println("  canal A: " + mensagem);
    }
}
