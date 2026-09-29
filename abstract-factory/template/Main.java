package abstractfactory;

public class Main {
    public static void main(String[] args) {
        for (Familia familia : Familia.values()) {
            System.out.println("familia " + familia);
            new Aplicacao(SeletorDeFabricas.porFamilia(familia)).executar();
        }
    }
}
