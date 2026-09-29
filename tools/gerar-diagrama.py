#!/usr/bin/env python3
"""Gera o diagrama de classes de uma pasta de arquivos .java, sem depender de rede.

Saidas:
    <pasta>/diagrama.puml   texto PlantUML, para abrir em qualquer ferramenta
    <pasta>/diagrama.svg    desenho pronto, gerado pelo proprio script

O que e lido de cada arquivo:
    - o tipo declarado (class, interface, record, enum), se abstrato, e sua visibilidade
    - extends e implements
    - campos do corpo da classe, com multiplicidade a partir de List, Set, Map e afins
    - componentes de record e constantes de enum
    - metodos, marcando os abstratos
    - instancias criadas com new X()
    - opcionalmente, os tipos citados nas assinaturas dos metodos

Para gerar PNG tambem, e preciso ter um conversor local (rsvg-convert, inkscape ou chromium):
    python3 tools/gerar-diagrama.py solid --png
"""

from __future__ import annotations

import argparse
import html
import pathlib
import re
import shutil
import subprocess
import sys

COLETIVOS = {"List", "Set", "Collection", "Iterable", "Queue", "Deque", "Map", "SortedMap", "SortedSet", "Stream"}
IGNORADOS = {"String", "Object", "Integer", "Double", "Long", "Boolean", "LocalDate", "AtomicInteger", "Locale", "Math"}

COMENTARIOS_BLOQUE = re.compile(r"/\*.*?\*/", re.S)
COMENTARIOS_LINHA = re.compile(r"//[^\n]*")
PACOTE = re.compile(r"^\s*package\s+([\w.]+)\s*;", re.M)
MODIFICADORES = (
    r"(?:public\s+|protected\s+|private\s+|static\s+|final\s+|abstract\s+|strictfp\s+"
    r"|synchronized\s+|native\s+|transient\s+|volatile\s+|default\s+)*"
)
TIPO = re.compile(
    r"^\s*(?:@\w+(?:\([^)]*\))?\s*)*(?P<mods>" + MODIFICADORES + r")"
    r"(?P<kind>class|interface|record|enum)\s+(?P<nome>\w+)"
    r"(?:\s*<[^{]*?>)?\s*(?P<cab>[^{]*)\{",
    re.M,
)
COMPONENTES_RECORD = re.compile(r"record\s+\w+\s*\(([^)]*)\)", re.S)
CAMPOS = re.compile(r"^\s*" + MODIFICADORES + r"([A-Za-z][\w.]*(?:\s*<[^;=]*>)?(?:\[\])?)\s+(\w+)\s*(?:=[^;]*)?;")
METODOS = re.compile(
    r"^\s*" + MODIFICADORES + r"(?:<[^>]+>\s*)?"
    r"([A-Za-z_][\w.]*(?:\s*<[^;{()]+>)?(?:\[\])?)\s+(\w+)\s*\(([^)]*)\)"
    r"(?:\s*throws\s+[\w.,\s]+)?\s*([;{])"
)
INSTANCIA = re.compile(r"new\s+(\w+)\s*[<(]")


class Tipo:
    def __init__(self, nome: str, kind: str, pacote: str, visibilidade: str, abstrato: bool) -> None:
        self.nome = nome
        self.kind = kind
        self.pacote = pacote
        self.visibilidade = visibilidade
        self.abstrato = abstrato
        self.extends: list[str] = []
        self.implements: list[str] = []
        self.campos: list[tuple[str, str]] = []
        self.metodos: list[tuple[str, str, str, bool]] = []
        self.constantes: list[str] = []
        self.instancias: set[str] = set()


def limpar(codigo: str) -> str:
    return COMENTARIOS_LINHA.sub("", COMENTARIOS_BLOQUE.sub("", codigo))


def tipo_simples(bruto: str) -> str:
    return bruto.split("<")[0].strip().split(".")[-1]


def sem_genericos(bruto: str) -> str:
    return re.sub(r"<[^>]*>", "", bruto).strip()


def genericos(bruto: str) -> list[str]:
    interno = re.search(r"<(.+)>$", bruto.strip())
    if not interno:
        return []
    partes, profundidade, atual = [], 0, ""
    for caractere in interno.group(1):
        if caractere == "<":
            profundidade += 1
        elif caractere == ">":
            profundidade -= 1
        if caractere == "," and profundidade == 0:
            partes.append(atual.strip())
            atual = ""
        else:
            atual += caractere
    if atual.strip():
        partes.append(atual.strip())
    return partes


