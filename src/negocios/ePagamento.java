package br.com.gymetrics.entidade;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.YearMonth;

public class ePagamento {
	private int id;
	private eMensalidade mensalidade;
	private YearMonth mesReferencia;
	private BigDecimal valorPago;
	private LocalDateTime dataPagamento = LocalDateTime.now();

	public ePagamento() {}

	public ePagamento(int id, eMensalidade mensalidade, YearMonth mesReferencia,
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

	public eMensalidade getMensalidade() { return mensalidade; }
	public void setMensalidade(eMensalidade mensalidade) { this.mensalidade = mensalidade; }

	public YearMonth getMesReferencia() { return mesReferencia; }
	public void setMesReferencia(YearMonth mesReferencia) { this.mesReferencia = mesReferencia; }

	public BigDecimal getValorPago() { return valorPago; }
	public void setValorPago(BigDecimal valorPago) { this.valorPago = valorPago; }

	public LocalDateTime getDataPagamento() { return dataPagamento; }
	public void setDataPagamento(LocalDateTime dataPagamento) { this.dataPagamento = dataPagamento; }
}
