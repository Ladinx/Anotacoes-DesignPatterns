public class client_singleton {

    // Atributos da classe
    public String nome;
    public String email;

    // Atributo privado e estatico pra instancia unica
    private static client_singleton instancia;

    // Construtor privado
    private client_singleton() {
    }

    // Metodo para obter a instancia unica
    public static client_singleton getInstance() {
        if (instancia == null) {
            instancia = new client_singleton();
        }
        return instancia;
    }
}
