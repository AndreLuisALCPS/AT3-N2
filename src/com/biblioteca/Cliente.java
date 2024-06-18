package com.biblioteca;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Cliente {
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 12345);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader console = new BufferedReader(new InputStreamReader(System.in))) {

            System.out.println("Conectado ao servidor. Digite comandos:");
            String comando;

            while ((comando = console.readLine()) != null) {
                out.println(comando);

                switch (comando.toLowerCase()) {
                    case "listar":
                        String resposta;
                        while ((resposta = in.readLine()) != null && !resposta.isEmpty()) {
                            System.out.println(resposta);
                        }
                        break;
                    case "cadastrar":
                        out.println(comando);
                        System.out.print("Autor: ");
                        String autor = console.readLine();
                        System.out.print("Título: ");
                        String titulo = console.readLine();
                        System.out.print("Gênero: ");
                        String genero = console.readLine();
                        System.out.print("Número de Exemplares: ");
                        String numExemplares = console.readLine();

                        // Envia os detalhes do livro após receber confirmação do servidor
                        out.println(autor);
                        out.println(titulo);
                        out.println(genero);
                        out.println(numExemplares);
                        System.out.println(in.readLine());
                        break;

                    case "alugar":
                        out.println(comando);
                        System.out.println(in.readLine()); // Recebe a mensagem "Pronto para alugar o livro."

                        System.out.print("Título: ");
                        String tituloAlugar = console.readLine();
                        out.println(tituloAlugar);

                        String respostaAluguel = in.readLine();
                        System.out.println(respostaAluguel); // Recebe a resposta do servidor
                        break;

                    case "devolver":
                        out.println(comando);
                        System.out.println(in.readLine()); // Recebe a mensagem "Pronto para devolver o livro."

                        System.out.print("Título: ");
                        out.println(console.readLine());

                        System.out.println(in.readLine()); // Recebe a resposta do servidor
                        break;
                    default:
                        System.out.println("Comando desconhecido.");
                        break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
