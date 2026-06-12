package br.com.dnd.view;

import br.com.dnd.exception.RegraMatematicaInvalidaException;
import br.com.dnd.model.AtaqueCorpoACorpo;
import br.com.dnd.model.AtaqueRanged;
import br.com.dnd.model.Construto;
import br.com.dnd.model.EntidadeRPG;
import br.com.dnd.model.HabilidadeAtiva;
import br.com.dnd.model.HabilidadePassiva;
import br.com.dnd.model.Magia;
import br.com.dnd.model.MonstroPadrao;
import br.com.dnd.model.MortoVivo;
import br.com.dnd.service.BalanceadorCRService;
import br.com.dnd.service.GerenciadorBestiarioService;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

public class TelaCadastroMonstro extends JDialog {

    private GerenciadorBestiarioService service;
    private Runnable onSaveCallback;

    // Campos do formulário básico
    private JTextField txtNome, txtCr, txtTamanho, txtHp, txtCa, txtVelocidade, txtSentidos, txtLinguagens;
    private JTextField txtStr, txtDex, txtCon, txtInt, txtWis, txtCha;
    private JComboBox<String> cbTipo;

    // Listas para armazenar as ações em memória antes de salvar
    private DefaultListModel<String> listModelAcoes;
    private List<AtaqueCorpoACorpo> listaMelee = new ArrayList<>();
    private List<AtaqueRanged> listaRanged = new ArrayList<>();
    private List<HabilidadePassiva> listaPassivas = new ArrayList<>();
    private List<HabilidadeAtiva> listaAtivas = new ArrayList<>();
    private List<Magia> listaMagias = new ArrayList<>();

    public TelaCadastroMonstro(JFrame parent, GerenciadorBestiarioService service, Runnable onSaveCallback) {
        super(parent, "Criar Novo Monstro", true);
        this.service = service;
        this.onSaveCallback = onSaveCallback;

        setSize(550, 700);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        // Criando o sistema de Abas
        JTabbedPane painelAbas = new JTabbedPane();
        painelAbas.addTab("Atributos Básicos", criarPainelBasico());
        painelAbas.addTab("Ações e Habilidades", criarPainelAcoes());

        add(painelAbas, BorderLayout.CENTER);

        // Painel de Botões (Rodapé)
        JPanel painelBotoes = new JPanel();
        JButton btnSalvar = new JButton("Salvar Monstro Completo");
        JButton btnCancelar = new JButton("Cancelar");

        btnSalvar.addActionListener(e -> salvarMonstro());
        btnCancelar.addActionListener(e -> dispose());

        painelBotoes.add(btnSalvar);
        painelBotoes.add(btnCancelar);
        add(painelBotoes, BorderLayout.SOUTH);
    }

    // --- ABA 1: FORMULÁRIO BÁSICO ---
    private JPanel criarPainelBasico() {
        JPanel painelForm = new JPanel(new GridLayout(15, 2, 10, 10));
        painelForm.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        painelForm.add(new JLabel("Nome do Monstro:")); txtNome = new JTextField(); painelForm.add(txtNome);
        painelForm.add(new JLabel("Tipo de Criatura:")); cbTipo = new JComboBox<>(new String[]{"Monstro Padrão", "Morto Vivo", "Construto"}); painelForm.add(cbTipo);
        painelForm.add(new JLabel("Challenge Rating (CR):"));
        
        // Criamos um sub-painel para colocar o campo de CR e o botão lado a lado
        JPanel painelCr = new JPanel(new BorderLayout(5, 0));
        txtCr = new JTextField("0");
        JButton btnAutoFill = new JButton("⚡ Auto");
        painelCr.add(txtCr, BorderLayout.CENTER);
        painelCr.add(btnAutoFill, BorderLayout.EAST);
        painelForm.add(painelCr);
        
        // Ação do botão
        btnAutoFill.addActionListener(e -> preencherAtributosAutomaticamente());
        painelForm.add(new JLabel("HP Base:")); txtHp = new JTextField("10"); painelForm.add(txtHp);
        painelForm.add(new JLabel("Armor Class (CA):")); txtCa = new JTextField("10"); painelForm.add(txtCa);
        painelForm.add(new JLabel("Tamanho:")); txtTamanho = new JTextField("Medium"); painelForm.add(txtTamanho);
        painelForm.add(new JLabel("Velocidade (ft):")); txtVelocidade = new JTextField("30"); painelForm.add(txtVelocidade);
        painelForm.add(new JLabel("Sentidos:")); txtSentidos = new JTextField("passive Perception 10"); painelForm.add(txtSentidos);
        painelForm.add(new JLabel("Linguagens:")); txtLinguagens = new JTextField("-"); painelForm.add(txtLinguagens);

        painelForm.add(new JLabel("FOR (STR):")); txtStr = new JTextField("10"); painelForm.add(txtStr);
        painelForm.add(new JLabel("DES (DEX):")); txtDex = new JTextField("10"); painelForm.add(txtDex);
        painelForm.add(new JLabel("CON (CON):")); txtCon = new JTextField("10"); painelForm.add(txtCon);
        painelForm.add(new JLabel("INT (INT):")); txtInt = new JTextField("10"); painelForm.add(txtInt);
        painelForm.add(new JLabel("SAB (WIS):")); txtWis = new JTextField("10"); painelForm.add(txtWis);
        painelForm.add(new JLabel("CAR (CHA):")); txtCha = new JTextField("10"); painelForm.add(txtCha);

        return painelForm;
    }

