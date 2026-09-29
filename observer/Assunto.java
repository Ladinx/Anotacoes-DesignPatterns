package observer;

import java.util.ArrayList;
import java.util.List;

public abstract class Assunto {
    private final List<Observador> observadores = new ArrayList<>();

    public void assinar(Observador observador) {
        observadores.add(observador);
        System.out.println(getClass().getSimpleName() + ": assinantes=" + observadores.size());
    }

    public void cancelar(Observador observador) {
        observadores.remove(observador);
        System.out.println(getClass().getSimpleName() + ": assinantes=" + observadores.size());
    }

    public void notificarTodos(Evento evento) {
        for (Observador observador : new ArrayList<>(observadores)) {
            observador.notificar(evento);
        }
    }

    public int totalAssinantes() {
        return observadores.size();
    }
}
