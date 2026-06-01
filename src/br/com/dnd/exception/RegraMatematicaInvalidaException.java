package br.com.dnd.exception;

// Estende RuntimeException (Unchecked). 
// Usada para erros de lógica que quebram as regras do D&D.
public class RegraMatematicaInvalidaException extends RuntimeException {
    public RegraMatematicaInvalidaException(String mensagem) {
        super(mensagem);
    }
}