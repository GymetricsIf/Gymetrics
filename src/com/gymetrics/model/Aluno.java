package br.com.gymetrics.model;

import java.time.LocalDate;

public class Aluno {
    public enum Status { ATIVO, INADIMPLENTE, BLOQUEADO }

    private int id;
    private String nome, cpf, endereco, telefone, codigoAcesso;
    private LocalDate dataNascimento;
    private Status status = Status.ATIVO;
    private Plano plano;

    public Aluno() {}
    public Aluno(int id, String nome, String cpf, String endereco, String telefone, LocalDate dataNascimento,
                 String codigoAcesso, Status status, Plano plano) {
        this.id=id; this.nome=nome; this.cpf=cpf; this.endereco=endereco; this.telefone=telefone;
        this.dataNascimento=dataNascimento; this.codigoAcesso=codigoAcesso;
        this.status=status==null?Status.ATIVO:status; this.plano=plano;
    }

    public boolean cadastroCompleto() {
        return nome!=null&&!nome.isBlank() && cpf!=null&&!cpf.isBlank() && endereco!=null&&!endereco.isBlank()
            && telefone!=null&&!telefone.isBlank() && dataNascimento!=null;
    }
    public boolean podeAcessar() { return status==Status.ATIVO && cadastroCompleto(); }
    public void bloquear() { status=Status.BLOQUEADO; }
    public void ativar() { status=Status.ATIVO; }

    public int getId(){return id;} public void setId(int v){id=v;}
    public String getNome(){return nome;} public void setNome(String v){nome=v;}
    public String getCpf(){return cpf;} public void setCpf(String v){cpf=v;}
    public String getEndereco(){return endereco;} public void setEndereco(String v){endereco=v;}
    public String getTelefone(){return telefone;} public void setTelefone(String v){telefone=v;}
    public LocalDate getDataNascimento(){return dataNascimento;} public void setDataNascimento(LocalDate v){dataNascimento=v;}
    public String getCodigoAcesso(){return codigoAcesso;} public void setCodigoAcesso(String v){codigoAcesso=v;}
    public Status getStatus(){return status;} public void setStatus(Status v){status=v;}
    public Plano getPlano(){return plano;} public void setPlano(Plano v){plano=v;}
}
