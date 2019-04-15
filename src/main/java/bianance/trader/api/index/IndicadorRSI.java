package bianance.trader.api.index;

import java.math.BigDecimal;
import java.util.ArrayList;

import bianance.trader.api.vo.RsiVO;

public class IndicadorRSI {

	private Double rsi;
	
    public void setRsi(Double rsi) {
		this.rsi = rsi;
	}

	public Double getRsi() {
		return rsi;
	}

	ArrayList<RsiVO> gananciasPerdidas;
    Double gananciaAcumulada;
    Double perdidaAcumulada;

    Integer longitud;
    ArrayList<BigDecimal> items;

    public IndicadorRSI(Integer longitud, ArrayList<BigDecimal> items) {
        this.longitud = longitud;
        this.items = items;

        this.gananciaAcumulada = (double)0;
        this.perdidaAcumulada = (double)0;

        gananciasPerdidas = new ArrayList<>();
    }

    public Double getRSI(BigDecimal cierrePrecio, boolean mismaVela) {
        Double rsiValor = (double) 0;
        // Valida si se puede realizar calculo de RSI
        if(this.gananciasPerdidas.size()>0) {

        	Double gananciaAcumulada = this.gananciaAcumulada;
            Double perdidaAcumulada = this.perdidaAcumulada;
            
            RsiVO ultimo = this.gananciasPerdidas.get(this.gananciasPerdidas.size()-1);
            RsiVO inicio = this.gananciasPerdidas.get(0);

            Double dif = cierrePrecio.subtract(ultimo.getPrecioOrigen()).doubleValue();

            RsiVO actual = new RsiVO();
            actual.setGanancia((dif >= (double)0)? true : false);
            actual.setValor(dif);
            actual.setPrecioOrigen(cierrePrecio);

            if (inicio.isGanancia()) {
                this.gananciaAcumulada -= inicio.getValor();
            } else {
                this.perdidaAcumulada -= inicio.getValor();
            }

            if (actual.isGanancia()) {
                this.gananciaAcumulada += actual.getValor();
            } else {
                this.perdidaAcumulada += actual.getValor();
            }
            if (!mismaVela) {
	            this.gananciasPerdidas.remove(0);
	            this.gananciasPerdidas.add(actual);
	            
	            rsiValor = calculaRSI();
            } else {
            	rsiValor = calculaRSI();
            	
            	this.gananciaAcumulada = gananciaAcumulada;
            	this.perdidaAcumulada = perdidaAcumulada;
            }
        }
        return rsiValor;
    }

    public Double inicializaRSI() {
        this.rsi = (double) 0;
        //Valido si por lo menos hay gun calculo de RSI previo.
        if (this.longitud == this.items.size()) {
            // Inicializo para el calculo del primer RSI de la lista.
            for (int i = 0; i < this.items.size()-1; i++) {
                Double valor = this.items.get(i+1).subtract(this.items.get(i)).doubleValue();
                RsiVO rsiVO = new RsiVO();
                if (valor <= 0) {
                    rsiVO.setGanancia(false);
                    rsiVO.setValor(valor);
                    this.perdidaAcumulada+= (valor*-1);
                } else {
                    rsiVO.setGanancia(true);
                    rsiVO.setValor(valor);
                    this.gananciaAcumulada+= valor;
                }
                rsiVO.setPrecioOrigen(items.get(i+1));
                this.gananciasPerdidas.add(rsiVO);

            }
            this.rsi = calculaRSI();
        }
        return this.rsi;
    }

    private Double calculaRSI() {
        Double rs = (this.gananciaAcumulada/this.longitud)/(this.perdidaAcumulada/this.longitud);
        return (100 - (100/(1+rs))) + 7;
    }
}
