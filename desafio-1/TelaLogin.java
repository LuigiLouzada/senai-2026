package teladelogin; // Define o pacote da classe

import javax.swing.JButton; // Importa o componente botão
import javax.swing.JFrame; // Importa a janela principal
import javax.swing.JLabel; // Importa o componente de texto
import javax.swing.JOptionPane; // Importa as caixas de mensagem
import javax.swing.JPanel; // Importa o painel da interface
import javax.swing.JPasswordField; // Importa o campo de senha
import javax.swing.JTextField; // Importa o campo de texto
import javax.swing.GroupLayout; // Importa o gerenciador de layout
import javax.swing.LayoutStyle; // Importa recursos de espaçamento do layout

public class TelaLogin extends JFrame { // Classe da tela de login, herdando JFrame

    private JPanel painelPrincipal; // Painel principal da tela

    private JLabel lblTitulo; // Texto do título da tela

    private JLabel lblUsuario; // Texto indicando o campo usuário

    private JLabel lblSenha; // Texto indicando o campo senha

    private JTextField txtUsuario; // Campo para digitar o usuário

    private JPasswordField txtSenha; // Campo para digitar a senha

    private JButton btnOk; // Botão para confirmar o login

    private JButton btnCancelar; // Botão para cancelar e fechar o sistema

    private UsuarioDAO usuarioDAO; // Objeto responsável por validar o login no banco

    private int tentativasGlobais; // Contador de tentativas falhidas globais

    public TelaLogin() { // Construtor da tela de login

        usuarioDAO = new UsuarioDAO(); // Cria o objeto de acesso aos dados do usuário

        inicializarComponentes(); // Monta todos os componentes da tela

        setLocationRelativeTo(null); // Centraliza a janela na tela

        setTitle("Tela de Login"); // Define o título da janela
    }

