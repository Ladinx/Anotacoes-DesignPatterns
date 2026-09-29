# Observer do zero

Ordem de implementação. Os nomes são genéricos de propósito: copie e renomeie para o seu domínio.

## Quando usar

Um evento dispara e vários precisam reagir. Sinais no código: um método que chama outros módulos um por
um, um `if` de tipo decidindo quem notificar, ou um componente que conhece todos só para avisá-los.

## Papéis

| Papel | Responsabilidade | Nome aqui |
|---|---|---|
| Subject | guarda a lista e notifica | `Assunto` |
| Concrete Subject | dispara o evento | `Barramento` |
| Observer | reage | `Observador` |
| Concrete Observer | reage de um jeito | `Log` |

## Passo a passo

1. Extraia o evento para um tipo próprio, com os dados que o reator precisa.
2. Crie a interface do observador, com um método que recebe o evento.
3. Crie o Subject com a lista e os métodos `assinar`, `cancelar` e `notificarTodos`.
4. Crie o Concrete Subject, que imprime o evento e delega para `notificarTodos`.
5. Crie um observador concreto por reação.
6. Ligue as assinaturas no ponto de composição.

## Código

```java
import java.util.ArrayList;
import java.util.List;

interface Evento {
    String descrever();
}

record EventoX(String campo) implements Evento {
    public String descrever() {
        return "x: " + campo;
    }
}

interface Observador {
    void notificar(Evento evento);
}

abstract class Assunto {
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

class Barramento extends Assunto {
    public void publicar(Evento evento) {
        System.out.println("publica " + evento.descrever());
        notificarTodos(evento);
    }
}

class Log implements Observador {
    public void notificar(Evento evento) {
        System.out.println("  log: " + evento.descrever());
    }
}
```

Uso:

```java
Barramento bus = new Barramento();
Log log = new Log();
bus.assinar(log);
bus.assinar(e -> System.out.println("  webhook: " + e.descrever()));

bus.publicar(new EventoX("ana"));
bus.cancelar(log);
bus.publicar(new EventoX("bob"));
```

Cada tipo vai para o seu arquivo, pelo nome da classe pública.

## Antes de terminar

- [ ] O Subject depende de `Observador`, e a classe concreta fica em outro arquivo
- [ ] A notificação percorre cópia da lista, para permitir cancelamento durante o evento
- [ ] A interface do observador tem um método
- [ ] Guarde a referência do observador em uma variável para conseguir cancelar
