package teladelogin; // Define o pacote da classe

import java.sql.Connection; // Importa a classe de conexão com o banco
import java.sql.PreparedStatement; // Importa a classe para comandos SQL com parâmetros
import java.sql.ResultSet; // Importa a classe que armazena resultados de consultas
import java.sql.SQLException; // Importa a classe para tratar erros SQL

public class UsuarioDAO { // Classe responsável por acessar os dados dos usuários no banco

    public Usuario buscarPorUsuario(String nomeUsuario) { // Método que busca um usuário pelo login informado

        String sql = "SELECT * FROM usuarios WHERE usuario = ?"; // Consulta SQL com parâmetro para evitar SQL Injection

        try (Connection conn = BancoDados.conectar(); // Abre conexão com o banco
             PreparedStatement stmt = conn.prepareStatement(sql)) { // Prepara o comando SQL

            stmt.setString(1, nomeUsuario); // Substitui o primeiro ? pelo nome do usuário digitado

            ResultSet rs = stmt.executeQuery(); // Executa a consulta e guarda o resultado

            if (rs.next()) { // Verifica se encontrou algum usuário

                Usuario usuario = new Usuario(); // Cria um objeto Usuario

                usuario.setId(rs.getInt("id")); // Preenche o id com o valor vindo do banco

                usuario.setUsuario(rs.getString("usuario")); // Preenche o login com o valor vindo do banco

                usuario.setSenha(rs.getString("senha")); // Preenche a senha com o valor vindo do banco

                usuario.setTentativasFalhas(rs.getInt("tentativas_falhas")); // Preenche as tentativas falhidas

                usuario.setBloqueado(rs.getBoolean("bloqueado")); // Preenche o status de bloqueio

                return usuario; // Retorna o usuário encontrado
            }

        } catch (SQLException e) { // Captura erros de banco de dados

            System.out.println("Erro ao buscar usuário: " + e.getMessage()); // Mostra o erro no console
        }

        return null; // Retorna null caso o usuário não seja encontrado
    }

    public void atualizarUsuario(Usuario usuario) { // Método para atualizar os dados do usuário no banco

        String sql = "UPDATE usuarios SET tentativas_falhas = ?, bloqueado = ? WHERE id = ?"; // Comando SQL para atualizar

        try (Connection conn = BancoDados.conectar(); // Abre conexão com o banco
             PreparedStatement stmt = conn.prepareStatement(sql)) { // Prepara o comando SQL

            stmt.setInt(1, usuario.getTentativasFalhas()); // Define as tentativas falhidas

            stmt.setBoolean(2, usuario.isBloqueado()); // Define o status de bloqueio

            stmt.setInt(3, usuario.getId()); // Define o id do usuário

            stmt.executeUpdate(); // Executa a atualização

        } catch (SQLException e) { // Captura erros de banco de dados

            System.out.println("Erro ao atualizar usuário: " + e.getMessage()); // Mostra o erro no console
        }
    }

    public boolean validarLogin(String nomeUsuario, String senhaDigitada) { // Método que valida usuário e senha com bloqueio

        Usuario usuario = buscarPorUsuario(nomeUsuario); // Busca o usuário no banco de dados

        if (usuario == null) { // Verifica se o usuário não foi encontrado

            return false; // Retorna falso porque o usuário não existe
        }

        if (usuario.isBloqueado()) { // Verifica se o usuário está bloqueado

            return false; // Retorna falso porque o usuário está bloqueado
        }

        if (usuario.getSenha().equals(senhaDigitada)) { // Verifica se a senha está correta

            usuario.setTentativasFalhas(0); // Reseta as tentativas falhidas

            atualizarUsuario(usuario); // Atualiza o usuário no banco

            return true; // Retorna verdadeiro para login bem-sucedido
        } else { // Senha incorreta

            usuario.setTentativasFalhas(usuario.getTentativasFalhas() + 1); // Incrementa tentativas falhadas

            if (usuario.getTentativasFalhas() >= 3) { // Verifica se atingiu o limite

                usuario.setBloqueado(true); // Bloqueia o usuário
            }

            atualizarUsuario(usuario); // Atualiza o usuário no banco

            return false; // Retorna falso para login falhado
        }
    }

    public boolean isUsuarioBloqueado(String nomeUsuario) { // Método para verificar se o usuário está bloqueado

        Usuario usuario = buscarPorUsuario(nomeUsuario); // Busca o usuário

        return usuario != null && usuario.isBloqueado(); // Retorna true se o usuário existe e está bloqueado
    }
}