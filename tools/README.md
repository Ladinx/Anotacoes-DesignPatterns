# Gerador de diagrama de classes

Script que lê os arquivos `.java` de uma pasta e escreve o diagrama de classes. Funciona sem internet:
o `.svg` é desenhado pelo próprio script, sem PlantUML, sem Graphviz e sem navegador.

## Uso

```bash
python3 tools/gerar-diagrama.py observer
python3 tools/gerar-diagrama.py solid --png
python3 tools/gerar-diagrama.py observer/template --titulo "observer (template)"
```

Cada execução escreve dois arquivos na pasta alvo:

| Arquivo | Para que serve |
|---|---|
| `diagrama.puml` | texto PlantUML, para abrir em IDE, wiki ou ferramenta de UML |
| `diagrama.svg` | o desenho pronto, que qualquer navegador abre |

Com `--png`, ele tenta converter o SVG em PNG usando o primeiro conversor que encontrar: `rsvg-convert`,
`inkscape` ou `chromium`. Sem nenhum deles, o SVG continua valendo.

## Opções

| Opção | Efeito |
|---|---|
| `--titulo T` | escreve o título no topo do desenho |
| `--saida ARQ` | muda o caminho do `.puml` e do `.svg` |
| `--png` | tenta gerar o PNG também |
| `--sem-svg` | escreve só o `.puml` |
| `--sem-metodos` | mostra apenas os campos, o que deixa o desenho menor |
| `--sem-main` | ignora a classe `Main`, tirando o ruído das linhas de instanciação |
| `--com-dependencias` | inclui seta para os tipos citados nas assinaturas de método |
| `--recursivo` | entra nas subpastas, útil para `template/` |

## O que ele lê

- o tipo declarado, se é `class`, `interface`, `record` ou `enum`, e se é abstrato
- `extends` e `implements`
- campos do corpo da classe, com multiplicidade quando o tipo é `List`, `Set`, `Map` e afins
- componentes de `record` e constantes de `enum`
- métodos, marcando os abstratos
- instâncias criadas com `new X()`
- com `--com-dependencias`, os tipos citados nos parâmetros dos métodos

## O que ele desenha

| Seta | Significado |
|---|---|
| contínua, triângulo vazio | herança ou implementação de classe |
| tracejada, triângulo vazio | implementação de interface |
| contínua, losango | composição por campo |
| contínua, seta aberta | associação por campo, com a multiplicidade |
| tracejada, seta aberta | dependência ou `new X()` dentro de um método |

## Limites conhecidos

O desenho é gerado por colunas de dependência, então classes sem relação entre si ficam empilhadas, e
setas de retorno podem cruzar o desenho. Para um diagrama de entrega, com anotação de regra de negócio
em cada classe, o `diagrama.puml` escrito à mão continua sendo a melhor opção, e este script serve para
conferi-lo.
