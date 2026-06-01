package br.com.dnd.model;


public class AtaqueRanged implements AcaoCombate, MultiAtaque {
    private String nome;
    private int alcance;
    private int bonusataque;
    private int danobonus;
    private int dadodano;
    private int numdados;
    private String tipodano;
    private int multiataque;



    public AtaqueRanged(String nome, int alcance, int bonusataque, int danobonus, int dadodano, int numdados, String tipodano, int multiataque) {
        this.nome = nome;
        this.alcance = alcance;
        this.bonusataque = bonusataque;
        this.danobonus = danobonus;
         this.dadodano = dadodano;
        this.numdados = numdados;
        this.tipodano = tipodano;
        this.multiataque = multiataque;
    }

    @Override
    public String getNome() {
        return this.nome + " (Melee)";
    }

    @Override
    public String getDescricao() {
        return nome + ". Ranged Weapon Attack: +" + bonusataque + "to hit, reach " + alcance +" ft., one target. Hit: " + Math.floor((dadodano/2+0.5) * numdados + danobonus) + "("+numdados+"d"+dadodano+" + "+danobonus+") " + tipodano+" damage.";

    }

        @Override
    public String getMultiataque() {
        return multiataque + " " + nome + "attack";

    }
    public int getBonusAtaque() { return bonusataque; }

    public void setBonusAtaque(int bonusataque) { this.bonusataque = bonusataque; }
    public void setDanoBonus(int danobonus) { this.danobonus = danobonus; }

    public int getDanoMedio() {
    return (int)((((dadodano + 1) / 2) * numdados + danobonus)*multiataque);
}
}
