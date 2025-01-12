package nl.ing.mortgage.assessment.demo.util;

import nl.ing.mortgage.assessment.demo.util.Utility;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UtilityTest {

    @Test
    void calculateMonthlyCost() {
        var cost = Utility.calculateMonthlyCost(200000.0, 2.0, 120);
        assertEquals(1840.27, cost);
    }
}