package checkout;

import java.util.LinkedHashMap;
import java.util.Map;

public enum Fabricas {
    BRASIL {
        @Override
        public FabricaCheckout criar() {
            return new FabricaBrasil(true);
        }
    },
    BRASIL_BOLETO {
        @Override
        public FabricaCheckout criar() {
            return new FabricaBrasil(false);
        }
    },
    ESTADOS_UNIDOS {
        @Override
        public FabricaCheckout criar() {
            return new FabricaEstadosUnidos();
        }
    },
    ALEMANHA {
        @Override
        public FabricaCheckout criar() {
            return new FabricaAlemanha();
        }
    };

    public abstract FabricaCheckout criar();

    public static Map<String, FabricaCheckout> todas() {
        Map<String, FabricaCheckout> familias = new LinkedHashMap<>();
        for (Fabricas familia : values()) {
            familias.put(familia.name(), familia.criar());
        }
        return familias;
    }
}
