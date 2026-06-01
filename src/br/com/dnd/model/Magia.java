package br.com.dnd.model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Magia implements AcaoCombate {
    private String atributoBase;
    private int saveDc;
    private Map<Integer, List<String>> tabelaMagias;

    public Magia(String atributoBase, int saveDc) {
        this.atributoBase = atributoBase;
        this.saveDc = saveDc;
        this.tabelaMagias = new LinkedHashMap<>();
    }

    public void adicionarMagia(int usosDiarios, String nomeMagia) {
        // Corrigido: As variáveis agora têm o mesmo nome
        if (!tabelaMagias.containsKey(usosDiarios)) {
            tabelaMagias.put(usosDiarios, new ArrayList<>());
        }
        tabelaMagias.get(usosDiarios).add(nomeMagia);
    }

    @Override
    public String getNome() {
        return "Spellcasting"; // Mudei para Spellcasting para a ficha ficar em inglês, mas pode voltar pra "magia" se quiser!
    }

    @Override
    public String getDescricao() {
        StringBuilder sb = new StringBuilder();
        
        // Corrigido: Consertei a quebra de linha e adicionei o espaço após a palavra "using "
        sb.append("The monster casts one of the following spells, requiring no material components and using ")
          .append(atributoBase)
          .append(" as the spellcasting ability (spell save DC ")
          .append(saveDc).append(").\n");

        for (Map.Entry<Integer, List<String>> entry : tabelaMagias.entrySet()) {
            int usos = entry.getKey();
            List<String> magias = entry.getValue();

            if (usos == 0) {
                sb.append("  At will: ");
            } else {
                sb.append("  ").append(usos).append("/day each: ");
            }
            
            sb.append(String.join(", ", magias)).append("\n");
        }
        
        return sb.toString();
    }

    public int getSaveDc() {
    return saveDc;
}

public void setSaveDc(int saveDc) {
    this.saveDc = saveDc;
}
}