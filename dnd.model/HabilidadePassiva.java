package br.com.dnd.model;

public class HabilidadePassiva implements AcaoCombate {
    private String nome;
    private String efeito;

    public HabilidadePassiva(String nome, String efeito) {
        this.nome = nome;
        this.efeito = efeito;
    }

    @Override
    public String getNome() {
        return this.nome + " (Passiva)";
    }

    @Override
    public String getDescricao() {
        return efeito;
    }
}
