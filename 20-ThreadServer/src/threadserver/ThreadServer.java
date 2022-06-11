/*
 * Esta Classe foi ensinada em aula e serve para abrir o servidor local
 * com uma porta selcionada
 * inclusive possui os mesmos nomes
 */
package threadserver;

import java.io.*;
import java.net.*;

/**
 *
 * @author Marcus
 */
public class ThreadServer {

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException {
        // TODO code application logic here
        ServerSocket s;
        Socket ns;
        s = new ServerSocket(4444);
        System.out.println("Servidor Online");
        System.out.println("Aguardando conexões");
        while (true) {
            ns = s.accept();
            new Thread(new ProtocolServer(ns)).start(); //inicia a Thread na classe ProtocolServer
        }
    }
}
