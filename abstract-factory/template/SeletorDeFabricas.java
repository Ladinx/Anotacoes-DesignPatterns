package abstractfactory;

public final class SeletorDeFabricas {
    private SeletorDeFabricas() {
    }

    public static FabricaAbstrata porFamilia(Familia familia) {
        return switch (familia) {
            case A -> new FabricaDaFamiliaA();
            case B -> new FabricaDaFamiliaB();
        };
    }
}
