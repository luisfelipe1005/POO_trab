package br.com.dnd.service;

import br.com.dnd.exception.MonstroNaoEncontradoException;
import br.com.dnd.exception.RegraMatematicaInvalidaException;
import br.com.dnd.model.EntidadeRPG; // <-- Atualizado aqui!
import br.com.dnd.persistence.ArquivoBestiarioDAO;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GerenciadorBestiarioService {
    
    // Coleção Polimórfica principal usando EntidadeRPG
    private List<EntidadeRPG> bestiario = new ArrayList<>();
    private ArquivoBestiarioDAO dao = new ArquivoBestiarioDAO();

    public void adicionarMonstro(EntidadeRPG m) {
        if (m.getCr() < 0 || m.getCr() > 30) {
            throw new RegraMatematicaInvalidaException("O CR do monstro deve ser entre 0 e 30 segundo as regras de D&D!");
        }
        bestiario.add(m);
    }

    public EntidadeRPG buscarMonstro(String nome) throws MonstroNaoEncontradoException {
        for (EntidadeRPG m : bestiario) {
            if (m.getNome().equalsIgnoreCase(nome)) {
                return m;
            }
        }
        throw new MonstroNaoEncontradoException("O monstro '" + nome + "' não foi encontrado no bestiário.");
    }

    public void removerMonstro(String nome) throws MonstroNaoEncontradoException {
        EntidadeRPG m = buscarMonstro(nome);
        bestiario.remove(m);
    }

    public List<EntidadeRPG> listarTodos() {
        return bestiario;
    }

    public void salvarDados() throws IOException {
        dao.salvarBestiario(bestiario);
    }

    public void carregarDados() throws IOException, ClassNotFoundException {
        this.bestiario = dao.carregarBestiario();
    }
}