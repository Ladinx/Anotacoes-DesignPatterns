package factorymethod;

public class Main {
    public static void main(String[] args) {
        for (String chave : SeletorDeCriadores.chaves()) {
            CriadorAbstrato criador = SeletorDeCriadores.para(chave);
            System.out.println("  -> " + criador.criar().descrever());
        }
    }
}
