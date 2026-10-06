public class teste {
    public static void main(String[] args) {
        client_singleton cliente = client_singleton.getInstance();
        cliente.nome = "João";
        cliente.email = "joao@email.com";
        System.out.println("Cliente 1: " + cliente.nome + ", " + cliente.email);

        client_singleton pessoa = client_singleton.getInstance();
        pessoa.nome = "Maria";
        pessoa.email = "maria@email.com";
        System.out.println("Pessoa: " + pessoa.nome + ", " + pessoa.email);
    }
}
