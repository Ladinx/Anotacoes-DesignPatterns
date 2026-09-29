package apolice;

public class CriadorApoliceAuto extends CriadorApolice {
    public CriadorApoliceAuto(GeradorNumero numeros) {
        super(numeros);
    }

    @Override
    protected Apolice criarApolice() {
        return new ApoliceAuto(numeros);
    }
}
