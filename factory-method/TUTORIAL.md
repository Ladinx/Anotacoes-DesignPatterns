# Factory Method do zero

Ordem de implementação. Os nomes são genéricos de propósito: copie e renomeie para o seu domínio.

## Pré-requisito

Já existe uma hierarquia de produtos, com uma abstração e subclasses. Sem hierarquia, o problema é
Simple Factory: uma classe com `switch` estático.

## Quando usar

Um componente decide qual objeto concreto criar. Sinais: `switch` ou cadeia de `if` escolhendo subclasse
em vários lugares, `new Concreto` espalhado, ou um método que precisa de um objeto e ainda não sabe
qual variante vai usar.

## Papéis

| Papel | Responsabilidade | Nome aqui |
|---|---|---|
| Product | abstração comum | `Produto` |
| Concrete Product | variante | `Livro`, `Jogo` |
| Creator | declara o método fábrica | `Criador` |
| Concrete Creator | devolve o seu produto | `CriadorLivro` |
| Seletor | escolhe o creator pela chave pedida | `FabricaCriadores` |

## Passo a passo

1. Coloque na abstração de produto o que todas as variantes compartilham, e declare o que varia.
2. Crie a classe abstrata de creator com o método fábrica abstrato, `criarProduto()`.
3. Coloque o algoritmo genérico em um método concreto e final da base, `criar()`, que chama o método
   fábrica e depois o gancho `preparar()`.
4. Crie um creator por variante, sobrescrevendo só o método fábrica.
5. Centralize a escolha de qual creator usar em um seletor, de preferência um `Map` imutável.
6. No cliente, peça o creator e trate a abstração `Criador`.

## Código

```java
import java.util.Locale;

abstract class Produto {
    private double preco;
    private boolean comCupom;

    protected Produto(double preco) {
        this.preco = preco;
    }

    public abstract String nome();

    public void aplicarCupom(double valor) {
        this.preco -= valor;
        this.comCupom = true;
    }

    public String descrever() {
        return nome() + " por " + String.format(Locale.ROOT, "%.2f", preco) + (comCupom ? " com cupom" : "");
    }
}

class Livro extends Produto {
    public Livro() {
        super(49.90);
    }

    public String nome() {
        return "livro";
    }
}

class Jogo extends Produto {
    public Jogo() {
        super(199.90);
    }

    public String nome() {
        return "jogo";
    }
}

abstract class Criador {
    public final Produto criar() {
        Produto produto = criarProduto();
        preparar(produto);
        return produto;
    }

    protected abstract Produto criarProduto();

    protected void preparar(Produto produto) {
        System.out.println("preparando " + produto.nome());
    }
}

class CriadorLivro extends Criador {
    protected Produto criarProduto() {
        return new Livro();
    }
}

class CriadorJogo extends Criador {
    protected Produto criarProduto() {
        return new Jogo();
    }
}
```

Seletor:

```java
import java.util.Map;

public final class FabricaCriadores {
    private static final Map<String, Criador> REGISTRO = Map.of(
            "livro", new CriadorLivro(),
            "jogo", new CriadorJogo());

    private FabricaCriadores() {
    }

    public static Criador para(String tipo) {
        Criador criador = REGISTRO.get(tipo);
        if (criador == null) {
            throw new IllegalArgumentException("tipo invalido: " + tipo);
        }
        return criador;
    }
}
```

Cada tipo vai para o seu arquivo, pelo nome da classe pública.

## Antes de terminar

- [ ] O método fábrica é abstrato e vive nas subclasses de creator
- [ ] O algoritmo está em um método concreto e final da base
- [ ] Nenhum `new` de produto concreto fora do creator correspondente
- [ ] O cliente escolhe creator pelo seletor, sem `if` por tipo
