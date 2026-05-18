package teladelogin;

public class Main {

    public static void main(String[] args) {

        BancoDados.inicializarBanco();

        System.out.println("Banco inicializado com sucesso!");

        TelaLogin tela = new TelaLogin();

        tela.setVisible(true);
    }
}