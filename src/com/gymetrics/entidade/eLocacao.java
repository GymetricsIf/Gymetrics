package br.com.gymetrics.entidade;

import java.time.LocalDate;

/**
 * Representa um registro de locação de uma fita por um cliente.
 */
public class eLocacao {
    private int id;
    private eCliente cliente;
    private eFita fita;
    private LocalDate dataLocacao;
    private LocalDate dataDevolucaoPrevista;
    private LocalDate dataDevolucao;

    public eLocacao() {
    }

    public eLocacao(int id, eCliente cliente, eFita fita,
                    LocalDate dataLocacao, LocalDate dataDevolucaoPrevista) {
        this.id = id;
        this.dataLocacao = dataLocacao;
        this.dataDevolucaoPrevista = dataDevolucaoPrevista;
        setCliente(cliente);
        setFita(fita);
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public eCliente getCliente() { return cliente; }

    public void setCliente(eCliente cliente) {
        if (this.cliente == cliente) {
            return;
        }
        if (this.cliente != null) {
            this.cliente.removerLocacao(this);
        }
        this.cliente = cliente;
        if (cliente != null) {
            cliente.adicionarLocacao(this);
        }
    }

    public eFita getFita() { return fita; }

    public void setFita(eFita fita) {
        if (this.fita == fita) {
            return;
        }
        if (this.fita != null && this.fita.getLocacaoAtual() == this) {
            this.fita.setLocacaoAtual(null);
        }
        this.fita = fita;
        if (fita != null && fita.getLocacaoAtual() != this) {
            fita.setLocacaoAtual(this);
        }
    }

    public LocalDate getDataLocacao() { return dataLocacao; }
    public void setDataLocacao(LocalDate dataLocacao) { this.dataLocacao = dataLocacao; }

    public LocalDate getDataDevolucaoPrevista() { return dataDevolucaoPrevista; }
    public void setDataDevolucaoPrevista(LocalDate dataDevolucaoPrevista) {
        this.dataDevolucaoPrevista = dataDevolucaoPrevista;
    }

    public LocalDate getDataDevolucao() { return dataDevolucao; }
    public void setDataDevolucao(LocalDate dataDevolucao) { this.dataDevolucao = dataDevolucao; }

    public boolean estaDevolvida() {
        return dataDevolucao != null;
    }

    public boolean estaEmAtraso(LocalDate dataReferencia) {
        return !estaDevolvida()
                && dataDevolucaoPrevista != null
                && dataReferencia != null
                && dataReferencia.isAfter(dataDevolucaoPrevista);
    }
}
