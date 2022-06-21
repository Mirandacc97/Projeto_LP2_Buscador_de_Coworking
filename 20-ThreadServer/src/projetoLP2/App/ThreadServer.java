/*
 * Esta Classe foi ensinada em aula e serve para abrir o servidor local
 * com uma porta selcionada
 * inclusive possui os mesmos nomes
 */
package projetoLP2.App;

import java.io.*;
import java.net.*;
import projetoLP2.App.serviceProtocolService.ProtocolServer;

/**
 *
 * @author Marcus
 */
public class ThreadServer {

    /**
     * @param args the command line arguments
     * 
     */
    public static void main(String[] args) {
        // TODO code application logic here
        new ProtocolServer();
        
}
}