def corpo_da_classe(codigo: str, inicio: int) -> str:
    profundidade, indice, tamanho = 1, inicio, len(codigo)
    while indice < tamanho and profundidade > 0:
        caractere = codigo[indice]
        if caractere == '"':
            indice += 1
            while indice < tamanho and codigo[indice] != '"':
                indice += 2 if codigo[indice] == "\\" else 1
        elif caractere == "'":
            indice += 1
            while indice < tamanho and codigo[indice] != "'":
                indice += 2 if codigo[indice] == "\\" else 1
        elif caractere == "{":
            profundidade += 1
        elif caractere == "}":
            profundidade -= 1
        indice += 1
    return codigo[inicio:indice - 1]


def linhas_do_corpo(corpo: str):
    profundidade = 0
    for linha in corpo.split("\n"):
        if profundidade == 0 and linha.strip():
            yield linha
        profundidade += linha.count("{") - linha.count("}")


def componentes_de_record(codigo: str) -> list[tuple[str, str]]:
    encontrados = COMPONENTES_RECORD.search(codigo)
    if not encontrados:
        return []
    saida = []
    for parte in encontrados.group(1).split(","):
        parte = parte.split("=")[0].strip()
        if not parte:
            continue
        partes = parte.rsplit(" ", 1)
        if len(partes) == 2:
            saida.append((partes[0].strip(), partes[1].strip()))
        else:
            saida.append(("String", partes[0].strip()))
    return saida


def analisar(caminho: pathlib.Path) -> Tipo | None:
    codigo = limpar(caminho.read_text(encoding="utf-8"))
    pacote = PACOTE.search(codigo)
    encontrado = TIPO.search(codigo)
    if not encontrado:
        return None

    mods = encontrado.group("mods")
    visibilidade = "private" if re.search(r"\bprivate\b", mods) else "public"
    abstrato = bool(re.search(r"\babstract\b", mods)) or encontrado.group("kind") == "interface"
    tipo = Tipo(
        encontrado.group("nome"),
        encontrado.group("kind"),
        pacote.group(1) if pacote else "",
        visibilidade,
        abstrato,
    )

    cabecalho = encontrado.group("cab")
    extends = re.search(r"\bextends\s+([\w.<>, ]+?)(?=\s+implements|\s*$)", cabecalho)
    implements = re.search(r"\bimplements\s+([\w.<>, ]+)$", cabecalho)
    if extends:
        tipo.extends = [p.strip() for p in extends.group(1).split(",")]
    if implements:
        tipo.implements = [p.strip() for p in implements.group(1).split(",")]

    corpo = corpo_da_classe(codigo, encontrado.end())

    if tipo.kind == "record":
        tipo.campos.extend(componentes_de_record(codigo))
    if tipo.kind == "enum":
        cabecalho_enum = corpo.split(";")[0].split("(")[0]
        tipo.constantes = [p for p in (q.strip() for q in cabecalho_enum.split(",")) if re.fullmatch(r"[A-Z][A-Z0-9_]*", p)]

    acessores = {nome for _, nome in tipo.campos} if tipo.kind == "record" else set()
    vistos: set[str] = set()
    for linha in linhas_do_corpo(corpo):
        metodo = METODOS.match(linha)
        if metodo:
            retorno, nome, parametros, terminador = metodo.group(1), metodo.group(2), metodo.group(3), metodo.group(4)
            if nome not in (tipo.nome, "equals", "hashCode", "toString") and nome not in acessores:
                tipo.metodos.append(
                    (retorno.strip(), nome, parametros.strip(), tipo.kind == "interface" or terminador == ";")
                )
            continue
        campo = CAMPOS.match(linha)
        if campo:
            tipo_campo, nome = campo.group(1).strip(), campo.group(2)
            if nome in vistos or (nome.isupper() and "<" not in tipo_campo):
                continue
            if tipo_simples(tipo_campo) in IGNORADOS and "<" not in tipo_campo:
                continue
            vistos.add(nome)
            tipo.campos.append((tipo_campo, nome))

    tipo.instancias = set(INSTANCIA.findall(codigo))
    return tipo


def carregar(raiz: pathlib.Path, recursivo: bool, ignorar_main: bool) -> dict[str, Tipo]:
    arquivos = sorted(raiz.rglob("*.java") if recursivo else raiz.glob("*.java"))
    tipos: dict[str, Tipo] = {}
    for arquivo in arquivos:
        if ignorar_main and arquivo.stem == "Main":
            continue
        tipo = analisar(arquivo)
        if tipo:
            tipos[tipo.nome] = tipo
    return tipos


