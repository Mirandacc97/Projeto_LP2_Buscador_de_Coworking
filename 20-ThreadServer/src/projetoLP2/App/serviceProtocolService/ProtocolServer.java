/*
 * Nesta classe implementamos a Ruunable e alguns métodos que utilizaremos
 * para que os clientes acessem o servidor seguindo essas "regras".
 * Usamos listas com o HashMap por serem simples e a busca pleso ídeces não
 * precisarem de uma ordem correta, ou indexada
 */
package projetoLP2.App.serviceProtocolService;

import projetoLP2.AppChatProtocol.Protocol;
import projetoLP2.AppChatProtocol.Protocol.Status;

import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author Marcus
 */
public class ProtocolServer {

    private ServerSocket s;
    private Socket ns;
    //↓Uma Map que contém como valor uma String e um ObjectOutputStream
    private Map<String, ObjectOutputStream> on = new HashMap<String, ObjectOutputStream>();
    /*Na lista Map listamos os usuarios online no servidor, mas os nomes deles não podem ser
    iguais. O usuário(key) é uma String e temos também seu ObjectOutputStream(value),
    utilizaremos essa técnica para percorrer a lista e verificar se o usuário consta na lista
    dos que estão online*/

    public ProtocolServer(){
        try {
            s  = new ServerSocket(1234);
            
            System.out.println ("Servidor Online");
            System.out.println ("Aguardando conexões");
            while (true) {
                ns = s.accept(); //inicia o socket
                new Thread(new Server(ns)).start(); //inicia a Thread
            }   } catch (IOException ex) {
            Logger.getLogger(ProtocolServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public class Server implements Runnable {
        
        private ObjectInputStream in; //variavel que executa a entrada de msg do servidor
        private ObjectOutputStream out;//variavel que executa a saida de msg do servidor

        public Server(Socket ns) { //construtor do servidor ouvinte
            try {
                //↓ depois de concectado esses dois objetos passam a ser do cliente
                this.in = new ObjectInputStream(ns.getInputStream());   //inicializa as variável
                this.out = new ObjectOutputStream(ns.getOutputStream());//inicializa as variável
            } catch (IOException ex) {
                Logger.getLogger(ProtocolServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        @Override
        public void run() {
            //método run, o servidor ouvinte
            Protocol protocol = null;
            //boolean conectou; //um interuptor para teste de conexão
            try {
                while ((protocol = (Protocol) in.readObject()) != null) {
                    Status status = protocol.getStatus();
                    /* ↑ Variavel dentro de Status que recebe o status que esta
            sendo enviado para a classe Protocol*/

                    if (status.equals(Status.CONECTADO)) { //se o usuario está no servidor...
                        boolean conectou = conectou(protocol, out); //chama o método que atribui uma string para os conectados
                        if (conectou) { //no caso dele conectar...
                            on.put(protocol.getNome(), out); //coloca o nome na lista dos conectados
                            System.out.println(protocol.getNome() + "CONECTADO");
                            addOnline(); //chama este método
                        }

                    } else if (status.equals(Status.DESCONECTADO)) {
                        desconectado(protocol, out); //chama o método
                        System.out.println(protocol.getNome() + "DESCONECTADO");
                        addOnline(); //atualiza a lista que não terá o nome do desconectado nela
                        return;
                    } else if (status.equals(Status.MSG_ENV)) {
                        msgParaTodos(protocol);//chama este método
                    } else if (status.equals(Status.MSG_PRIVADA)) {
                        msgIndividual(protocol);
                    }
                }
            } catch (IOException ex) {
                Protocol aviso = new Protocol();
                aviso.setNome(protocol.getNome());
                desconectado(aviso, out);
                addOnline(); // serve para enviar os que estão online apos um desconectar
                System.out.println(protocol.getNome() + " deixou o chat!");
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ProtocolServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        private void desconectado(Protocol protocol, ObjectOutputStream out) {
            on.remove(protocol.getNome());//pega este nome na lista e o remove
            System.out.println("Adeus " + protocol.getNome());
            protocol.setTexto("Volte sempre!");
            protocol.setStatus(Status.MSG_PRIVADA);
            msgParaTodos(protocol);
            System.out.println(protocol.getNome() + " desconectou.");

        }

        private void addOnline() { //usado para atualizar nomes a lista dos conectados
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
                if (!pegaKey.equals(protocol.getNome())) { //se o nome da lista não se repetir, capture seu nome
                    protocol.setStatus(Status.MSG_PRIVADA); //Vai em protocol e usa o MSG_ENV
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
            for (Map.Entry<String, ObjectOutputStream> pegaKey : on.entrySet()) {
                if (pegaKey.getKey().equals(protocol.getNomeNalista())) {
                    try {
                        //percorre a lista de nomes para escolher uma
                        pegaKey.getValue().writeObject(protocol);
                    } catch (IOException ex) {
                        Logger.getLogger(ProtocolServer.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }

        private boolean conectou(Protocol protocol, ObjectOutputStream out) {
            if (on.size() == 0) {
                protocol.setTexto("itsAlive"); //coloca atribui o texto apra vivi(online) para testar se ta on
                envia(protocol, out); //retorna pro cliente a msg
                return true; // colcoa o boleano como true, conexão feita
            } else if (on.containsKey(protocol.getNome())) { //se o nome digitado ja ta online
                protocol.setTexto("notAlive"); //mumda esse texto
                envia(protocol, out);//envia
                return false;//e não conecta
            } else { //caso não
                protocol.setTexto("itsAlive");
                envia(protocol, out);
                return true;
            }
        }

        private void envia(Protocol protocol, ObjectOutputStream out) {
            try {
                out.writeObject(protocol); //escreve na saída do objeto a msg desejada 
            } catch (IOException ex) {
                Logger.getLogger(ProtocolServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
