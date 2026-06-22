package br.com.dnd.view;

import br.com.dnd.exception.MonstroNaoEncontradoException;
import br.com.dnd.model.EntidadeRPG;
import br.com.dnd.service.GerenciadorBestiarioService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.util.List;

public class TelaPrincipal extends JFrame {

    private GerenciadorBestiarioService service = new GerenciadorBestiarioService();
    private JTextArea areaTextoFichas;
    private JTable tabelaMonstros;
    private DefaultTableModel tableModel;
    private JTextField txtBusca;

    public TelaPrincipal() {
        setTitle("Bestiário D&D - Grimório");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // --- PAINEL DIVIDIDO (MASTER-DETAIL) ---
        
        // Esquerda: Painel que contém a Busca + Tabela
        JPanel painelEsquerda = new JPanel(new BorderLayout());

        // Barra de Busca
        JPanel painelBusca = new JPanel(new BorderLayout(5, 0));
        painelBusca.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        painelBusca.add(new JLabel("🔍 Buscar:"), BorderLayout.WEST);
        
        txtBusca = new JTextField();
        // O evento de teclado (KeyAdapter) faz a mágica de buscar a cada letra digitada!
        txtBusca.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                atualizarTabela();
            }
        });
        painelBusca.add(txtBusca, BorderLayout.CENTER);
        painelEsquerda.add(painelBusca, BorderLayout.NORTH);

        // Tabela
        String[] colunas = {"Nome", "CR", "HP", "CA"};
        tableModel = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tabelaMonstros = new JTable(tableModel);
        tabelaMonstros.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollTabela = new JScrollPane(tabelaMonstros);
        painelEsquerda.add(scrollTabela, BorderLayout.CENTER);

        // Direita: Ficha do Monstro
        areaTextoFichas = new JTextArea();
        areaTextoFichas.setEditable(false);
        areaTextoFichas.setFont(new Font("Monospaced", Font.PLAIN, 14));
        areaTextoFichas.setBackground(new Color(253, 245, 230)); // Corzinha de pergaminho de livro!
        JScrollPane scrollFicha = new JScrollPane(areaTextoFichas);

        // Divisor da tela (Atenção: mude de scrollTabela para painelEsquerda)
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, painelEsquerda, scrollFicha);
        splitPane.setDividerLocation(350);
        add(splitPane, BorderLayout.CENTER);

        // --- AÇÃO DE CLICAR NA TABELA ---
        tabelaMonstros.getSelectionModel().addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting() && tabelaMonstros.getSelectedRow() != -1) {
                String nomeSelecionado = (String) tableModel.getValueAt(tabelaMonstros.getSelectedRow(), 0);
                try {
                    EntidadeRPG monstro = service.buscarMonstro(nomeSelecionado);
                    areaTextoFichas.setText(monstro.formatarFicha());
                    areaTextoFichas.setCaretPosition(0); // Rola pro topo
                } catch (MonstroNaoEncontradoException ex) {
                    areaTextoFichas.setText("Monstro não encontrado.");
                }
            }
        });

        // --- PAINEL INFERIOR COM OS BOTÕES ---
        JPanel painelBotoes = new JPanel();
        JButton btnAdicionar = new JButton("Criar Monstro");
        JButton btnRemover = new JButton("Remover Selecionado");
        JButton btnAnalisar = new JButton("Analisar Balanceamento (CR)");
        JButton btnSalvar = new JButton("Salvar Arquivo");
        JButton btnCarregar = new JButton("Carregar Arquivo");

        painelBotoes.add(btnAdicionar);
        painelBotoes.add(btnRemover);
        painelBotoes.add(btnAnalisar);
        painelBotoes.add(btnSalvar);
        painelBotoes.add(btnCarregar);
        add(painelBotoes, BorderLayout.SOUTH);

        // --- AÇÕES DOS BOTÕES ---

        btnAdicionar.addActionListener(e -> {
            // Abre o nosso novo formulário limpo!
            TelaCadastroMonstro dialog = new TelaCadastroMonstro(this, service, this::atualizarTabela);
            dialog.setVisible(true);
        });

        btnRemover.addActionListener(e -> {
            int linha = tabelaMonstros.getSelectedRow();
            if (linha == -1) {
                JOptionPane.showMessageDialog(this, "Selecione um monstro na tabela para remover.");
                return;
            }
            String nome = (String) tableModel.getValueAt(linha, 0);
            try {
                service.removerMonstro(nome);
                atualizarTabela();
                areaTextoFichas.setText("");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        // O botão de Análise que fizemos antes
btnAnalisar.addActionListener(e -> {
            int linha = tabelaMonstros.getSelectedRow();
            if (linha == -1) {
                JOptionPane.showMessageDialog(this, "Selecione um monstro na tabela para analisar!");
                return;
            }
            
            // Pega o nome do monstro selecionado na JTable
            String nomeSelecionado = (String) tableModel.getValueAt(linha, 0);
            
            try {
                EntidadeRPG monstro = service.buscarMonstro(nomeSelecionado);
                BalanceadorCRService balanceador = new BalanceadorCRService();
                
                float crCalculado = balanceador.calcularCRReal(monstro);
                float crImaginado = monstro.getCr();

                // Se o cálculo bateu certinho com o esperado
                if (Math.abs(crCalculado - crImaginado) < 0.01) {
                    JOptionPane.showMessageDialog(this, 
                        "Ficha Perfeita!\nO monstro está perfeitamente balanceado para o CR " + (int)crImaginado + ".", 
                        "Análise Concluída", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    // Se houver desbalanceamento, abre o menu de 3 opções
                    String mensagem = "Desbalanceamento Detectado!\n\n"
                            + "CR Declarado: " + (int)crImaginado + "\n"
                            + "CR Real Calculado (Média): " + (int)crCalculado + "\n\n"
                            + "O que você deseja fazer?";

                    String[] opcoes = {
                        "Manter como está", 
                        "Corrigir apenas o CR para " + (int)crCalculado, 
                        "Balancear a ficha para CR " + (int)crImaginado
                    };
                    
                    int escolha = JOptionPane.showOptionDialog(this, mensagem, "Calculadora de CR",
                            JOptionPane.DEFAULT_OPTION, java.awt.TrayIcon.MessageType.WARNING.ordinal(), 
                            null, opcoes, opcoes[0]);

                    if (escolha == 1) { 
                        // Opção: Corrigir apenas o CR textual
                        monstro.setCr(crCalculado);
                        atualizarTabela();
                        areaTextoFichas.setText(monstro.formatarFicha());
                        JOptionPane.showMessageDialog(this, "O nível do monstro foi alterado para CR " + (int)crCalculado + "!");
                        
                    } else if (escolha == 2) { 
                        // Opção: Ajustar os números da ficha para caber no CR pretendido original
                        balanceador.forcarBalanceamento(monstro);
                        atualizarTabela();
                        areaTextoFichas.setText(monstro.formatarFicha());
                        JOptionPane.showMessageDialog(this, "Sucesso! Vida, CA e Ataques reajustados para o CR " + (int)crImaginado + ".");
                    }
                }
            } catch (MonstroNaoEncontradoException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnSalvar.addActionListener(e -> {
            try {
                service.salvarDados();
                JOptionPane.showMessageDialog(this, "Grimório salvo com sucesso!");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Erro de gravação: " + ex.getMessage(), "Erro IO", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnCarregar.addActionListener(e -> {
            try {
                service.carregarDados();
                atualizarTabela();
                JOptionPane.showMessageDialog(this, "Grimório carregado!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao carregar arquivo.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    // Alimenta a JTable com os dados filtrados
    private void atualizarTabela() {
        tableModel.setRowCount(0); // Limpa a tabela
        
        // Pega o que o usuário digitou na barra de busca (se a barra já existir)
        String termo = (txtBusca != null) ? txtBusca.getText() : "";
        
        // Chama o nosso método novo do Service!
        List<EntidadeRPG> lista = service.filtrarMonstrosPorNome(termo);
        
        for (EntidadeRPG m : lista) {
            tableModel.addRow(new Object[]{m.getNome(), m.getCr(), m.getHpBase(), m.getCa()});
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TelaPrincipal tela = new TelaPrincipal();
            tela.setVisible(true);
        });
    }
}