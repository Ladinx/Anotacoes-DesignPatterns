package solid;

import java.util.List;

public class ServicoOperacao {
    private final Pagamento pagamento;
    private final Validador validador;
    private final NotificadorA notificadorA;
    private final NotificadorB notificadorB;

    public ServicoOperacao(Pagamento pagamento, Validador validador,
                           NotificadorA notificadorA, NotificadorB notificadorB) {
        this.pagamento = pagamento;
        this.validador = validador;
        this.notificadorA = notificadorA;
        this.notificadorB = notificadorB;
    }

    public boolean executar(Pedido pedido) {
        List<String> erros = validador.erros(pedido);
        if (!erros.isEmpty()) {
            notificadorA.enviarA("recusado: " + erros);
            return false;
        }
        pagamento.pagar(pedido.total());
        notificadorA.enviarA("confirmado para " + pedido.cliente());
        notificadorB.enviarB("pagamento de " + pedido.total() + " aprovado");
        return true;
    }
}
