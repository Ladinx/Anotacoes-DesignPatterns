package apolice;

public class CriadorApoliceVida extends CriadorApolice {
    public CriadorApoliceVida(GeradorNumero numeros) {
        super(numeros);
    }

    @Override
    protected Apolice criarApolice() {
        return new ApoliceVida(numeros);
    }
}
