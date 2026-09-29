package br.com.gymetrics.dao;

import br.com.gymetrics.model.*; import br.com.gymetrics.util.ConexaoSQLite; import java.sql.*;

public class UsuarioDAO {
    public Usuario autenticar(String login,String senha)throws SQLException{
        try(Connection c=ConexaoSQLite.conectar();PreparedStatement p=c.prepareStatement("SELECT * FROM usuarios WHERE login=? AND senha=? AND ativo=1")){p.setString(1,login);p.setString(2,senha);try(ResultSet r=p.executeQuery()){if(!r.next())return null;return "ADMINISTRADOR".equals(r.getString("perfil"))?new Administrador(r.getInt("id"),login,senha,r.getString("nome"),true):new Recepcionista(r.getInt("id"),login,senha,r.getString("nome"),true);}}
    }
    public void bloquear(String login)throws SQLException{try(Connection c=ConexaoSQLite.conectar();PreparedStatement p=c.prepareStatement("UPDATE usuarios SET ativo=0 WHERE login=?")){p.setString(1,login);p.executeUpdate();}}
}
