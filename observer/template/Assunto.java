package observer;

import java.util.ArrayList;
import java.util.List;

public abstract class Assunto {
    private final List<Observador> observadores = new ArrayList<>();

    public void assinar(Observador observador) {
        observadores.add(observador);
    }

    public void cancelar(Observador observador) {
        observadores.remove(observador);
    }

    public void notificarTodos(Evento evento) {
        for (Observador observador : new ArrayList<>(observadores)) {
            observador.notificar(evento);
        }
    }
}
