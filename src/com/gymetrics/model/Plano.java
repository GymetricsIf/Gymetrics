package br.com.gymetrics.model;

import java.math.BigDecimal;

public class Plano {
    private int id, diaVencimento;
    private String nome;
    private BigDecimal valorMensal;
    private boolean ativo=true;

    public Plano() {}
    public Plano(int id,String nome,BigDecimal valor,int dia,boolean ativo){this.id=id;this.nome=nome;this.valorMensal=valor;setDiaVencimento(dia);this.ativo=ativo;}
    public int getId(){return id;} public void setId(int v){id=v;}
    public String getNome(){return nome;} public void setNome(String v){nome=v;}
    public BigDecimal getValorMensal(){return valorMensal;} public void setValorMensal(BigDecimal v){valorMensal=v;}
    public int getDiaVencimento(){return diaVencimento;}
    public void setDiaVencimento(int v){if(v<1||v>31)throw new IllegalArgumentException("Vencimento deve estar entre 1 e 31");diaVencimento=v;}
    public boolean isAtivo(){return ativo;} public void setAtivo(boolean v){ativo=v;}
}
