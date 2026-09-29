package br.com.gymetrics.entidade;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Representa o cliente e seus dados cadastrais.
 */
public class eCliente {
    private int id;
    private String nome;
    private String cpf;
    private String telefone;
    private String endereco;
    private final List<eLocacao> locacoes = new ArrayList<>();

    public eCliente() {
    }

    public eCliente(int id, String nome, String cpf, String telefone, String endereco) {
        this.id = id;
        this.nome = nome;
        this.cpf = cpf;
        this.telefone = telefone;
        this.endereco = endereco;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }

    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }

    public String getEndereco() { return endereco; }
    public void setEndereco(String endereco) { this.endereco = endereco; }

    public List<eLocacao> getLocacoes() {
        return Collections.unmodifiableList(locacoes);
    }

    public void adicionarLocacao(eLocacao locacao) {
        if (locacao != null && !locacoes.contains(locacao)) {
            locacoes.add(locacao);
            if (locacao.getCliente() != this) {
                locacao.setCliente(this);
            }
        }
    }

    public void removerLocacao(eLocacao locacao) {
        if (locacoes.remove(locacao) && locacao != null && locacao.getCliente() == this) {
            locacao.setCliente(null);
        }
    }
}
