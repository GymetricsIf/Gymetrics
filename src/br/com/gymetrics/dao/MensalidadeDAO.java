package br.com.gymetrics.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

import br.com.gymetrics.Data;
import br.com.gymetrics.negocio.Aluno;
import br.com.gymetrics.negocio.Mensalidade;
import br.com.gymetrics.negocio.Plano;

public class MensalidadeDAO {
	private static final String SELECT =
		"SELECT m.*, a.nome, a.cpf, a.status a_status, p.id p_id, p.nome p_nome, p.valor_mensal, p.dia_vencimento, p.ativo p_ativo "
		+ "FROM mensalidades m JOIN alunos a ON a.id = m.aluno_id LEFT JOIN planos p ON p.id = a.plano_id ";

	private final Data db;

	public MensalidadeDAO(Data db) {
		this.db = db;
	}

	/** Cria a mensalidade do mês para todo aluno com plano que ainda não tem uma, e marca as vencidas. */
	public void prepararMes(LocalDate hoje) throws SQLException {
		YearMonth mes = YearMonth.from(hoje);
		db.transacao(() -> {
			db.update(
				"INSERT INTO mensalidades(aluno_id, vencimento, valor, status) "
				+ "SELECT a.id, printf('%s-%02d', ?, min(p.dia_vencimento, ?)), p.valor_mensal, 'PENDENTE' "
				+ "FROM alunos a JOIN planos p ON p.id = a.plano_id "
				+ "WHERE NOT EXISTS (SELECT 1 FROM mensalidades m WHERE m.aluno_id = a.id AND substr(m.vencimento, 1, 7) = ?)",
				ps -> {
					ps.setString(1, mes.toString());
					ps.setInt(2, mes.lengthOfMonth());
					ps.setString(3, mes.toString());
				});
			db.update("UPDATE mensalidades SET status='VENCIDA' WHERE status='PENDENTE' AND vencimento < ?",
				ps -> ps.setString(1, hoje.toString()));
			db.update("UPDATE alunos SET status='INADIMPLENTE' WHERE status='ATIVO' "
				+ "AND id IN (SELECT aluno_id FROM mensalidades WHERE status='VENCIDA')", ps -> {});
		});
	}

	public List<Mensalidade> listarDoMes(YearMonth mes) throws SQLException {
		return db.exec(SELECT + "WHERE substr(m.vencimento, 1, 7) = ? ORDER BY a.nome",
			ps -> ps.setString(1, mes.toString()), MensalidadeDAO::map);
	}

	/** Mensalidades ainda não pagas que vencem entre hoje e hoje + dias (RF12). */
	public List<Mensalidade> vencendo(LocalDate hoje, int dias) throws SQLException {
		return db.exec(SELECT + "WHERE m.status = 'PENDENTE' AND m.vencimento BETWEEN ? AND ? ORDER BY m.vencimento, a.nome",
			ps -> {
				ps.setString(1, hoje.toString());
				ps.setString(2, hoje.plusDays(dias).toString());
			}, MensalidadeDAO::map);
	}

	private static Mensalidade map(ResultSet rs) throws SQLException {
		Plano plano = null;
		if (rs.getObject("p_id") != null)
			plano = new Plano(rs.getInt("p_id"), rs.getString("p_nome"), rs.getBigDecimal("valor_mensal"),
					rs.getInt("dia_vencimento"), rs.getBoolean("p_ativo"));

		Aluno aluno = new Aluno();
		aluno.setId(rs.getInt("aluno_id"));
		aluno.setNome(rs.getString("nome"));
		aluno.setCpf(rs.getString("cpf"));
		aluno.setStatus(Aluno.Status.valueOf(rs.getString("a_status")));
		aluno.setPlano(plano);

		return new Mensalidade(rs.getInt("id"), aluno, LocalDate.parse(rs.getString("vencimento")),
				rs.getBigDecimal("valor"), Mensalidade.Status.valueOf(rs.getString("status")));
	}
}
