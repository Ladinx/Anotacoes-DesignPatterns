package observer;

public class Main {
    public static void main(String[] args) {
        BarramentoEventos bus = new BarramentoEventos();
        ObservadorAuditoria auditoria = new ObservadorAuditoria("auditoria.log");
        ContadorEventos contador = new ContadorEventos();

        bus.assinar(auditoria);
        bus.assinar(contador);
        bus.assinar(new NotificadorMensagem());
        bus.assinar(evento -> System.out.println("  webhook: " + evento.descrever()));

        bus.publicar(new MensagemRecebida("ana", "oi"));
        bus.publicar(new ArquivoLido("app.log"));

        bus.cancelar(auditoria);
        bus.publicar(new MensagemRecebida("bob", "tchau"));

        System.out.println("contador recebeu " + contador.total() + " eventos");
    }
}
