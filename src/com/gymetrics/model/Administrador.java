package br.com.gymetrics.model;
public class Administrador extends Usuario {
    public Administrador() {}
    public Administrador(int id,String login,String senha,String nome,boolean ativo){super(id,login,senha,nome,ativo);}
    public String getPerfil(){return "ADMINISTRADOR";}
}
