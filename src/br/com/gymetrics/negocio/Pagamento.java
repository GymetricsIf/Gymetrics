package br.com.gymetrics.negocio;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.YearMonth;

public class Pagamento {
	private int id;
	private Mensalidade mensalidade;
	private YearMonth mesReferencia;
	private BigDecimal valorPago;
	private LocalDateTime dataPagamento = LocalDateTime.now();

	public Pagamento() {}

	public Pagamento(int id, Mensalidade mensalidade, YearMonth mesReferencia,
			BigDecimal valorPago, LocalDateTime dataPagamento) {
		this.id = id;
		this.mensalidade = mensalidade;
		this.mesReferencia = mesReferencia;
		this.valorPago = valorPago;
		this.dataPagamento = dataPagamento;
	}

	public void validar() {
		if (mensalidade == null || mesReferencia == null || valorPago == null || dataPagamento == null)
			throw new IllegalArgumentException("Dados do pagamento incompletos");
	}

	public int getId() { return id; }
	public void setId(int id) { this.id = id; }

	public Mensalidade getMensalidade() { return mensalidade; }
	public void setMensalidade(Mensalidade mensalidade) { this.mensalidade = mensalidade; }

	public YearMonth getMesReferencia() { return mesReferencia; }
	public void setMesReferencia(YearMonth mesReferencia) { this.mesReferencia = mesReferencia; }

	public BigDecimal getValorPago() { return valorPago; }
	public void setValorPago(BigDecimal valorPago) { this.valorPago = valorPago; }

	public LocalDateTime getDataPagamento() { return dataPagamento; }
	public void setDataPagamento(LocalDateTime dataPagamento) { this.dataPagamento = dataPagamento; }
}
