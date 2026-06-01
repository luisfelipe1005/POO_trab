package br.com.dnd.model;

public class MonstroPadrao extends EntidadeRPG {

   public MonstroPadrao(
        String nome,
        float cr,
        String tamanho,
        int hpBase,
        int ca,
        String velocidade,
        String sentidos,
        String linguagens,
        int STR,
        int DEX,
        int CON,
        int intel,
        int WIS,
        int CHA) {

    super(
        nome,
        cr,
        tamanho,
        hpBase,
        ca,
        velocidade,
        sentidos,
        linguagens,
        STR,
        DEX,
        CON,
        intel,
        WIS,
        CHA
    );
}
    // --- MÉTODOS OBRIGATÓRIOS DA CLASSE MÃE ---

    @Override
    protected void aplicarCaracteristicasDoTipo() {
        // Um monstro padrão genérico não possui imunidades biológicas nativas,
        // então deixamos vazio. Mas o método precisa existir para cumprir a herança!
    }

    @Override
    public int calcularHpFinal() {
        // O HP final de um monstro padrão é exatamente o HP base dele
        return getHpBase();
    }

    // ------------------------------------------

   @Override
public String formatarFicha() {
    StringBuilder sb = new StringBuilder();

    // Cabeçalho
    sb.append(getNome())
      .append(" | CR ")
      .append(getCr())
      .append("\n");

    sb.append("Medium Monstrosity")
      .append("\n");

    sb.append("Armor Class ??")
      .append("\n");

    sb.append("Hit Points ")
      .append(calcularHpFinal())
      .append("\n");

    sb.append("Speed ?? ft.")
      .append("\n\n");

    // atributos
    sb.append("STR   DEX   CON   INT   WIS   CHA\n");

    sb.append((getStr())).append("   ")
      .append((getDex())).append("   ")
      .append((getCon())).append("   ")
      .append((getInt())).append("   ")
      .append((getWis())).append("   ")
      .append((getCha()))
      .append("\n\n");

    // proficiência
    sb.append("Proficiency Bonus +")
      .append(calcularBonusProficiencia())
      .append("\n");

    // imunidades
    if (!getImunidades().isEmpty()) {
        sb.append("Immune ")
          .append(String.join(", ", getImunidades()))
          .append("\n");
    }

    sb.append("\nTRAITS\n");

    for (AcaoCombate acao : getAcoes()) {
        if (!(acao instanceof AtaqueCorpoACorpo)
                && !(acao instanceof AtaqueRanged)) {

            sb.append(acao.getNome())
              .append(". ")
              .append(acao.getDescricao())
              .append("\n");
        }
    }

    sb.append("\nACTIONS\n");

    for (AcaoCombate acao : getAcoes()) {
        if (acao instanceof AtaqueCorpoACorpo
                || acao instanceof AtaqueRanged) {

            sb.append(acao.getNome())
              .append(". ")
              .append(acao.getDescricao())
              .append("\n");
        }
    }

    return sb.toString();
}
}