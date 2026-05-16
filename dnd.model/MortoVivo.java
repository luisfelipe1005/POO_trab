package br.com.dnd.model;

public class MonstroLendario extends EntidadeRPG {

    private int usosAcaoLendaria;

    public MonstroLendario(String nome, int cr, int hpBase, int usosAcaoLendaria) {
        super(nome, cr, hpBase);
        this.usosAcaoLendaria = usosAcaoLendaria;
    }

    @Override
    public String formatarFicha() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== MONSTRO LENDÁRIO: ").append(getNome()).append(" ===\n");
        sb.append("CR: ").append(getCr()).append(" | HP (Reforçado): ").append(getHpBase() + 50); // Bônus lendário
        sb.append(" | Proficiência: +").append(calcularBonusProficiencia()).append("\n");
        sb.append("Ações Lendárias por Turno: ").append(usosAcaoLendaria).append("\n");
        
        sb.append("--- Habilidades e Ações ---\n");
        for (AcaoCombate acao : getAcoes()) {
            sb.append("> ").append(acao.getNome()).append(": ").append(acao.getDescricao()).append("\n");
        }
        return sb.toString();
    }
}
