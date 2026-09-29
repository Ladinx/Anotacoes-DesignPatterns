package observer;

public record ArquivoLido(String caminho) implements Evento {
    public String descrever() {
        return "arquivo lido: " + caminho;
    }
}
