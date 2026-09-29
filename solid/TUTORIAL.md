# SOLID do zero

Cada princípio em três partes: sintoma, teste e correção. Os exemplos usam checkout de pedido.

Ordem de aplicação que funciona: D primeiro, porque reorganiza as dependências. Depois S, que separa o
que ficou misturado. Depois O, depois I, e L por último, já que mexer em herança cedo demais costuma
quebrar mais do que resolve.

## S, Responsabilidade Única

Sintoma: uma classe valida, formata, persiste e notifica.

Teste: quantas razões independentes fazem esta classe mudar? Uma é o objetivo, e duas já são duas
responsabilidades.

Correção: extrair cada razão para uma classe própria. O que sobra é o orquestrador, e orquestrar é a
responsabilidade dele.

```java
public class Validador {
    public List<String> erros(Pedido pedido) {
        // devolve a lista de problemas encontrados
    }
}
```

## O, Aberto para extensão, Fechado para modificação

Sintoma: `if (tipo.equals("A")) desconto = ...`, e a cadeia cresce a cada regra nova.

Teste: para atender um caso novo, preciso editar uma classe que já funciona?

Correção: cada regra vira uma implementação da interface, e o fluxo consome a abstração.

```java
public interface Desconto {
    double aplicar(double total);
}

public class DescontoPercentual implements Desconto {
    public double aplicar(double total) {
        return total * 0.9;
    }
}
```

## L, Substituição de Liskov

Sintoma: subclasse lança `UnsupportedOperationException` para um método que o pai promete.

Teste: consigo trocar a subclasse pela classe base sem que o cliente mude de comportamento?

Correção: a hierarquia promete o que todas cumprem. Capacidade extra mora em subinterface.

```java
public interface Pagamento {
    void pagar(double valor);
}

public interface PagamentoReembolsavel extends Pagamento {
    void reembolsar(double valor);
}

public class PagamentoPix implements Pagamento { }                 // paga
public class PagamentoBoleto implements PagamentoReembolsavel { }  // paga e reembolsa
```

O cliente que precisa reembolsa reconhece a capacidade:

```java
if (meio instanceof PagamentoReembolsavel reembolsavel) {
    reembolsavel.reembolsar(pedido.total());
}
```

## I, Segregação de Interfaces

Sintoma: interface com oito métodos, e implementações com seis deles lançando exceção.

Teste: meu cliente depende de métodos que não usa?

Correção: uma interface por cliente, com o método que ele usa.

```java
public interface NotificadorEmail {
    void email(String mensagem);
}

public interface NotificadorSms {
    void sms(String mensagem);
}
```

## D, Inversão de Dependência

Sintoma: `new ServicoHttp()` dentro do serviço de negócio, com import de framework no meio.

Teste: para testar essa classe, preciso de rede, disco ou banco?

Correção: o serviço declara a abstração que precisa e recebe por construtor. O ponto de composição
escolhe o concreto.

```java
public class CheckoutService {
    private final Pagamento pagamento;     // abstração
    private final NotificadorEmail email;  // abstração

    public CheckoutService(Pagamento pagamento, NotificadorEmail email) {
        this.pagamento = pagamento;
        this.email = email;
    }
}
```

```java
// ponto de composição
var servico = new CheckoutService(new PagamentoBoleto(), new ServicoEmail());
```

## Antes de terminar

- [ ] Cada classe responde a um motivo de mudança só
- [ ] Caso novo entra como classe nova, sem edição de classe que já funcionava
- [ ] Nenhuma subclasse lança exceção para método herdado
- [ ] Nenhuma interface tem método que o cliente não usa
- [ ] Nenhuma classe de negócio instancia dependência concreta
