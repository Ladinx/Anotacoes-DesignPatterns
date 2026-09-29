package observer;

public class BarramentoEventos extends Assunto {
    public void publicar(Evento evento) {
        System.out.println(getClass().getSimpleName() + ": publica " + evento.descrever());
        notificarTodos(evento);
    }
}
