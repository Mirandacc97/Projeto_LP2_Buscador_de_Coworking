/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg18.protocolclient;

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
            out.writeObject(protocol);
        } catch (IOException ex) {
            Logger.getLogger(ProtocolClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
}
