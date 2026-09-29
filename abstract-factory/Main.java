package abstractfactory;

public class Main {
    public static void main(String[] args) {
        for (Tema tema : Tema.values()) {
            System.out.println("tema " + tema);
            new Aplicacao(FabricaUis.porTema(tema)).desenhar();
        }
    }
}
