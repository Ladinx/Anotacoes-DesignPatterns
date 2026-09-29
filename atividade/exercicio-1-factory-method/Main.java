package apolice;

import java.time.LocalDate;

public class Main {
    private static final LocalDate HOJE = LocalDate.of(2026, 9, 29);

    public static void main(String[] args) {
        GeradorNumero numeros = new GeradorNumero();
        Seguradora seguradora = new Seguradora()
                .registrar("AUTO", new CriadorApoliceAuto(numeros))
                .registrar("RES", new CriadorApoliceResidencial(numeros))
                .registrar("VID", new CriadorApoliceVida(numeros))
                .registrar("VIA", new CriadorApoliceViagem(numeros));

        System.out.println("linhas registradas: " + seguradora.linhas());
        System.out.println();

        contratar(seguradora, new Contratacao()
                .tipo("AUTO")
                .segurado("Ana Ribeiro")
                .idade(22)
                .tempoHabilitacao(1)
                .valorVeiculo(52_000)
                .coberturaTerceiros(120_000)
                .documento("cnh")
                .documento("CRLV")
                .documento("comprovante de residência")
                .emissao(HOJE), "RF01 Automóvel emitida (condutor jovem, habilitação recente)");

        contratar(seguradora, new Contratacao()
                .tipo("AUTO")
                .segurado("Bruno Alves")
                .idade(40)
                .tempoHabilitacao(12)
                .valorVeiculo(52_000)
                .coberturaTerceiros(30_000)
                .documento("CNH")
                .documento("CRLV")
                .emissao(HOJE), "RF01 Automóvel rejeitada (cobertura de terceiros insuficiente)");

        contratar(seguradora, new Contratacao()
                .tipo("RES")
                .segurado("Carla Menezes")
                .valorImovel(800_000)
                .altoPadrao(true)
                .documento("Contrato de locação")
                .documento("comprovante de residÊNCIA")
                .emissao(HOJE), "RF02 Residencial emitida (imóvel alto padrão, contrato de locação)");

        contratar(seguradora, new Contratacao()
                .tipo("RES")
                .segurado("Diego Prado")
                .valorImovel(300_000)
                .emissao(HOJE), "RF02 Residencial rejeitada (sem escritura nem contrato)");

        contratar(seguradora, new Contratacao()
                .tipo("VID")
                .segurado("Elisa Torres")
                .idade(40)
                .capitalSegurado(200_000)
                .fumante(true)
                .documento("documento de identidade")
                .documento("CPF")
                .emissao(HOJE), "RF03 Vida emitida (segurado fumante)");

        contratar(seguradora, new Contratacao()
                .tipo("VID")
                .segurado("Felipe Nunes")
                .idade(45)
                .capitalSegurado(600_000)
                .documento("RG")
                .documento("CPF")
                .emissao(HOJE), "RF03 Vida rejeitada (capital acima de R$ 500.000,00 sem atestado)");

        contratar(seguradora, new Contratacao()
                .tipo("VIA")
                .segurado("Gabriela Souza")
                .diasViagem(12)
                .internacional(true)
                .coberturaAssistencia(50_000)
                .documento("itinerário de viagem")
                .documento("passaporte")
                .emissao(HOJE), "RF04 Viagem emitida (destino internacional)");

        contratar(seguradora, new Contratacao()
                .tipo("VIA")
                .segurado("Henrique Dias")
                .diasViagem(20)
                .internacional(true)
                .coberturaAssistencia(50_000)
                .documento("Itinerário de viagem")
                .emissao(HOJE), "RF04 Viagem rejeitada (internacional sem passaporte)");
    }

    private static void contratar(Seguradora seguradora, Contratacao contratacao, String titulo) {
        System.out.println("--- " + titulo);
        Resultado resultado = seguradora.contratar(contratacao);
        if (resultado.emitida()) {
            System.out.println(resultado.resumo());
        } else {
            System.out.println("CONTRATACAO REJEITADA (" + resultado.tipo() + ")");
            resultado.erros().forEach(erro -> System.out.println("  - " + erro));
        }
        System.out.println();
    }
}
