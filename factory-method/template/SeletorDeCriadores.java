package factorymethod;

import java.util.Map;

public final class SeletorDeCriadores {
    private static final Map<String, CriadorAbstrato> REGISTRO = Map.of(
            "1", new Criador1(),
            "2", new Criador2(),
            "2-ajustado", new Criador2Ajustado());

    private SeletorDeCriadores() {
    }

    public static CriadorAbstrato para(String chave) {
        CriadorAbstrato criador = REGISTRO.get(chave);
        if (criador == null) {
            throw new IllegalArgumentException("chave invalida: " + chave);
        }
        return criador;
    }

    public static Iterable<String> chaves() {
        return REGISTRO.keySet();
    }
}
