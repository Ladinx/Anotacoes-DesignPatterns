package observer;

public class ObservadorA implements Observador {
    public void notificar(Evento evento) {
        System.out.println("  observador A: " + evento.descrever());
    }
}
