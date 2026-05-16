package br.com.dnd.model;

public class AtaqueCorpoACorpo implements AcaoCombate {
    private String nome;
    private String alcance;
    private String dano;

    public AtaqueCorpoACorpo(String nome, String alcance, String dano) {
        this.nome = nome;
        this.alcance = alcance;
        this.dano = dano;
    }

    @Override
    public String getNome() {
        return this.nome + " (Melee)";
    }

    @Override
    public String getDescricao() {
        return "Alcance " + alcance + ". Dano: " + dano;
    }
}
