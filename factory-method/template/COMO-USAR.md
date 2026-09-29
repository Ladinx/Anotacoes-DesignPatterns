# Como usar o template

Copie os arquivos para o seu projeto, troque o pacote e renomeie as classes conforme a tabela. O código
compila e roda como está.

```bash
javac -d out *.java
java -cp out factorymethod.Main
```

## Tabela de renomeação

| Placeholder | Vira | Exemplo |
|---|---|---|
| `ProdutoAbstrato` | abstração do produto | `Produto` |
| `Produto1`, `Produto2` | produtos concretos | `Livro`, `Jogo` |
| `descricaoAdicional()` | o que só cada produto faz | `categoria()` |
| `ajustarPreco(fator)` | transformação comum do produto | `aplicarCupom(valor)` |
| `CriadorAbstrato` | classe abstrata do creator | `Criador` |
| `criarProduto()` | o método fábrica | `criarProduto()` |
| `preparar(produto)` | o gancho sobrescrevível | `preparar(produto)` |
| `Criador1`, `Criador2` | creators concretos | `CriadorLivro`, `CriadorJogo` |
| `Criador2Ajustado` | creator que só muda o gancho | `CriadorJogoPromocional` |
| `SeletorDeCriadores` | registro chave para creator | `FabricaCriadores` |
| `Main` | ponto de composição | `Main` |

## O que ajustar em cada projeto

1. Campos de `Produto1` e `Produto2` viram o que distingue as variantes.
2. `descricaoAdicional()` recebe o nome do método que faz sentido no seu domínio.
3. `Criador2Ajustado` mostra o gancho mudando o resultado, e é opcional.
4. Acrescente um `ProdutoN` e um `CriadorN` quando precisar de uma variante nova.
