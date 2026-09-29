# Como usar o template

Copie os arquivos para o seu projeto, troque o pacote e renomeie as classes conforme a tabela. O
código compila e roda como está, então dá para testar antes de adaptar.

```bash
javac -d out *.java
java -cp out observer.Main
```

## Tabela de renomeação

| Placeholder | Vira | Exemplo |
|---|---|---|
| `Evento` | abstração do evento do seu domínio | `Mensagem` |
| `Evento1`, `Evento2` | eventos concretos | `MensagemRecebida`, `ArquivoLido` |
| `Observador` | interface do observador | `Ouvinte` |
| `ObservadorA`, `ObservadorB` | observadores concretos | `Log`, `Contador` |
| `Assunto` | classe abstrata que guarda a lista | `Barramento` |
| `AssuntoConcreto` | quem dispara o evento | `CentralEventos` |
| `Main` | ponto de composição | `Main` |

## O que ajustar em cada projeto

1. Campos de `Evento1` e `Evento2` viram os dados que o seu reator precisa.
2. O `println` de cada observador vira a reação real.
3. `ObservadorB` mostra o filtro por tipo de evento, com `instanceof`.
4. Se o seu emissor tiver nome de domínio, ele é o `AssuntoConcreto`.
