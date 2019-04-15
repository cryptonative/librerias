package bianance.trader.api.vo;

import java.io.Serializable;
import java.math.BigDecimal;

import bianance.trader.api.enumeracion.CriptoActivoEnum;

public class MercadoVO implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private CriptoActivoEnum activoEnum;
    private BigDecimal precioUltimoTrade;
    private String ultimoTrade;

    public CriptoActivoEnum getActivoEnum() {
        return activoEnum;
    }

    public void setActivoEnum(CriptoActivoEnum activoEnum) {
        this.activoEnum = activoEnum;
    }

    public BigDecimal getPrecioUltimoTrade() {
        return precioUltimoTrade;
    }

    public void setPrecioUltimoTrade(BigDecimal precioUltimoTrade) {
        this.precioUltimoTrade = precioUltimoTrade;
    }

    public String getUltimoTrade() {
        return ultimoTrade;
    }

    public void setUltimoTrade(String ultimoTrade) {
        this.ultimoTrade = ultimoTrade;
    }
}