    // --- ABA 2: AÇÕES E HABILIDADES ---
    private JPanel criarPainelAcoes() {
        JPanel painelAcoes = new JPanel(new BorderLayout(10, 10));
        painelAcoes.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        listModelAcoes = new DefaultListModel<>();
        JList<String> listAcoes = new JList<>(listModelAcoes);
        painelAcoes.add(new JScrollPane(listAcoes), BorderLayout.CENTER);

        JPanel painelBotoesAcao = new JPanel(new GridLayout(5, 1, 5, 5));
        JButton btnAddMelee = new JButton("+ Ataque Corpo a Corpo");
        JButton btnAddRanged = new JButton("+ Ataque Ranged");
        JButton btnAddPassiva = new JButton("+ Habilidade Passiva");
        JButton btnAddAtiva = new JButton("+ Habilidade Ativa");
        JButton btnAddMagia = new JButton("+ Magias (Spellcasting)");

        btnAddMelee.addActionListener(e -> adicionarMelee());
        btnAddRanged.addActionListener(e -> adicionarRanged());
        btnAddPassiva.addActionListener(e -> adicionarPassiva());
        btnAddAtiva.addActionListener(e -> adicionarAtiva());
        btnAddMagia.addActionListener(e -> adicionarMagia());

        painelBotoesAcao.add(btnAddMelee);
        painelBotoesAcao.add(btnAddRanged);
        painelBotoesAcao.add(btnAddPassiva);
        painelBotoesAcao.add(btnAddAtiva);
        painelBotoesAcao.add(btnAddMagia);

        painelAcoes.add(painelBotoesAcao, BorderLayout.EAST);

        return painelAcoes;
    }

