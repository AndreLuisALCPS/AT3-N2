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
                        System.out.print("Autor: ");
                        out.println(console.readLine());
                        System.out.print("Título: ");
                        out.println(console.readLine());
                        System.out.print("Gênero: ");
                        out.println(console.readLine());
                        System.out.print("Número de Exemplares: ");
                        out.println(console.readLine());
                        System.out.println(in.readLine());
                        break;
                    case "alugar":
                        System.out.print("Título: ");
                        out.println(console.readLine());
                        System.out.println(in.readLine());
                        break;
                    case "devolver":
                        System.out.print("Título: ");
                        out.println(console.readLine());
                        System.out.println(in.readLine());
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
