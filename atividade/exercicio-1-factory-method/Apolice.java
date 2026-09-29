package apolice;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

public abstract class Apolice {
    private static final DateTimeFormatter DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final Locale BR = Locale.of("pt", "BR");

    private final GeradorNumero numeros;

    protected Apolice(GeradorNumero numeros) {
        this.numeros = numeros;
    }

    public abstract String tipo();

    public abstract double premio(Contratacao contratacao);

    public abstract List<String> validar(Contratacao contratacao);

    public abstract List<String> documentosExigidos(Contratacao contratacao);

    protected abstract String prefixo();

    public final String numero(Contratacao contratacao) {
        return numeros.proximo(prefixo(), contratacao.emissao());
    }

    public final String resumo(Contratacao contratacao, String numero) {
        return String.join("\n",
                "Apólice " + numero,
                "Linha: " + tipo(),
                "Segurado: " + contratacao.segurado(),
                "Emissão: " + contratacao.emissao().format(DATA),
                String.format(BR, "Prêmio mensal: R$ %,.2f", premio(contratacao)),
                "Documentos exigidos: " + String.join(", ", documentosExigidos(contratacao)));
    }
}
