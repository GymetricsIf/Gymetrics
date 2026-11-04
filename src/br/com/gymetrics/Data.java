package br.com.gymetrics;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Data {
	@FunctionalInterface
	public interface RowMapper<T> {
		T map(ResultSet rs) throws SQLException;
	}

	@FunctionalInterface
	public interface SqlAction {
		void run() throws SQLException;
	}

	public Data() {}
	private Connection mConnection = null;

	public void open(String fName) throws SQLException {
		if (null != mConnection)
			throw new RuntimeException("BUG: BD já esta aberto.");

        mConnection = DriverManager.getConnection("jdbc:sqlite:" + fName);
	}

	private void check() {
		if (null == mConnection)
			throw new RuntimeException("BUG: Tentando executar em um BD que está fechado!");
	}

	/** SELECT: devolve uma lista com um objeto por linha. */
	public <T> List<T> exec(String sql, SetupExecCallback setup, RowMapper<T> mapper) throws SQLException {
		check();

		try (PreparedStatement pStmt = mConnection.prepareStatement(sql)) {
			setup.setupExecParams(pStmt);
			try (ResultSet rs = pStmt.executeQuery()) {
				List<T> lista = new ArrayList<>();
				while (rs.next())
					lista.add(mapper.map(rs));
				return lista;
			}
		}
	}

	/** INSERT: devolve o id gerado. */
	public int insert(String sql, SetupExecCallback setup) throws SQLException {
		check();

		try (PreparedStatement pStmt = mConnection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			setup.setupExecParams(pStmt);
			pStmt.executeUpdate();
			try (ResultSet rs = pStmt.getGeneratedKeys()) {
				return rs.next() ? rs.getInt(1) : 0;
			}
		}
	}

	/** UPDATE / DELETE: devolve quantas linhas foram afetadas. */
	public int update(String sql, SetupExecCallback setup) throws SQLException {
		check();

		try (PreparedStatement pStmt = mConnection.prepareStatement(sql)) {
			setup.setupExecParams(pStmt);
			return pStmt.executeUpdate();
		}
	}

	/** SQL solto sem parâmetros (CREATE TABLE etc). */
	public void run(String sql) throws SQLException {
		check();

		try (Statement stmt = mConnection.createStatement()) {
			stmt.execute(sql);
		}
	}

	/** Tudo o que rodar dentro da ação entra numa transação só. */
	public void transacao(SqlAction acao) throws SQLException {
		check();

		mConnection.setAutoCommit(false);
		try {
			acao.run();
			mConnection.commit();
		} catch (SQLException | RuntimeException e) {
			mConnection.rollback();
			throw e;
		} finally {
			mConnection.setAutoCommit(true);
		}
	}

	public void criarTabelas() throws SQLException {
		run("CREATE TABLE IF NOT EXISTS planos(id INTEGER PRIMARY KEY AUTOINCREMENT, nome TEXT NOT NULL, valor_mensal REAL NOT NULL, dia_vencimento INTEGER NOT NULL, ativo INTEGER NOT NULL DEFAULT 1)");
		run("CREATE TABLE IF NOT EXISTS alunos(id INTEGER PRIMARY KEY AUTOINCREMENT, nome TEXT NOT NULL, cpf TEXT NOT NULL UNIQUE, endereco TEXT, telefone TEXT, data_nascimento TEXT, codigo_acesso TEXT UNIQUE, status TEXT NOT NULL, plano_id INTEGER REFERENCES planos(id))");
		run("CREATE TABLE IF NOT EXISTS usuarios(id INTEGER PRIMARY KEY AUTOINCREMENT, login TEXT NOT NULL UNIQUE, senha TEXT NOT NULL, nome TEXT NOT NULL, perfil TEXT NOT NULL, ativo INTEGER NOT NULL DEFAULT 1)");
		run("CREATE TABLE IF NOT EXISTS mensalidades(id INTEGER PRIMARY KEY AUTOINCREMENT, aluno_id INTEGER NOT NULL REFERENCES alunos(id), vencimento TEXT NOT NULL, valor REAL NOT NULL, status TEXT NOT NULL)");
		run("CREATE TABLE IF NOT EXISTS pagamentos(id INTEGER PRIMARY KEY AUTOINCREMENT, mensalidade_id INTEGER NOT NULL REFERENCES mensalidades(id), mes_referencia TEXT NOT NULL, valor_pago REAL NOT NULL, data_pagamento TEXT NOT NULL)");
		run("CREATE TABLE IF NOT EXISTS registros_acesso(id INTEGER PRIMARY KEY AUTOINCREMENT, aluno_id INTEGER REFERENCES alunos(id), data_hora TEXT NOT NULL, tipo TEXT NOT NULL, observacao TEXT)");
		run("CREATE TABLE IF NOT EXISTS bloqueios_acesso(id INTEGER PRIMARY KEY AUTOINCREMENT, aluno_id INTEGER NOT NULL REFERENCES alunos(id), motivo TEXT NOT NULL, data_hora TEXT NOT NULL, usuario_id INTEGER REFERENCES usuarios(id))");
	}

	public void suicide() throws SQLException {
		if (null == mConnection)
			throw new RuntimeException("BUG: Tentando fechar um BD já fechado");

		mConnection.close();
		mConnection = null;
	}
}
