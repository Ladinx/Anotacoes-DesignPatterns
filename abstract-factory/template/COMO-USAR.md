# Como usar o template

Copie os arquivos para o seu projeto, troque o pacote e renomeie as classes conforme a tabela. O código
compila e roda como está.

```bash
javac -d out *.java
java -cp out abstractfactory.Main
```

## Tabela de renomeação

| Placeholder | Vira | Exemplo |
|---|---|---|
| `Produto1`, `Produto2` | abstrações dos itens da família | `Botao`, `Caixa` |
| `executar()`, `resumo()` | o que o cliente usa de cada item | `pintar()` |
| `Produto1DaFamiliaA` | item 1 da família A | `BotaoClaro` |
| `Produto2DaFamiliaA` | item 2 da família A | `CaixaClara` |
| `Produto1DaFamiliaB` | item 1 da família B | `BotaoEscuro` |
| `Produto2DaFamiliaB` | item 2 da família B | `CaixaEscura` |
| `FabricaAbstrata` | interface da fábrica | `FabricaUi` |
| `criarProduto1()`, `criarProduto2()` | um método por item | `criarBotao()`, `criarCaixa()` |
| `FabricaDaFamiliaA` | fábrica da família A | `FabricaUiClara` |
| `FabricaDaFamiliaB` | fábrica da família B | `FabricaUiEscura` |
| `Familia` | seletor das variantes | `Tema` |
| `SeletorDeFabricas` | fábrica do seletor | `FabricaUis` |
| `Aplicacao` | cliente que consome a família | `Aplicacao` |
| `Main` | ponto de composição | `Main` |

## O que ajustar em cada projeto

1. A família pode ter quantos itens você precisar. Cada item novo ganha interface, duas implementações
   e um método a mais na interface da fábrica.
2. As variantes A e B são o que impede a combinação inválida. Confira método por método que cada
   fábrica devolve a mesma família nos dois itens.
3. `Familia` é o `enum` que o cliente usa para pedir a variante, e o `switch` tem que ser exaustivo.
