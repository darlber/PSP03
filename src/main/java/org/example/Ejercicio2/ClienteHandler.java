package org.example.Ejercicio2;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

class ClienteHandler extends Thread {
    private Socket cliente;

    public ClienteHandler(Socket cliente) {
        this.cliente = cliente;
    }

    @Override
    public void run() {
        System.out.println("Directorio actual: " + new File(".").getAbsolutePath());

        try (BufferedReader entrada = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
             PrintWriter salida = new PrintWriter(cliente.getOutputStream(), true)) {

            salida.println("Introduce el nombre del archivo que deseas recibir:");

            String nombreArchivo = entrada.readLine();

            if (nombreArchivo == null) {
                System.out.println("Cliente no proporcionó un nombre de archivo.");
                return;
            }

            File archivo = new File(nombreArchivo);
            System.out.println("Buscando archivo en: " + archivo.getAbsolutePath());

            if (archivo.exists() && archivo.isFile()) {
                salida.println("OK");
                enviarArchivo(archivo, cliente);
            } else {
                salida.println("ERROR");
                System.out.println("El archivo no existe: " + archivo.getAbsolutePath());
            }
        } catch (IOException e) {
            System.err.println("Error en la comunicación con el cliente: " + e.getMessage());
        } finally {
            cerrarConexion();
        }
    }

    private void enviarArchivo(File archivo, Socket cliente) {
        try (BufferedInputStream archivoInput = new BufferedInputStream(new FileInputStream(archivo));
             OutputStream salida = cliente.getOutputStream()) {

            byte[] buffer = new byte[4096];
            int bytesLeidos;

            while ((bytesLeidos = archivoInput.read(buffer)) != -1) {
                salida.write(buffer, 0, bytesLeidos);
            }
            salida.flush();
            System.out.println("Archivo enviado con éxito: " + archivo.getName());
        } catch (IOException e) {
            System.err.println("Error al enviar el archivo: " + e.getMessage());
        }
    }

    private void cerrarConexion() {
        try {
            if (cliente != null && !cliente.isClosed()) {
                cliente.close();
                System.out.println("Conexión con el cliente cerrada.");
            }
        } catch (IOException e) {
            System.err.println("Error al cerrar la conexión con el cliente: " + e.getMessage());
        }
    }
}