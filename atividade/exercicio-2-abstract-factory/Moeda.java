package checkout;

import java.util.Locale;

public final class Moeda {
    public static final Locale BR = Locale.of("pt", "BR");
    public static final Locale US = Locale.US;

    private Moeda() {
    }

    public static String brl(double valor) {
        return String.format(BR, "R$ %,.2f", valor);
    }

    public static String usd(double valor) {
        return String.format(US, "US$ %,.2f", valor);
    }

    public static String eur(double valor) {
        return String.format(BR, "€ %,.2f", valor);
    }

    public static String percentual(double fracao) {
        return String.format(BR, "%.2f%%", fracao * 100);
    }
}
