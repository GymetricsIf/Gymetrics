package br.com.gymetrics.model;

import java.time.LocalDateTime;

public class LogSistema {
    private int id; private Usuario usuario; private String acao; private LocalDateTime dataHora=LocalDateTime.now();
    public LogSistema() {}
    public LogSistema(int id,Usuario u,String a,LocalDateTime d){this.id=id;usuario=u;acao=a;if(d!=null)dataHora=d;}
    public int getId(){return id;} public void setId(int v){id=v;}
    public Usuario getUsuario(){return usuario;} public void setUsuario(Usuario v){usuario=v;}
    public String getAcao(){return acao;} public void setAcao(String v){acao=v;}
    public LocalDateTime getDataHora(){return dataHora;} public void setDataHora(LocalDateTime v){dataHora=v;}
}
