package checkout;

public class EtiquetaDeutschePost implements GeradorEtiqueta {
    public String nome() {
        return "Deutsche Post";
    }

    public String gerar(Pedido pedido) {
        return String.join("\n",
                "Carrier: " + nome(),
                "PLZ: " + normalizar(pedido.cepEntrega()) + " (DE)",
                "Ziel: " + pedido.destino());
    }

    private String normalizar(String plz) {
        String digitos = plz.replaceAll("\\D", "");
        if (digitos.length() != 5) {
            throw new IllegalArgumentException("PLZ alemão exige 5 dígitos: " + plz);
        }
        return digitos;
    }
}
