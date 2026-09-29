package solid;

public record Item(String nome, int quantidade, double precoUnitario) {
    public double total() {
        return quantidade * precoUnitario;
    }
}
