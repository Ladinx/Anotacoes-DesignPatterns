package factorymethod;

public class Main {
    public static void main(String[] args) {
        for (String tipo : new String[]{"livro", "jogo", "promo"}) {
            Criador criador = FabricaCriadores.para(tipo);
            System.out.println("  -> " + criador.criar().descrever());
        }
    }
}
