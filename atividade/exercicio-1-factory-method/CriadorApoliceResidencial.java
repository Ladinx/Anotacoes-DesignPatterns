package apolice;

public class CriadorApoliceResidencial extends CriadorApolice {
    public CriadorApoliceResidencial(GeradorNumero numeros) {
        super(numeros);
    }

    @Override
    protected Apolice criarApolice() {
        return new ApoliceResidencial(numeros);
    }
}
