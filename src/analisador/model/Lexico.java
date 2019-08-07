/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package analisador.model;

/**
 *
 * @author Alison
 */
public class Lexico {
    private int id;
    private String token;
    private String classificacao;
    private int linha;
    private String color;


    
    public Lexico(){
        
    }

    public Lexico(String token, String classificacao, int linha, String color) {
        this.token = token;
        this.classificacao = classificacao;
        this.linha = linha;
        this.color = color;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getClassificacao() {
        return classificacao;
    }

    public void setClassificacao(String classificacao) {
        this.classificacao = classificacao;
    }

    public int getLinha() {
        return linha;
    }

    public void setLinha(int linha) {
        this.linha = linha;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

  
    
    
}
