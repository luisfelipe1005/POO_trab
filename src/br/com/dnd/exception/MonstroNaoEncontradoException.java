package br.com.dnd.exception;

// Estende Exception (Checked).
// Obriga quem chamar o método de busca a usar um try/catch.
public class MonstroNaoEncontradoException extends Exception {
    public MonstroNaoEncontradoException(String nomeDoMonstro) {
        super("O monstro '" + nomeDoMonstro + "' não foi encontrado no bestiário.");
    }
}