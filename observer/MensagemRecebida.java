package observer;

public record MensagemRecebida(String remetente, String texto) implements Evento {
    public String descrever() {
        return "mensagem de " + remetente + ": " + texto;
    }
}
