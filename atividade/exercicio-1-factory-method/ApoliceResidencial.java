package apolice;

import java.util.ArrayList;
import java.util.List;

public class ApoliceResidencial extends Apolice {
    public static final String ESCRITURA = "Escritura";
    public static final String CONTRATO_LOCACAO = "Contrato de locação";
    public static final String COMPROVANTE_RESIDENCIA = "Comprovante de residência";

    public ApoliceResidencial(GeradorNumero numeros) {
        super(numeros);
    }

    public String tipo() {
        return "Residencial";
    }

    protected String prefixo() {
        return "RES-";
    }

    public double premio(Contratacao contratacao) {
        double anual = 0.015 * contratacao.valorImovel();
        if (contratacao.altoPadrao()) {
            anual *= 1.25;
        }
        return anual / 12;
    }

    public List<String> validar(Contratacao contratacao) {
        List<String> erros = new ArrayList<>();
        if (!contratacao.possuiDocumento(ESCRITURA) && !contratacao.possuiDocumento(CONTRATO_LOCACAO)) {
            erros.add("escritura ou contrato de locação não apresentado");
        }
        return erros;
    }

    public List<String> documentosExigidos(Contratacao contratacao) {
        return List.of(ESCRITURA, COMPROVANTE_RESIDENCIA);
    }
}
