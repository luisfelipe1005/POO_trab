package br.com.dnd.model;

public class NPC extends EntidadeRPG {
    private String raca;

    public NPC(String nome, int cr, int hpBase, String raca) {
        super(nome, cr, hpBase);
        this.raca = raca;
    }

    @Override
    public String formatarFicha() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== NPC [").append(raca).append("]: ").append(getNome()).append(" ===\n");
        sb.append("CR: ").append(getCr()).append(" | HP: ").append(getHpBase()).append("\n");
        
        sb.append("--- Habilidades e Ações ---\n");
        for (AcaoCombate acao : getAcoes()) {
            sb.append("> ").append(acao.getNome()).append(": ").append(acao.getDescricao()).append("\n");
        }
        return sb.toString();
    }
}
