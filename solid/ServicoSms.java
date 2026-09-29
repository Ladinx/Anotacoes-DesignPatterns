package solid;

public class ServicoSms implements NotificadorSms {
    public void sms(String mensagem) {
        System.out.println("  sms: " + mensagem);
    }
}
