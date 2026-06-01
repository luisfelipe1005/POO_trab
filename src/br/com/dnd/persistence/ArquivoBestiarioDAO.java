package br.com.dnd.persistence;

import br.com.dnd.model.EntidadeRPG; // <-- Atualizado aqui!

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class ArquivoBestiarioDAO {

    private static final String NOME_ARQUIVO = "bestiario.dat";

    public void salvarBestiario(List<EntidadeRPG> bestiario) throws IOException {
        File arquivo = new File(NOME_ARQUIVO);
        try (FileOutputStream fos = new FileOutputStream(arquivo);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(bestiario);
        }
    }

    @SuppressWarnings("unchecked")
    public List<EntidadeRPG> carregarBestiario() throws IOException, ClassNotFoundException {
        File arquivo = new File(NOME_ARQUIVO);
        if (!arquivo.exists()) {
            return new ArrayList<>();
        }
        try (FileInputStream fis = new FileInputStream(arquivo);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            return (List<EntidadeRPG>) ois.readObject();
        }
    }
}