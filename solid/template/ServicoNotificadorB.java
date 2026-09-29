package solid;

public class ServicoNotificadorB implements NotificadorB {
    public void enviarB(String mensagem) {
        System.out.println("  canal B: " + mensagem);
    }
}
