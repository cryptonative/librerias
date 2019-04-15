package bianance.trader.api;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import com.binance.api.client.BinanceApiClientFactory;
import com.binance.api.client.BinanceApiWebSocketClient;
import com.binance.api.client.domain.event.CandlestickEvent;
import com.binance.api.client.domain.market.Candlestick;
import com.binance.api.client.domain.market.CandlestickInterval;

import bianance.trader.api.enumeracion.CriptoActivoEnum;
import bianance.trader.api.index.IndicadorRSI;
import bianance.trader.api.vo.CandlestickPriceVO;
import bianance.trader.api.vo.MercadoVO;

/**
 * Hello world!
 *
 */
public class App 
{

	private Map<Long, Candlestick> candlesticksCache;
	private ArrayList<Double> RSIs;
	
    public static void main( String[] args )
    {
    	App app = new App();
    	
    	CandlestickInterval tiempo = CandlestickInterval.ONE_MINUTE;
    	app.RSIs = new ArrayList<>();
    	
    	MercadoVO mercadoVO = new MercadoVO();
    	mercadoVO.setActivoEnum(CriptoActivoEnum.ETHUSDT);
        PreCalculoRSI primerRSI = new PreCalculoRSI(mercadoVO, tiempo);
        
        IndicadorRSI rsi = primerRSI.inicia();
        
        app.RSIs.add(rsi.getRsi());
       
        
        printRSI(app.RSIs);
        
        
        
        app.startCandlestickEventStreaming(rsi, CriptoActivoEnum.ETHUSDT.toString(), tiempo);
        
    }

    private static void printRSI(ArrayList<Double> RSIs) {
    	for (Double rsi : RSIs) {
    		System.out.println(rsi);
    	}
    }
    
    private void startCandlestickEventStreaming(IndicadorRSI rsi, String symbol, CandlestickInterval interval) {
        BinanceApiClientFactory factory = BinanceApiClientFactory.newInstance();
        BinanceApiWebSocketClient client = factory.newWebSocketClient();

        this.candlesticksCache = new TreeMap<>();
       
        client.onCandlestickEvent(symbol.toLowerCase(), interval, response -> {
          
        Long openTime = response.getOpenTime();
        Candlestick updateCandlestick = candlesticksCache.get(openTime);
        CandlestickPriceVO vela = setCandelsticksPrices(response);
        
        if (updateCandlestick == null) {
	        // new candlestick
        	
        	rsi.setRsi(rsi.getRSI(vela.getClose(), false));
        	System.out.println("---------> Nueva vela rsi: " + rsi.getRsi());
        	
	        updateCandlestick = new Candlestick();
        }

          rsi.setRsi(rsi.getRSI(vela.getClose(), true));
          
          System.out.println("Precio: " + response.getClose());
          System.out.println("Misma vela rsi: " + rsi.getRsi());
          
          // update candlestick with the stream data
          updateCandlestick.setOpenTime(response.getOpenTime());
          
          updateCandlestick.setOpen(response.getOpen());
          updateCandlestick.setLow(response.getLow());
          updateCandlestick.setHigh(response.getHigh());
          updateCandlestick.setClose(response.getClose());
          updateCandlestick.setCloseTime(response.getCloseTime());
          updateCandlestick.setVolume(response.getVolume());
          updateCandlestick.setNumberOfTrades(response.getNumberOfTrades());
          updateCandlestick.setQuoteAssetVolume(response.getQuoteAssetVolume());
          updateCandlestick.setTakerBuyQuoteAssetVolume(response.getTakerBuyQuoteAssetVolume());
          updateCandlestick.setTakerBuyBaseAssetVolume(response.getTakerBuyQuoteAssetVolume());
         
          // Store the updated candlestick in the cache
          candlesticksCache.put(openTime, updateCandlestick);
          
//          System.out.println(symbol + ": " +updateCandlestick);
        });
      }
    
    private CandlestickPriceVO setCandelsticksPrices(CandlestickEvent response) {
  	  
  	  CandlestickPriceVO vela = new CandlestickPriceVO();
  	  
        BigDecimal open = new BigDecimal(response.getOpen());
        BigDecimal low = new BigDecimal(response.getLow());
        BigDecimal close = new BigDecimal(response.getClose());
        BigDecimal high = new BigDecimal(response.getHigh());
        
        vela.setOpen(open == null ? BigDecimal.ZERO : open);
        vela.setLow(low == null ? BigDecimal.ZERO : low);
        vela.setClose(close == null ? BigDecimal.ZERO : close);
        vela.setHigh(high == null ? BigDecimal.ZERO : high);
        
        return vela;
    }
    
    /*
    private Double getRSI(BigDecimal precioActual, Boolean esMismaVela) {
    	this.rsi.getRSI(precioActual);
	    return (double) 0;
    }*/
}
