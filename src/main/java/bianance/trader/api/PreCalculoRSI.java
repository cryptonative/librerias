package bianance.trader.api;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.binance.api.client.BinanceApiClientFactory;
import com.binance.api.client.BinanceApiRestClient;
import com.binance.api.client.domain.market.Candlestick;
import com.binance.api.client.domain.market.CandlestickInterval;

import bianance.trader.api.index.IndicadorRSI;
import bianance.trader.api.vo.MercadoVO;


/**
 * Obtener los periodos anteriores a la vela actual.
 * @author Del Carmen
 *
 */
public class PreCalculoRSI {
   
    private MercadoVO mercadoVO;
    private CandlestickInterval candlestickInterval;
        
    public PreCalculoRSI(MercadoVO mercadoVO, CandlestickInterval candlestickInterval) {
    	this.candlestickInterval = candlestickInterval;
        this.mercadoVO = mercadoVO;
    }
    
    public IndicadorRSI inicia() {
        IndicadorRSI rsi = new IndicadorRSI(14, obtenerValoresCierreAnteriores());
		rsi.inicializaRSI();
		return rsi;
    }
	    
    private ArrayList<BigDecimal> obtenerValoresCierreAnteriores() {
        BinanceApiClientFactory factory = BinanceApiClientFactory.newInstance();
        BinanceApiRestClient cliente = factory.newRestClient();

        List<Candlestick> candlestickBars = cliente.getCandlestickBars(this.mercadoVO.getActivoEnum().getMercado(), candlestickInterval, 14, null, null);

        ArrayList<BigDecimal> cierres = new ArrayList<>();

        for (int i = 0; i < candlestickBars.size(); i++) {
            cierres.add(new BigDecimal(candlestickBars.get(i).getClose()));
        }
        return cierres;
    }
}
