package solid;

import java.util.List;

public record Pedido(String cliente, List<Item> itens, Desconto desconto) {
    public double bruto() {
        return itens.stream().mapToDouble(Item::total).sum();
    }

    public double total() {
        return desconto.aplicar(bruto());
    }
}
