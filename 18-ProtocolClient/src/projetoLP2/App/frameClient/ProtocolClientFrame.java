/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package projetoLP2.App.frameClient;

import java.awt.event.KeyEvent;
import projetoLP2.App.serviceProtocolService.ConectaCliente;
import projetoLP2.AppChatProtocol.Protocol;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Button;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import projetoLP2.AppChatProtocol.Protocol.Status;

/**
 *
 * @author Marcio Ballem
 */
public class ProtocolClientFrame extends javax.swing.JFrame {

    private Socket s;
    private Protocol protocol;
    private ConectaCliente conectaCliente;

    /**
     * Creates new form ClienteFrame
     */
    public ProtocolClientFrame() { //construtor
        initComponents();
    }

    private class ListenerSocket implements Runnable { //inicia a Thread

        private ObjectInputStream input; //como é o cliente, não precisa do output

        public ListenerSocket(Socket s) {
            try {
                this.input = new ObjectInputStream(s.getInputStream());
            } catch (Exception e) {}
        }

        @Override
        public void run() {
            Protocol protocol = null; //igual ao do servidor
            try {
                while ((protocol = (Protocol) input.readObject()) != null) {//mesmos procedimentos do servidor
                    Status status = protocol.getStatus(); //Irá reescrever a ação que o servidor está enviando
                    switch (status) {
                        case CONECTADO:
                            conectar(protocol);
                            break;
                        case DESCONECTADOC:
                            desconectar(); //Chama o método desconectar
                            s.close(); // fecha o socket 
                            //(BUG encontrado quando era colocado no método,
                            //pois o while continuava rodando ma so socket, tentanso ler o input, só que 
                            //estava fechado, e gerava um erro por isso precisou ser colocado aqui)
                            break;
                        case MSG_PRIVADA:
                            System.out.println("Você diz: " + protocol.getTexto());
                            msg_recebida(protocol);
                            break;
                        case PROFISSIONAIS_ON:
                            pro_online(protocol);
                            break;
                        default:
                            break;
                    }
                }
            } catch (IOException ex) {
                Logger.getLogger(ProtocolClientFrame.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ProtocolClientFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void conectar(Protocol protocol) {//método para preencher o protocol na lista enumerada do CONECTAR
        if (protocol.getTexto().equals("Offline")) { //Não houve conexão, usado quando é negado a conexão, Ex. quando já tem o memso nome na lista dos onlines.
            this.txtName.setText(""); //apaga o nome no cliente
            JOptionPane.showMessageDialog(this, "A conexão Falhou!");//POP UP!
            return;
        }
        //O else aqui é implícito, caso não seja "offline", então ele irá conectar,então faça o que está abaixo
        this.protocol = protocol; //Pega o objeto protocol e passa o valor que recebemos como msg
        this.btnConnectar.setEnabled(false); //desabilita este botão
        this.txtName.setEditable(false); //não deixa mais mudar o nome na JTextField

        this.btnSair.setEnabled(true); //habilita este botão
        this.txtAreaSend.setEnabled(true);
        this.txtAreaReceive.setEnabled(true); //habilita a área de texto que recebe
        this.btnEnviar.setEnabled(true); //habilita o botão enviar
        this.btnLimpar.setEnabled(true); //habilita o botão limpar

        JOptionPane.showMessageDialog(this, "Conexão estabelecida."); //POP UP
    }

    private void desconectar() { //apenas habilita ou desabilita itens na tela
        //☼ ↓AREAS DE TEXTO
        this.txtName.setEditable(true);
        this.txtAreaSend.setEnabled(false);
        this.txtAreaReceive.setEnabled(false);
        //◙ BOTÕES
        this.btnConnectar.setEnabled(true);
        this.btnSair.setEnabled(false);
        this.btnEnviar.setEnabled(false);
        this.btnLimpar.setEnabled(false);
        //♫ LIMPA AS CAIXAS DE TEXTO
        this.txtAreaReceive.setText("");
        this.txtAreaSend.setText("");
        
        System.out.println(":::Desconectado:::");

        JOptionPane.showMessageDialog(this, "CONEXÃO PERDIDA!"); //POP UP!
    }

    private void msg_recebida(Protocol protocol) {
        this.txtAreaReceive.append(protocol.getNome() + " disse: " + protocol.getTexto() + "\n");
        //o apend permite acumular várias msg que chegam e não precisa substituir uma msg por outra
    }

    private void pro_online(Protocol protocol) {
        System.out.println(protocol.getSetOn().toString());//teste no console 
        
        Set<String> proNames = protocol.getSetOn(); //uma set de string para recuperar quem ta on
        
        // proNames.remove(protocol.getNome()); //serve para remover o proprio nome da lista. como o cluente n ve outro cliente, então tando faz
        
        String[] array = (String[]) proNames.toArray(new String[proNames.size()]);//o jlist so aceita um array
        //faz o array ter exatamente o mesmo tamanho do das strings
        
        //↓ propriedades usadas no jList para seu funcionamento
        this.listOnlines.setListData(array); //passar o array para o jList
        this.listOnlines.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); //serve para aceitar ser um nome selecionado, sem isso era possivel selecionar vários
        this.listOnlines.setLayoutOrientation(JList.VERTICAL); //organiza os nomes verticalmente
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        txtName = new javax.swing.JTextField();
        btnConnectar = new javax.swing.JButton();
        btnSair = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        listOnlines = new javax.swing.JList();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtAreaSend = new javax.swing.JTextArea();
        btnEnviar = new javax.swing.JButton();
        btnLimpar = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jRadioButton1 = new javax.swing.JRadioButton();
        jRadioButton2 = new javax.swing.JRadioButton();
        jRadioButton3 = new javax.swing.JRadioButton();
        jRadioButton4 = new javax.swing.JRadioButton();
        jButton1 = new javax.swing.JButton();
        txtNomeProf = new javax.swing.JLabel();
        txtProfProf = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtAreaReceive = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Conectar"));

        btnConnectar.setText("Connectar");
        btnConnectar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnConnectarActionPerformed(evt);
            }
        });

