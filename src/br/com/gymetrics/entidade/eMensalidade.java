package br.com.gymetrics.entidade;

import java.math.BigDecimal;
import java.time.LocalDate;

public class eMensalidade {
	public enum Status { PENDENTE, VENCIDA, PAGA }

	private int id;
	private eAluno aluno;
	private LocalDate vencimento;
	private BigDecimal valor;
	private Status status = Status.PENDENTE;

	public eMensalidade() {}

	public eMensalidade(int id, eAluno aluno, LocalDate vencimento, BigDecimal valor, Status status) {
		this.id = id;
		this.aluno = aluno;
		this.vencimento = vencimento;
		this.valor = valor;
		this.status = status;
	}

	public int getId() { return id; }
	public void setId(int id) { this.id = id; }

	public eAluno getAluno() { return aluno; }
	public void setAluno(eAluno aluno) { this.aluno = aluno; }

	public LocalDate getVencimento() { return vencimento; }
	public void setVencimento(LocalDate vencimento) { this.vencimento = vencimento; }

	public BigDecimal getValor() { return valor; }
	public void setValor(BigDecimal valor) { this.valor = valor; }

	public Status getStatus() { return status; }
	public void setStatus(Status status) { this.status = status; }
}
