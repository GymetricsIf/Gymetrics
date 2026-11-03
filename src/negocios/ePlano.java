package br.com.gymetrics.entidade;

import java.math.BigDecimal;

public class ePlano {
	private int id;
	private String nome;
	private BigDecimal valorMensal;
	private int diaVencimento;
	private boolean ativo = true;

	public ePlano() {}

	public ePlano(int id, String nome, BigDecimal valorMensal, int diaVencimento, boolean ativo) {
		this.id = id;
		this.nome = nome;
		this.valorMensal = valorMensal;
		this.diaVencimento = diaVencimento;
		this.ativo = ativo;
	}

	public void validar() {
		if (nome == null || nome.isBlank() || valorMensal == null)
			throw new IllegalArgumentException("Nome e valor são obrigatórios");
		if (diaVencimento < 1 || diaVencimento > 31)
			throw new IllegalArgumentException("Vencimento deve estar entre 1 e 31");
	}

	public int getId() { return id; }
	public void setId(int id) { this.id = id; }

	public String getNome() { return nome; }
	public void setNome(String nome) { this.nome = nome; }

	public BigDecimal getValorMensal() { return valorMensal; }
	public void setValorMensal(BigDecimal valorMensal) { this.valorMensal = valorMensal; }

	public int getDiaVencimento() { return diaVencimento; }
	public void setDiaVencimento(int diaVencimento) { this.diaVencimento = diaVencimento; }

	public boolean isAtivo() { return ativo; }
	public void setAtivo(boolean ativo) { this.ativo = ativo; }
}
