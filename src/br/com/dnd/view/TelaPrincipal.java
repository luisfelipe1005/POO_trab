package br.com.dnd.view;

import br.com.dnd.exception.MonstroNaoEncontradoException;
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
import java.io.IOException;
import javax.swing.*;

public class TelaPrincipal extends JFrame {

    private GerenciadorBestiarioService service = new GerenciadorBestiarioService();
    private JTextArea areaTextoFichas;

    public TelaPrincipal() {
        // Configurações básicas da Janela
        setTitle("Bestiário D&D - Teste Funcional");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centraliza na tela
        setLayout(new BorderLayout());

        // Área central para mostrar o texto das fichas
        areaTextoFichas = new JTextArea();
        areaTextoFichas.setEditable(false);
        areaTextoFichas.setFont(new Font("Monospaced", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(areaTextoFichas);
        add(scrollPane, BorderLayout.CENTER);

        // Painel inferior com os botões
        JPanel painelBotoes = new JPanel();
        
        JButton btnAdicionar = new JButton("Adicionar");
        JButton btnBuscar = new JButton("Buscar/Remover");
        JButton btnAnalisar = new JButton("Analisar Balanceamento");

        JButton btnListar = new JButton("Listar Todos");
        JButton btnSalvar = new JButton("Salvar Arquivo");
        JButton btnCarregar = new JButton("Carregar Arquivo");

        painelBotoes.add(btnAdicionar);
        painelBotoes.add(btnBuscar);
        painelBotoes.add(btnListar);
        painelBotoes.add(btnSalvar);
        painelBotoes.add(btnCarregar);
painelBotoes.add(btnAnalisar);
        
        add(painelBotoes, BorderLayout.SOUTH);

        // --- AÇÕES DOS BOTÕES ---

        btnListar.addActionListener(e -> atualizarTela());

        btnAdicionar.addActionListener(e -> adicionarMonstroDialog());

        btnBuscar.addActionListener(e -> buscarRemoverMonstroDialog());
btnAnalisar.addActionListener(e -> analisarMonstroDialog());

        // Tratamento de Exceções de Arquivo na Camada View!
        btnSalvar.addActionListener(e -> {
            try {
                service.salvarDados();
                JOptionPane.showMessageDialog(this, "Bestiário salvo com sucesso!");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Erro de gravação: " + ex.getMessage(), "Erro IO", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnCarregar.addActionListener(e -> {
            try {
                service.carregarDados();
                atualizarTela();
                JOptionPane.showMessageDialog(this, "Bestiário carregado com sucesso!");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Erro ao ler arquivo: " + ex.getMessage(), "Erro IO", JOptionPane.ERROR_MESSAGE);
            } catch (ClassNotFoundException ex) {
                JOptionPane.showMessageDialog(this, "Arquivo corrompido ou versão incompatível.", "Erro Classe", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    // Método para atualizar o texto do JTextArea com o Polimorfismo
    private void atualizarTela() {
        areaTextoFichas.setText(""); // Limpa a tela
        for (EntidadeRPG m : service.listarTodos()) {
            // A mágica do Polimorfismo acontece aqui! O Java sabe se deve usar as regras
            // de formatação do MortoVivo, do Construto, ou do Monstro Padrão.
            areaTextoFichas.append(m.formatarFicha() + "\n\n");
        }
    }

    // Caixinhas simples para adicionar
    private void adicionarMonstroDialog() {
        try {
            String nome = JOptionPane.showInputDialog(this, "Nome do Monstro:");
            if(nome == null || nome.trim().isEmpty()) return;

            String crStr = JOptionPane.showInputDialog(this, "Challenge Rating (0 a 30):");
            String hpStr = JOptionPane.showInputDialog(this, "HP Base:");

            // Pode estourar NumberFormatException (Unchecked) se digitar letras
            float cr = Float.parseFloat(crStr);
            int hp = Integer.parseInt(hpStr);

            String tamanho = JOptionPane.showInputDialog("Tamanho:");
int ca = Integer.parseInt(JOptionPane.showInputDialog("CA:"));
String velocidade = JOptionPane.showInputDialog("Velocidade:");
String sentidos = JOptionPane.showInputDialog("Sentidos:");
String linguagens = JOptionPane.showInputDialog("Linguagens:");

int str = Integer.parseInt(
    JOptionPane.showInputDialog(this, "STR:")
);

int dex = Integer.parseInt(
    JOptionPane.showInputDialog(this, "DEX:")
);

int con = Integer.parseInt(
    JOptionPane.showInputDialog(this, "CON:")
);

int intel = Integer.parseInt(
    JOptionPane.showInputDialog(this, "INT:")
);

int wis = Integer.parseInt(
    JOptionPane.showInputDialog(this, "WIS:")
);

int cha = Integer.parseInt(
    JOptionPane.showInputDialog(this, "CHA:")
);
            String[] opcoes = {"Monstro Padrão", "Morto Vivo", "construto"};
            int escolha = JOptionPane.showOptionDialog(this, "Tipo de Criatura:", "Escolha o Tipo", 
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, opcoes, opcoes[0]);
            
            EntidadeRPG novoMonstro;
            if (escolha == 1) {
                novoMonstro = new MortoVivo(
        nome,
         cr,
         tamanho,
         hp,
         ca,
         velocidade,
         sentidos,
         linguagens,
         str,
         dex,
         con,
         intel,
         wis,
         cha
    );
            } 

            else if(escolha == 2){
novoMonstro = new Construto(
           nome,
         cr,
         tamanho,
         hp,
         ca,
         velocidade,
         sentidos,
         linguagens,
         str,
         dex,
         con,
         intel,
         wis,
         cha
    );
            }
            else{
                novoMonstro = new MonstroPadrao(
          nome,
         cr,
         tamanho,
         hp,
         ca,
         velocidade,
         sentidos,
         linguagens,
         str,
         dex,
         con,
         intel,
         wis,
         cha
    );
            } 
            String ArmaAtt = ".";
            while(true){
         ArmaAtt = JOptionPane.showInputDialog(this, "Digite o nome da arma corpo a corpo que o monstro irá usar(caso não tenha nenhum ataque corpo a corpo a mais pra adicionar cancele):");
       
    if (ArmaAtt == null || ArmaAtt.trim().isEmpty()) {
        break;
    }
String inputAlcance = JOptionPane.showInputDialog(this, "Digite o alcance da arma corpo a corpo que o monstro está usando (cancele se quer padrão 0):");
int alcance = 0; 
if (inputAlcance != null && !inputAlcance.trim().isEmpty()) {
    alcance = Integer.parseInt(inputAlcance);
}
    
String inputBonus = JOptionPane.showInputDialog(this, "Digite o bônus de acerto da arma corpo a corpo que o monstro está usando (cancele para 0):");
int bonusataque = 0; 
if (inputBonus != null && !inputBonus.trim().isEmpty()) {
    bonusataque = Integer.parseInt(inputBonus);
}
String inputDano = JOptionPane.showInputDialog(this, "Digite o dano bônus da arma corpo a corpo que o monstro está usando (cancele para 0):");
int danobonus = 0; // Valor padrão
if (inputDano != null && !inputDano.trim().isEmpty()) {
    danobonus = Integer.parseInt(inputDano);
}



int dadodano = 0;

while (dadodano <= 0) {
    String inputDado = JOptionPane.showInputDialog(
        this,
        "Digite o dado de dano da arma corpo a corpo (ex: 6 para d6):"
    );

    try {
        if (inputDado == null || inputDado.trim().isEmpty()) {
            throw new NumberFormatException();
        }

        dadodano = Integer.parseInt(inputDado);

        if (dadodano <= 0) {
            JOptionPane.showMessageDialog(
                this,
                "Digite um número maior que zero.",
                "Erro",
                JOptionPane.ERROR_MESSAGE
            );
        }

    } catch (NumberFormatException ex) {
        JOptionPane.showMessageDialog(
            this,
            "Digite um número válido.",
            "Erro",
            JOptionPane.ERROR_MESSAGE
        );
    }
}

String inputNumDados = JOptionPane.showInputDialog(this, "Digite o número de dados da arma corpo a corpo (cancele para padrão 1):");
int numdados = 1; // Valor padrão
if (inputNumDados != null && !inputNumDados.trim().isEmpty()) {
    numdados = Integer.parseInt(inputNumDados);
}


String tipodano = null;
while (tipodano == null || tipodano.trim().isEmpty()) {
    tipodano = JOptionPane.showInputDialog(this, "Tipo de dano do Monstro (Obrigatório):");
    
    // Se o usuário cancelou ou deixou em branco, avisa ele do erro
    if (tipodano == null || tipodano.trim().isEmpty()) {
        JOptionPane.showMessageDialog(this, "O tipo de dano é obrigatório! Você não pode cancelar ou deixar em branco.", "Erro", JOptionPane.ERROR_MESSAGE);
    }
}
String inputMultAtaque = JOptionPane.showInputDialog(this, "Digite quantos ataques com a arma ele faz (cancele para padrão 1):");
int MultAtaque = 1; // Valor padrão
if (inputMultAtaque != null && !inputMultAtaque.trim().isEmpty()) {
    MultAtaque = Integer.parseInt(inputMultAtaque);
}


        AtaqueCorpoACorpo novoAtaquecorpo= new AtaqueCorpoACorpo( ArmaAtt,  alcance,  bonusataque,  danobonus,  dadodano,  numdados, tipodano, MultAtaque);
    novoMonstro.adicionarAcao(novoAtaquecorpo);    
    }








               ArmaAtt = ".";
            while(true){
         ArmaAtt = JOptionPane.showInputDialog(this, "Digite o nome da arma ranged que o monstro irá usar(caso não tenha nenhum ataque ranged a mais pra adicionar cancele):");
       
    if (ArmaAtt == null || ArmaAtt.trim().isEmpty()) {
        break;
    }
String inputAlcance = JOptionPane.showInputDialog(this, "Digite o alcance da arma ranged que o monstro está usando (cancele se quer padrão 0):");
int alcance = 0; 
if (inputAlcance != null && !inputAlcance.trim().isEmpty()) {
    alcance = Integer.parseInt(inputAlcance);
}
    
String inputBonus = JOptionPane.showInputDialog(this, "Digite o bônus de acerto da arma ranged que o monstro está usando (cancele para 0):");
int bonusataque = 0; 
if (inputBonus != null && !inputBonus.trim().isEmpty()) {
    bonusataque = Integer.parseInt(inputBonus);
}
String inputDano = JOptionPane.showInputDialog(this, "Digite o dano bônus da arma ranged que o monstro está usando (cancele para 0):");
int danobonus = 0; // Valor padrão
if (inputDano != null && !inputDano.trim().isEmpty()) {
    danobonus = Integer.parseInt(inputDano);
}



int dadodano = 0;

while (dadodano <= 0) {
    String inputDado = JOptionPane.showInputDialog(
        this,
        "Digite o dado de dano da arma ranged (ex: 6 para d6):"
    );

    try {
        if (inputDado == null || inputDado.trim().isEmpty()) {
            throw new NumberFormatException();
        }

        dadodano = Integer.parseInt(inputDado);

        if (dadodano <= 0) {
            JOptionPane.showMessageDialog(
                this,
                "Digite um número maior que zero.",
                "Erro",
                JOptionPane.ERROR_MESSAGE
            );
        }

    } catch (NumberFormatException ex) {
        JOptionPane.showMessageDialog(
            this,
            "Digite um número válido.",
            "Erro",
            JOptionPane.ERROR_MESSAGE
        );
    }
}

String inputNumDados = JOptionPane.showInputDialog(this, "Digite o número de dados da arma ranged (cancele para padrão 1):");
int numdados = 1; // Valor padrão
if (inputNumDados != null && !inputNumDados.trim().isEmpty()) {
    numdados = Integer.parseInt(inputNumDados);
}
String tipodano = null;
while (tipodano == null || tipodano.trim().isEmpty()) {
    tipodano = JOptionPane.showInputDialog(this, "Tipo de dano do Monstro (Obrigatório):");
    
    // Se o usuário cancelou ou deixou em branco, avisa ele do erro
    if (tipodano == null || tipodano.trim().isEmpty()) {
        JOptionPane.showMessageDialog(this, "O tipo de dano é obrigatório! Você não pode cancelar ou deixar em branco.", "Erro", JOptionPane.ERROR_MESSAGE);
    }
}

String inputMultAtaque = JOptionPane.showInputDialog(this, "Digite quantos ataques com a arma ele faz (cancele para padrão 1):");
int MultAtaque = 1; // Valor padrão
if (inputMultAtaque != null && !inputMultAtaque.trim().isEmpty()) {
    MultAtaque = Integer.parseInt(inputMultAtaque);
}



        AtaqueRanged novoAtaque= new AtaqueRanged( ArmaAtt,  alcance,  bonusataque,  danobonus,  dadodano,  numdados, tipodano, MultAtaque);
    novoMonstro.adicionarAcao(novoAtaque);    
    }

while (true) {
    String nomePassiva = JOptionPane.showInputDialog(
        this,
        "Digite o nome da habilidade passiva (cancelar para sair):"
    );

    if (nomePassiva == null || nomePassiva.trim().isEmpty()) {
        break;
    }

    String efeito = JOptionPane.showInputDialog(
        this,
        "Digite a descrição/efeito da habilidade:"
    );

    if (efeito == null || efeito.trim().isEmpty()) {
        JOptionPane.showMessageDialog(
            this,
            "Descrição obrigatória.",
            "Erro",
            JOptionPane.ERROR_MESSAGE
        );
        continue;
    }

    HabilidadePassiva passiva =
        new HabilidadePassiva(nomePassiva, efeito);

    novoMonstro.adicionarAcao(passiva);
}
while (true) {
    String nomeHab = JOptionPane.showInputDialog(
        this,
        "Digite o nome da habilidade ativa (cancelar para sair):"
    );

    if (nomeHab == null || nomeHab.trim().isEmpty()) {
        break; // sai do loop
    }

    String descricao = JOptionPane.showInputDialog(
        this,
        "Digite a descrição/efeito da habilidade:"
    );

    if (descricao == null || descricao.trim().isEmpty()) {
        JOptionPane.showMessageDialog(
            this,
            "Descrição obrigatória.",
            "Erro",
            JOptionPane.ERROR_MESSAGE
        );
        continue;
    }

    String[] tipos = {"Action", "Bonus Action", "Legendary Action"};
    String tipo = (String) JOptionPane.showInputDialog(
        this,
        "Tipo da habilidade:",
        "Selecione o tipo",
        JOptionPane.QUESTION_MESSAGE,
        null,
        tipos,
        tipos[0]
    );

    String recarga = JOptionPane.showInputDialog(
        this,
        "Recarga (Recharge 6, 4/Day, Costs 2 Actions ou vazio):"
    );

    HabilidadeAtiva habAtiva = new HabilidadeAtiva(
        nomeHab,
        descricao,
        tipo,
        recarga
    );

    novoMonstro.adicionarAcao(habAtiva);
}

int respostaMagia = JOptionPane.showConfirmDialog(
    this,
    "Esse monstro possui magias?",
    "Spellcasting",
    JOptionPane.YES_NO_OPTION
);

if (respostaMagia == JOptionPane.YES_OPTION) {

    String atributo = JOptionPane.showInputDialog(
        this,
        "Atributo base (INT, WIS, CHA):"
    );
    int saveDc=0;
if(atributo.equals("INT")){
saveDc=8+ intel+ novoMonstro.calcularBonusProficiencia();
}
else if(atributo.equals("WIS")){
saveDc=8+ wis+ novoMonstro.calcularBonusProficiencia();
}
else if(atributo.equals("CHA")){
saveDc=8+ cha+ novoMonstro.calcularBonusProficiencia();
}
else{
     JOptionPane.showMessageDialog(
                this,
                "Escolha um atributo valido.",
                "Erro",
                JOptionPane.ERROR_MESSAGE
            );
}

     Integer.parseInt(
        JOptionPane.showInputDialog(
            this,
            "Spell Save DC:"
        )
    );

    Magia magia = new Magia(atributo, saveDc);

    while (true) {
        String nomeMagia = JOptionPane.showInputDialog(
            this,
            "Nome da magia (cancelar para finalizar):"
        );

        if (nomeMagia == null || nomeMagia.trim().isEmpty()) {
            break;
        }

        int usos = Integer.parseInt(
            JOptionPane.showInputDialog(
                this,
                "Usos por dia (0 = At Will):"
            )
        );

        magia.adicionarMagia(usos, nomeMagia);
    }

    novoMonstro.adicionarAcao(magia);
}


            // Pode estourar RegraMatematicaInvalidaException (Unchecked) se CR < 0 ou CR > 30
            service.adicionarMonstro(novoMonstro);
            atualizarTela();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "O CR e o HP devem ser números inteiros!", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (RegraMatematicaInvalidaException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro de Regra de D&D", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Ocorreu um erro inesperado: " + ex.getMessage(), "Erro Genérico", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buscarRemoverMonstroDialog() {
        String nome = JOptionPane.showInputDialog(this, "Digite o nome do monstro para buscar:");
        if(nome == null || nome.trim().isEmpty()) return;

        try {
            // Pode estourar MonstroNaoEncontradoException (Checked)
            EntidadeRPG m = service.buscarMonstro(nome);
            
            int resposta = JOptionPane.showConfirmDialog(this, 
                "Monstro encontrado: " + m.getNome() + "\nDeseja REMOVÊ-LO do bestiário?", 
                "Ação", JOptionPane.YES_NO_OPTION);
                
            if(resposta == JOptionPane.YES_OPTION) {
                service.removerMonstro(nome);
                atualizarTela();
            }

        } catch (MonstroNaoEncontradoException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Não Encontrado", JOptionPane.INFORMATION_MESSAGE);
        }
    }
private void analisarMonstroDialog() {
    String nome = JOptionPane.showInputDialog(this, "Digite o nome do monstro para analisar:");
    if (nome == null || nome.trim().isEmpty()) return;

    try {
        EntidadeRPG monstro = service.buscarMonstro(nome);
        BalanceadorCRService balanceador = new BalanceadorCRService();
        
        float crCalculado = balanceador.calcularCRReal(monstro);
        float crImaginado = monstro.getCr();

        if (crCalculado == crImaginado) {
            JOptionPane.showMessageDialog(this, 
                "Tudo certo!\nO CR do monstro está perfeitamente balanceado (" + crCalculado + ").", 
                "Análise Concluída", JOptionPane.INFORMATION_MESSAGE);
        } else {
            // Aqui entra a sua regra maravilhosa das 3 opções!
            String mensagem = "Desbalanceamento detectado!\n\n"
                    + "CR Esperado: " + crImaginado + "\n"
                    + "CR Calculado (Real): " + crCalculado + "\n\n"
                    + "O que deseja fazer?";

            String[] opcoes = {"Manter como está", "Corrigir CR para " + crCalculado, "Balancear Ficha para " + crImaginado};
            
            int escolha = JOptionPane.showOptionDialog(this, mensagem, "Balanceamento",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, opcoes, opcoes[0]);

            if (escolha == 1) {
                // Opção 2: Atualiza o CR para a realidade
                monstro.setCr(crCalculado);
                atualizarTela();
                JOptionPane.showMessageDialog(this, "O CR do monstro foi atualizado para " + crCalculado + "!");
                
            } else if (escolha == 2) {
                // Opção 3: Modifica a vida e CA para bater com o CR que o mestre queria
                balanceador.balancearFichaParaCR(monstro);
                atualizarTela();
                JOptionPane.showMessageDialog(this, "A ficha foi reescrita e balanceada para o CR " + crImaginado + "!");
            }
        }
    } catch (MonstroNaoEncontradoException ex) {
        JOptionPane.showMessageDialog(this, ex.getMessage(), "Não Encontrado", JOptionPane.ERROR_MESSAGE);
    }
}




    // O Ponto de Entrada do Aplicativo
    public static void main(String[] args) {
        // Padrão do Swing para rodar a interface de forma segura
        SwingUtilities.invokeLater(() -> {
            TelaPrincipal tela = new TelaPrincipal();
            tela.setVisible(true);
        });
    }
}