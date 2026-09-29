package factorymethod;

import java.util.Map;

public final class FabricaCriadores {
    private static final Map<String, Criador> REGISTRO = Map.of(
            "livro", new CriadorLivro(),
            "jogo", new CriadorJogo(),
            "promo", new CriadorJogoPromocional());

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
