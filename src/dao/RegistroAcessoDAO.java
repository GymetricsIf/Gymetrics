package br.com.gymetrics.dao;

import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import br.com.gymetrics.Data;
import br.com.gymetrics.entidade.eAluno;
import br.com.gymetrics.entidade.eRegistroAcesso;

public class RegistroAcessoDAO {
	private final Data db;

	public RegistroAcessoDAO(Data db) {
		this.db = db;
	}

	public void salvar(eRegistroAcesso r) throws SQLException {
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
	public eRegistroAcesso registrarEntrada(eAluno aluno) throws SQLException {
		eRegistroAcesso r = eRegistroAcesso.entrada(aluno);
		if (aluno != null) salvar(r);
		return r;
	}

	public eRegistroAcesso registrarSaida(eAluno aluno) throws SQLException {
		eRegistroAcesso r = eRegistroAcesso.saida(aluno);
		salvar(r);
		return r;
	}

	public List<eRegistroAcesso> listarPorPeriodo(LocalDate inicio, LocalDate fim) throws SQLException {
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
				eAluno aluno = null;
				if (rs.getObject("aluno_id") != null) {
					aluno = new eAluno();
					aluno.setId(rs.getInt("aluno_id"));
					aluno.setNome(rs.getString("nome"));
					aluno.setCpf(rs.getString("cpf"));
					aluno.setStatus(eAluno.Status.valueOf(rs.getString("status")));
				}
				return new eRegistroAcesso(rs.getInt("id"), aluno, LocalDateTime.parse(rs.getString("data_hora")),
						eRegistroAcesso.Tipo.valueOf(rs.getString("tipo")), rs.getString("observacao"));
			});
	}
}
