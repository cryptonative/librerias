package bianance.trader.api.enumeracion;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import bianance.trader.api.constante.Constante;

public enum CriptoActivoEnum {

    XRPUSDT("XRPUSDT", Constante.SCALE_XRPUSDT),
    ETHUSDT("ETHUSDT", Constante.SCALE_ETHUSDT)
    ;

    private String mercado;
    private int scale;
    private static final Map<String,CriptoActivoEnum> ENUM_MAP;

    private CriptoActivoEnum (String mercado, int scale){
        this.mercado = mercado;
        this.scale = scale;
    }
    
    public String getMercado() {
        return mercado;
    }
    
    public int getScale() {
        return scale;
    }

    static {
        Map<String,CriptoActivoEnum> map = new ConcurrentHashMap<String,CriptoActivoEnum>();
        for (CriptoActivoEnum instance : CriptoActivoEnum.values()) {
            map.put(instance.getMercado(),instance);
        }
        ENUM_MAP = Collections.unmodifiableMap(map);
    }

    public static CriptoActivoEnum get (String name) {
        return ENUM_MAP.get(name);
    }
}
