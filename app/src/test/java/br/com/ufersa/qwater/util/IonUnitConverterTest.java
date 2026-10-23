package br.com.ufersa.qwater.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Conversões de unidade, conferidas com valores calculados à mão a partir das
 * massas molares do código original.
 */
public class IonUnitConverterTest {

    private final IonUnitConverter converter = new IonUnitConverter();

    @Test
    public void convertsMilliequivalentsToMilligramsForDivalentIon() {
        assertEquals(400.8, converter.milliequivalentsToMilligrams(Ion.CALCIUM, 20), 0.0001);
    }

    @Test
    public void convertsMilliequivalentsToMilligramsForMonovalentIon() {
        assertEquals(299.9, converter.milliequivalentsToMilligrams(Ion.SODIUM, 10), 0.0001);
        assertEquals(354.53, converter.milliequivalentsToMilligrams(Ion.CHLORIDE, 10), 0.0001);
    }

    @Test
    public void convertsMilligramsToMilliequivalentsForDivalentIon() {
        assertEquals(20.0, converter.milligramsToMilliequivalents(Ion.CALCIUM, 400.8), 0.0001);
    }

    @Test
    public void convertsMilligramsToMilliequivalentsForMonovalentIon() {
        assertEquals(10.0, converter.milligramsToMilliequivalents(Ion.POTASSIUM, 391.0), 0.0001);
    }

    @Test
    public void convertsMillimolesToMilliequivalentsUsingValence() {
        assertEquals(20.0, converter.millimolesToMilliequivalents(Ion.CALCIUM, 10), 0.0001);
        assertEquals(10.0, converter.millimolesToMilliequivalents(Ion.POTASSIUM, 10), 0.0001);
    }

    @Test
    public void convertsMilliequivalentsToMillimolesUsingValence() {
        assertEquals(10.0, converter.milliequivalentsToMillimoles(Ion.MAGNESIUM, 20), 0.0001);
        assertEquals(10.0, converter.milliequivalentsToMillimoles(Ion.BICARBONATE, 10), 0.0001);
    }

    @Test
    public void convertsMilligramsToMillimolesWithTheOriginalFormula() {
        // fórmula preservada: (mg / valência) / massa molar
        assertEquals((1000.0 / 2) / 40.08, converter.milligramsToMillimoles(Ion.CALCIUM, 1000), 0.0001);
        assertEquals(1000.0 / 61.0168, converter.milligramsToMillimoles(Ion.BICARBONATE, 1000), 0.0001);
    }

    @Test
    public void roundTripBetweenMilligramsAndMilliequivalentsKeepsTheValue() {
        for (Ion ion : Ion.values()) {
            double milligrams = converter.milliequivalentsToMilligrams(ion, 12.5);
            assertEquals(12.5, converter.milligramsToMilliequivalents(ion, milligrams), 0.0001);
        }
    }

    @Test
    public void roundTripBetweenMillimolesAndMilliequivalentsKeepsTheValue() {
        for (Ion ion : Ion.values()) {
            double milliequivalents = converter.millimolesToMilliequivalents(ion, 7.0);
            assertEquals(7.0, converter.milliequivalentsToMillimoles(ion, milliequivalents), 0.0001);
        }
    }
}
