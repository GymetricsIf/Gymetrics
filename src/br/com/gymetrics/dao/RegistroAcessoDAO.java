package br.com.gymetrics.dao;

import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import br.com.gymetrics.Data;
import br.com.gymetrics.negocio.Aluno;
import br.com.gymetrics.negocio.RegistroAcesso;

public class RegistroAcessoDAO {
	private final Data db;

	public RegistroAcessoDAO(Data db) {
		this.db = db;
	}

	public void salvar(RegistroAcesso r) throws SQLException {
		r.setId(db.insert(
			"INSERT INTO registros_acesso(aluno_id, data_hora, tipo, observacao) VALUES(?,?,?,?)",
			ps -> {
				if (r.getAluno() == null) ps.setNull(1, Types.INTEGER);
				else ps.setInt(1, r.getAluno().getId());
				ps.setString(2, r.getDataHora().toString());
				ps.setString(3, r.getTipo().name());
				ps.setString(4, r.getObservacao());
			}));
	}

	/** Aluno pode ser null (não encontrado): nesse caso nada é gravado. */
	public RegistroAcesso registrarEntrada(Aluno aluno) throws SQLException {
		RegistroAcesso r = RegistroAcesso.entrada(aluno);
		if (aluno != null) salvar(r);
		return r;
	}

	public RegistroAcesso registrarSaida(Aluno aluno) throws SQLException {
		RegistroAcesso r = RegistroAcesso.saida(aluno);
		salvar(r);
		return r;
	}

	public int contarEntradas(LocalDate dia) throws SQLException {
		return db.exec("SELECT COUNT(*) FROM registros_acesso WHERE tipo='CHECK_IN' AND date(data_hora) = ?",
			ps -> ps.setString(1, dia.toString()), rs -> rs.getInt(1)).get(0);
	}

	/** True se a última movimentação do aluno hoje foi uma entrada, ou seja, ele ainda está na academia. */
	public boolean estaPresente(Aluno aluno, LocalDate dia) throws SQLException {
		List<String> ultimo = db.exec(
			"SELECT tipo FROM registros_acesso WHERE aluno_id = ? AND date(data_hora) = ? "
			+ "AND tipo IN ('CHECK_IN','CHECK_OUT') ORDER BY id DESC LIMIT 1",
			ps -> {
				ps.setInt(1, aluno.getId());
				ps.setString(2, dia.toString());
			}, rs -> rs.getString(1));
		return !ultimo.isEmpty() && ultimo.get(0).equals("CHECK_IN");
	}

	/** Alunos cuja última movimentação do dia foi uma entrada (RF08). */
	public int contarPresentes(LocalDate dia) throws SQLException {
		return db.exec(
			"SELECT COUNT(*) FROM registros_acesso r WHERE r.tipo='CHECK_IN' AND date(r.data_hora) = ? "
			+ "AND r.id = (SELECT MAX(r2.id) FROM registros_acesso r2 WHERE r2.aluno_id = r.aluno_id "
			+ "AND r2.tipo IN ('CHECK_IN','CHECK_OUT') AND date(r2.data_hora) = ?)",
			ps -> {
				ps.setString(1, dia.toString());
				ps.setString(2, dia.toString());
			}, rs -> rs.getInt(1)).get(0);
	}

	public List<RegistroAcesso> listarPorPeriodo(LocalDate inicio, LocalDate fim) throws SQLException {
		if (inicio == null || fim == null || fim.isBefore(inicio))
			throw new IllegalArgumentException("Período inválido");

		return db.exec(
			"SELECT r.*, a.nome, a.cpf, a.status FROM registros_acesso r "
			+ "LEFT JOIN alunos a ON a.id = r.aluno_id "
			+ "WHERE date(r.data_hora) BETWEEN ? AND ? ORDER BY r.data_hora",
			ps -> {
				ps.setString(1, inicio.toString());
				ps.setString(2, fim.toString());
			},
			rs -> {
				Aluno aluno = null;
				if (rs.getObject("aluno_id") != null) {
					aluno = new Aluno();
					aluno.setId(rs.getInt("aluno_id"));
					aluno.setNome(rs.getString("nome"));
					aluno.setCpf(rs.getString("cpf"));
					aluno.setStatus(Aluno.Status.valueOf(rs.getString("status")));
				}
				return new RegistroAcesso(rs.getInt("id"), aluno, LocalDateTime.parse(rs.getString("data_hora")),
						RegistroAcesso.Tipo.valueOf(rs.getString("tipo")), rs.getString("observacao"));
			});
	}
}
