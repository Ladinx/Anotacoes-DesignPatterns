package observer;

public class ContadorEventos implements Observador {
    private int total;

    public void notificar(Evento evento) {
        total++;
        System.out.println("  contador: " + total + " (" + evento.descrever() + ")");
    }

    public int total() {
        return total;
    }
}
