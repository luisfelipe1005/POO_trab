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


    // Adicione este método junto com os outros (buscar, listarTodos, etc.)
    public List<EntidadeRPG> filtrarMonstrosPorNome(String termoBusca) {
        // Se a busca estiver vazia, retorna a lista completa
        if (termoBusca == null || termoBusca.trim().isEmpty()) {
            return listarTodos();
        }

        List<EntidadeRPG> filtrados = new ArrayList<>();
        String termoLowerCase = termoBusca.toLowerCase();

        for (EntidadeRPG m : bestiario) {
            // Verifica se o nome do monstro contém o trecho digitado
            if (m.getNome().toLowerCase().contains(termoLowerCase)) {
                filtrados.add(m);
            }
        }
        return filtrados;
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