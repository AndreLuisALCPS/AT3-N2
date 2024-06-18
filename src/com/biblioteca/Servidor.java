package com.biblioteca;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.ArrayList;

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
    }

    public void cadastrarLivro(String autor, String titulo, String genero, int exemplares) {
        livros.add(new Livro(autor, titulo, genero, exemplares));
        salvarLivros();
    }

    public boolean alugarLivro(String titulo) {
        for (Livro livro : livros) {
            if (livro.getNome().equalsIgnoreCase(titulo) && livro.getNumeroExemplares() > 0) {
                livro.setExemplares(livro.getExemplares() - 1);
                salvarLivros();
                return true;
            }
        }
        return false;
    }

    public boolean devolverLivro(String titulo) {
        for (Livro livro : livros) {
            if (livro.getNome().equalsIgnoreCase(titulo)) {
                livro.setExemplares(livro.getExemplares() + 1);
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
                try (Socket clientSocket = serverSocket.accept();
                     BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                     PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

                    String comando = in.readLine();
                    if (comando != null) {
                        switch (comando.toLowerCase()) {
                            case "listar":
                                listarLivros(out);
                                break;
                            case "cadastrar":
                                out.println("Pronto para receber detalhes do livro."); // Confirmação para o cliente
                                String autor = in.readLine();
                                String titulo = in.readLine();
                                String genero = in.readLine();
                                int exemplares = Integer.parseInt(in.readLine());
                                cadastrarLivro(autor, titulo, genero, exemplares);
                                out.println("Livro cadastrado com sucesso!");
                                break;

                            case "alugar":
                                out.println("Pronto para alugar o livro."); // Confirmação para o cliente
                                titulo = in.readLine();
                                if (alugarLivro(titulo)) {
                                    out.println("Livro alugado com sucesso!");
                                } else {
                                    out.println("Falha ao alugar o livro.");
                                }
                                break;

                            case "devolver":
                                titulo = in.readLine();
                                if (devolverLivro(titulo)) {
                                    out.println("Livro devolvido com sucesso!");
                                } else {
                                    out.println("Falha ao devolver o livro.");
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Servidor servidor = new Servidor();
        servidor.iniciar();
    }
}
