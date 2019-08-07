/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package analisador.model;

/**
 *
 * @author Alison Rodolfo
 */
public class Dic {
    private String simbolo;
    private String token;
    private String classificacao;
    private String tipo;
    private int linha;

    public Dic(String simbolo, String token, String classificacao, String tipo, int linha) {
        this.simbolo = simbolo;
        this.token = token;
        this.classificacao = classificacao;
        this.tipo = tipo;
        this.linha = linha;
    }

    public String getSimbolo() {
        return simbolo;
    }

    public void setSimbolo(String simbolo) {
        this.simbolo = simbolo;
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

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getLinha() {
        return linha;
    }

    public void setLinha(int linha) {
        this.linha = linha;
    }

   

    
    
}
