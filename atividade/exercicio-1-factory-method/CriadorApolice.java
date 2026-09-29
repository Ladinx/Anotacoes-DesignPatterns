package apolice;

import java.util.List;

public abstract class CriadorApolice {
    protected final GeradorNumero numeros;

    protected CriadorApolice(GeradorNumero numeros) {
        this.numeros = numeros;
    }

    public final Resultado processar(Contratacao contratacao) {
        Apolice apolice = criarApolice();
        List<String> erros = apolice.validar(contratacao);
        if (!erros.isEmpty()) {
            return Resultado.rejeitado(apolice.tipo(), erros);
        }
        String numero = apolice.numero(contratacao);
        return Resultado.emitido(apolice.tipo(), numero, apolice.resumo(contratacao, numero));
    }

    protected abstract Apolice criarApolice();
}