class Relacao:
    def __init__(self, origem: str, destino: str, estilo: str, rotulo: str = "") -> None:
        self.origem = origem
        self.destino = destino
        self.estilo = estilo
        self.rotulo = rotulo


def montar_relacoes(tipos: dict[str, Tipo], com_dependencias: bool) -> list[Relacao]:
    relacoes: list[Relacao] = []
    vistas: set[tuple[str, str, str]] = set()

    def adicionar(origem: str, destino: str, estilo: str, rotulo: str = "") -> None:
        if destino not in tipos or origem == destino:
            return
        chave = (origem, destino, estilo)
        if chave in vistas:
            return
        vistas.add(chave)
        relacoes.append(Relacao(origem, destino, estilo, rotulo))

    for nome, tipo in sorted(tipos.items()):
        for pai in tipo.extends:
            alvo = tipo_simples(pai)
            adicionar(nome, alvo, "heranca" if tipos.get(alvo) and tipos[alvo].kind == "class" else "realizacao")
        for interface in tipo.implements:
            alvo = tipo_simples(interface)
            adicionar(nome, alvo, "heranca" if tipos.get(alvo) and tipos[alvo].kind == "class" else "realizacao")
        for tipo_campo, _ in tipo.campos:
            base = tipo_simples(tipo_campo)
            argumentos = genericos(tipo_campo)
            if base in COLETIVOS and argumentos:
                adicionar(nome, tipo_simples(argumentos[0]), "composicao", "*")
            else:
                adicionar(nome, base, "associacao", "1")
        if com_dependencias:
            for _, nome_metodo, parametros, _ in tipo.metodos:
                for parametro in parametros.split(","):
                    adicionar(nome, tipo_simples(parametro), "dependencia", nome_metodo + "()")
        for instancia in sorted(tipo.instancias):
            adicionar(nome, instancia, "instancia", "<<instancia>>")
    return relacoes


def gerar_puml(tipos: dict[str, Tipo], relacoes: list[Relacao], titulo: str, com_metodos: bool) -> str:
    setas = {
        "heranca": "--|>",
        "realizacao": "..|>",
        "composicao": 'o-- "*"',
        "associacao": '--> "1"',
        "dependencia": "..>",
        "instancia": "..>",
    }
    blocos = []
    for nome, tipo in sorted(tipos.items()):
        prefixo = "abstract " if tipo.abstrato and tipo.kind == "class" else ""
        estereotipo = " <<record>>" if tipo.kind == "record" else ""
        palavra = {"enum": "enum", "interface": "interface"}.get(tipo.kind, "class")
        linhas = [f"  - {nome_campo} : {sem_genericos(nome_tipo)}" for nome_tipo, nome_campo in tipo.campos]
        linhas += [f"  {constante}" for constante in tipo.constantes]
        if com_metodos:
            for retorno, nome_metodo, parametros, eh_abstrato in tipo.metodos:
                marca = " {abstract}" if eh_abstrato else ""
                linhas.append(f"  + {nome_metodo}({parametros}) : {sem_genericos(retorno)}{marca}")
        blocos.append(f"{prefixo}{palavra} {nome}{estereotipo} {{\n" + "\n".join(linhas) + "\n}")

    ligacoes = [f"{r.origem} {setas[r.estilo]} {r.destino}" + (f" : {r.rotulo}" if r.estilo in ("dependencia", "instancia") else "")
                for r in relacoes]
    return "\n".join([
        f"@startuml {titulo}",
        "left to right direction",
        "skinparam backgroundColor transparent",
        "skinparam classAttributeIconSize 0",
        "skinparam shadowing false",
        "",
    ] + blocos + [""] + ligacoes + [
        "",
        "legend right",
        "  | linha contínua | herança e associação |",
        "  | tracejado | dependência e instanciação |",
        "  | multiplicidade | quantidade de referências |",
        "endlegend",
        "@enduml",
        "",
    ])


