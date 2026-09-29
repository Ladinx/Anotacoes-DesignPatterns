package apolice;

import java.util.ArrayList;
import java.util.List;

public class ApoliceVida extends Apolice {
    public static final String IDENTIDADE = "Documento de identidade";
    public static final String CPF = "CPF";
    public static final String ATESTADO_MEDICO = "Atestado médico";
    private static final double CAPITAL_MAXIMO_SEM_ATESTADO = 500_000;

    public ApoliceVida(GeradorNumero numeros) {
        super(numeros);
    }

    public String tipo() {
        return "Vida";
    }

    protected String prefixo() {
        return "VID-";
    }

    public double premio(Contratacao contratacao) {
        double mensal = contratacao.idade() * 12 + contratacao.capitalSegurado() * 0.002;
        if (contratacao.fumante()) {
            mensal *= 1.50;
        }
        return mensal;
    }

    public List<String> validar(Contratacao contratacao) {
        List<String> erros = new ArrayList<>();
        if (exigeAtestado(contratacao) && !contratacao.possuiDocumento(ATESTADO_MEDICO)) {
            erros.add("atestado médico obrigatório para capital segurado acima de R$ 500.000,00");
        }
        return erros;
    }

    public List<String> documentosExigidos(Contratacao contratacao) {
        List<String> exigidos = new ArrayList<>(List.of(IDENTIDADE, CPF));
        if (exigeAtestado(contratacao)) {
            exigidos.add(ATESTADO_MEDICO);
        }
        return exigidos;
    }

    private boolean exigeAtestado(Contratacao contratacao) {
        return contratacao.capitalSegurado() > CAPITAL_MAXIMO_SEM_ATESTADO;
    }
}
