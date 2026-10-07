Uma classe só pode ter um objeto de si mesmo instanciado. 

É NECESSARIO: 
*Controle de Instancia Unica*: Deve haver implementação para garantir impossibilidade de criar novas instancias. 
*Acesso Global* : Deve ser acessivel de forma global, sem necessidade de passar a instancia de forma explicita. 

```java
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
```
public class teste {`

`public static void main(String[] args) {`

`client_singleton cliente = client_singleton.getInstance();`

`cliente.nome = "João";`

`cliente.email = "joao@email.com";`

`System.out.println("Cliente 1: " + cliente.nome + ", " + cliente.email);`

  

`client_singleton pessoa = client_singleton.getInstance();`

`pessoa.nome = "Maria";`

`pessoa.email = "maria@email.com";`

`System.out.println("Pessoa: " + pessoa.nome + ", " + pessoa.email);`

`}`

`}
```java
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
```