def nivel_de(tipos: dict[str, Tipo], relacoes: list[Relacao]) -> dict[str, int]:
    entradas: dict[str, list[str]] = {nome: [] for nome in tipos}
    for relacao in relacoes:
        if relacao.estilo in ("heranca", "realizacao", "composicao", "associacao"):
            entradas[relacao.destino].append(relacao.origem)

    niveis: dict[str, int] = {}

    def calcular(nome: str, caminho: frozenset[str]) -> int:
        if nome in niveis:
            return niveis[nome]
        if nome in caminho:
            return 0
        pais = entradas.get(nome, [])
        if not pais:
            niveis[nome] = 0
            return 0
        nivel = 1 + max(calcular(pai, caminho | {nome}) for pai in pais)
        niveis[nome] = nivel
        return nivel

    for nome in sorted(tipos):
        calcular(nome, frozenset())
    return niveis


FONTE_MEMBRO = 11
FONTE_NOME = 13
ALTURA_LINHA = 15
LARGURA_CHAR_MEMBRO = 7.3
LARGURA_CHAR_NOME = 8.1
LARGURA_CHAR_ESTEREOTIPO = 6.0
REPOSICAO_X = 70
REPOSICAO_Y = 45
MARGEM = 24
REPOUSAO_MEMBRO = 24


def medir(linhas_membros: list[str], nome: str, estereotipo: str) -> tuple[int, int, int]:
    largura = max(
        [len(nome) * LARGURA_CHAR_NOME]
        + [len(estereotipo) * LARGURA_CHAR_ESTEREOTIPO if estereotipo else 0]
        + [len(linha) * LARGURA_CHAR_MEMBRO for linha in linhas_membros]
    ) + REPOUSAO_MEMBRO
    altura_cabecalho = 30 + (14 if estereotipo else 0)
    altura = altura_cabecalho + ALTURA_LINHA * len(linhas_membros) + 14
    return int(largura), int(altura), altura_cabecalho


