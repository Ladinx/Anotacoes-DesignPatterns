package abstractfactory;

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
