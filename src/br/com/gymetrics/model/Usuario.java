package br.com.gymetrics.model;

public abstract class Usuario {
    private int id;
    private String login, senha, nome;
    private boolean ativo = true;

    protected Usuario() {}
    protected Usuario(int id, String login, String senha, String nome, boolean ativo) {
        this.id=id; this.login=login; setSenha(senha); this.nome=nome; this.ativo=ativo;
    }
    public abstract String getPerfil();

    public int getId(){return id;} public void setId(int v){id=v;}
    public String getLogin(){return login;} public void setLogin(String v){login=v;}
    public String getSenha(){return senha;}
    public void setSenha(String v){ if(v!=null && v.length()<6) throw new IllegalArgumentException("A senha deve ter no mínimo 6 caracteres"); senha=v; }
    public String getNome(){return nome;} public void setNome(String v){nome=v;}
    public boolean isAtivo(){return ativo;} public void setAtivo(boolean v){ativo=v;}
}
