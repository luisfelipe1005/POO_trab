package br.com.dnd.model;

public class MonstroPadrao extends EntidadeRPG {

    public MonstroPadrao(String nome, int cr, int hpBase) {
        super(nome, cr, hpBase);
    }

    @Override
    public String formatarFicha() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== MONSTRO: ").append(getNome()).append(" ===\n");
        sb.append("CR: ").append(getCr()).append(" | HP: ").append(getHpBase());
        sb.append(" | Proficiência: +").append(calcularBonusProficiencia()).append("\n");
        
        sb.append("--- Habilidades e Ações ---\n");
        for (AcaoCombate acao : getAcoes()) {
            sb.append("> ").append(acao.getNome()).append(": ").append(acao.getDescricao()).append("\n");
        }
        return sb.toString();
    }
}
