package observer;

public class AssuntoConcreto extends Assunto {
    public void publicar(Evento evento) {
        System.out.println("publica " + evento.descrever());
        notificarTodos(evento);
    }
}
