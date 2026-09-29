package br.com.gymetrics.entidade;

public class eUsuario {
	private int id;
	private String login;
	private String senha;
	private String nome;
	private String perfil; // ADMINISTRADOR ou RECEPCIONISTA
	private boolean ativo = true;

	public eUsuario() {}

	public eUsuario(int id, String login, String senha, String nome, String perfil, boolean ativo) {
		this.id = id;
		this.login = login;
		this.senha = senha;
		this.nome = nome;
		this.perfil = perfil;
		this.ativo = ativo;
	}

	public void validar() {
		if (login == null || login.isBlank() || nome == null || nome.isBlank() || perfil == null)
			throw new IllegalArgumentException("Login, nome e perfil são obrigatórios");
		if (senha == null || senha.length() < 6)
			throw new IllegalArgumentException("A senha deve ter no mínimo 6 caracteres");
	}

	public int getId() { return id; }
	public void setId(int id) { this.id = id; }

	public String getLogin() { return login; }
	public void setLogin(String login) { this.login = login; }

	public String getSenha() { return senha; }
	public void setSenha(String senha) { this.senha = senha; }

	public String getNome() { return nome; }
	public void setNome(String nome) { this.nome = nome; }

	public String getPerfil() { return perfil; }
	public void setPerfil(String perfil) { this.perfil = perfil; }

	public boolean isAtivo() { return ativo; }
	public void setAtivo(boolean ativo) { this.ativo = ativo; }
}
