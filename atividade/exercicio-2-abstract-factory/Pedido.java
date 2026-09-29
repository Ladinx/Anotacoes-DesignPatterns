package checkout;

public record Pedido(
        String id,
        String origem,
        String destino,
        String cepEntrega,
        String cepCobranca,
        double valor,
        boolean essencial) {

    public boolean interestadual() {
        return !origem.equalsIgnoreCase(destino);
    }
}
