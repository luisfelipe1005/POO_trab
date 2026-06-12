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

    public TelaPrincipal() {
        setTitle("Bestiário D&D - Grimório");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // --- PAINEL DIVIDIDO (MASTER-DETAIL) ---
        
        // Esquerda: Tabela de Monstros
        String[] colunas = {"Nome", "CR", "HP", "CA"};
        tableModel = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; } // Impede edição direta
        };
        tabelaMonstros = new JTable(tableModel);
        tabelaMonstros.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollTabela = new JScrollPane(tabelaMonstros);

        // Direita: Ficha do Monstro
        areaTextoFichas = new JTextArea();
        areaTextoFichas.setEditable(false);
        areaTextoFichas.setFont(new Font("Monospaced", Font.PLAIN, 14));
        areaTextoFichas.setBackground(new Color(253, 245, 230)); // Corzinha de pergaminho de livro!
        JScrollPane scrollFicha = new JScrollPane(areaTextoFichas);

        // Divisor da tela
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, scrollTabela, scrollFicha);
        splitPane.setDividerLocation(350); // Largura da tabela
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
            // Coloque aqui a chamada para o seu BalanceadorCRService 
            // (Passando o monstro selecionado da mesma forma que fizemos no código de remover)
            JOptionPane.showMessageDialog(this, "A funcionalidade de Análise de CR entrará em ação aqui!");
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

    // Alimenta a JTable com os dados da lista polimórfica
    private void atualizarTabela() {
        tableModel.setRowCount(0); // Limpa a tabela
        List<EntidadeRPG> lista = service.listarTodos();
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