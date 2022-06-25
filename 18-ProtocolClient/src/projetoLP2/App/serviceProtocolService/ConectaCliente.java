/*
 * Assim como ensinado em sala de aula, essa classe tem o socket com oo ipe e aporta de conexão
 */
package projetoLP2.App.serviceProtocolService;

import projetoLP2.AppChatProtocol.Protocol;
import projetoLP2.App.ProtocolClient;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author Marcus
 */
public class ConectaCliente {
    private Socket s;
    private ObjectOutputStream out;
    
    public Socket connect() {
        try {
            this.s = new Socket("localhost", 1234);
            this.out = new ObjectOutputStream(s.getOutputStream());
        } catch (UnknownHostException ex) {
            Logger.getLogger(ProtocolClient.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ProtocolClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return s;
    }
        public void enviar(Protocol protocol) {
        try {
            out.writeObject(protocol); //através ObjectOutputstream ele escreve no objeto com o writeObject
        } catch (Exception e) {}
    }
    
}
