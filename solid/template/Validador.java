package solid;

import java.util.ArrayList;
import java.util.List;

public class Validador {
    public List<String> erros(Pedido pedido) {
        List<String> erros = new ArrayList<>();
        if (pedido.cliente() == null || pedido.cliente().isBlank()) {
            erros.add("cliente ausente");
        }
        if (pedido.itens().isEmpty()) {
            erros.add("pedido sem itens");
        }
        for (Item item : pedido.itens()) {
            if (item.quantidade() <= 0 || item.precoUnitario() <= 0) {
                erros.add("item invalido: " + item.nome());
            }
        }
        return erros;
    }
}
