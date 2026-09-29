package solid;

import java.util.List;

public class CheckoutService {
    private final Pagamento pagamento;
    private final Validador validador;
    private final NotificadorEmail email;
    private final NotificadorSms sms;

    public CheckoutService(Pagamento pagamento, Validador validador, NotificadorEmail email, NotificadorSms sms) {
        this.pagamento = pagamento;
        this.validador = validador;
        this.email = email;
        this.sms = sms;
    }

    public boolean finalizar(Pedido pedido) {
        List<String> erros = validador.erros(pedido);
        if (!erros.isEmpty()) {
            email.email("pedido recusado de " + pedido.cliente() + ": " + erros);
            return false;
        }
        pagamento.pagar(pedido.total());
        email.email("pedido confirmado para " + pedido.cliente());
        sms.sms("pagamento de " + pedido.total() + " aprovado");
        return true;
    }
}
