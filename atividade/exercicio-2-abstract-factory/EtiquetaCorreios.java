package checkout;

public class EtiquetaCorreios implements GeradorEtiqueta {
    public String nome() {
        return "Correios";
    }

    public String gerar(Pedido pedido) {
        return String.join("\n",
                "Carrier: " + nome(),
                "CEP: " + normalizar(pedido.cepEntrega(), 8) + " (BR)",
                "Destino: " + pedido.destino());
    }

    private String normalizar(String cep, int tamanho) {
        String digitos = cep.replaceAll("\\D", "");
        if (digitos.length() != tamanho) {
            throw new IllegalArgumentException("CEP brasileiro deve ter 8 dígitos: " + cep);
        }
        return digitos.substring(0, tamanho - 3) + "-" + digitos.substring(tamanho - 3);
    }
}
