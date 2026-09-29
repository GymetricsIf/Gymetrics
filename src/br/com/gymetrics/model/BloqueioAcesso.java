package br.com.gymetrics.model;

import java.time.LocalDateTime;

public class BloqueioAcesso {
    private int id; private Aluno aluno; private String motivo; private LocalDateTime dataHora=LocalDateTime.now(); private Usuario responsavel;
    public BloqueioAcesso() {}
    public BloqueioAcesso(int id,Aluno a,String m,LocalDateTime d,Usuario u){this.id=id;aluno=a;motivo=m;if(d!=null)dataHora=d;responsavel=u;}
    public int getId(){return id;} public void setId(int v){id=v;}
    public Aluno getAluno(){return aluno;} public void setAluno(Aluno v){aluno=v;}
    public String getMotivo(){return motivo;} public void setMotivo(String v){motivo=v;}
    public LocalDateTime getDataHora(){return dataHora;} public void setDataHora(LocalDateTime v){dataHora=v;}
    public Usuario getResponsavel(){return responsavel;} public void setResponsavel(Usuario v){responsavel=v;}
}
