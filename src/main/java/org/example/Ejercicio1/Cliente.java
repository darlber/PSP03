package org.example.Ejercicio1;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Cliente {
    public static void main(String[] args) {
        String host = "localhost";
        int puerto = 2000;

        try (Socket socket = new Socket(host, puerto);
             DataInputStream entrada = new DataInputStream(socket.getInputStream());
             DataOutputStream salida = new DataOutputStream(socket.getOutputStream());
             Scanner scanner = new Scanner(System.in)) {

            System.out.println("Conectado al servidor en el puerto " + puerto);

            boolean acertado = false;
            while (!acertado) {
                System.out.print("Introduce un número entre 0 y 100: ");
                int numero = scanner.nextInt();
                salida.writeInt(numero);

                //leer respuesta
                String respuesta = entrada.readUTF();

                System.out.println("Servidor: " + respuesta);

                acertado = entrada.readBoolean();
            }

        } catch (IOException e) {
            System.err.println("Error al comunicarse con el servidor: " + e.getMessage());
        }
    }
}
