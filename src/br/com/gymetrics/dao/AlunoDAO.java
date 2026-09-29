package br.com.gymetrics.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;
import java.util.List;

import br.com.gymetrics.Data;
import br.com.gymetrics.entidade.eAluno;
import br.com.gymetrics.entidade.ePlano;
import br.com.gymetrics.entidade.eUsuario;

public class AlunoDAO {
	private static final String SELECT =
		"SELECT a.*, p.id p_id, p.nome p_nome, p.valor_mensal, p.dia_vencimento, p.ativo p_ativo "
		+ "FROM alunos a LEFT JOIN planos p ON p.id = a.plano_id ";

	private final Data db;

	public AlunoDAO(Data db) {
		this.db = db;
	}

	public void salvar(eAluno a) throws SQLException {
		a.validar();
		a.setId(db.insert(
			"INSERT INTO alunos(nome, cpf, endereco, telefone, data_nascimento, codigo_acesso, status, plano_id) VALUES(?,?,?,?,?,?,?,?)",
			ps -> preencher(ps, a)));
	}

	public void atualizar(eAluno a) throws SQLException {
		a.validar();
		db.update(
			"UPDATE alunos SET nome=?, cpf=?, endereco=?, telefone=?, data_nascimento=?, codigo_acesso=?, status=?, plano_id=? WHERE id=?",
			ps -> {
				preencher(ps, a);
				ps.setInt(9, a.getId());
			});
	}

	public List<eAluno> buscar(String termo) throws SQLException {
		String like = "%" + (termo == null ? "" : termo) + "%";
		return db.exec(SELECT + "WHERE a.nome LIKE ? OR a.cpf LIKE ? ORDER BY a.nome",
			ps -> {
				ps.setString(1, like);
				ps.setString(2, like);
			}, AlunoDAO::map);
	}

	public eAluno buscarPorCpfOuCodigo(String valor) throws SQLException {
		List<eAluno> lista = db.exec(SELECT + "WHERE a.cpf = ? OR a.codigo_acesso = ?",
			ps -> {
				ps.setString(1, valor);
				ps.setString(2, valor);
			}, AlunoDAO::map);
		return lista.isEmpty() ? null : lista.get(0);
	}

	public void bloquear(eAluno a, String motivo, eUsuario responsavel) throws SQLException {
		if (motivo == null || motivo.isBlank())
			throw new IllegalArgumentException("Informe o motivo do bloqueio");

		db.transacao(() -> {
			db.update("UPDATE alunos SET status='BLOQUEADO' WHERE id=?", ps -> ps.setInt(1, a.getId()));
			db.insert("INSERT INTO bloqueios_acesso(aluno_id, motivo, data_hora, usuario_id) VALUES(?,?,datetime('now'),?)",
				ps -> {
					ps.setInt(1, a.getId());
					ps.setString(2, motivo);
					if (responsavel == null) ps.setNull(3, Types.INTEGER);
					else ps.setInt(3, responsavel.getId());
				});
		});
		a.setStatus(eAluno.Status.BLOQUEADO);
	}

	private void preencher(PreparedStatement ps, eAluno a) throws SQLException {
		ps.setString(1, a.getNome());
		ps.setString(2, a.getCpf());
		ps.setString(3, a.getEndereco());
		ps.setString(4, a.getTelefone());
		ps.setString(5, a.getDataNascimento() == null ? null : a.getDataNascimento().toString());
		ps.setString(6, a.getCodigoAcesso());
		ps.setString(7, a.getStatus().name());
		if (a.getPlano() == null || a.getPlano().getId() == 0) ps.setNull(8, Types.INTEGER);
		else ps.setInt(8, a.getPlano().getId());
	}

	private static eAluno map(ResultSet rs) throws SQLException {
		ePlano plano = null;
		if (rs.getObject("p_id") != null)
			plano = new ePlano(rs.getInt("p_id"), rs.getString("p_nome"), rs.getBigDecimal("valor_mensal"),
					rs.getInt("dia_vencimento"), rs.getBoolean("p_ativo"));

		String nascimento = rs.getString("data_nascimento");
		return new eAluno(rs.getInt("id"), rs.getString("nome"), rs.getString("cpf"),
				rs.getString("endereco"), rs.getString("telefone"),
				nascimento == null ? null : LocalDate.parse(nascimento),
				rs.getString("codigo_acesso"), eAluno.Status.valueOf(rs.getString("status")), plano);
	}
}