def gerar_svg(tipos: dict[str, Tipo], relacoes: list[Relacao], titulo: str, com_metodos: bool) -> str:
    niveis = nivel_de(tipos, relacoes)
    colunas: dict[int, list[str]] = {}
    for nome, nivel in sorted(niveis.items(), key=lambda par: (par[1], par[0])):
        colunas.setdefault(nivel, []).append(nome)

    medidas: dict[str, tuple[int, int, int]] = {}
    membros: dict[str, list[tuple[str, str]]] = {}
    for nome, tipo in tipos.items():
        estereotipo = "«record»" if tipo.kind == "record" else f"«{tipo.kind}»" if tipo.kind == "enum" else "«interface»" if tipo.kind == "interface" else "«abstract»" if tipo.abstrato else ""
        linhas: list[tuple[str, str]] = []
        if com_metodos:
            linhas += [("-", f"{nome_campo} : {sem_genericos(nome_tipo)}") for nome_tipo, nome_campo in tipo.campos]
            linhas += [("", constante) for constante in tipo.constantes]
            linhas += [("+", f"{nome_metodo}({parametros}) : {sem_genericos(retorno)}" + (" {abstract}" if eh_abstrato else ""))
                       for retorno, nome_metodo, parametros, eh_abstrato in tipo.metodos]
        else:
            linhas += [("-", f"{nome_campo} : {sem_genericos(nome_tipo)}") for nome_tipo, nome_campo in tipo.campos]
            linhas += [("", constante) for constante in tipo.constantes]
        membros[nome] = linhas
        medidas[nome] = medir([texto for _, texto in linhas], nome, estereotipo)

    posicoes: dict[str, tuple[int, int]] = {}
    x = MARGEM
    for nivel in sorted(colunas):
        nomes = colunas[nivel]
        y = MARGEM + 10
        for nome in nomes:
            largura, altura, _ = medidas[nome]
            posicoes[nome] = (x, y)
            y += altura + REPOSICAO_Y
        largura_maxima = max(medidas[nome][0] for nome in nomes)
        x += largura_maxima + REPOSICAO_X

    largura_total = x - REPOSICAO_X + MARGEM
    altura_maxima = MARGEM
    for nome, (px, py) in posicoes.items():
        altura_maxima = max(altura_maxima, py + medidas[nome][1])
    legenda = 4 * 18
    altura_total = altura_maxima + legenda + 20

    partes: list[str] = []
    partes.append(
        f'<svg xmlns="http://www.w3.org/2000/svg" width="{largura_total}" height="{altura_total}" '
        f'viewBox="0 0 {largura_total} {altura_total}" font-family="Helvetica,Arial,sans-serif">'
    )
    partes.append("""<defs>
<marker id="seta" viewBox="0 0 10 10" refX="9" refY="5" markerWidth="9" markerHeight="9" orient="auto-start-reverse">
<path d="M0,1 L9,5 L0,9" fill="none" stroke="#37474F" stroke-width="1.3"/></marker>
<marker id="triangulo" viewBox="0 0 12 12" refX="11" refY="6" markerWidth="13" markerHeight="13" orient="auto-start-reverse">
<path d="M1,1 L11,6 L1,11 z" fill="#ffffff" stroke="#37474F" stroke-width="1.3"/></marker>
<marker id="losango" viewBox="0 0 12 12" refX="11" refY="6" markerWidth="12" markerHeight="12" orient="auto-start-reverse">
<path d="M6,1 L11,6 L6,11 L1,6 z" fill="#ffffff" stroke="#37474F" stroke-width="1.3"/></marker>
</defs>""")
    partes.append(f'<rect width="100%" height="100%" fill="#ffffff"/>')
    partes.append(f'<text x="{MARGEM}" y="{MARGEM}" font-size="15" font-weight="600" fill="#1a202c">{html.escape(titulo)}</text>')

    estilos = {
        "heranca": ("#37474F", "", "triangulo", True),
        "realizacao": ("#37474F", "5,4", "triangulo", True),
        "composicao": ("#37474F", "", "losango", True),
        "associacao": ("#37474F", "", "seta", True),
        "dependencia": ("#607d8b", "5,4", "seta", True),
        "instancia": ("#90a4ae", "5,4", "seta", True),
    }
    for relacao in relacoes:
        if relacao.origem not in posicoes or relacao.destino not in posicoes:
            continue
        x1, y1 = posicoes[relacao.origem]
        l1, a1, cab1 = medidas[relacao.origem]
        x2, y2 = posicoes[relacao.destino]
        l2, a2, _ = medidas[relacao.destino]
        cor, tracejado, marcador, seta = estilos[relacao.estilo]
        if x1 + l1 <= x2:
            inicio, fim = (x1 + l1, y1 + a1 // 2), (x2, y2 + a2 // 2)
            caminho = f"M {inicio[0]},{inicio[1]} L {fim[0]},{fim[1]}"
        elif x2 + l2 <= x1:
            inicio, fim = (x1, y1 + a1 // 2), (x2 + l2, y2 + a2 // 2)
            caminho = f"M {inicio[0]},{inicio[1]} L {fim[0]},{fim[1]}"
        else:
            desvio = max(x1 + l1, x2 + l2) + 26
            inicio, fim = (x1 + l1, y1 + a1 // 2), (x2 + l2, y2 + a2 // 2)
            caminho = (f"M {inicio[0]},{inicio[1]} C {desvio},{inicio[1]} {desvio},{fim[1]} {fim[0]},{fim[1]}")
        partes.append(
            f'<path d="{caminho}" fill="none" stroke="{cor}" stroke-width="1.3"'
            + (f' stroke-dasharray="{tracejado}"' if tracejado else "")
            + f' marker-end="url(#{marcador})"/>'
        )
        if relacao.rotulo and relacao.estilo in ("composicao", "associacao"):
            partes.append(
                f'<text x="{(inicio[0] + fim[0]) / 2:.0f}" y="{(inicio[1] + fim[1]) / 2 - 5:.0f}" '
                f'font-size="10" fill="#455a64" text-anchor="middle">{html.escape(relacao.rotulo)}</text>'
            )

    for nome, tipo in tipos.items():
        px, py = posicoes[nome]
        largura, altura, altura_cabecalho = medidas[nome]
        borda = "#37474F"
        preenchimento = "#eef1f5" if tipo.abstrato or tipo.kind in ("interface", "enum") else "#f7f8fa"
        partes.append(
            f'<g><rect x="{px}" y="{py}" width="{largura}" height="{altura}" rx="6" ry="6" '
            f'fill="{preenchimento}" stroke="{borda}" stroke-width="1.3"/>'
            f'<path d="M {px},{py + altura_cabecalho} L {px + largura},{py + altura_cabecalho}" '
            f'stroke="{borda}" stroke-width="1"/>'
        )
        deslocamento = 16
        estereotipo = "«record»" if tipo.kind == "record" else f"«{tipo.kind}»" if tipo.kind == "enum" else "«interface»" if tipo.kind == "interface" else "«abstract»" if tipo.abstrato else ""
        if estereotipo:
            partes.append(
                f'<text x="{px + largura / 2:.0f}" y="{py + 15}" font-size="10" fill="#546e7a" '
                f'text-anchor="middle" font-style="italic">{html.escape(estereotipo)}</text>'
            )
            deslocamento = 30
        partes.append(
            f'<text x="{px + largura / 2:.0f}" y="{py + deslocamento + 4}" font-size="{FONTE_NOME}" '
            f'font-weight="600" fill="#1a202c" text-anchor="middle">{html.escape(nome)}</text>'
        )
        y = py + altura_cabecalho + 4
        for visibilidade, texto in membros[nome]:
            partes.append(
                f'<text x="{px + 10}" y="{y + ALTURA_LINHA - 4}" font-size="{FONTE_MEMBRO}" '
                f'font-family="DejaVu Sans Mono,Consolas,monospace" fill="#263238">'
                f'{html.escape(visibilidade + " " + texto)}</text>'
            )
            y += ALTURA_LINHA
        partes.append("</g>")

    y_legenda = altura_maxima + 8
    linhas_legenda = [
        "heranca / realizacao : classe, interface",
        "associacao / composicao : campo, com multiplicidade",
        "dependencia / instancia : tracejado, seta aberta",
    ]
    for indice, texto in enumerate(linhas_legenda):
        partes.append(
            f'<text x="{MARGEM}" y="{y_legenda + 16 + indice * 18}" font-size="11" fill="#607d8b">{html.escape(texto)}</text>'
        )
    partes.append("</svg>")
    return "\n".join(partes)


def converter_para_png(svg: pathlib.Path) -> pathlib.Path | None:
    destino = svg.with_suffix(".png")
    if shutil.which("rsvg-convert"):
        subprocess.run(["rsvg-convert", "-z", "2", "-o", str(destino), str(svg)], check=True)
        return destino
    if shutil.which("inkscape"):
        subprocess.run(["inkscape", str(svg), "--export-type=png", "--export-filename", str(destino)], check=True)
        return destino
    chromium = shutil.which("chromium") or shutil.which("chromium-browser") or shutil.which("google-chrome")
    if chromium:
        subprocess.run([
            chromium, "--headless", "--disable-gpu", "--no-sandbox", "--hide-scrollbars",
            f"--screenshot={destino}", f"--window-size=2400,1800", svg.as_uri(),
        ], check=True, capture_output=True)
        return destino if destino.exists() else None
    return None


def main() -> int:
    analisador = argparse.ArgumentParser(description="Gera o diagrama de classes de uma pasta, sem rede")
    analisador.add_argument("pasta", type=pathlib.Path)
    analisador.add_argument("--titulo", help="titulo do diagrama (padrao: nome da pasta)")
    analisador.add_argument("--saida", type=pathlib.Path, help="arquivo .puml (padrao: <pasta>/diagrama.puml)")
    analisador.add_argument("--sem-svg", action="store_true", help="gera so o .puml")
    analisador.add_argument("--png", action="store_true", help="tenta gerar tambem o .png")
    analisador.add_argument("--sem-metodos", action="store_true", help="mostra so os campos")
    analisador.add_argument("--sem-main", action="store_true", help="ignora a classe Main")
    analisador.add_argument("--com-dependencias", action="store_true", help="inclui os tipos citados nas assinaturas")
    analisador.add_argument("--recursivo", action="store_true", help="entra em subpastas")
    argumentos = analisador.parse_args()

    raiz: pathlib.Path = argumentos.pasta
    if not raiz.is_dir():
        print(f"pasta inexistente: {raiz}", file=sys.stderr)
        return 1

    tipos = carregar(raiz, argumentos.recursivo, argumentos.sem_main)
    if not tipos:
        print("nenhum tipo encontrado", file=sys.stderr)
        return 1

    titulo = argumentos.titulo or raiz.name
    relacoes = montar_relacoes(tipos, argumentos.com_dependencias)

    destino = argumentos.saida or raiz / "diagrama.puml"
    destino.write_text(gerar_puml(tipos, relacoes, titulo, not argumentos.sem_metodos), encoding="utf-8")
    print(f"{destino} ({len(tipos)} tipos, {len(relacoes)} relacoes)")

    if not argumentos.sem_svg:
        svg = destino.with_suffix(".svg")
        svg.write_text(gerar_svg(tipos, relacoes, titulo, not argumentos.sem_metodos), encoding="utf-8")
        print(f"{svg} (svg)")
        if argumentos.png:
            gerado = converter_para_png(svg)
            print(f"{gerado} (png)" if gerado else "png indisponivel: instale rsvg-convert, inkscape ou use chromium")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
