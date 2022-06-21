/*
 * Esta Classe foi ensinada em aula e serve para abrir o servidor local
 * com uma porta selcionada, inclusive possui os mesmos nomes
 * Esta classe chama a classe onde existe a criação do socket do servidor
 * suas implementações
 */
package projetoLP2.App;

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
        new ProtocolServer(); //instancia do Protocol
        
}
}
