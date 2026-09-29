package observer;

public class ObservadorAuditoria implements Observador {
    private final String destino;

    public ObservadorAuditoria(String destino) {
        this.destino = destino;
    }

    public void notificar(Evento evento) {
        System.out.println("  auditoria -> " + destino + " | " + evento.descrever());
    }
}
