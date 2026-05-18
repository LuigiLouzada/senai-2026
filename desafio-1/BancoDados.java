package teladelogin; // Define o pacote da classe

import java.sql.Connection; // Importa a classe responsável pela conexão com o banco
import java.sql.DriverManager; // Importa a classe que gerencia o driver de conexão
import java.sql.SQLException; // Importa a classe usada para tratar erros SQL
import java.sql.Statement; // Importa a classe usada para executar comandos SQL simples

public class BancoDados { // Classe responsável pela conexão e criação do banco

private static final String URL = "jdbc:sqlite:usuarios.db";

    public static Connection conectar() throws SQLException { // Método que abre conexão com o banco

        return DriverManager.getConnection(URL); // Retorna uma conexão ativa com o banco usuarios.sql
    }

    public static void inicializarBanco() { // Método responsável por criar a tabela e inserir usuário padrão

        String criarTabela = "CREATE TABLE IF NOT EXISTS usuarios (" // Inicia o comando SQL de criação da tabela
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, " // Cria o campo id como chave primária automática
                + "usuario TEXT NOT NULL UNIQUE, " // Cria o campo usuario, obrigatório e sem repetição
                + "senha TEXT NOT NULL, " // Cria o campo senha, obrigatório
                + "tentativas_falhas INTEGER DEFAULT 0, " // Cria o campo para contar tentativas falhidas
                + "bloqueado BOOLEAN DEFAULT FALSE" // Cria o campo para indicar se o usuário está bloqueado
                + ");"; // Finaliza o comando SQL

        String inserirUsuario = "INSERT OR IGNORE INTO usuarios (usuario, senha) " // Inicia o comando para inserir usuário
                + "VALUES ('aluno', 'aluno');"; // Define o usuário padrão aluno e senha aluno

        try (Connection conn = conectar(); // Abre a conexão com o banco de dados
             Statement stmt = conn.createStatement()) { // Cria um executor de comandos SQL

            stmt.execute(criarTabela); // Executa o comando para criar a tabela

            try {
                stmt.execute("ALTER TABLE usuarios ADD COLUMN tentativas_falhas INTEGER DEFAULT 0;"); // Adiciona coluna tentativas_falhas se não existir
            } catch (SQLException e) {
                // Ignora erro se a coluna já existir
            }

            try {
                stmt.execute("ALTER TABLE usuarios ADD COLUMN bloqueado BOOLEAN DEFAULT FALSE;"); // Adiciona coluna bloqueado se não existir
            } catch (SQLException e) {
                // Ignora erro se a coluna já existir
            }

            stmt.execute(inserirUsuario); // Executa o comando para inserir o usuário padrão

        } catch (SQLException e) { // Captura possíveis erros de banco de dados

            System.out.println("Erro ao inicializar banco: " + e.getMessage()); // Mostra o erro no console
        }
    }
}