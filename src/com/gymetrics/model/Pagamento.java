package br.com.gymetrics.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.YearMonth;

public class Pagamento {
    private int id; private Mensalidade mensalidade; private YearMonth mesReferencia; private BigDecimal valorPago; private LocalDateTime dataPagamento=LocalDateTime.now();
    public Pagamento() {}
    public Pagamento(int id,Mensalidade m,YearMonth mes,BigDecimal valor,LocalDateTime data){this.id=id;mensalidade=m;mesReferencia=mes;valorPago=valor;if(data!=null)dataPagamento=data;}
    public int getId(){return id;} public void setId(int v){id=v;}
    public Mensalidade getMensalidade(){return mensalidade;} public void setMensalidade(Mensalidade v){mensalidade=v;}
    public YearMonth getMesReferencia(){return mesReferencia;} public void setMesReferencia(YearMonth v){mesReferencia=v;}
    public BigDecimal getValorPago(){return valorPago;} public void setValorPago(BigDecimal v){valorPago=v;}
    public LocalDateTime getDataPagamento(){return dataPagamento;} public void setDataPagamento(LocalDateTime v){dataPagamento=v;}
}
