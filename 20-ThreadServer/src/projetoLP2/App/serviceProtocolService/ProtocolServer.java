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
    //↓Uma Map que contém como CHAVE uma String e como VALOR um ObjectOutputStream
    private Map<String, ObjectOutputStream> on = new HashMap<String, ObjectOutputStream>();   //lista online Cliente
    private Map<String, ObjectOutputStream> cadList = new HashMap<String, ObjectOutputStream>();

    /*Na lista Map listamos os usuarios online no servidor, mas os nomes deles não podem ser
    iguais. O usuário(key) é uma String e temos também seu ObjectOutputStream(value),
    utilizaremos essa técnica para percorrer a lista e verificar se o usuário consta na lista
    dos que estão online.
    Todos que se conectarem ao servidor, serão adicionados a esta lista
    assim sempre que for preciso, poderemso saber quais usuários estão online,
    para enviar a mensagem para todos os usuários como tbm para enviar para a 
    aplicação cliente, todos os usuarios que estão conectados no chat*/
    public ProtocolServer() { //método construtor
        try {
            s = new ServerSocket(1234); //inicia o objeto com sua porta

            System.out.println("Servidor Online"); //printa no servidor
            System.out.println("Aguardando conexões");
            while (true) { //executa enquanto o server está online
                ns = s.accept(); //inicia o socket
                new Thread(new Server(ns)).start(); //inicia a Thread na Runnable e passa como parametro o socket.
                //sempre que um novo usuario se conecta, se cria uma nova e dois objetos output são criados pra esse cliente
            }
        } catch (IOException ex) {
            Logger.getLogger(ProtocolServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public class Server implements Runnable {

        private ObjectInputStream in; //variavel que executa a entrada de msg do servidor
        private ObjectOutputStream out;//variavel que recebe as msg enciadas pelos clientes

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
                while ((protocol = (Protocol) in.readObject()) != null) {//recebe as msg enviadas pelos conectados, convertido em Protocil pois retorna um objeto da classe object
                    Status status = protocol.getStatus();
                    /* ↑ Variavel dentro de Status que recebe o status que esta
            sendo enviado para a classe Protocol*/
                    boolean conectou;

                    //↓↓↓↓↓ A partir de agora, testamos qual msg está vindo do usuário
                    switch (status) {
                        case CONECTADO:
                            //Pedido de Conexão do cliente
                            conectou = conectou(protocol, out); //chama o método que atribui uma string para os conectados
                            if (conectou) { //no caso dele conectar...
                                on.put(protocol.getNome(), out); //coloca o nome na lista dos  CLIENTES conectados
                                System.out.println(protocol.getNome() + " CONECTADO");
                                refreshOnline(); //chama este método, sempre que um cliente conecta a lista é atualizada
                            }
                            break;
                        case DESCONECTADO:
                            // usuario quer deixar o chat
                            desconectado(protocol, out); //chama o método
                            refreshOnline(); //atualiza a lista que não terá o nome do desconectado nela
                            return;//Força o fechamento do while:::: https://www.delftstack.com/pt/howto/java/exit-while-loop-in-java/ :::
                        case MSG_ENV:
                            //manda msg a todos no chat
                            msgParaTodos(protocol);//chama este método
                            break;
                        case MSG_PRIVADA:
                            //pede msg privada
                            msgIndividual(protocol);
                            break;
                        default:
                            break;
                    }
                }

            } catch (IOException ex) {
                System.out.println(protocol.getNome() + " deixou o chat!");
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ProtocolServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void desconectado(Protocol protocol, ObjectOutputStream out) {
        on.remove(protocol.getNome());//pega este nome na lista e o remove
        System.out.println("Cliente " + protocol.getNome() + " Desconectou"); //msg no terminal
    }

    private void refreshOnline() { //usado para atualizar nomes a lista dos conectados
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
        protocol.setSetOn(nomesOnline); //Aqui preenche o set com todos os nomes
        //↓ foreach para enviar para cada um dos usuarios do map a lista, incluindo a si mesmo
        for (Map.Entry<String, ObjectOutputStream> valorChave : on.entrySet()) {
            protocol.setNome(valorChave.getKey());
            try {
                valorChave.getValue().writeObject(protocol);
            } catch (Exception e) {
            }
        }

        /*CRIAR AQUI ABAIXO, OU DENTRO DO FOR UMA FORMA DE NÃO MOSTRAR PARA SI
       SEU PRÓPRIO NOME ONLINE,PARA NÃO CONVERSAR CONSIGO MESMO E CRIAR UMA EXEÇÃO*/
    }

    private void msgParaTodos(Protocol protocol) { // usado para o profissional mandar msg para todos os clientes conectados
        /*ForEach para percorrer a lista dos usuarios conectados e assim
          Se o nome estiver na lista este irá receber a mensagem*/
        for (Map.Entry<String, ObjectOutputStream> pegaKey : on.entrySet()) {
            //a string "nome" está armz na key e o object é o valor e isso dá acesso ao método weite object
            if (!pegaKey.equals(protocol.getNome())) { //se a key for diferente da pessoa que está mandando a mensagem...
                protocol.setStatus(Status.MSG_PRIVADA); //a msg é enviada pros usuarios que passaram no if
                try {
                    pegaKey.getValue().writeObject(protocol);//aqui passamos a msg
                } catch (Exception e) {
                }
            }
        }
    }

    private void msgIndividual(Protocol protocol) { // usado para o USUARIO mandar mensagens privadas
        //forEach com a mesma ideia do método anterior
        for (Map.Entry<String, ObjectOutputStream> pegaKey : on.entrySet()) {//percorre a lista
            if (pegaKey.getKey().equals(protocol.getNomeNalista())) {
                try {
                    //percorre a lista de nomes para escolher uma
                    pegaKey.getValue().writeObject(protocol);
                } catch (Exception e) {
                }
            }
        }
    }

    // ↓↓↓☻ conectou(Cria um método boleano para escrever em protocol setando que se queira)    
    private boolean conectou(Protocol protocol, ObjectOutputStream out) {
        if (on.size() == 0) { //se a lista tiver tamanho ZERO
            protocol.setTexto("Online"); //atribui o texto pra online para testar se ta on
            envia(protocol, out); //usa a variável "envia" e manda a msg Online
            return true; // colcoa o boleano como true, conexão feita
        } else if (on.containsKey(protocol.getNome())) { //Percorre a lista, se o nome digitado ja ta online
            protocol.setTexto("Offline"); //muda esse texto
            envia(protocol, out);//envia
            return false;//e não conecta
        } else { //caso não tenha nenhum nem outro... (Isso nunca irá acontecer, ou alista tem alguém ou não tem ninguém, colocado apenas pra evitar bugs)
            protocol.setTexto("Online");
            envia(protocol, out);
            return true;
        }
    }   
    private void envia(Protocol protocol, ObjectOutputStream out) {
        try {
            out.writeObject(protocol); // a partir da variavel out. envia a msg com o metodo write (passando o protocol como parametro))escreve na saída do objeto a msg desejada 
        } catch (IOException ex) {
            Logger.getLogger(ProtocolServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}