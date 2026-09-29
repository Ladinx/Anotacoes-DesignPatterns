package apolice;

import java.util.ArrayList;
import java.util.List;

public class ApoliceAuto extends Apolice {
    public static final String CNH = "CNH";
    public static final String CRLV = "CRLV";
    public static final String COMPROVANTE_RESIDENCIA = "Comprovante de residência";
    private static final double COBERTURA_MINIMA = 50_000;

    public ApoliceAuto(GeradorNumero numeros) {
        super(numeros);
    }

    public String tipo() {
        return "Automóvel";
    }

    protected String prefixo() {
        return "AUTO-";
    }

    public double premio(Contratacao contratacao) {
        double anual = 0.08 * contratacao.valorVeiculo();
        if (contratacao.idade() < 25) {
            anual *= 1.30;
        }
        if (contratacao.tempoHabilitacao() < 2) {
            anual *= 1.20;
        }
        return anual / 12;
    }

    public List<String> validar(Contratacao contratacao) {
        List<String> erros = new ArrayList<>();
        if (contratacao.coberturaTerceiros() < COBERTURA_MINIMA) {
            erros.add("cobertura de terceiros mínima de R$ 50.000,00 não atendida");
        }
        return erros;
    }

    public List<String> documentosExigidos(Contratacao contratacao) {
        return List.of(CNH, CRLV, COMPROVANTE_RESIDENCIA);
    }
}
