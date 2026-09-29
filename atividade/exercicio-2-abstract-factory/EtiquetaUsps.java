package checkout;

public class EtiquetaUsps implements GeradorEtiqueta {
    public String nome() {
        return "USPS";
    }

    public String gerar(Pedido pedido) {
        return String.join("\n",
                "Carrier: " + nome(),
                "ZIP+4: " + normalizar(pedido.cepEntrega()) + " (US)",
                "Destino: " + pedido.destino());
    }

    private String normalizar(String cep) {
        String digitos = cep.replaceAll("\\D", "");
        if (digitos.length() != 9) {
            throw new IllegalArgumentException("ZIP+4 exige 9 dígitos: " + cep);
        }
        return digitos.substring(0, 5) + "-" + digitos.substring(5);
    }
}
