# Abstract Factory do zero

Ordem de implementação. Os nomes são genéricos de propósito: copie e renomeie para o seu domínio.

## Quando usar

Precisa criar uma família de objetos que têm de combinar entre si. Sinais: dois ou mais produtos que
variam sempre juntos, como país, tema ou integração, e risco de combinação inválida, tipo documento
fiscal de um país com etiqueta de envio de outro. Produto que varia sozinho é caso de Factory Method.

## Papéis

| Papel | Responsabilidade | Nome aqui |
|---|---|---|
| Abstract Factory | um método por produto da família | `FabricaUi` |
| Concrete Factory | devolve a mesma variante em todos os métodos | `FabricaUiClara` |
| Product | abstração de um item | `Botao` |
| Concrete Product | variante do item | `BotaoClaro` |
| Seletor | escolhe a variante pedida | `Tema`, `FabricaUis` |
| Client | consome a família inteira | `Aplicacao` |

## Passo a passo

1. Liste os produtos que formam a família. O que não varia junto fica fora da fábrica.
2. Defina uma interface por produto, contendo o que o cliente usa.
3. Defina a interface da fábrica, com um método por produto.
4. Implemente uma fábrica por variante e confira método por método que todos devolvem a mesma variante.
5. Injete a fábrica no cliente pelo construtor.
6. Centralize a escolha da variante num seletor, de preferência um `enum` com switch exaustivo.

## Código

```java
interface Botao {
    void pintar();
}

interface Caixa {
    void pintar();
}

interface FabricaUi {
    Botao criarBotao();

    Caixa criarCaixa();
}

class FabricaUiClara implements FabricaUi {
    public Botao criarBotao() {
        return new BotaoClaro();
    }

    public Caixa criarCaixa() {
        return new CaixaClara();
    }
}

class FabricaUiEscura implements FabricaUi {
    public Botao criarBotao() {
        return new BotaoEscuro();
    }

    public Caixa criarCaixa() {
        return new CaixaEscura();
    }
}

class BotaoClaro implements Botao {
    public void pintar() {
        System.out.println("[claro] botao");
    }
}

class BotaoEscuro implements Botao {
    public void pintar() {
        System.out.println("[escuro] botao");
    }
}

class CaixaClara implements Caixa {
    public void pintar() {
        System.out.println("[claro] caixa");
    }
}

class CaixaEscura implements Caixa {
    public void pintar() {
        System.out.println("[escuro] caixa");
    }
}

class Aplicacao {
    private final FabricaUi fabrica;

    public Aplicacao(FabricaUi fabrica) {
        this.fabrica = fabrica;
    }

    public void desenhar() {
        fabrica.criarBotao().pintar();
        fabrica.criarCaixa().pintar();
    }
}
```

Seletor:

```java
enum Tema {
    CLARO,
    ESCURO
}

public final class FabricaUis {
    private FabricaUis() {
    }

    public static FabricaUi porTema(Tema tema) {
        return switch (tema) {
            case CLARO -> new FabricaUiClara();
            case ESCURO -> new FabricaUiEscura();
        };
    }
}
```

Cada tipo vai para o seu arquivo, pelo nome da classe pública.

## Antes de terminar

- [ ] Existe uma interface de produto por item da família
- [ ] A interface da fábrica tem um método por produto
- [ ] Cada fábrica concreta devolve a mesma variante em todos os métodos
- [ ] O cliente recebe a fábrica pelo construtor e não menciona variantes
- [ ] A combinação inválida foi tentada e o compilador recusou
