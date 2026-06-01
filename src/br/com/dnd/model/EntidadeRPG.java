package br.com.dnd.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class EntidadeRPG implements CalculadorEstatistica, Serializable {

    private String nome;
    private String tamanho;
    private float cr;
    private int hpBase;
    private int ca;
    private String velocidade;
    private String sentidos;
    private String linguagens;

    // novos atributos
    private int str;
    private int dex;
    private int con;
    private int intel;   // não pode chamar "int"
    private int wis;
    private int cha;

    private List<AcaoCombate> acoes;
    private List<String> imunidades;
    private List<String> resistencias;

    public EntidadeRPG(
            String nome,
            float cr,
            String tamanho,
            int hpBase,
            int ca,
            String velocidade,
            String sentidos,
            String linguagens,
            int str,
            int dex,
            int con,
            int intel,
            int wis,
            int cha) {

        this.tamanho = tamanho;
        this.nome = nome;
        this.cr = cr;
        this.hpBase = hpBase;
        this.ca = ca;
        this.velocidade = velocidade;
        this.sentidos = sentidos;
        this.linguagens = linguagens;

        this.str = str;
        this.dex = dex;
        this.con = con;
        this.intel = intel;
        this.wis = wis;
        this.cha = cha;

        this.acoes = new ArrayList<>();
        this.imunidades = new ArrayList<>();
        this.resistencias = new ArrayList<>();

        aplicarCaracteristicasDoTipo();
    }

    public void adicionarAcao(AcaoCombate acao) {
        this.acoes.add(acao);
    }

    public void adicionarImunidade(String imunidade) {
        this.imunidades.add(imunidade);
    }

        public void adicionarResistencia(String resistencia) {
        this.resistencias.add(resistencia);
    }

    @Override
    public int calcularBonusProficiencia() {
        if (cr >= 0 && cr <= 4) return 2;
        if (cr >= 5 && cr <= 8) return 3;
        if (cr >= 9 && cr <= 12) return 4;
        if (cr >= 13 && cr <= 16) return 5;
        if (cr >= 17 && cr <= 20) return 6;
        if (cr >= 21 && cr <= 24) return 7;
        return 8;
    }

    public abstract String formatarFicha();
    protected abstract void aplicarCaracteristicasDoTipo();
    public abstract int calcularHpFinal();

    // getters antigos
    public String getNome() { return nome; }
    public String getTamanho() { return tamanho; }
    public float getCr() { return cr; }
    public int getHpBase() { return hpBase; }
    public int getCa() { return ca; }
    public String getVelocidade() {
    return velocidade;
}

public String getSentidos() {
    return sentidos;
}

public String getLinguagens() {
    return linguagens;
}
    public List<AcaoCombate> getAcoes() { return acoes; }
    public List<String> getImunidades() { return imunidades; }
    public List<String> getResistencias() { return resistencias; }

    // novos getters
    public int getStr() { return str; }
    public int getDex() { return dex; }
    public int getCon() { return con; }
    public int getInt() { return intel; }
    public int getWis() { return wis; }
    public int getCha() { return cha; }





public void setCr(float cr) { this.cr = cr; }
    public void setHpBase(int hpBase) { this.hpBase = hpBase; }
    public void setCa(int ca) { this.ca = ca; }


}