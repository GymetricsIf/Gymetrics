package br.com.gymetrics.negocio;

import java.time.LocalDateTime;

public class RegistroAcesso {
	public enum Tipo { CHECK_IN, CHECK_OUT, TENTATIVA_NEGADA }

	private int id;
	private Aluno aluno; // null quando o aluno não foi encontrado
	private LocalDateTime dataHora = agora();
	private Tipo tipo;
	private String observacao;

	public RegistroAcesso() {}

	public RegistroAcesso(int id, Aluno aluno, LocalDateTime dataHora, Tipo tipo, String observacao) {
		this.id = id;
		this.aluno = aluno;
		this.dataHora = dataHora;
		this.tipo = tipo;
		this.observacao = observacao;
	}

	/** Catraca: decide se libera a entrada. */
	public static RegistroAcesso entrada(Aluno aluno) {
		if (aluno == null)
			return new RegistroAcesso(0, null, agora(), Tipo.TENTATIVA_NEGADA, "Aluno não encontrado");
		if (aluno.podeAcessar())
			return new RegistroAcesso(0, aluno, agora(), Tipo.CHECK_IN, "Acesso Liberado");

		String obs;
		if (aluno.getStatus() == Aluno.Status.BLOQUEADO) obs = "Acesso bloqueado";
		else if (aluno.getStatus() == Aluno.Status.INADIMPLENTE) obs = "Mensalidade vencida";
		else obs = "Cadastro incompleto";
		return new RegistroAcesso(0, aluno, agora(), Tipo.TENTATIVA_NEGADA, obs);
	}

	public static RegistroAcesso saida(Aluno aluno) {
		return new RegistroAcesso(0, aluno, agora(), Tipo.CHECK_OUT, "Saída registrada");
	}

	private static LocalDateTime agora() {
		return LocalDateTime.now().withNano(0);
	}

	public int getId() { return id; }
	public void setId(int id) { this.id = id; }

	public Aluno getAluno() { return aluno; }
	public void setAluno(Aluno aluno) { this.aluno = aluno; }

	public LocalDateTime getDataHora() { return dataHora; }
	public void setDataHora(LocalDateTime dataHora) { this.dataHora = dataHora; }

	public Tipo getTipo() { return tipo; }
	public void setTipo(Tipo tipo) { this.tipo = tipo; }

	public String getObservacao() { return observacao; }
	public void setObservacao(String observacao) { this.observacao = observacao; }
}
