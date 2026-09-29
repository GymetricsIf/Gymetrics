package br.com.gymetrics.dao;

import br.com.gymetrics.model.*; import br.com.gymetrics.util.ConexaoSQLite; import java.sql.*;

public class PagamentoDAO {
    public void salvar(Pagamento p)throws SQLException{
        try(Connection c=ConexaoSQLite.conectar()){c.setAutoCommit(false);try(PreparedStatement q=c.prepareStatement("INSERT INTO pagamentos(mensalidade_id,mes_referencia,valor_pago,data_pagamento) VALUES(?,?,?,?)",Statement.RETURN_GENERATED_KEYS);PreparedStatement m=c.prepareStatement("UPDATE mensalidades SET status='PAGA' WHERE id=?");PreparedStatement a=c.prepareStatement("UPDATE alunos SET status='ATIVO' WHERE status='INADIMPLENTE' AND id=(SELECT aluno_id FROM mensalidades WHERE id=?)")){q.setInt(1,p.getMensalidade().getId());q.setString(2,p.getMesReferencia().toString());q.setBigDecimal(3,p.getValorPago());q.setString(4,p.getDataPagamento().toString());q.executeUpdate();try(ResultSet r=q.getGeneratedKeys()){if(r.next())p.setId(r.getInt(1));}m.setInt(1,p.getMensalidade().getId());m.executeUpdate();a.setInt(1,p.getMensalidade().getId());a.executeUpdate();c.commit();p.getMensalidade().marcarComoPaga();if(p.getMensalidade().getAluno()!=null)p.getMensalidade().getAluno().ativar();}catch(SQLException e){c.rollback();throw e;}}
    }
}
