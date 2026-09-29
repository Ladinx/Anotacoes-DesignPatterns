package solid;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        Validador validador = new Validador();
        NotificadorA canalA = new ServicoNotificadorA();
        NotificadorB canalB = new ServicoNotificadorB();

        Pedido pedido = new Pedido("cliente", List.of(new Item("item", 2, 50.00)),
                new RegraPercentual(0.10));

        for (Pagamento meio : List.of(new PagamentoA(), new PagamentoB(), new PagamentoC())) {
            System.out.println("meio " + meio.getClass().getSimpleName());
            new ServicoOperacao(meio, validador, canalA, canalB).executar(pedido);
            if (meio instanceof PagamentoReembolsavel reembolsavel) {
                reembolsavel.reembolsar(pedido.total());
            }
        }
    }
}