    // --- MÉTODOS DE CADASTRO DE AÇÕES (Refatorados do sistema antigo) ---
    private void adicionarMelee() {
        try {
            String nome = JOptionPane.showInputDialog(this, "Nome da arma corpo a corpo:");
            if (nome == null || nome.trim().isEmpty()) return;

            int alcance = parseInputDefault("Alcance da arma (padrão 5):", 5);
            int bonusAtaque = parseInputDefault("Bônus de acerto (padrão 0):", 0);
            int danoBonus = parseInputDefault("Dano bônus fixo (padrão 0):", 0);
            int dadoDano = parseInputDefault("Dado de dano (ex: 6 para d6):", 6);
            int numDados = parseInputDefault("Número de dados (padrão 1):", 1);
            
            String tipoDano = JOptionPane.showInputDialog(this, "Tipo de dano (ex: Slashing):");
            if (tipoDano == null || tipoDano.trim().isEmpty()) tipoDano = "Slashing";

            int multAtaque = parseInputDefault("Quantos ataques faz (padrão 1):", 1);

            AtaqueCorpoACorpo ataque = new AtaqueCorpoACorpo(nome, alcance, bonusAtaque, danoBonus, dadoDano, numDados, tipoDano, multAtaque);
            listaMelee.add(ataque);
            listModelAcoes.addElement("[Melee] " + nome + " (" + numDados + "d" + dadoDano + " + " + danoBonus + " " + tipoDano + ")");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Entrada inválida.");
        }
    }
private void preencherAtributosAutomaticamente() {
        try {
            // Pega o CR digitado (troca vírgula por ponto para evitar erro)
            float cr = Float.parseFloat(txtCr.getText().replace(",", "."));
            
            BalanceadorCRService balanceador = new BalanceadorCRService();
            int[] statusRecomendados = balanceador.obterStatusBasePorCR(cr);

            if (statusRecomendados != null) {
                // Preenche Vida e CA
                txtHp.setText(String.valueOf(statusRecomendados[0]));
                txtCa.setText(String.valueOf(statusRecomendados[1]));

                // Pega o Atributo Principal que calculamos no Service
                String atributoAlto = String.valueOf(statusRecomendados[2]);

                // Distribui os atributos de forma lógica para um monstro "Brutamontes" padrão
                txtStr.setText(atributoAlto); // Força alta para bater
                txtCon.setText(atributoAlto); // Constituição alta para aguentar pancada
                txtDex.setText("14");         // Destreza mediana
                txtInt.setText("10");         // Inteligência base
                txtWis.setText("12");         // Sabedoria um pouco acima da base
                txtCha.setText("8");          // Carisma baixo

                JOptionPane.showMessageDialog(this, "Status preenchidos com base no CR " + cr + "!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "CR não encontrado na tabela oficial. Use valores como 0.25, 0.5, 1, 2...", "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Digite um número de CR válido primeiro!", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void adicionarRanged() {
        try {
            String nome = JOptionPane.showInputDialog(this, "Nome da arma Ranged:");
            if (nome == null || nome.trim().isEmpty()) return;

            int alcance = parseInputDefault("Alcance da arma (padrão 30):", 30);
            int bonusAtaque = parseInputDefault("Bônus de acerto (padrão 0):", 0);
            int danoBonus = parseInputDefault("Dano bônus fixo (padrão 0):", 0);
            int dadoDano = parseInputDefault("Dado de dano (ex: 8 para d8):", 8);
            int numDados = parseInputDefault("Número de dados (padrão 1):", 1);
            
            String tipoDano = JOptionPane.showInputDialog(this, "Tipo de dano (ex: Piercing):");
            if (tipoDano == null || tipoDano.trim().isEmpty()) tipoDano = "Piercing";

            int multAtaque = parseInputDefault("Quantos ataques faz (padrão 1):", 1);

            AtaqueRanged ataque = new AtaqueRanged(nome, alcance, bonusAtaque, danoBonus, dadoDano, numDados, tipoDano, multAtaque);
            listaRanged.add(ataque);
            listModelAcoes.addElement("[Ranged] " + nome + " (" + numDados + "d" + dadoDano + " + " + danoBonus + " " + tipoDano + ")");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Entrada inválida.");
        }
    }

    private void adicionarPassiva() {
        String nome = JOptionPane.showInputDialog(this, "Nome da habilidade passiva:");
        if (nome == null || nome.trim().isEmpty()) return;
        
        String efeito = JOptionPane.showInputDialog(this, "Descrição/efeito da habilidade:");
        if (efeito != null && !efeito.trim().isEmpty()) {
            listaPassivas.add(new HabilidadePassiva(nome, efeito));
            listModelAcoes.addElement("[Passiva] " + nome);
        }
    }

    private void adicionarAtiva() {
        String nome = JOptionPane.showInputDialog(this, "Nome da habilidade ativa:");
        if (nome == null || nome.trim().isEmpty()) return;

        String descricao = JOptionPane.showInputDialog(this, "Descrição/efeito da habilidade:");
        if (descricao == null || descricao.trim().isEmpty()) return;

        String[] tipos = {"Action", "Bonus Action", "Legendary Action"};
        String tipo = (String) JOptionPane.showInputDialog(this, "Tipo da habilidade:", "Selecione", JOptionPane.QUESTION_MESSAGE, null, tipos, tipos[0]);
        String recarga = JOptionPane.showInputDialog(this, "Recarga (Ex: Recharge 5-6 ou deixe vazio):");

        listaAtivas.add(new HabilidadeAtiva(nome, descricao, tipo, recarga == null ? "" : recarga));
        listModelAcoes.addElement("[Ativa - " + tipo + "] " + nome);
    }

    private void adicionarMagia() {
        try {
            String atributo = JOptionPane.showInputDialog(this, "Atributo base (INT, WIS, CHA):");
            if (atributo == null || atributo.trim().isEmpty()) return;

            int saveDc = parseInputDefault("Spell Save DC (ex: 15):", 15);
            Magia magia = new Magia(atributo.toUpperCase(), saveDc);

            while (true) {
                String nomeMagia = JOptionPane.showInputDialog(this, "Nome da magia (deixe vazio para terminar):");
                if (nomeMagia == null || nomeMagia.trim().isEmpty()) break;

                int usos = parseInputDefault("Usos por dia (0 = At Will):", 0);
                magia.adicionarMagia(usos, nomeMagia);
            }

            listaMagias.add(magia);
            listModelAcoes.addElement("[Magias] Baseado em " + atributo.toUpperCase() + " (DC " + saveDc + ")");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao adicionar magia.");
        }
    }

    // Utilitário para evitar repetição de Try-Catch nos JOptionPanes
    private int parseInputDefault(String mensagem, int valorPadrao) {
        String input = JOptionPane.showInputDialog(this, mensagem);
        if (input == null || input.trim().isEmpty()) return valorPadrao;
        try { return Integer.parseInt(input); } catch (NumberFormatException e) { return valorPadrao; }
    }

    // --- SALVAMENTO FINAL ---
    private void salvarMonstro() {
        try {
            String nome = txtNome.getText();
            if (nome.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "O nome é obrigatório!");
                return;
            }

            float cr = Float.parseFloat(txtCr.getText());
            int hp = Integer.parseInt(txtHp.getText());
            int ca = Integer.parseInt(txtCa.getText());
            int str = Integer.parseInt(txtStr.getText());
            int dex = Integer.parseInt(txtDex.getText());
            int con = Integer.parseInt(txtCon.getText());
            int intel = Integer.parseInt(txtInt.getText());
            int wis = Integer.parseInt(txtWis.getText());
            int cha = Integer.parseInt(txtCha.getText());
            String tipo = (String) cbTipo.getSelectedItem();

            // Instancia o monstro de acordo com o Polimorfismo
            EntidadeRPG novoMonstro;
            if ("Morto Vivo".equals(tipo)) {
                novoMonstro = new MortoVivo(nome, cr, txtTamanho.getText(), hp, ca, txtVelocidade.getText(), txtSentidos.getText(), txtLinguagens.getText(), str, dex, con, intel, wis, cha);
            } else if ("Construto".equals(tipo)) {
                novoMonstro = new Construto(nome, cr, txtTamanho.getText(), hp, ca, txtVelocidade.getText(), txtSentidos.getText(), txtLinguagens.getText(), str, dex, con, intel, wis, cha);
            } else {
                novoMonstro = new MonstroPadrao(nome, cr, txtTamanho.getText(), hp, ca, txtVelocidade.getText(), txtSentidos.getText(), txtLinguagens.getText(), str, dex, con, intel, wis, cha);
            }

            // Injeta todas as ações cadastradas na aba 2
            listaMelee.forEach(novoMonstro::adicionarAcao);
            listaRanged.forEach(novoMonstro::adicionarAcao);
            listaPassivas.forEach(novoMonstro::adicionarAcao);
            listaAtivas.forEach(novoMonstro::adicionarAcao);
            listaMagias.forEach(novoMonstro::adicionarAcao);

            // Salva no banco/lista em memória
            service.adicionarMonstro(novoMonstro);
            onSaveCallback.run(); 
            
            JOptionPane.showMessageDialog(this, "Monstro '" + nome + "' criado com sucesso com todas as ações!");
            dispose();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Certifique-se de que CR, HP, CA e Atributos são números válidos!", "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (RegraMatematicaInvalidaException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro de Regra", JOptionPane.WARNING_MESSAGE);
        }
    }
}