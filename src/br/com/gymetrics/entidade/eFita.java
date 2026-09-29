package br.com.gymetrics.entidade;

/**
 * Representa uma unidade física de fita disponível para locação.
 */
public class eFita {
    public enum Status {
        DISPONIVEL,
        ALUGADA,
        DANIFICADA,
        INDISPONIVEL
    }

    private int id;
    private String codigo;
    private eFilme filme;
    private Status status = Status.DISPONIVEL;
    private eLocacao locacaoAtual;

    public eFita() {
    }

    public eFita(int id, String codigo, eFilme filme) {
        this.id = id;
        this.codigo = codigo;
        setFilme(filme);
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }

    public eFilme getFilme() { return filme; }
    public void setFilme(eFilme filme) {
        if (this.filme == filme) {
            return;
        }
        if (this.filme != null) {
            this.filme.removerFita(this);
        }
        this.filme = filme;
        if (filme != null && !filme.getFitas().contains(this)) {
            filme.adicionarFita(this);
        }
    }

    public Status getStatus() { return status; }
    public void setStatus(Status status) {
        this.status = status == null ? Status.DISPONIVEL : status;
    }

    public eLocacao getLocacaoAtual() { return locacaoAtual; }

    public void setLocacaoAtual(eLocacao locacaoAtual) {
        this.locacaoAtual = locacaoAtual;
        if (locacaoAtual != null) {
            this.status = Status.ALUGADA;
        } else if (status == Status.ALUGADA) {
            this.status = Status.DISPONIVEL;
        }
    }

    public boolean estaDisponivel() {
        return status == Status.DISPONIVEL && locacaoAtual == null;
    }
}
