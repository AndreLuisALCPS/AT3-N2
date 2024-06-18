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
                        while ((resposta = in.readLine()) != null && !resposta.equals("END")) {
                            System.out.println(resposta);
                        }
                        break;
                    case "cadastrar":
                        System.out.print("Autor: ");
                        String autor = console.readLine();
                        System.out.print("Título: ");
                        String titulo = console.readLine();
                        System.out.print("Gênero: ");
                        String genero = console.readLine();
                        System.out.print("Número de Exemplares: ");
                        String numExemplares = console.readLine();

                        out.println(autor);
                        out.println(titulo);
                        out.println(genero);
                        out.println(numExemplares);
                        System.out.println(in.readLine());
                        break;
                    case "alugar":
                        System.out.println(in.readLine()); // Recebe "Pronto para alugar o livro."
                        System.out.print("Título: ");
                        String tituloAlugar = console.readLine();
                        out.println(tituloAlugar);
                        System.out.println(in.readLine()); // Recebe resposta do aluguel
                        break;
                    case "devolver":
                        System.out.println(in.readLine()); // Recebe "Pronto para devolver o livro."
                        System.out.print("Título: ");
                        String tituloDevolver = console.readLine();
                        out.println(tituloDevolver);
                        System.out.println(in.readLine()); // Recebe resposta da devolução
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
