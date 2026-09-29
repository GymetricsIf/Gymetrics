package br.com.gymetrics.controller;

import br.com.gymetrics.dao.RegistroAcessoDAO; import br.com.gymetrics.model.RelatorioFrequencia; import java.sql.SQLException; import java.time.LocalDate;

public class RelatorioController {
    private final RegistroAcessoDAO dao=new RegistroAcessoDAO();
    public RelatorioFrequencia gerar(LocalDate inicio,LocalDate fim)throws SQLException{if(inicio==null||fim==null||fim.isBefore(inicio))throw new IllegalArgumentException("Período inválido");return new RelatorioFrequencia(inicio,fim,dao.listarPorPeriodo(inicio,fim));}
}
