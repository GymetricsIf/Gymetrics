package br.com.gymetrics.controller;

import br.com.gymetrics.dao.PagamentoDAO; import br.com.gymetrics.model.Pagamento; import java.sql.SQLException;

public class PagamentoController {
    private final PagamentoDAO dao=new PagamentoDAO();
    public void registrar(Pagamento p)throws SQLException{if(p==null||p.getMensalidade()==null||p.getMesReferencia()==null||p.getValorPago()==null)throw new IllegalArgumentException("Dados do pagamento incompletos");dao.salvar(p);}
}
