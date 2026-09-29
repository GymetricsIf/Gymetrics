package br.com.gymetrics.dao;

import br.com.gymetrics.model.Plano;
import br.com.gymetrics.util.ConexaoSQLite;
import java.sql.*; import java.util.*;

public class PlanoDAO {
    public void salvar(Plano p) throws SQLException {
        try(Connection c=ConexaoSQLite.conectar();PreparedStatement ps=c.prepareStatement("INSERT INTO planos(nome,valor_mensal,dia_vencimento,ativo) VALUES(?,?,?,?)",Statement.RETURN_GENERATED_KEYS)){
            ps.setString(1,p.getNome());ps.setBigDecimal(2,p.getValorMensal());ps.setInt(3,p.getDiaVencimento());ps.setBoolean(4,p.isAtivo());ps.executeUpdate();try(ResultSet r=ps.getGeneratedKeys()){if(r.next())p.setId(r.getInt(1));}
        }
    }
    public List<Plano> listar() throws SQLException { List<Plano> l=new ArrayList<>(); try(Connection c=ConexaoSQLite.conectar();Statement s=c.createStatement();ResultSet r=s.executeQuery("SELECT * FROM planos ORDER BY nome")){while(r.next())l.add(map(r));} return l; }
    static Plano map(ResultSet r)throws SQLException{return new Plano(r.getInt("id"),r.getString("nome"),r.getBigDecimal("valor_mensal"),r.getInt("dia_vencimento"),r.getBoolean("ativo"));}
}