        btnSair.setText("Sair");
        btnSair.setEnabled(false);
        btnSair.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSairActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtName)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnConnectar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnSair)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(btnConnectar)
                .addComponent(btnSair))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Profissionais Online"));

        jScrollPane3.setViewportView(listOnlines);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 80, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3)
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        txtAreaSend.setColumns(20);
        txtAreaSend.setRows(5);
        txtAreaSend.setEnabled(false);
        jScrollPane2.setViewportView(txtAreaSend);

        btnEnviar.setText("Enviar");
        btnEnviar.setEnabled(false);
        btnEnviar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEnviarActionPerformed(evt);
            }
        });

        btnLimpar.setText("Limpar");
        btnLimpar.setEnabled(false);
        btnLimpar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimparActionPerformed(evt);
            }
        });

        jLabel1.setText("Nome:");

        jLabel2.setText("Profissão:");

        jRadioButton1.setText("8-10");

        jRadioButton2.setText("10-12");

        jRadioButton3.setText("14-16");

        jRadioButton4.setText("16-18");

        jButton1.setText("Contratar");

        txtNomeProf.setText("NOME");

        txtProfProf.setText("PROFISSÂO");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 282, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnLimpar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnEnviar))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jRadioButton2)
                                .addGap(36, 36, 36)
                                .addComponent(jRadioButton4))
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel3Layout.createSequentialGroup()
                                    .addComponent(jLabel1)
                                    .addGap(41, 41, 41)
                                    .addComponent(txtNomeProf))
                                .addGroup(jPanel3Layout.createSequentialGroup()
                                    .addComponent(jLabel2)
                                    .addGap(18, 18, 18)
                                    .addComponent(txtProfProf))
                                .addGroup(jPanel3Layout.createSequentialGroup()
                                    .addComponent(jRadioButton1)
                                    .addGap(43, 43, 43)
                                    .addComponent(jRadioButton3))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton1)
                        .addGap(9, 9, 9)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnLimpar)
                    .addComponent(btnEnviar))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(txtNomeProf))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(txtProfProf))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jRadioButton1)
                            .addComponent(jRadioButton3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jRadioButton4)
                            .addComponent(jRadioButton2)))
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(45, 45, 45))
        );

        txtAreaReceive.setEditable(false);
        txtAreaReceive.setColumns(20);
        txtAreaReceive.setRows(5);
        txtAreaReceive.setEnabled(false);
        jScrollPane1.setViewportView(txtAreaReceive);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 298, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(22, 22, 22)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 218, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 224, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnConnectarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConnectarActionPerformed
        String name = this.txtName.getText(); //pega o nome que esta na caixa de diálogo

        if (!name.isEmpty()) { //Teste para não conectar sem nome, se tem nome digitado...
            this.protocol = new Protocol(); //INICIA(instancia) o objeto
            this.protocol.setStatus(Status.CONECTADO); //Seta o Status DO CLIENTE em Protocol
            this.protocol.setNome(name);// seta o nome do cliente

            this.conectaCliente = new ConectaCliente(); //inicia uma nova Thread socket que está enste objeto
            this.s = this.conectaCliente.connect(); //inicia o método conecta que retorna o socket

            new Thread(new ListenerSocket(this.s)).start(); //com os dados preenchidos, inicia a Thread

            this.conectaCliente.enviar(protocol); //chama o método enviar no conecta Cliente
        }
    }//GEN-LAST:event_btnConnectarActionPerformed

    private void btnSairActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSairActionPerformed
        //o botão sair desconecta sem que o programa feche, permitindo reconectar
        Protocol protocol = new Protocol();//instancia essa ação
        protocol.setNome(this.protocol.getNome()); //coloca o nome desse usuario no protocol
        protocol.setStatus(Status.DESCONECTADOC);//Setamos seu status
        this.conectaCliente.enviar(protocol);//envia esse status para o servidor
        desconectar();
    }//GEN-LAST:event_btnSairActionPerformed

    private void btnLimparActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimparActionPerformed
        this.txtAreaSend.setText(""); //teste de limpeza de área de texto: OK
    }//GEN-LAST:event_btnLimparActionPerformed

    private void btnEnviarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEnviarActionPerformed
        
        String text = this.txtAreaSend.getText(); //Cria variavesrecuperadas desta area
        String name = this.protocol.getNome(); //captura o nome
        
        this.protocol = new Protocol(); // cria a instancia
        
        if (this.listOnlines.getSelectedIndex() > -1) { //Teste para testar um nome selecionado
            //no jList há um método getSelectedIndex que fica com -1 se n estiver ninguém selecionado
            //por isso esse if diz: Se for maior que -1, então está selecionado
            this.protocol.setNomeNalista((String) this.listOnlines.getSelectedValue()); //cast pra string para poder setar
            this.txtAreaSend.setEnabled(true); 
            this.btnEnviar.setEnabled(true);
            this.btnLimpar.setEnabled(true);
            this.protocol.setStatus(Status.MSG_PRIVADA); //envia a msg para o selecionado
            this.listOnlines.clearSelection(); // limpa a seleção para não ficar eternamente selecionado
        } else {
           this.protocol.setStatus(Status.MSG_ENV);// Sua ação é feita para enviar msg a todos
          //JOptionPane.showMessageDialog(this, "SELECIONE UM PROFISSIONAL!"); //Isso não irá acontecer, pois o campo de escrever so fica ativo quando seleciona um profissional
        }
        
        if (!text.isEmpty()) {//assim, quando algo na String
            this.protocol.setNome(name); //seta o nome no protocol
            this.protocol.setTexto(text); //e seta o texto figitado na string

            //this.txtAreaReceive.append("Você disse: " + text + "\n"); //escreve na area de texto recebida com um append(append permute escrever de forma acumulada)
            
            this.conectaCliente.enviar(this.protocol); //chama o método enviar
        }
        
        this.txtAreaSend.setText(""); //limpa automaticamente a area apos enviar a msg
    }//GEN-LAST:event_btnEnviarActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnConnectar;
    private javax.swing.JButton btnEnviar;
    private javax.swing.JButton btnLimpar;
    private javax.swing.JButton btnSair;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JRadioButton jRadioButton3;
    private javax.swing.JRadioButton jRadioButton4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JList listOnlines;
    private javax.swing.JTextArea txtAreaReceive;
    private javax.swing.JTextArea txtAreaSend;
    private javax.swing.JTextField txtName;
    private javax.swing.JLabel txtNomeProf;
    private javax.swing.JLabel txtProfProf;
    // End of variables declaration//GEN-END:variables
}
