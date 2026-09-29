package checkout;

public class Main {
    public static void main(String[] args) {
        Pedido brasil = new Pedido("BR-1042", "SP", "SP", "01310-100", "01310-100", 1200.00, false);
        Pedido eua = new Pedido("US-2077", "CA", "CA", "941051234", "941051234", 850.00, false);
        Pedido alemanha = new Pedido("DE-3310", "BE", "BE", "10115", "10115", 430.00, true);

        System.out.println("RF01 Brasil (interestadual, Pix)");
        System.out.println(new ServicoCheckout(Fabricas.BRASIL.criar()).finalizar(
                new Pedido("BR-1043", "SP", "RJ", "20031-000", "01310-100", 1200.00, false)).formatar());
        System.out.println();

        System.out.println("RF01 Brasil (interna, Pix)");
        System.out.println(new ServicoCheckout(Fabricas.BRASIL.criar()).finalizar(brasil).formatar());
        System.out.println();

        System.out.println("RF01 Brasil (interna, boleto)");
        System.out.println(new ServicoCheckout(Fabricas.BRASIL_BOLETO.criar()).finalizar(brasil).formatar());
        System.out.println();

        System.out.println("RF02 Estados Unidos (California)");
        System.out.println(new ServicoCheckout(Fabricas.ESTADOS_UNIDOS.criar()).finalizar(eua).formatar());
        System.out.println();

        System.out.println("RF02 Estados Unidos (Oregon, isento)");
        System.out.println(new ServicoCheckout(Fabricas.ESTADOS_UNIDOS.criar()).finalizar(
                new Pedido("US-2078", "OR", "OR", "972051234", "972051234", 640.00, false)).formatar());
        System.out.println();

        System.out.println("RF03 Alemanha (produto essencial, USt 7%)");
        System.out.println(new ServicoCheckout(Fabricas.ALEMANHA.criar()).finalizar(alemanha).formatar());
        System.out.println();

        System.out.println("familias disponiveis: " + Fabricas.todas().keySet());
    }
}
