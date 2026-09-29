package apolice;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.text.Normalizer;

public class Contratacao {
    private String tipo = "";
    private String segurado = "";
    private int idade;
    private int tempoHabilitacao;
    private double valorVeiculo;
    private double valorImovel;
    private boolean altoPadrao;
    private double capitalSegurado;
    private boolean fumante;
    private int diasViagem;
    private boolean internacional;
    private double coberturaTerceiros;
    private double coberturaAssistencia;
    private LocalDate emissao = LocalDate.now();
    private final Set<String> documentos = new LinkedHashSet<>();

    public Contratacao tipo(String tipo) {
        this.tipo = tipo;
        return this;
    }

    public Contratacao segurado(String segurado) {
        this.segurado = segurado;
        return this;
    }

    public Contratacao idade(int idade) {
        this.idade = idade;
        return this;
    }

    public Contratacao tempoHabilitacao(int anos) {
        this.tempoHabilitacao = anos;
        return this;
    }

    public Contratacao valorVeiculo(double valor) {
        this.valorVeiculo = valor;
        return this;
    }

    public Contratacao valorImovel(double valor) {
        this.valorImovel = valor;
        return this;
    }

    public Contratacao altoPadrao(boolean altoPadrao) {
        this.altoPadrao = altoPadrao;
        return this;
    }

    public Contratacao capitalSegurado(double capital) {
        this.capitalSegurado = capital;
        return this;
    }

    public Contratacao fumante(boolean fumante) {
        this.fumante = fumante;
        return this;
    }

    public Contratacao diasViagem(int dias) {
        this.diasViagem = dias;
        return this;
    }

    public Contratacao internacional(boolean internacional) {
        this.internacional = internacional;
        return this;
    }

    public Contratacao coberturaTerceiros(double valor) {
        this.coberturaTerceiros = valor;
        return this;
    }

    public Contratacao coberturaAssistencia(double valor) {
        this.coberturaAssistencia = valor;
        return this;
    }

    public Contratacao documento(String documento) {
        documentos.add(chave(documento));
        return this;
    }

    public Contratacao emissao(LocalDate data) {
        this.emissao = data;
        return this;
    }

    public String tipo() {
        return tipo;
    }

    public String segurado() {
        return segurado;
    }

    public int idade() {
        return idade;
    }

    public int tempoHabilitacao() {
        return tempoHabilitacao;
    }

    public double valorVeiculo() {
        return valorVeiculo;
    }

    public double valorImovel() {
        return valorImovel;
    }

    public boolean altoPadrao() {
        return altoPadrao;
    }

    public double capitalSegurado() {
        return capitalSegurado;
    }

    public boolean fumante() {
        return fumante;
    }

    public int diasViagem() {
        return diasViagem;
    }

    public boolean internacional() {
        return internacional;
    }

    public double coberturaTerceiros() {
        return coberturaTerceiros;
    }

    public double coberturaAssistencia() {
        return coberturaAssistencia;
    }

    public LocalDate emissao() {
        return emissao;
    }

    public List<String> documentosApresentados() {
        return List.copyOf(documentos);
    }

    public boolean possuiDocumento(String documento) {
        return documentos.contains(chave(documento));
    }

    public static String chave(String texto) {
        String semAcento = Normalizer.normalize(texto, Normalizer.Form.NFD).replaceAll("\\p{M}", "");
        return semAcento.toUpperCase(Locale.ROOT).replaceAll("[^A-Z0-9]", "");
    }
}
