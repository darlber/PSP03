package org.example.Ejercicio2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Cliente {

    public static void main(String[] args) {
        String servidor = "localhost";
        int puerto = 1500;

        try (Socket socket = new Socket(servidor, puerto);
             BufferedReader entradaTeclado = new BufferedReader(new InputStreamReader(System.in));
             PrintWriter salida = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            System.out.println("Conectado al servidor " + servidor + " en el puerto " + puerto);
            System.out.print("Introduce el nombre del fichero que deseas solicitar: ");

            String nombreFichero = entradaTeclado.readLine();
            salida.println(nombreFichero);

            String respuesta = entrada.readLine();
            if ("ERROR".equalsIgnoreCase(respuesta)) {
                System.out.println("El fichero solicitado no existe en el servidor.");
            } else {
                System.out.println("Recibiendo contenido del fichero: " + nombreFichero);
                String linea;
                while ((linea = entrada.readLine()) != null) {
                    System.out.println(linea);
                }
            }
        } catch (UnknownHostException e) {
            System.err.println("No se pudo conectar al servidor: " + servidor);
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("Error de E/S al comunicar con el servidor.");
            e.printStackTrace();
        }
    }
}