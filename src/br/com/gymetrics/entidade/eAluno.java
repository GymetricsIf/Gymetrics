package br.com.gymetrics.entidade;

import java.time.LocalDate;

public class eAluno {
	public enum Status { ATIVO, INADIMPLENTE, BLOQUEADO }

	private int id;
	private String nome;
	private String cpf;
	private String endereco;
	private String telefone;
	private LocalDate dataNascimento;
	private String codigoAcesso;
	private Status status = Status.ATIVO;
	private ePlano plano;

	public eAluno() {}

	public eAluno(int id, String nome, String cpf, String endereco, String telefone,
			LocalDate dataNascimento, String codigoAcesso, Status status, ePlano plano) {
		this.id = id;
		this.nome = nome;
		this.cpf = cpf;
		this.endereco = endereco;
		this.telefone = telefone;
		this.dataNascimento = dataNascimento;
		this.codigoAcesso = codigoAcesso;
		this.status = status;
		this.plano = plano;
	}

	public boolean cadastroCompleto() {
		return !vazio(nome) && !vazio(cpf) && !vazio(endereco)
				&& !vazio(telefone) && dataNascimento != null;
	}

	public boolean podeAcessar() {
		return status == Status.ATIVO && cadastroCompleto();
	}

	public void validar() {
		if (!cadastroCompleto())
			throw new IllegalArgumentException("Preencha nome, CPF, endereço, telefone e data de nascimento");
		if (codigoAcesso != null && !codigoAcesso.matches("\\d+"))
			throw new IllegalArgumentException("O código de acesso deve ser numérico");
	}

	private static boolean vazio(String s) {
		return s == null || s.isBlank();
	}

	public int getId() { return id; }
	public void setId(int id) { this.id = id; }

	public String getNome() { return nome; }
	public void setNome(String nome) { this.nome = nome; }

	public String getCpf() { return cpf; }
	public void setCpf(String cpf) { this.cpf = cpf; }

	public String getEndereco() { return endereco; }
	public void setEndereco(String endereco) { this.endereco = endereco; }

	public String getTelefone() { return telefone; }
	public void setTelefone(String telefone) { this.telefone = telefone; }

	public LocalDate getDataNascimento() { return dataNascimento; }
	public void setDataNascimento(LocalDate dataNascimento) { this.dataNascimento = dataNascimento; }

	public String getCodigoAcesso() { return codigoAcesso; }
	public void setCodigoAcesso(String codigoAcesso) { this.codigoAcesso = codigoAcesso; }

	public Status getStatus() { return status; }
	public void setStatus(Status status) { this.status = status; }

	public ePlano getPlano() { return plano; }
	public void setPlano(ePlano plano) { this.plano = plano; }
}
