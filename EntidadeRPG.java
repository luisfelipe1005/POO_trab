package br.com.dnd.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class EntidadeRPG implements CalculadorEstatistica, Serializable {
    // Atributos encapsulados
    private String nome;
    private int cr;
    private int hpBase;
    
    // Coleção Polimórfica de Ações (Delegação)
    private List<AcaoCombate> acoes;

    // Construtor
    public EntidadeRPG(String nome, int cr, int hpBase) {
        this.nome = nome;
        this.cr = cr;
        this.hpBase = hpBase;
        this.acoes = new ArrayList<>();
    }

    // Método Concreto 1: Adicionar ação à lista
    public void adicionarAcao(AcaoCombate acao) {
        this.acoes.add(acao);
    }

    // Método Concreto 2: Implementação da Interface
    @Override
    public int calcularBonusProficiencia() {
        // Regra simplificada do D&D baseada no CR
        if (cr >= 0 && cr <= 4) return 2;
        if (cr >= 5 && cr <= 8) return 3;
        if (cr >= 9 && cr <= 12) return 4;
        if (cr >= 13 && cr <= 16) return 5;
        if (cr >= 17 && cr <= 20) return 6;
        if (cr >= 21 && cr <= 24) return 7;
        return 8; // CR 25 a 30
    }

    // Método Abstrato (As filhas são obrigadas a implementar)
    public abstract String formatarFicha();

    // Getters
    public String getNome() { return nome; }
    public int getCr() { return cr; }
    public int getHpBase() { return hpBase; }
    public List<AcaoCombate> getAcoes() { return acoes; }
}
