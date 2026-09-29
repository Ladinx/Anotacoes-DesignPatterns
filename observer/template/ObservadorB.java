package observer;

public class ObservadorB implements Observador {
    public void notificar(Evento evento) {
        if (evento instanceof Evento1 evento1) {
            System.out.println("  observador B: so reage a evento 1, " + evento1.campo1());
        }
    }
}
