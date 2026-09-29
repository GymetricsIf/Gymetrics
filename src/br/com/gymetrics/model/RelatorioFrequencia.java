package br.com.gymetrics.model;

import java.time.LocalDate;
import java.util.List;

public class RelatorioFrequencia {
    private final LocalDate inicio, fim; private final List<RegistroAcesso> registros;
    public RelatorioFrequencia(LocalDate inicio,LocalDate fim,List<RegistroAcesso> registros){this.inicio=inicio;this.fim=fim;this.registros=registros;}
    public LocalDate getInicio(){return inicio;} public LocalDate getFim(){return fim;} public List<RegistroAcesso> getRegistros(){return registros;}
}
