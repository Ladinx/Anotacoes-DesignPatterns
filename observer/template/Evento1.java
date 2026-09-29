package observer;

public record Evento1(String campo1) implements Evento {
    public String descrever() {
        return "evento1: " + campo1;
    }
}
