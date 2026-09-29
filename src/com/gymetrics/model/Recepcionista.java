package br.com.gymetrics.model;
public class Recepcionista extends Usuario {
    public Recepcionista() {}
    public Recepcionista(int id,String login,String senha,String nome,boolean ativo){super(id,login,senha,nome,ativo);}
    public String getPerfil(){return "RECEPCIONISTA";}
}
