package br.com.dnd.model;

import java.io.Serializable;

// Interface que será implementada pelas diferentes partes (Composição)
// Serializable é essencial para podermos salvar no arquivo depois!
public interface AcaoCombate extends Serializable {
    String getNome();
    String getDescricao();
}
