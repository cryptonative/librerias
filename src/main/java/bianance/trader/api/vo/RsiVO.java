package bianance.trader.api.vo;

import java.io.Serializable;
import java.math.BigDecimal;

public class RsiVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Boolean ganancia;
	private Double valor;
	private BigDecimal precioOrigen; 
	
	public Boolean isGanancia() {
		return ganancia;
	}
	public void setGanancia(Boolean ganancia) {
		this.ganancia = ganancia;
	}
	public Double getValor() {
		return valor;
	}
	public void setValor(Double valor) {
		if (valor < 0) {
			this.valor = -1 * valor ;
		} else {
			this.valor = valor;
		}
	}
	public BigDecimal getPrecioOrigen() {
		return precioOrigen;
	}
	public void setPrecioOrigen(BigDecimal precioOrigen) {
		this.precioOrigen = precioOrigen;
	}
	
}
