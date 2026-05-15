package br.com.dnd.model;

public class Magia implements AcaoCombate {
    private String nome;
    private int circulo;
    private String usoDiario;

    public Magia(String nome, int circulo, String usoDiario) {
        this.nome = nome;
        this.circulo = circulo;
        this.usoDiario = usoDiario;
    }

    @Override
    public String getNome() {
        return this.nome + " (Magia nv " + circulo + ")";
    }

    @Override
    public String getDescricao() {
        return "Pode ser usada: " + usoDiario;
    }
}
