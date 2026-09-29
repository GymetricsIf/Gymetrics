package br.com.gymetrics.entidade;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Representa os dados de um filme associado às suas fitas.
 */
public class eFilme {
    private int id;
    private String titulo;
    private String genero;
    private int anoLancamento;
    private String classificacaoIndicativa;
    private String sinopse;
    private final List<eFita> fitas = new ArrayList<>();

    public eFilme() {
    }

    public eFilme(int id, String titulo, String genero, int anoLancamento,
                  String classificacaoIndicativa, String sinopse) {
        this.id = id;
        this.titulo = titulo;
        this.genero = genero;
        this.anoLancamento = anoLancamento;
        this.classificacaoIndicativa = classificacaoIndicativa;
        this.sinopse = sinopse;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getGenero() { return genero; }
    public void setGenero(String genero) { this.genero = genero; }

    public int getAnoLancamento() { return anoLancamento; }
    public void setAnoLancamento(int anoLancamento) { this.anoLancamento = anoLancamento; }

    public String getClassificacaoIndicativa() { return classificacaoIndicativa; }
    public void setClassificacaoIndicativa(String classificacaoIndicativa) {
        this.classificacaoIndicativa = classificacaoIndicativa;
    }

    public String getSinopse() { return sinopse; }
    public void setSinopse(String sinopse) { this.sinopse = sinopse; }

    public List<eFita> getFitas() {
        return Collections.unmodifiableList(fitas);
    }

    public void adicionarFita(eFita fita) {
        if (fita != null && !fitas.contains(fita)) {
            fitas.add(fita);
            if (fita.getFilme() != this) {
                fita.setFilme(this);
            }
        }
    }

    public void removerFita(eFita fita) {
        fitas.remove(fita);
        if (fita != null && fita.getFilme() == this) {
            fita.setFilme(null);
        }
    }
}
