/*
 * Diferente de um chat, nosso programa não vai trabalhar com uma classe String
 * e sim com o objeto protocol. è como se fosse uma caixa onde dentro dela estivessem
 * todas essas informações. E dependendo da situação, poderemos detalahr o que precisaremos
 * Se é o nome do cliente ou o texto da msg, ou o nome do profissional, ou o local onde sera feito o aluguel, etc;
 * É simular ao Protocol ensinado em aula, mas
 * é adaptado a realidade nosso código que utiliza o JavaSwing
 */
package projetoLP2.AppChatProtocol;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Marcus
 */
public class Protocol implements Serializable{
    
    /*variáveis de instancia*/    
    private String nome; //aramazena o nome do conectado
    private String nomeL;
    private String profissao;
    private String nomeNalista; //aramazena o nome do conectado que irá receber uma msg privada
    private String texto; //aramazena o texto da mensagem
       
    /*Usamos a lista Set por ter apenas valores únicos e por não precisarmos de
    um INDEX. Não precisamos de uma ordem exata das posições dos participantes
    que estão online no servidor*/
    private Set<String> setOn = new HashSet<String>(); //setOn irá armazenar o nome de todos os conectados no servidor enquanto o memso estiver ativo
    //interface Set e sua implementação HashSet
    /*O Hashset é mais rápido e essa é uma lista simples, não possui ordem e o
    tempo de execução é o mesmo. */
   
    private Status status;//Um enumerado(mas facil que variaveis estáticas)
  
    public enum Status { //pra cada msg enviada para o servidor ele vai dizer qual é a ação que ele quer executar
        CONECTADO, DESCONECTADO,
        MSG_ENV, MSG_PRIVADA,
        CLIENTES_ON, LOC
        
        /*enun estende a clase java.lang.enum, como como não precisa de herança multipla
        escolhemos enum, ao invés de static, por ser mais prático, pois todos os valores
        aqui tem o mesmo sentido. Por serem objetos imutáveis, os declaramos com letras 
        maiusculas, como boa pratica de programação*/
    }

    //↓Métodos get e set
    public void setStatus(Status status) {
        this.status = status;
    }
    public Status getStatus() {
        return status;
    }

    public String getNome() {
        return nome;
    }

    public String getNomeL() {
        return nomeL;
    }

    public String getProfissao() {
        return profissao;
    }

    public void setProfissao(String profissao) {
        this.profissao = profissao;
    }
    

    public String getNomeNalista() {
        return nomeNalista;
    }

    public String getTexto() {
        return texto;
    }

    public Set<String> getSetOn() {
        return setOn;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setNomeL(String nomeL) {
        this.nomeL = nomeL;
    }
    
    public void setNomeNalista(String nomeNalista) {
        this.nomeNalista = nomeNalista;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public void setSetOn(Set<String> setOn) {
        this.setOn = setOn;
    }
}