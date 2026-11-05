package br.com.gymetrics.dao;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.gymetrics.Data;
import br.com.gymetrics.negocio.Usuario;

public class UsuarioDAO {
	private static final int MAX_TENTATIVAS = 10;

	private final Data db;
	private final Map<String, Integer> tentativas = new HashMap<>();

	public UsuarioDAO(Data db) {
		this.db = db;
	}

	public void salvar(Usuario u) throws SQLException {
		u.validar();
		u.setId(db.insert(
			"INSERT INTO usuarios(login, senha, nome, perfil, ativo) VALUES(?,?,?,?,?)",
			ps -> {
				ps.setString(1, u.getLogin());
				ps.setString(2, u.getSenha());
				ps.setString(3, u.getNome());
				ps.setString(4, u.getPerfil());
				ps.setBoolean(5, u.isAtivo());
			}));
	}

	public int contar() throws SQLException {
		return db.exec("SELECT COUNT(*) FROM usuarios", ps -> {}, rs -> rs.getInt(1)).get(0);
	}

	public boolean estaBloqueado(String login) throws SQLException {
		return !db.exec("SELECT 1 FROM usuarios WHERE login=? AND ativo=0",
			ps -> ps.setString(1, login == null ? "" : login.trim()), rs -> 1).isEmpty();
	}

	/** Devolve o usuário, ou null se o login/senha estiverem errados. Bloqueia após 10 erros seguidos. */
	public Usuario autenticar(String login, String senha) throws SQLException {
		if (login == null || senha == null) return null;
		login = login.trim();
		final String l = login;

		List<Usuario> lista = db.exec("SELECT * FROM usuarios WHERE login=? AND senha=? AND ativo=1",
			ps -> {
				ps.setString(1, l);
				ps.setString(2, senha);
			},
			rs -> new Usuario(rs.getInt("id"), rs.getString("login"), rs.getString("senha"),
					rs.getString("nome"), rs.getString("perfil"), rs.getBoolean("ativo")));

		if (!lista.isEmpty()) {
			tentativas.remove(l);
			return lista.get(0);
		}

		if (tentativas.merge(l, 1, Integer::sum) >= MAX_TENTATIVAS)
			db.update("UPDATE usuarios SET ativo=0 WHERE login=?", ps -> ps.setString(1, l));
		return null;
	}
}
