class Produto {

    // Identificador único do produto.
    private int id;

    // Nome do doce.
    private String nome;

    // Preço do doce.
    private double preco;

    // Construtor usado para criar cada produto.
    public Produto(int id, String nome, double preco) {
        this.id = id;
        this.nome = nome;
        this.preco = preco;
    }

    // Retorna o ID do produto.
    public int getId() {
        return id;
    }

    // Retorna o nome do produto.
    public String getNome() {
        return nome;
    }

    // Retorna o preço do produto.
    public double getPreco() {
        return preco;
    }
}