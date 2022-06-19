/*
 * Nesta classe implementamos a Ruunable e alguns métodos que utilizaremos
 * para que os clientes acessem o servidor seguindo essas "regras".
 * Usamos listas com o HashMap por serem simples e a busca pleso ídeces não
 * precisarem de uma ordem correta, ou indexada
 */
package threadserver;

import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import threadserver.Protocol.Status;

/**
 *
 * @author Marcus
 */
public class ProtocolServer implements Runnable {

    Socket s; //inicia o Socket
    private ObjectInputStream input; //variavel que executa a entrada de msg do servidor
    private ObjectOutputStream output;//variavel que executa a saida de msg do servidor

    //↓Uma Map que contém como valor uma String e um ObjectOutputStream
    private Map<String, ObjectOutputStream> on = new HashMap<String, ObjectOutputStream>();

    /*Na lista Map listamos os usuarios online no servidor, mas os nomes deles não podem ser
    iguais. O usuário(key) é uma String e temos também seu ObjectOutputStream(value),
    utilizaremos essa técnica para percorrer a lista e verificar se o usuário consta na lista
    dos que estão online*/
    public ProtocolServer(Socket ns) { //construtor do servidor ouvinte
        s = ns;

        try {
            //↓ depois de concectado esses dois objetos passam a ser do cliente
            this.input = new ObjectInputStream(ns.getInputStream());   //inicializa as variável
            this.output = new ObjectOutputStream(ns.getOutputStream());//inicializa as variável
        } catch (IOException ex) {
            Logger.getLogger(ProtocolServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void run() { //método run, o servidor ouvinte
        Protocol protocol = null;
        try {
            // DataInputStream in = new DataInputStream(s.getInputStream());
            // DataOutputStream out = new DataOutputStream(s.getOutputStream());

            while (true) {
                Status status = protocol.getStatus();
                /* ↑ Variavel dentro de Status que recebe o stauts que esta
                    sendo enviado para a classe Protocol*/

                if (status.equals(Status.CONECTADO)) { //se o usuario está no servidor...
                    on.put(protocol.getNome(), output); //coloca o nome na lista dos conectados
                    System.out.println(protocol.getNome() + "CONECTADO");
                    addOnline(); //chama este método

                } else if (status.equals(Status.DESCONECTADO)) {
                    desconectado(protocol, output); //chama o método
                    addOnline(); //atualiza a lista que não terá o nome do desconectado nela
                    return;
                } else if (status.equals(Status.MSG_ENV)) {
                    msgParaTodos(protocol);//chama este método
                } else if (status.equals(Status.MSG_PRIVADA)) {
                    msgIndividual(protocol);
                }
                try {
                    if ((protocol = (Protocol) input.readObject()) != null) {
                        System.out.println(protocol.getNome() + " DESCONECTADO");
                        break;
                    }
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(ProtocolServer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();            
        }
    }

    private void desconectado(Protocol protocol, ObjectOutputStream output) {
        on.remove(protocol.getNome());//pega este nome na lista e o remove
        System.out.println("Adeus " + protocol.getNome());
        protocol.setTexto("Volte sempre!");
        protocol.setStatus(Status.MSG_ENV);
        msgParaTodos(protocol);
        System.out.println(protocol.getNome() + " desconectou.");
        
    }

    private void addOnline() { //usado para atualizarr nomes a lista dos conectados
        Set<String> nomesOnline = new HashSet<String>(); //cria a lista de nomes conectados
        for (Map.Entry<String, ObjectOutputStream> valorChave : on.entrySet()) { //percorre a lista com um ForEach
            //   ↑ para cada nome na lista: faça o seginte:
            nomesOnline.add(valorChave.getKey()); // pegue sua key e add na lista
            /*
            TESTAR, VER SE O PROPRIO NOME NÃO FICA APARECENDO PARA SI.
            if(nomesOnline.equals(valorChave)){
                nomesOnline.remove(valorChave.getKey());                
            }*/
        }
        Protocol protocol = new Protocol(); //Cria um novo objeto na classe Protocol
        protocol.setStatus(Status.CLIENTES_ON);// vai no método status de Protocol, coloca na lista enumerada
        protocol.setSetOn(nomesOnline); //coloca este nome no método SetOn.

        /*CRIAR AQUI ABAIXO, OU DENTRO DO FOR UMA FORMA DE NÃO MOSTRAR PARA SI
       SEU PRÓPRIO NOME ONLINE,PARA NÃO CONVERSAR CONSIGO MESMO E CRIAR UMA EXEÇÃO*/
        //  throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void msgParaTodos(Protocol protocol) { // usado para o profissional mandar msg para todos os clientes conectados
        /*ForEach para percorrer a lista dos clientes conectados (on) e assim
          Se o nome estiver na lista este irá receber a mensagem*/
        for (Map.Entry<String, ObjectOutputStream> pegaKey : on.entrySet()) {
            if(!pegaKey.equals(protocol.getNome())){ //se o nome da lista não se repetir, capture seu nome
                protocol.setStatus(Status.MSG_ENV); //Vai em protocol e usa o MSG_ENV
                try {
                    pegaKey.getValue().writeObject(protocol);
                    //vai em protocol e escreve a msg neste objeto
                } catch (IOException ex) {
                    Logger.getLogger(ProtocolServer.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            }

        }

    }

    private void msgIndividual(Protocol protocol) { // usado par ao cliente mandar msg para ao profissional
    //forEach com a mesma ideia do método anterior
    for (Map.Entry<String, ObjectOutputStream> pegaKey : on.entrySet()){
        if(pegaKey.getKey().equals(protocol.getNomeNalista())){ 
            try {
            //percorre a lista de nomes para escolher uma
            pegaKey.getValue().writeObject(protocol);
            } catch (IOException ex) {
                Logger.getLogger(ProtocolServer.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
    }
    }
}
