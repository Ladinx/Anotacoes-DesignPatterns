package apolice;

import java.util.ArrayList;
import java.util.List;

public class ApoliceViagem extends Apolice {
    public static final String ITINERARIO = "Itinerário de viagem";
    public static final String PASSPORTE = "Passaporte";
    private static final double DIARIA = 15.00;
    private static final double ACRESCIMO_INTERNACIONAL = 100.00;
    private static final double ASSISTENCIA_MINIMA = 30_000;

    public ApoliceViagem(GeradorNumero numeros) {
        super(numeros);
    }

    public String tipo() {
        return "Viagem";
    }

    protected String prefixo() {
        return "VIA-";
    }

    public double premio(Contratacao contratacao) {
        return contratacao.diasViagem() * DIARIA + (contratacao.internacional() ? ACRESCIMO_INTERNACIONAL : 0);
    }

    public List<String> validar(Contratacao contratacao) {
        List<String> erros = new ArrayList<>();
        if (contratacao.internacional()) {
            if (contratacao.coberturaAssistencia() < ASSISTENCIA_MINIMA) {
                erros.add("assistência médica mínima de US$ 30.000,00 não contratada");
            }
            if (!contratacao.possuiDocumento(PASSPORTE)) {
                erros.add("passaporte não apresentado");
            }
        }
        return erros;
    }

    public List<String> documentosExigidos(Contratacao contratacao) {
        return contratacao.internacional() ? List.of(ITINERARIO, PASSPORTE) : List.of(ITINERARIO);
    }
}
