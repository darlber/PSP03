package org.example.Ejercicio1;

import java.io.*;
import java.net.Socket;
import java.util.Random;

public class ClienteHandler extends Thread {

    private Socket cliente;

    public ClienteHandler(Socket cliente) {
        this.cliente = cliente;
    }

    @Override
    public void run() {
        Random random = new Random();
        int numeroSecreto = random.nextInt(101); // Número entre 0 y 100
        System.out.println("El número secreto para el cliente " + cliente.getInetAddress() + " es: " + numeroSecreto);

        try (DataInputStream entrada = new DataInputStream(cliente.getInputStream());
             DataOutputStream salida = new DataOutputStream(cliente.getOutputStream())) {

            boolean acertado = false;
            while (!acertado) {
                int numeroCliente;
                try {
                    numeroCliente = entrada.readInt();
                } catch (IOException e) {
                    System.out.println("Error al leer el número del cliente: " + e.getMessage());
                    break;
                }

                if (numeroCliente < numeroSecreto) {
                    salida.writeUTF("El número es mayor.");
                    salida.writeBoolean(false);
                } else if (numeroCliente > numeroSecreto) {
                    salida.writeUTF("El número es menor.");
                    salida.writeBoolean(false);
                } else {
                    salida.writeUTF("¡Correcto! Has adivinado el número secreto.");
                    salida.writeBoolean(true);
                    acertado = true;
                }
            }
        } catch (IOException e) {
            System.out.println("Error en la comunicación con el cliente: " + e.getMessage());
        } finally {
            try {
                System.out.println("Cliente " +cliente.getInetAddress() + " desconectado");
                cliente.close();
            } catch (IOException e) {
                System.out.println("Error al cerrar la conexión con el cliente: " + e.getMessage());
            }
        }
    }
}
