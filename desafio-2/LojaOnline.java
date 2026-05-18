import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpExchange;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class LojaOnline {

    static List<Produto> produtos = new ArrayList<>();
    static Map<Integer, Integer> carrinho = new HashMap<>();

    public static void main(String[] args) throws Exception {

        // Adiciona os doces que serão exibidos na loja.
        produtos.add(new Produto(1, "Brigadeiro", 3.50));
        produtos.add(new Produto(2, "Beijinho", 3.00));
        produtos.add(new Produto(3, "Brownie", 6.50));
        produtos.add(new Produto(4, "Cupcake", 7.00));
        produtos.add(new Produto(5, "Pão de Mel", 5.50));
        produtos.add(new Produto(6, "Trufa de Chocolate", 4.50));

        // Cria o servidor local na porta 8080.
        HttpServer servidor = HttpServer.create(new InetSocketAddress(8080), 0);

        // Cria a rota principal da loja.
        servidor.createContext("/", exchange -> {
            try {
                paginaInicial(exchange);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        // Cria a rota usada pelo botão "Adicionar ao Carrinho".
        servidor.createContext("/adicionar", exchange -> {
            try {
                adicionarAoCarrinho(exchange);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        // Cria a rota para visualizar o carrinho.
        servidor.createContext("/carrinho", exchange -> {
            try {
                visualizarCarrinho(exchange);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        // Cria a rota para remover item do carrinho.
        servidor.createContext("/remover", exchange -> {
            try {
                removerDoCarrinho(exchange);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        // Cria a rota para alterar quantidade.
        servidor.createContext("/alterar", exchange -> {
            try {
                alterarQuantidade(exchange);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        // Inicia o servidor.
        servidor.start();

        // Mostra o endereço no console.
        System.out.println("Loja online rodando em: http://localhost:8080");
    }

    public static void paginaInicial(HttpExchange exchange) throws Exception {

        // Monta o HTML da página.
        StringBuilder html = new StringBuilder();

        html.append("<!DOCTYPE html>");
        html.append("<html lang='pt-br'>");
        html.append("<head>");
        html.append("<meta charset='UTF-8'>");
        html.append("<title>Doceria Online Java</title>");

        // Estilo visual da página.
        html.append("<style>");
        html.append("body { font-family: Arial; background:#fff3f3; margin:0; padding:0; }");
        html.append("header { background:#8B1E3F; color:white; padding:20px; text-align:center; }");
        html.append(".container { width:85%; margin:30px auto; display:grid; grid-template-columns:repeat(3,1fr); gap:20px; }");
        html.append(".produto { background:white; padding:20px; border-radius:12px; box-shadow:0 0 8px #d9a7b0; text-align:center; }");
        html.append(".produto h2 { color:#8B1E3F; }");
        html.append(".preco { color:#2e7d32; font-size:20px; font-weight:bold; }");
        html.append("button { background:#D81B60; color:white; border:none; padding:10px 15px; border-radius:6px; cursor:pointer; }");
        html.append("button:hover { background:#AD1457; }");
        html.append("</style>");

        html.append("</head>");
        html.append("<body>");

        html.append("<header>");
        html.append("<h1>Doceria Online Java</h1>");
        html.append("<p>Exemplo base para atividade de carrinho de compras</p>");
        html.append("<a href='/carrinho' style='color:white; margin-left:20px;'>Ver Carrinho</a>");
        html.append("</header>");

        html.append("<div class='container'>");

        // Percorre todos os doces cadastrados.
        for (Produto produto : produtos) {

            html.append("<div class='produto'>");

            // Mostra o nome do doce.
            html.append("<h2>").append(produto.getNome()).append("</h2>");

            // Mostra o preço do doce.
            html.append("<p class='preco'>R$ ").append(String.format("%.2f", produto.getPreco())).append("</p>");

            // Botão que envia o id do produto para a rota /adicionar.
            html.append("<a href='/adicionar?id=").append(produto.getId()).append("'>");
            html.append("<button>Adicionar ao Carrinho</button>");
            html.append("</a>");

            html.append("</div>");
        }

        html.append("</div>");
        html.append("</body>");
        html.append("</html>");

        enviarResposta(exchange, html.toString());
    }

    public static void adicionarAoCarrinho(HttpExchange exchange) throws Exception {

        // Captura o ID do produto pela URL.
        String query = exchange.getRequestURI().getQuery();
        if (query != null && query.startsWith("id=")) {
            int id = Integer.parseInt(query.substring(3));

            // Procura o produto correspondente.
            Produto produto = buscarProdutoPorId(id);
            if (produto != null) {
                // Verifica se o produto já existe no carrinho.
                // Se existir, aumenta a quantidade; senão, adiciona como novo.
                carrinho.put(id, carrinho.getOrDefault(id, 0) + 1);
            }
        }

        // Redireciona para a página do carrinho.
        exchange.getResponseHeaders().set("Location", "/carrinho");
        exchange.sendResponseHeaders(302, -1);
    }

    public static void visualizarCarrinho(HttpExchange exchange) throws Exception {

        // Monta o HTML da página do carrinho.
        StringBuilder html = new StringBuilder();

        html.append("<!DOCTYPE html>");
        html.append("<html lang='pt-br'>");
        html.append("<head>");
        html.append("<meta charset='UTF-8'>");
        html.append("<title>Seu Carrinho</title>");

        // Estilo visual da página.
        html.append("<style>");
        html.append("body { font-family: Arial; background:#fff3f3; margin:0; padding:0; }");
        html.append("header { background:#8B1E3F; color:white; padding:20px; text-align:center; }");
        html.append(".container { width:85%; margin:30px auto; }");
        html.append(".produto { background:white; padding:20px; border-radius:12px; box-shadow:0 0 8px #d9a7b0; margin-bottom:10px; }");
        html.append(".produto h2 { color:#8B1E3F; }");
        html.append(".preco { color:#2e7d32; font-size:20px; font-weight:bold; }");
        html.append(".quantidade { margin:10px 0; }");
        html.append("button { background:#D81B60; color:white; border:none; padding:10px 15px; border-radius:6px; cursor:pointer; }");
        html.append("button:hover { background:#AD1457; }");
        html.append("</style>");

        html.append("</head>");
        html.append("<body>");

        html.append("<header>");
        html.append("<h1>Seu Carrinho</h1>");
        html.append("<a href='/' style='color:white; margin-left:20px;'>Continuar Comprando</a>");
        html.append("</header>");

        html.append("<div class='container'>");

        // Verifica se o carrinho está vazio.
        if (carrinho.isEmpty()) {
            html.append("<p>Seu carrinho está vazio.</p>");
        } else {
            // Percorre os itens do carrinho.
            for (Map.Entry<Integer, Integer> entry : carrinho.entrySet()) {
                int idProduto = entry.getKey();
                int quantidade = entry.getValue();

                // Busca o produto correspondente pelo ID.
                Produto produto = buscarProdutoPorId(idProduto);

                if (produto != null) {
                    html.append("<div class='produto'>");

                    // Mostra o nome do doce.
                    html.append("<h2>").append(produto.getNome()).append("</h2>");

                    // Mostra o preço do doce.
                    html.append("<p class='preco'>R$ ").append(String.format("%.2f", produto.getPreco())).append("</p>");

                    // Mostra a quantidade no carrinho e o subtotal.
                    double subtotal = produto.getPreco() * quantidade;
                    html.append("<p class='quantidade'>Quantidade: ").append(quantidade).append(" | Subtotal: R$ ").append(String.format("%.2f", subtotal)).append("</p>");

                    // Formulário para alterar a quantidade.
                    html.append("<form action='/alterar' method='get' style='display:inline;'>");
                    html.append("<input type='hidden' name='id' value='").append(produto.getId()).append("'>");
                    html.append("<input type='number' name='quantidade' value='").append(quantidade).append("' min='1' style='width:60px;'>");
                    html.append("<button type='submit'>Alterar</button>");
                    html.append("</form>");

                    // Botão para remover o item do carrinho.
                    html.append("<a href='/remover?id=").append(produto.getId()).append("' style='margin-left:10px;'>");
                    html.append("<button>Remover</button>");
                    html.append("</a>");

                    html.append("</div>");
                }
            }

            // Mostra o total da compra.
            double total = calcularTotalCarrinho();
            html.append("<h2>Total: R$ ").append(String.format("%.2f", total)).append("</h2>");
        }

        html.append("</div>");
        html.append("</body>");
        html.append("</html>");

        enviarResposta(exchange, html.toString());
    }

    public static void removerDoCarrinho(HttpExchange exchange) throws Exception {

        // Obtém o ID do produto pela URL.
        String query = exchange.getRequestURI().getQuery();
        int idProduto = Integer.parseInt(query.split("=")[1]);

        // Remove o produto do carrinho.
        carrinho.remove(idProduto);

        // Redireciona para a página do carrinho.
        exchange.getResponseHeaders().set("Location", "/carrinho");
        exchange.sendResponseHeaders(302, -1);
    }

    public static void alterarQuantidade(HttpExchange exchange) throws Exception {

        // Obtém o ID e a nova quantidade pela URL.
        String query = exchange.getRequestURI().getQuery();
        String[] params = query.split("&");
        int idProduto = Integer.parseInt(params[0].split("=")[1]);
        int novaQuantidade = Integer.parseInt(params[1].split("=")[1]);

        // Atualiza a quantidade no carrinho.
        if (novaQuantidade <= 0) {
            // Se a nova quantidade for zero ou negativa, remove o item do carrinho.
            carrinho.remove(idProduto);
        } else {
            // Caso contrário, apenas atualiza a quantidade.
            carrinho.put(idProduto, novaQuantidade);
        }

        // Redireciona para a página do carrinho.
        exchange.getResponseHeaders().set("Location", "/carrinho");
        exchange.sendResponseHeaders(302, -1);
    }

    public static void enviarResposta(HttpExchange exchange, String resposta) throws Exception {

        // Converte o HTML em bytes.
        byte[] bytes = resposta.getBytes("UTF-8");

        // Informa ao navegador que a resposta é HTML.
        exchange.getResponseHeaders().add("Content-Type", "text/html; charset=UTF-8");

        // Envia o status HTTP 200, indicando sucesso.
        exchange.sendResponseHeaders(200, bytes.length);

        // Abre o canal de saída.
        OutputStream os = exchange.getResponseBody();

        // Escreve o conteúdo HTML no navegador.
        os.write(bytes);

        // Fecha o canal de saída.
        os.close();
    }

    public static Produto buscarProdutoPorId(int id) {
        for (Produto produto : produtos) {
            if (produto.getId() == id) {
                return produto;
            }
        }
        return null;
    }

    public static double calcularTotalCarrinho() {
        double total = 0.0;
        for (Map.Entry<Integer, Integer> entry : carrinho.entrySet()) {
            int idProduto = entry.getKey();
            int quantidade = entry.getValue();

            Produto produto = buscarProdutoPorId(idProduto);
            if (produto != null) {
                total += produto.getPreco() * quantidade;
            }
        }
        return total;
    }
}