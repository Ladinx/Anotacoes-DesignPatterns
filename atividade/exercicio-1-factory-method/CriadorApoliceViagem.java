package apolice;

public class CriadorApoliceViagem extends CriadorApolice {
    public CriadorApoliceViagem(GeradorNumero numeros) {
        super(numeros);
    }

    @Override
    protected Apolice criarApolice() {
        return new ApoliceViagem(numeros);
    }
}
