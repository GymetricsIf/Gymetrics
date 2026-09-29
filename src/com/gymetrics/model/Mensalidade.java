package br.com.gymetrics.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Mensalidade {
    public enum Status { PENDENTE, VENCIDA, PAGA }
    private int id; private Aluno aluno; private LocalDate vencimento; private BigDecimal valor; private Status status=Status.PENDENTE;
    public Mensalidade() {}
    public Mensalidade(int id,Aluno aluno,LocalDate vencimento,BigDecimal valor,Status status){this.id=id;this.aluno=aluno;this.vencimento=vencimento;this.valor=valor;this.status=status==null?Status.PENDENTE:status;}
    public Status getStatus(){if(status!=Status.PAGA&&vencimento!=null&&vencimento.isBefore(LocalDate.now()))return Status.VENCIDA;return status;}
    public void marcarComoPaga(){status=Status.PAGA;}
    public boolean venceNosProximosDias(int dias){return getStatus()!=Status.PAGA&&vencimento!=null&&!vencimento.isBefore(LocalDate.now())&&!vencimento.isAfter(LocalDate.now().plusDays(dias));}
    public int getId(){return id;} public void setId(int v){id=v;}
    public Aluno getAluno(){return aluno;} public void setAluno(Aluno v){aluno=v;}
    public LocalDate getVencimento(){return vencimento;} public void setVencimento(LocalDate v){vencimento=v;}
    public BigDecimal getValor(){return valor;} public void setValor(BigDecimal v){valor=v;}
    public void setStatus(Status v){status=v;}
}
