package observer;

public record Evento2(String campo1, String campo2) implements Evento {
    public String descrever() {
        return "evento2: " + campo1 + " / " + campo2;
    }
}
