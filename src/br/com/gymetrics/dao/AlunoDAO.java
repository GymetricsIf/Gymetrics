package br.com.gymetrics.dao;

import br.com.gymetrics.model.*;
import br.com.gymetrics.util.ConexaoSQLite;
import java.sql.*; import java.time.LocalDate; import java.util.*;

public class AlunoDAO {
    private static final String BASE="SELECT a.*,p.id p_id,p.nome p_nome,p.valor_mensal,p.dia_vencimento,p.ativo p_ativo FROM alunos a LEFT JOIN planos p ON p.id=a.plano_id ";
    public void salvar(Aluno a)throws SQLException{try(Connection c=ConexaoSQLite.conectar();PreparedStatement p=c.prepareStatement("INSERT INTO alunos(nome,cpf,endereco,telefone,data_nascimento,codigo_acesso,status,plano_id) VALUES(?,?,?,?,?,?,?,?)",Statement.RETURN_GENERATED_KEYS)){preencher(p,a);p.executeUpdate();try(ResultSet r=p.getGeneratedKeys()){if(r.next())a.setId(r.getInt(1));}}}
    public void atualizar(Aluno a)throws SQLException{try(Connection c=ConexaoSQLite.conectar();PreparedStatement p=c.prepareStatement("UPDATE alunos SET nome=?,cpf=?,endereco=?,telefone=?,data_nascimento=?,codigo_acesso=?,status=?,plano_id=? WHERE id=?")){preencher(p,a);p.setInt(9,a.getId());p.executeUpdate();}}
    public List<Aluno> buscar(String termo)throws SQLException{return consultar(BASE+"WHERE a.nome LIKE ? OR a.cpf LIKE ? ORDER BY a.nome","%"+(termo==null?"":termo)+"%",true);}
    public Aluno buscarPorCpfOuCodigo(String id)throws SQLException{List<Aluno> l=consultar(BASE+"WHERE a.cpf=? OR a.codigo_acesso=?",id,true);return l.isEmpty()?null:l.get(0);}
    public void bloquear(Aluno a,String motivo,Usuario u)throws SQLException{
        if(motivo==null||motivo.isBlank())throw new IllegalArgumentException("Informe o motivo do bloqueio"); a.bloquear();
        try(Connection c=ConexaoSQLite.conectar()){c.setAutoCommit(false);try(PreparedStatement p=c.prepareStatement("UPDATE alunos SET status='BLOQUEADO' WHERE id=?");PreparedStatement b=c.prepareStatement("INSERT INTO bloqueios_acesso(aluno_id,motivo,data_hora,usuario_id) VALUES(?,?,datetime('now'),?)")){p.setInt(1,a.getId());p.executeUpdate();b.setInt(1,a.getId());b.setString(2,motivo);if(u==null)b.setNull(3,Types.INTEGER);else b.setInt(3,u.getId());b.executeUpdate();c.commit();}catch(SQLException e){c.rollback();throw e;}}
    }
    private List<Aluno> consultar(String sql,String v,boolean duplicar)throws SQLException{List<Aluno> l=new ArrayList<>();try(Connection c=ConexaoSQLite.conectar();PreparedStatement p=c.prepareStatement(sql)){p.setString(1,v);if(duplicar)p.setString(2,v);try(ResultSet r=p.executeQuery()){while(r.next())l.add(map(r));}}return l;}
    private void preencher(PreparedStatement p,Aluno a)throws SQLException{p.setString(1,a.getNome());p.setString(2,a.getCpf());p.setString(3,a.getEndereco());p.setString(4,a.getTelefone());p.setString(5,a.getDataNascimento()==null?null:a.getDataNascimento().toString());p.setString(6,a.getCodigoAcesso());p.setString(7,a.getStatus().name());if(a.getPlano()==null||a.getPlano().getId()==0)p.setNull(8,Types.INTEGER);else p.setInt(8,a.getPlano().getId());}
    private Aluno map(ResultSet r)throws SQLException{String d=r.getString("data_nascimento");Plano p=r.getObject("p_id")==null?null:new Plano(r.getInt("p_id"),r.getString("p_nome"),r.getBigDecimal("valor_mensal"),r.getInt("dia_vencimento"),r.getBoolean("p_ativo"));return new Aluno(r.getInt("id"),r.getString("nome"),r.getString("cpf"),r.getString("endereco"),r.getString("telefone"),d==null?null:LocalDate.parse(d),r.getString("codigo_acesso"),Aluno.Status.valueOf(r.getString("status")),p);}
}