    private void inicializarComponentes() { // Método responsável por construir a interface gráfica

        painelPrincipal = new JPanel(); // Cria o painel principal

        lblTitulo = new JLabel(); // Cria o rótulo do título

        lblUsuario = new JLabel(); // Cria o rótulo do usuário

        lblSenha = new JLabel(); // Cria o rótulo da senha

        txtUsuario = new JTextField(); // Cria o campo de texto para usuário

        txtSenha = new JPasswordField(); // Cria o campo de senha

        btnOk = new JButton(); // Cria o botão OK

        btnCancelar = new JButton(); // Cria o botão Cancelar

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Encerra o programa ao fechar a janela

        painelPrincipal.setBorder(javax.swing.BorderFactory.createTitledBorder("Login do Sistema")); // Adiciona borda com título ao painel

        lblTitulo.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 18)); // Define fonte do título

        lblTitulo.setText("Acesso ao Sistema"); // Define o texto do título

        lblUsuario.setText("Usuário:"); // Define o texto do rótulo usuário

        lblSenha.setText("Senha:"); // Define o texto do rótulo senha

        btnOk.setText("OK"); // Define o texto do botão OK

        btnCancelar.setText("Cancelar"); // Define o texto do botão Cancelar

        btnOk.addActionListener(evento -> realizarLogin()); // Chama o método de login ao clicar em OK

        btnCancelar.addActionListener(evento -> System.exit(0)); // Fecha o sistema ao clicar em Cancelar

        GroupLayout layout = new GroupLayout(painelPrincipal); // Cria o layout para organizar os componentes

        painelPrincipal.setLayout(layout); // Define o layout no painel principal

        layout.setAutoCreateGaps(true); // Cria espaçamentos automáticos entre componentes

        layout.setAutoCreateContainerGaps(true); // Cria espaçamentos automáticos nas bordas do painel

        layout.setHorizontalGroup( // Define a organização horizontal dos componentes
            layout.createParallelGroup(GroupLayout.Alignment.CENTER) // Cria grupo centralizado
                .addComponent(lblTitulo) // Adiciona o título na horizontal
                .addGroup(layout.createSequentialGroup() // Cria uma linha para usuário
                    .addComponent(lblUsuario) // Adiciona o rótulo usuário
                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED) // Adiciona pequeno espaço
                    .addComponent(txtUsuario, 160, 160, 160)) // Adiciona o campo usuário com largura fixa
                .addGroup(layout.createSequentialGroup() // Cria uma linha para senha
                    .addComponent(lblSenha) // Adiciona o rótulo senha
                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED) // Adiciona pequeno espaço
                    .addComponent(txtSenha, 160, 160, 160)) // Adiciona o campo senha com largura fixa
                .addGroup(layout.createSequentialGroup() // Cria uma linha para botões
                    .addComponent(btnOk, 90, 90, 90) // Adiciona o botão OK com largura fixa
                    .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED) // Adiciona espaço entre os botões
                    .addComponent(btnCancelar, 90, 90, 90)) // Adiciona o botão Cancelar com largura fixa
        );

        layout.setVerticalGroup( // Define a organização vertical dos componentes
            layout.createSequentialGroup() // Cria um grupo sequencial de cima para baixo
                .addComponent(lblTitulo) // Adiciona o título no topo
                .addGap(20) // Adiciona espaço vertical
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE) // Cria linha do usuário
                    .addComponent(lblUsuario) // Adiciona o rótulo usuário
                    .addComponent(txtUsuario, 30, 30, 30)) // Adiciona o campo usuário
                .addGap(10) // Adiciona espaço vertical
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE) // Cria linha da senha
                    .addComponent(lblSenha) // Adiciona o rótulo senha
                    .addComponent(txtSenha, 30, 30, 30)) // Adiciona o campo senha
                .addGap(20) // Adiciona espaço vertical
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE) // Cria linha dos botões
                    .addComponent(btnOk) // Adiciona botão OK
                    .addComponent(btnCancelar)) // Adiciona botão Cancelar
        );

        add(painelPrincipal); // Adiciona o painel principal à janela

        pack(); // Ajusta o tamanho da janela automaticamente
    }

    private void realizarLogin() { // Método responsável por validar o login

        String usuarioDigitado = txtUsuario.getText(); // Captura o usuário digitado

        String senhaDigitada = new String(txtSenha.getPassword()); // Captura a senha digitada

        if (usuarioDigitado.isEmpty() || senhaDigitada.isEmpty()) { // Verifica se algum campo está vazio

            JOptionPane.showMessageDialog(this, "Preencha usuário e senha."); // Mostra aviso ao usuário

            return; // Interrompe o método para não continuar a validação
        }

        boolean loginValido = usuarioDAO.validarLogin(usuarioDigitado, senhaDigitada); // Valida o login no banco

        if (loginValido) { // Verifica se o login foi validado corretamente

            JOptionPane.showMessageDialog(this, "Login realizado com sucesso!"); // Mostra mensagem de sucesso

        } else { // Caso o login esteja incorreto ou usuário bloqueado

            tentativasGlobais++; // Incrementa tentativas globais falhadas

            if (tentativasGlobais >= 3) { // Verifica se atingiu o limite global

                JOptionPane.showMessageDialog(this, "Sistema bloqueado devido a múltiplas tentativas falhadas. Reinicie a aplicação."); // Mostra mensagem de bloqueio total

                btnOk.setEnabled(false); // Desabilita o botão OK

                txtUsuario.setEnabled(false); // Desabilita o campo usuário

                txtSenha.setEnabled(false); // Desabilita o campo senha

                return; // Interrompe o método
            }

            if (usuarioDAO.isUsuarioBloqueado(usuarioDigitado)) { // Verifica se o usuário está bloqueado

                JOptionPane.showMessageDialog(this, "Usuário bloqueado devido a múltiplas tentativas falhadas."); // Mostra mensagem de bloqueio
            } else {

                JOptionPane.showMessageDialog(this, "Usuário ou senha incorretos."); // Mostra mensagem de erro
            }
        }
    }
}