package observer;

public class Main {
    public static void main(String[] args) {
        AssuntoConcreto assunto = new AssuntoConcreto();
        ObservadorA observadorA = new ObservadorA();

        assunto.assinar(observadorA);
        assunto.assinar(new ObservadorB());
        assunto.assinar(evento -> System.out.println("  lambda: " + evento.descrever()));

        assunto.publicar(new Evento1("dado1"));
        assunto.publicar(new Evento2("dado1", "dado2"));

        assunto.cancelar(observadorA);
        assunto.publicar(new Evento1("dado3"));
    }
}
