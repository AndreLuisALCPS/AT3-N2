package com.biblioteca;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Servidor {
    private static final String ARQUIVO_JSON = "livros.json";
    private List<Livro> livros;
    private Gson gson;

    public Servidor() {
        gson = new Gson();
        livros = carregarLivros();
    }

    private List<Livro> carregarLivros() {
        try (Reader reader = new FileReader(ARQUIVO_JSON)) {
            JsonObject jsonObject = gson.fromJson(reader, JsonObject.class);
            Type livroListType = new TypeToken<ArrayList<Livro>>() {}.getType();
            return gson.fromJson(jsonObject.get("livros"), livroListType);
        } catch (IOException | IllegalStateException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    private void salvarLivros() {
        try (Writer writer = new FileWriter(ARQUIVO_JSON)) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.add("livros", gson.toJsonTree(livros));
            gson.toJson(jsonObject, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void listarLivros(PrintWriter out) {
        for (Livro livro : livros) {
            out.println(livro);
        }
        out.println("END");
    }

    public void cadastrarLivro(String autor, String titulo, String genero, int exemplares) {
        livros.add(new Livro(autor, titulo, genero, exemplares));
        salvarLivros();
    }

    public boolean alugarLivro(String titulo) {
        for (Livro livro : livros) {
            if (livro.getTitulo().equalsIgnoreCase(titulo) && livro.getNumeroExemplares() > 0) {
                livro.setExemplares(livro.getNumeroExemplares() - 1);
                salvarLivros();
                return true;
            }
        }
        return false;
    }

    public boolean devolverLivro(String titulo) {
        for (Livro livro : livros) {
            if (livro.getTitulo().equalsIgnoreCase(titulo)) {
                livro.setExemplares(livro.getNumeroExemplares() + 1);
                salvarLivros();
                return true;
            }
        }
        return false;
    }

    public void iniciar() {
        try (ServerSocket serverSocket = new ServerSocket(12345)) {
            System.out.println("Servidor iniciado na porta 12345...");
            while (true) {
                Socket clientSocket = serverSocket.accept();
                new Thread(new ClientHandler(clientSocket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class ClientHandler implements Runnable {
        private Socket clientSocket;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        @Override
        public void run() {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                 PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

                String comando;
                while ((comando = in.readLine()) != null) {
                    System.out.println("Comando: " + comando);
                    switch (comando.toLowerCase()) {
                        case "listar":
                            listarLivros(out);
                            break;
                        case "cadastrar":
                            out.println("Pronto para receber detalhes do livro.");
                            String autor = in.readLine();
                            String titulo = in.readLine();
                            String genero = in.readLine();
                            String exemplaresStr = in.readLine();

                            if (autor != null && titulo != null && genero != null && exemplaresStr != null) {
                                try {
                                    int exemplares = Integer.parseInt(exemplaresStr);
                                    cadastrarLivro(autor, titulo, genero, exemplares);
                                    out.println("Livro cadastrado com sucesso!");
                                } catch (NumberFormatException e) {
                                    out.println("Erro: Número de exemplares inválido.");
                                }
                            } else {
                                out.println("Erro: Informações incompletas recebidas do cliente.");
                            }
                            break;
                        case "alugar":
                            out.println("Pronto para alugar o livro.");
                            String tituloAlugar = in.readLine();
                            if (tituloAlugar != null && !tituloAlugar.isEmpty()) {
                                if (alugarLivro(tituloAlugar)) {
                                    out.println("Livro alugado com sucesso!");
                                } else {
                                    out.println("Falha ao alugar o livro.");
                                }
                            } else {
                                out.println("Erro: Título do livro não recebido.");
                            }
                            break;
                        case "devolver":
                            out.println("Pronto para devolver o livro.");
                            String tituloDevolver = in.readLine();
                            if (tituloDevolver != null && !tituloDevolver.isEmpty()) {
                                if (devolverLivro(tituloDevolver)) {
                                    out.println("Livro devolvido com sucesso!");
                                } else {
                                    out.println("Falha ao devolver o livro.");
                                }
                            } else {
                                out.println("Erro: Título do livro não recebido.");
                            }
                            break;
                        default:
                            out.println("Comando desconhecido.");
                            break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        Servidor servidor = new Servidor();
        servidor.iniciar();
    }
}
