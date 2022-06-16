/*
 * Nesta classe implemtentamos os nomes que iremos trablahar para
 * as classes que virão a seguir, e os valores que eles terão dentro
 * das classes seguintes. é simular ao Protocol ensinado em aula, mas
 * é adaptado a realidade nosso código que utiliza o JavaSwing
 */
package pkg18.protocolclient;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Marcus
 */
class Protocol implements Serializable{
    
    private String funcao;
    private String endereco;
    private int horario;
   
    private String nome;
    private String nomeNalista;
    private String texto;
    private String cadastro;
    private String profissional;
    private String senhorio;
    
    /*Usamos a lista Set por ter apenas valores únicos e por não precisarmos de
    um INDEX. Não precisamos de uma ordem exata das posições dos participantes
    que estão online no servidor*/
    private Set<String> setOn = new HashSet<String>(); //interface Set e sua implementação HashSet
    /*O Hashset é mais rápido e essa é uma lista simples, não possui ordem e o
    tempo de execução é o mesmo. */
    private Status status;
    
    public enum Status { 
        CONECTADO, DESCONECTADO, MSG_ENV, MSG_PRIVADA, CLIENTES_ON, PROFISSIONAIS_ON
        
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
    
    /*public Protocol(String funcao, String endereco, int horario){
        this.funcao = funcao;
        this.endereco = endereco;
        this.horario = horario;
    }*/
    
    public String getFuncao() {
        return funcao;
    }

    public String getEndereco() {
        return endereco;
    }

    public int getHorario() {
        return horario;
    }

    public String getNome() {
        return nome;
    }

    public String getNomeNalista() {
        return nomeNalista;
    }

    public String getTexto() {
        return texto;
    }

    public String getCadastro() {
        return cadastro;
    }

    public String getProfissional() {
        return profissional;
    }

    public String getSenhorio() {
        return senhorio;
    }

    public Set<String> getSetOn() {
        return setOn;
    }

    public void setFuncao(String funcao) {
        this.funcao = funcao;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public void setHorario(int horario) {
        this.horario = horario;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setNomeNalista(String nomeNalista) {
        this.nomeNalista = nomeNalista;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public void setCadastro(String cadastro) {
        this.cadastro = cadastro;
    }

    public void setProfissional(String profissional) {
        this.profissional = profissional;
    }

    public void setSenhorio(String senhorio) {
        this.senhorio = senhorio;
    }

    public void setSetOn(Set<String> setOn) {
        this.setOn = setOn;
    }
    
  
}