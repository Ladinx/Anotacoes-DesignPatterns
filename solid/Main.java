package solid;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        Validador validador = new Validador();
        NotificadorEmail email = new ServicoEmail();
        NotificadorSms sms = new ServicoSms();

        Pedido pedido = new Pedido("ana", List.of(new Item("livro", 2, 49.90), new Item("mapa", 1, 20.00)),
                new DescontoPercentual(0.10));

        List<Pagamento> meios = List.of(new PagamentoBoleto(), new PagamentoCartao(), new PagamentoPix());
        for (Pagamento meio : meios) {
            System.out.println("meio " + meio.getClass().getSimpleName());
            new CheckoutService(meio, validador, email, sms).finalizar(pedido);
            if (meio instanceof PagamentoReembolsavel reembolsavel) {
                reembolsavel.reembolsar(pedido.total());
            }
        }

        Pedido invalido = new Pedido(" ", List.of(new Item("x", 0, 10)), new SemDesconto());
        new CheckoutService(new PagamentoPix(), validador, email, sms).finalizar(invalido);
    }
}
