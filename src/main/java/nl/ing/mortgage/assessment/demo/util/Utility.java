package nl.ing.mortgage.assessment.demo.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Utility {
    /**
     * Calculate monthly cost for mortgage
     *
     * @param loanValue
     * @param annualRate
     * @param maturityMonths
     * @return monthly cost
     */
    public static Double calculateMonthlyCost(Double loanValue, Double annualRate, Integer maturityMonths) {
        double monthlyRate = (annualRate / 100) / 12;

        // Special case: if interest rate is 0
        if (monthlyRate == 0) {
            return loanValue / maturityMonths;
        }

        // Loan amortization formula: M = P * [r(1 + r)^n] / [(1 + r)^n - 1]
        double onePlusRate = 1 + monthlyRate;
        double numerator = monthlyRate * Math.pow(onePlusRate, maturityMonths);
        double denominator = Math.pow(onePlusRate, maturityMonths) - 1;

        // Monthly payment
        return roundToTwoDecimalPlaces(loanValue * (numerator / denominator));
    }

    /**
     * Rounds a double value to two decimal places.
     *
     * @param value the double value to round
     * @return the rounded value
     */
    public static double roundToTwoDecimalPlaces(double value) {
        return BigDecimal.valueOf(value)
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
    }
}
