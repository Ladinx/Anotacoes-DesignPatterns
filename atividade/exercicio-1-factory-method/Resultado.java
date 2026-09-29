package apolice;

import java.util.List;

public record Resultado(String tipo, boolean emitida, String numero, String resumo, List<String> erros) {
    public static Resultado emitido(String tipo, String numero, String resumo) {
        return new Resultado(tipo, true, numero, resumo, List.of());
    }

    public static Resultado rejeitado(String tipo, List<String> erros) {
        return new Resultado(tipo, false, "", "", List.copyOf(erros));
    }
}
