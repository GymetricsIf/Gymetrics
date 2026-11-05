package br.com.gymetrics.dao;

import java.sql.SQLException;
import java.util.List;

import br.com.gymetrics.Data;
import br.com.gymetrics.negocio.Plano;

public class PlanoDAO {
	private final Data db;

	public PlanoDAO(Data db) {
		this.db = db;
	}

	public void salvar(Plano p) throws SQLException {
		p.validar();
		p.setId(db.insert(
			"INSERT INTO planos(nome, valor_mensal, dia_vencimento, ativo) VALUES(?,?,?,?)",
			ps -> {
				ps.setString(1, p.getNome());
				ps.setBigDecimal(2, p.getValorMensal());
				ps.setInt(3, p.getDiaVencimento());
				ps.setBoolean(4, p.isAtivo());
			}));
	}

	public void atualizar(Plano p) throws SQLException {
		p.validar();
		db.update("UPDATE planos SET nome=?, valor_mensal=?, dia_vencimento=?, ativo=? WHERE id=?",
			ps -> {
				ps.setString(1, p.getNome());
				ps.setBigDecimal(2, p.getValorMensal());
				ps.setInt(3, p.getDiaVencimento());
				ps.setBoolean(4, p.isAtivo());
				ps.setInt(5, p.getId());
			});
	}

	public List<Plano> listar() throws SQLException {
		return db.exec("SELECT * FROM planos ORDER BY nome", ps -> {}, PlanoDAO::map);
	}

	static Plano map(java.sql.ResultSet rs) throws SQLException {
		return new Plano(rs.getInt("id"), rs.getString("nome"), rs.getBigDecimal("valor_mensal"),
				rs.getInt("dia_vencimento"), rs.getBoolean("ativo"));
	}
}
