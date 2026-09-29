# Como usar o template

Copie os arquivos para o seu projeto, troque o pacote e renomeie as classes conforme a tabela. O código
compila e roda como está.

```bash
javac -d out *.java
java -cp out solid.Main
```

## Tabela de renomeação

| Placeholder | Vira | Exemplo |
|---|---|---|
| `Item`, `Pedido` | dados da operação | `Item`, `Pedido` |
| `Regra` | abstração do que varia no cálculo | `Desconto` |
| `RegraNenhuma` | uma regra | `SemDesconto` |
| `RegraPercentual` | uma regra | `DescontoPercentual` |
| `RegraValorFixo` | uma regra | `DescontoFixo` |
| `Validador` | classe de validação | `Validador` |
| `Pagamento` | abstração do meio de pagamento | `Pagamento` |
| `PagamentoReembolsavel` | capacidade extra em subinterface | `PagamentoReembolsavel` |
| `PagamentoA`, `PagamentoB` | meios que reembolsam | `PagamentoBoleto`, `PagamentoCartao` |
| `PagamentoC` | meio que só paga | `PagamentoPix` |
| `NotificadorA`, `NotificadorB` | abstrações de canal | `NotificadorEmail`, `NotificadorSms` |
| `ServicoNotificadorA`, `ServicoNotificadorB` | implementação do canal | `ServicoEmail`, `ServicoSms` |
| `ServicoOperacao` | classe que orquestra | `CheckoutService` |
| `Main` | ponto de composição | `Main` |

## Onde cada princípio aparece

| Princípio | Onde mexer |
|---|---|
| S, responsabilidade única | `Validador` só valida, `ServicoOperacao` só orquestra |
| O, aberto/fechado | `Regra` e suas implementações, sem `if` no serviço |
| L, substituição de Liskov | `PagamentoC` só paga, reembolso mora em `PagamentoReembolsavel` |
| I, segregação de interfaces | `NotificadorA` e `NotificadorB` separadas, uma cada |
| D, inversão de dependência | `ServicoOperacao` recebe abstrações pelo construtor |

## O que ajustar em cada projeto

1. `Regra` é o ponto aberto: regra nova entra como classe nova, e o serviço continua igual.
2. Se um meio de pagamento não devolve nada, ele implementa só `Pagamento`, e o `instanceof` no `Main`
   cuida do reembolso.
3. Canais de notificação novos viram interface nova, e o construtor do serviço recebe mais uma
   abstração.
