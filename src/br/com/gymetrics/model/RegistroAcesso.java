package br.com.gymetrics.model;

import java.time.LocalDateTime;

public class RegistroAcesso {
    public enum Tipo { CHECK_IN, CHECK_OUT, TENTATIVA_NEGADA }
    private int id; private Aluno aluno; private LocalDateTime dataHora=LocalDateTime.now(); private Tipo tipo; private String observacao;
    public RegistroAcesso() {}
    public RegistroAcesso(int id,Aluno aluno,LocalDateTime data,Tipo tipo,String obs){this.id=id;this.aluno=aluno;if(data!=null)dataHora=data;this.tipo=tipo;observacao=obs;}
    public int getId(){return id;} public void setId(int v){id=v;}
    public Aluno getAluno(){return aluno;} public void setAluno(Aluno v){aluno=v;}
    public LocalDateTime getDataHora(){return dataHora;} public void setDataHora(LocalDateTime v){dataHora=v;}
    public Tipo getTipo(){return tipo;} public void setTipo(Tipo v){tipo=v;}
    public String getObservacao(){return observacao;} public void setObservacao(String v){observacao=v;}
}
