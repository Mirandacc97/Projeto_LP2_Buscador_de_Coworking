/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package projetoLP2.App.frameClient;

import java.awt.Color;
import projetoLP2.App.serviceProtocolService.ConectaCliente;
import projetoLP2.AppChatProtocol.Protocol;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
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
        jPanelChat.setVisible(false);
        jPanelAlugarLocal.setVisible(false);
        jPanelChat.setVisible(false);
        jPanelLocatario.setVisible(false);

    }

    private class ListenerSocket implements Runnable { //inicia a Thread

        private ObjectInputStream input; //como é o cliente, não precisa do output

        public ListenerSocket(Socket s) {
            try {
                this.input = new ObjectInputStream(s.getInputStream());
            } catch (Exception e) {
            }
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
                        case DESCONECTADO:
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
                        case CLIENTES_ON:
                            clienteOnlines(protocol);
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
        this.btnConnectarCliente.setEnabled(false); //desabilita este botão
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
        this.btnConnectarCliente.setEnabled(true);
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

    public void clienteOnlines(Protocol protocol) {
        System.out.println(protocol.getSetOn().toString());//teste no console 

        Set<String> NamesC = protocol.getSetOn(); //uma set de string para recuperar quem ta on

        // proNames.remove(protocol.getNome()); //serve para remover o proprio nome da lista. como o cluente n ve outro cliente, então tando faz
        String[] array = (String[]) NamesC.toArray(new String[NamesC.size()]);//o jlist so aceita um array
        //faz o array ter exatamente o mesmo tamanho do das strings

        //↓ propriedades usadas no jList para seu funcionamento
        this.listOnlines.setListData(array); //passar o array para o jList
        this.listOnlines.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); //serve para aceitar ser um nome selecionado, sem isso era possivel selecionar vários
        this.listOnlines.setLayoutOrientation(JList.VERTICAL); //organiza os nomes verticalmente
        this.listOnlines1.setListData(array); //passar o array para o jList
        //this.listOnlines1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); //serve para aceitar ser um nome selecionado, sem isso era possivel selecionar vários
        this.listOnlines1.setLayoutOrientation(JList.VERTICAL);
    }   

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanelAlugarLocal = new javax.swing.JPanel();
        jLabelNomeProfissional = new javax.swing.JLabel();
        jTextSuaPro = new javax.swing.JTextField();
        jButtonAluAndChat = new javax.swing.JButton();
        jPanelInicial = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        txtName = new javax.swing.JTextField();
        btnConnectarCliente = new javax.swing.JButton();
        btnConnectarProf = new javax.swing.JButton();
        btnConnectarLoc = new javax.swing.JButton();
        jPanelChat = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtAreaSend = new javax.swing.JTextArea();
        btnEnviar = new javax.swing.JButton();
        btnSair = new javax.swing.JButton();
        btnLimpar = new javax.swing.JButton();
        jScrollPane5 = new javax.swing.JScrollPane();
        listOnlines1 = new javax.swing.JList();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        listOnlines = new javax.swing.JList();
        jLabelSeuNome = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtAreaReceive = new javax.swing.JTextArea();
        jPanelLocatario = new javax.swing.JPanel();
        jLabelNomeLocatario = new javax.swing.JLabel();
        txtLocal = new javax.swing.JTextField();
        bt1 = new javax.swing.JRadioButton();
        jLabel4 = new javax.swing.JLabel();
        bt2 = new javax.swing.JRadioButton();
        bt3 = new javax.swing.JRadioButton();
        bt4 = new javax.swing.JRadioButton();
        btnCadastrarEspaco = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanelAlugarLocal.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanelAlugarLocal.setToolTipText("Alugar um Espaço");

        jLabelNomeProfissional.setText("Sua Profissão");

        jButtonAluAndChat.setText("Alugar e falar com clientes");
        jButtonAluAndChat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAluAndChatActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelAlugarLocalLayout = new javax.swing.GroupLayout(jPanelAlugarLocal);
        jPanelAlugarLocal.setLayout(jPanelAlugarLocalLayout);
        jPanelAlugarLocalLayout.setHorizontalGroup(
            jPanelAlugarLocalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelAlugarLocalLayout.createSequentialGroup()
                .addGap(49, 49, 49)
                .addGroup(jPanelAlugarLocalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButtonAluAndChat, javax.swing.GroupLayout.PREFERRED_SIZE, 301, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelNomeProfissional)
                    .addComponent(jTextSuaPro, javax.swing.GroupLayout.PREFERRED_SIZE, 321, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(43, Short.MAX_VALUE))
        );
        jPanelAlugarLocalLayout.setVerticalGroup(
            jPanelAlugarLocalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelAlugarLocalLayout.createSequentialGroup()
                .addGap(61, 61, 61)
                .addComponent(jLabelNomeProfissional)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextSuaPro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(139, 139, 139)
                .addComponent(jButtonAluAndChat, javax.swing.GroupLayout.DEFAULT_SIZE, 53, Short.MAX_VALUE)
                .addGap(185, 185, 185))
        );

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Digite seu nome"));

        txtName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNameActionPerformed(evt);
            }
        });

        btnConnectarCliente.setText("Cliente");
        btnConnectarCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnConnectarClienteActionPerformed(evt);
            }
        });

        btnConnectarProf.setText("Profissional");
        btnConnectarProf.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnConnectarProfActionPerformed(evt);
            }
        });

        btnConnectarLoc.setText("Locatário");
        btnConnectarLoc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnConnectarLocActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnConnectarCliente, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtName, javax.swing.GroupLayout.DEFAULT_SIZE, 267, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(btnConnectarProf, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(btnConnectarLoc, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnConnectarCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnConnectarProf, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnConnectarLoc, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(32, 32, 32))
        );

        javax.swing.GroupLayout jPanelInicialLayout = new javax.swing.GroupLayout(jPanelInicial);
        jPanelInicial.setLayout(jPanelInicialLayout);
        jPanelInicialLayout.setHorizontalGroup(
            jPanelInicialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanelInicialLayout.setVerticalGroup(
            jPanelInicialLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelInicialLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(71, Short.MAX_VALUE))
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

        btnSair.setText("Sair");
        btnSair.setEnabled(false);
        btnSair.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSairActionPerformed(evt);
            }
        });

        btnLimpar.setText("Limpar");
        btnLimpar.setEnabled(false);
        btnLimpar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimparActionPerformed(evt);
            }
        });

        listOnlines1.setBackground(new java.awt.Color(204, 255, 204));
        listOnlines1.setFont(new java.awt.Font("Arial Black", 0, 12)); // NOI18N
        jScrollPane5.setViewportView(listOnlines1);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane5)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 252, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnEnviar)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(btnLimpar)
                                .addGap(0, 1, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnSair, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(btnLimpar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnSair))
                    .addComponent(btnEnviar, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 125, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Onlines"));

        listOnlines.setBackground(new java.awt.Color(204, 255, 204));
        listOnlines.setFont(new java.awt.Font("Arial Black", 0, 12)); // NOI18N
        jScrollPane4.setViewportView(listOnlines);

        jLabelSeuNome.setFont(new java.awt.Font("sansserif", 1, 12)); // NOI18N
        jLabelSeuNome.setText("Seu Nome");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jLabelSeuNome)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jLabelSeuNome)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane4)
                .addContainerGap())
        );

        txtAreaReceive.setEditable(false);
        txtAreaReceive.setColumns(20);
        txtAreaReceive.setRows(5);
        txtAreaReceive.setEnabled(false);
        jScrollPane1.setViewportView(txtAreaReceive);

        javax.swing.GroupLayout jPanelChatLayout = new javax.swing.GroupLayout(jPanelChat);
        jPanelChat.setLayout(jPanelChatLayout);
        jPanelChatLayout.setHorizontalGroup(
            jPanelChatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelChatLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelChatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanelChatLayout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 256, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanelChatLayout.setVerticalGroup(
            jPanelChatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelChatLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelChatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanelChatLayout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 278, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabelNomeLocatario.setText("NOME");

        txtLocal.setText("Localização");

        bt1.setText("08-10");

        jLabel4.setText("Horários Disponíveis");

        bt2.setText("10-12");

        bt3.setText("14-16");

        bt4.setText("16-18");

        btnCadastrarEspaco.setText("Cadastrar");
        btnCadastrarEspaco.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCadastrarEspacoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelLocatarioLayout = new javax.swing.GroupLayout(jPanelLocatario);
        jPanelLocatario.setLayout(jPanelLocatarioLayout);
        jPanelLocatarioLayout.setHorizontalGroup(
            jPanelLocatarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelLocatarioLayout.createSequentialGroup()
                .addGroup(jPanelLocatarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelLocatarioLayout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addGroup(jPanelLocatarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabelNomeLocatario)
                            .addComponent(txtLocal, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanelLocatarioLayout.createSequentialGroup()
                        .addGap(33, 33, 33)
                        .addComponent(jLabel4))
                    .addGroup(jPanelLocatarioLayout.createSequentialGroup()
                        .addGap(66, 66, 66)
                        .addGroup(jPanelLocatarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(bt2)
                            .addComponent(bt1))
                        .addGap(62, 62, 62)
                        .addGroup(jPanelLocatarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(bt3)
                            .addComponent(bt4)))
                    .addGroup(jPanelLocatarioLayout.createSequentialGroup()
                        .addGap(66, 66, 66)
                        .addComponent(btnCadastrarEspaco, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(64, Short.MAX_VALUE))
        );
        jPanelLocatarioLayout.setVerticalGroup(
            jPanelLocatarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelLocatarioLayout.createSequentialGroup()
                .addGap(43, 43, 43)
                .addComponent(jLabelNomeLocatario)
                .addGap(64, 64, 64)
                .addComponent(txtLocal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(33, 33, 33)
                .addComponent(jLabel4)
                .addGap(31, 31, 31)
                .addGroup(jPanelLocatarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(bt1)
                    .addComponent(bt3))
                .addGap(36, 36, 36)
                .addGroup(jPanelLocatarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(bt2)
                    .addComponent(bt4))
                .addGap(67, 67, 67)
                .addComponent(btnCadastrarEspaco, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(58, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 468, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jPanelAlugarLocal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jPanelInicial, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jPanelChat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jPanelLocatario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 546, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jPanelAlugarLocal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jPanelInicial, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jPanelChat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jPanelLocatario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnConnectarClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConnectarClienteActionPerformed
        String name = this.txtName.getText(); //pega o nome que esta na caixa de diálogo

        if (!name.isEmpty()) { //Teste para não conectar sem nome, se tem nome digitado...
            this.protocol = new Protocol(); //INICIA(instancia) o objeto
            this.protocol.setStatus(Status.CONECTADO); //Seta o Status DO CLIENTE em Protocol
            this.protocol.setNome(name);// seta o nome do cliente

            this.conectaCliente = new ConectaCliente(); //inicia uma nova Thread socket que está enste objeto
            this.s = this.conectaCliente.connect(); //inicia o método conecta que retorna o socket

            new Thread(new ListenerSocket(this.s)).start(); //com os dados preenchidos, inicia a Thread

            this.conectaCliente.enviar(protocol); //chama o método enviar no conecta Cliente
            this.jPanelInicial.setVisible(false);
            this.jPanelChat.setVisible(true);
            this.jLabelSeuNome.setText(name);
        }
    }//GEN-LAST:event_btnConnectarClienteActionPerformed

    private void btnSairActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSairActionPerformed
        //o botão sair desconecta sem que o programa feche, permitindo reconectar
        Protocol protocol = new Protocol();//instancia essa ação
        protocol.setNome(this.protocol.getNome()); //coloca o nome desse usuario no protocol
        protocol.setStatus(Status.DESCONECTADO);//Setamos seu status
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
            this.protocol.setStatus(Status.MSG_PRIVADA); //envia a msg para o selecionado
            this.listOnlines.clearSelection(); // limpa a seleção para não ficar eternamente selecionado
        } else {
            this.protocol.setStatus(Status.MSG_ENV);// Sua ação é feita para enviar msg a todos
            //JOptionPane.showMessageDialog(this, "SELECIONE UM PROFISSIONAL!"); //Isso não irá acontecer, pois o campo de escrever so fica ativo quando seleciona um profissional
        }

        if (!text.isEmpty()) {//assim, quando há algo na String
            this.protocol.setNome(name); //seta o nome no protocol
            this.protocol.setTexto(text); //e seta o texto figitado na string

            this.txtAreaReceive.append("Você disse: " + text + "\n"); //escreve na area de texto recebida com um append(append permute escrever de forma acumulada)

            this.conectaCliente.enviar(this.protocol); //chama o método enviar
        }

        this.txtAreaSend.setText(""); //limpa automaticamente a area apos enviar a msg
    }//GEN-LAST:event_btnEnviarActionPerformed

    private void txtNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNameActionPerformed

    private void btnConnectarProfActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConnectarProfActionPerformed
        // TODO add your handling code here:
        this.jPanelInicial.setVisible(false);
        this.jPanelAlugarLocal.setVisible(true);

    }//GEN-LAST:event_btnConnectarProfActionPerformed

    private void btnConnectarLocActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConnectarLocActionPerformed
        this.jPanelInicial.setVisible(false);
        this.jPanelLocatario.setVisible(true);
    }//GEN-LAST:event_btnConnectarLocActionPerformed

    private void jButtonAluAndChatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAluAndChatActionPerformed
        Color cor = Color.RED;
        String name = this.txtName.getText(); //pega o nome que esta na caixa de diálogo
        String profi = this.jTextSuaPro.getText();

        if (!name.isEmpty() && !profi.isEmpty()) { //Teste para não conectar sem nome, se tem nome digitado...
            this.protocol = new Protocol(); //INICIA(instancia) o objeto
            this.protocol.setStatus(Status.CONECTADO); //Seta o Status DO CLIENTE em Protocol
            this.protocol.setNome("[" + profi + "] " + name);// seta o nome do cliente

            this.conectaCliente = new ConectaCliente(); //inicia uma nova Thread socket que está enste objeto
            this.s = this.conectaCliente.connect(); //inicia o método conecta que retorna o socket

            new Thread(new ListenerSocket(this.s)).start(); //com os dados preenchidos, inicia a Thread

            this.conectaCliente.enviar(protocol); //chama o método enviar no conecta Cliente
            //this.jPanelInicial.setVisible(false);
            this.jPanelAlugarLocal.setVisible(false);
            this.jPanelChat.setVisible(true);            
            this.jLabelSeuNome.setText(profi + ": " + name);
        }
    }//GEN-LAST:event_jButtonAluAndChatActionPerformed

    private void btnCadastrarEspacoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCadastrarEspacoActionPerformed
        String name = this.txtName.getText(); //pega o nome que esta na caixa de diálogo
        String local = this.txtLocal.getText();
        String h1, h2, h3, h4;
        if(bt1.isSelected()){
            h1 = "08 - 10, ";
            
        }else{
            h1 = "";
        }
         if(bt2.isSelected()){
            h2 = "10 - 12, ";
        }else{
            h2 = "";
        } if(bt3.isSelected()){
            h3 = "14 - 16, ";
        }else{
            h3 = "";
        } if(bt4.isSelected()){
            h4 = "16 - 18.";
        }else{
            h4 = "";
        }
        

        if (!name.isEmpty() && !local.isEmpty()) { //Teste para não conectar sem nome, se tem nome digitado...
            this.protocol = new Protocol(); //INICIA(instancia) o objeto
            this.protocol.setStatus(Status.CONECTADO); //Seta o Status DO CLIENTE em Protocol
            this.protocol.setNome("[" + name + "] " + local + h1+h2+h3+h4);// seta o nome do cliente

            this.conectaCliente = new ConectaCliente(); //inicia uma nova Thread socket que está enste objeto
            this.s = this.conectaCliente.connect(); //inicia o método conecta que retorna o socket

            new Thread(new ListenerSocket(this.s)).start(); //com os dados preenchidos, inicia a Thread

            this.conectaCliente.enviar(protocol); //chama o método enviar no conecta Cliente
            //this.jPanelInicial.setVisible(false);
            this.jPanelAlugarLocal.setVisible(false);
            this.jPanelChat.setVisible(true);            
            this.jLabelSeuNome.setText(local + ": " + name);
        }

    }//GEN-LAST:event_btnCadastrarEspacoActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JRadioButton bt1;
    private javax.swing.JRadioButton bt2;
    private javax.swing.JRadioButton bt3;
    private javax.swing.JRadioButton bt4;
    private javax.swing.JButton btnCadastrarEspaco;
    private javax.swing.JButton btnConnectarCliente;
    private javax.swing.JButton btnConnectarLoc;
    private javax.swing.JButton btnConnectarProf;
    private javax.swing.JButton btnEnviar;
    private javax.swing.JButton btnLimpar;
    private javax.swing.JButton btnSair;
    private javax.swing.JButton jButtonAluAndChat;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabelNomeLocatario;
    private javax.swing.JLabel jLabelNomeProfissional;
    private javax.swing.JLabel jLabelSeuNome;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanelAlugarLocal;
    private javax.swing.JPanel jPanelChat;
    private javax.swing.JPanel jPanelInicial;
    private javax.swing.JPanel jPanelLocatario;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JTextField jTextSuaPro;
    private javax.swing.JList listOnlines;
    private javax.swing.JList listOnlines1;
    private javax.swing.JTextArea txtAreaReceive;
    private javax.swing.JTextArea txtAreaSend;
    private javax.swing.JTextField txtLocal;
    private javax.swing.JTextField txtName;
    // End of variables declaration//GEN-END:variables
}
