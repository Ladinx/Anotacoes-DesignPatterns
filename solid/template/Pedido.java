package solid;

import java.util.List;

public record Pedido(String cliente, List<Item> itens, Regra regra) {
    public double bruto() {
        return itens.stream().mapToDouble(Item::total).sum();
    }

    public double total() {
        return regra.aplicar(bruto());
    }
}
