package apolice;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class GeradorNumero {
    private final Map<String, Integer> sequencias = new HashMap<>();

    public String proximo(String prefixo, LocalDate emissao) {
        int numero = sequencias.merge(prefixo, 1, Integer::sum);
        return prefixo + emissao.format(DateTimeFormatter.BASIC_ISO_DATE) + "-"
                + String.format(Locale.ROOT, "%04d", numero);
    }

    public int emitidas(String prefixo) {
        return sequencias.getOrDefault(prefixo, 0);
    }
}
