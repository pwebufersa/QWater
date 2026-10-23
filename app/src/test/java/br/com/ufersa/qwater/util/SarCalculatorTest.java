package br.com.ufersa.qwater.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Cálculo da RAS normal e da RAS corrigida.
 *
 * A razão HCO3/Ca cai na tabela de cálcio corrigido, cujo comportamento nos
 * limites está descrito em CorrectedCalciumTableTest.
 */
public class SarCalculatorTest {

    private static final double DELTA = 0.0001;

    private final SarCalculator calculator = new SarCalculator();

    @Test
    public void dividesSodiumByTheSquareRootOfTheMeanOfCalciumAndMagnesium() {
        // 10 / raiz(3) = 5,7735
        assertEquals(5.7735, calculator.calculateNormalSar(4, 2, 10), DELTA);
    }

    @Test
    public void normalSarIsZeroWhenSodiumIsZero() {
        assertEquals(0.0, calculator.calculateNormalSar(4, 2, 0), DELTA);
    }

    @Test
    public void normalSarIsInfiniteWhenCalciumAndMagnesiumAreZero() {
        assertTrue(Double.isInfinite(calculator.calculateNormalSar(0, 0, 10)));
    }

    @Test
    public void correctedSarUsesTheCorrectedCalciumFromTheTable() {
        // razão HCO3/Ca = 2/4 = 0,5, condutividade 0,1: cálcio corrigido 2,84
        // 10 / raiz((2,84 + 2)/2) = 6,4282
        assertEquals(6.4282, calculator.calculateCorrectedSar(4, 2, 10, 0.1, 2.0), DELTA);
    }

    @Test
    public void correctedSarWithoutBicarbonateUsesTheHeaderRow() {
        // razão zero: cálcio corrigido 0,1, como no código original
        // 10 / raiz((0,1 + 2)/2) = 9,759
        assertEquals(9.759, calculator.calculateCorrectedSar(4, 2, 10, 0.1, 0), DELTA);
    }

    @Test
    public void correctedSarIsNaNWhenTheConductivityExceedsTheTable() {
        assertTrue(Double.isNaN(calculator.calculateCorrectedSar(4, 2, 10, 10, 0.2)));
    }
}
