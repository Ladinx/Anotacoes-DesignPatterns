package observer;

public class NotificadorMensagem implements Observador {
    public void notificar(Evento evento) {
        if (evento instanceof MensagemRecebida mensagem) {
            System.out.println("  notificacao: " + mensagem.remetente() + " -> " + mensagem.texto());
        }
    }
}
