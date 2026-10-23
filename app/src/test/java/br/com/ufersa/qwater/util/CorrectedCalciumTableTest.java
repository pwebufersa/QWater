package br.com.ufersa.qwater.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Interpolação linear dupla da tabela de cálcio corrigido.
 *
 * A linha de cabeçalho, que guarda as condutividades, é usada como linha de
 * dados pela busca original. Por isso a razão 0,05 é interpolada entre o
 * cabeçalho e a primeira linha, e não devolve o valor 13,61 da célula. Esse
 * comportamento está preservado e registrado em docs/calculos.md.
 */
public class CorrectedCalciumTableTest {

    private static final double DELTA = 0.00001;

    private final CorrectedCalciumTable table = new CorrectedCalciumTable();

    @Test
    public void returnsTheInterpolatedValueOfTheOriginalTest() {
        assertEquals(0.3175, table.interpolate(25, 7), DELTA);
    }

    @Test
    public void returnsTheExactCellValueWhenTheInputFallsOnACrossing() {
        // razão 0,30 e condutividade 0,2 correspondem à célula 4,12 da tabela
        assertEquals(4.12, table.interpolate(0.30, 0.2), DELTA);
    }

    @Test
    public void averagesTheFourSurroundingCellsAtTheMiddleOfASquare() {
        // média de 4,00 e 4,12 com a média de 3,61 e 3,72
        assertEquals(3.8625, table.interpolate(0.325, 0.15), DELTA);
    }

    @Test
    public void usesTheHeaderRowAsDataForTheLowestRatio() {
        // preserva o comportamento original: 13,2 e não 13,61
        assertEquals(13.2, table.interpolate(0.05, 0.1), DELTA);
    }

    @Test
    public void returnsTheLastCellAtTheUpperLimits() {
        assertEquals(0.28, table.interpolate(30.0, 8.0), DELTA);
    }

    @Test
    public void returnsNaNWhenTheRatioExceedsTheTable() {
        assertTrue(Double.isNaN(table.interpolate(40, 2)));
    }

    @Test
    public void returnsNaNWhenTheConductivityExceedsTheTable() {
        assertTrue(Double.isNaN(table.interpolate(1, 10)));
    }
}
