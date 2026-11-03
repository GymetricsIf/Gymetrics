package br.com.gymetrics.dao;

import java.sql.SQLException;

import br.com.gymetrics.Data;
import br.com.gymetrics.entidade.eAluno;
import br.com.gymetrics.entidade.ePagamento;
import br.com.gymetrics.entidade.eMensalidade;

public class PagamentoDAO {
	private final Data db;

	public PagamentoDAO(Data db) {
		this.db = db;
	}

	/** Grava o pagamento, quita a mensalidade e reativa o aluno inadimplente, tudo numa transação. */
	public void salvar(ePagamento p) throws SQLException {
		p.validar();
		int mensalidadeId = p.getMensalidade().getId();

		db.transacao(() -> {
			p.setId(db.insert(
				"INSERT INTO pagamentos(mensalidade_id, mes_referencia, valor_pago, data_pagamento) VALUES(?,?,?,?)",
				ps -> {
					ps.setInt(1, mensalidadeId);
					ps.setString(2, p.getMesReferencia().toString());
					ps.setBigDecimal(3, p.getValorPago());
					ps.setString(4, p.getDataPagamento().toString());
				}));
			db.update("UPDATE mensalidades SET status='PAGA' WHERE id=?", ps -> ps.setInt(1, mensalidadeId));
			db.update("UPDATE alunos SET status='ATIVO' WHERE status='INADIMPLENTE' "
					+ "AND id = (SELECT aluno_id FROM mensalidades WHERE id=?)", ps -> ps.setInt(1, mensalidadeId));
		});

		eMensalidade m = p.getMensalidade();
		m.setStatus(eMensalidade.Status.PAGA);
		if (m.getAluno() != null && m.getAluno().getStatus() == eAluno.Status.INADIMPLENTE)
			m.getAluno().setStatus(eAluno.Status.ATIVO);
	}
}
