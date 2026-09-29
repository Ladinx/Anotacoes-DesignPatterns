package apolice;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class Seguradora {
    private final Map<String, CriadorApolice> criadores = new LinkedHashMap<>();

    public Seguradora registrar(String tipo, CriadorApolice criador) {
        criadores.put(tipo, criador);
        return this;
    }

    public Resultado contratar(Contratacao contratacao) {
        CriadorApolice criador = criadores.get(contratacao.tipo());
        if (criador == null) {
            throw new IllegalArgumentException("linha de produto sem criador registrado: " + contratacao.tipo());
        }
        return criador.processar(contratacao);
    }

    public Set<String> linhas() {
        return Collections.unmodifiableSet(criadores.keySet());
    }
}
