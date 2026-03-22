package digitalwallet_sde2.service;

import digitalwallet_sde2.Currency;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CurrencyConverter {
    // SDE3: Thread-safe map, although read-only here, good practice for dynamic updates later
    private static final Map<Currency, BigDecimal> exchangeRates = new ConcurrentHashMap<>();

    static {
        // Represents how much of this currency equals 1 Base Unit (USD)
        exchangeRates.put(Currency.USD, BigDecimal.ONE);
        exchangeRates.put(Currency.EUR, new BigDecimal("0.85")); // 1 USD = 0.85 EUR
        exchangeRates.put(Currency.GBP, new BigDecimal("0.72")); // 1 USD = 0.72 GBP
        exchangeRates.put(Currency.JPY, new BigDecimal("110.00")); // 1 USD = 110 JPY
    }

    public static BigDecimal convert(BigDecimal amount, Currency sourceCurrency, Currency targetCurrency) {
        if (sourceCurrency == targetCurrency) return amount;

        BigDecimal sourceRate = exchangeRates.get(sourceCurrency);
        BigDecimal targetRate = exchangeRates.get(targetCurrency);

        // Correct Mathematical Formula: 
        // Base Amount = amount / sourceRate
        // Target Amount = Base Amount * targetRate
        // To prevent precision loss, multiply first: (amount * targetRate) / sourceRate
        
        return amount.multiply(targetRate)
                     .divide(sourceRate, 2, RoundingMode.HALF_UP);
    }
}
