package br.com.gymetrics.entidade;

import java.time.LocalDateTime;

public class eRegistroAcesso {
	public enum Tipo { CHECK_IN, CHECK_OUT, TENTATIVA_NEGADA }

	private int id;
	private eAluno aluno; // null quando o aluno não foi encontrado
	private LocalDateTime dataHora = LocalDateTime.now();
	private Tipo tipo;
	private String observacao;

	public eRegistroAcesso() {}

	public eRegistroAcesso(int id, eAluno aluno, LocalDateTime dataHora, Tipo tipo, String observacao) {
		this.id = id;
		this.aluno = aluno;
		this.dataHora = dataHora;
		this.tipo = tipo;
		this.observacao = observacao;
	}

	/** Catraca: decide se libera a entrada. */
	public static eRegistroAcesso entrada(eAluno aluno) {
		if (aluno == null)
			return new eRegistroAcesso(0, null, LocalDateTime.now(), Tipo.TENTATIVA_NEGADA, "Aluno não encontrado");
		if (aluno.podeAcessar())
			return new eRegistroAcesso(0, aluno, LocalDateTime.now(), Tipo.CHECK_IN, "Acesso Liberado");

		String obs = aluno.cadastroCompleto() ? "Acesso bloqueado" : "Cadastro incompleto";
		return new eRegistroAcesso(0, aluno, LocalDateTime.now(), Tipo.TENTATIVA_NEGADA, obs);
	}

	public static eRegistroAcesso saida(eAluno aluno) {
		return new eRegistroAcesso(0, aluno, LocalDateTime.now(), Tipo.CHECK_OUT, "Saída registrada");
	}

	public int getId() { return id; }
	public void setId(int id) { this.id = id; }

	public eAluno getAluno() { return aluno; }
	public void setAluno(eAluno aluno) { this.aluno = aluno; }

	public LocalDateTime getDataHora() { return dataHora; }
	public void setDataHora(LocalDateTime dataHora) { this.dataHora = dataHora; }

	public Tipo getTipo() { return tipo; }
	public void setTipo(Tipo tipo) { this.tipo = tipo; }

	public String getObservacao() { return observacao; }
	public void setObservacao(String observacao) { this.observacao = observacao; }
}
