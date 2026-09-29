package br.com.gymetrics.controller;

import br.com.gymetrics.dao.PlanoDAO; import br.com.gymetrics.model.Plano; import java.sql.SQLException; import java.util.List;

public class PlanoController {
    private final PlanoDAO dao=new PlanoDAO();
    public void cadastrar(Plano p)throws SQLException{if(p==null||p.getNome()==null||p.getNome().isBlank()||p.getValorMensal()==null||p.getDiaVencimento()==0)throw new IllegalArgumentException("Nome, valor e vencimento são obrigatórios");dao.salvar(p);} public List<Plano> listar()throws SQLException{return dao.listar();}
}
