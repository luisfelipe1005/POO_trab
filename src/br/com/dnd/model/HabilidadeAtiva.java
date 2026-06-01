package br.com.dnd.model;

public class HabilidadeAtiva implements AcaoCombate {

    private String nome;
    private String descricao;
    private String tipo; // Action, Bonus Action, Legendary Action
    private String recarga; // "Recharge 6", "4/Day", "Costs 2 Actions"

    public HabilidadeAtiva(
            String nome,
            String descricao,
            String tipo,
            String recarga) {

        this.nome = nome;
        this.descricao = descricao;
        this.tipo = tipo;
        this.recarga = recarga;
    }

    @Override
    public String getNome() {
        if (recarga != null && !recarga.isEmpty()) {
            return nome + " (" + recarga + ")";
        }
        return nome;
    }

    @Override
    public String getDescricao() {
        return "[" + tipo + "] " + descricao;
    }
}