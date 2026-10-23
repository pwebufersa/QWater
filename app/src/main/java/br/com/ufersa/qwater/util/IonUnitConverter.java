package br.com.ufersa.qwater.util;

/**
 * Conversões entre as unidades de concentração usadas no relatório de qualidade
 * da água: mmolc/L, mg/L e mEq/L.
 *
 * As cinco conversões preservam as fórmulas do código original. A conversão de
 * mg/L para mmol/L continua dividindo pela valência, como antes; a decisão está
 * registrada em docs/calculos.md.
 *
 * Exemplo: 20 mEq/L de cálcio equivalem a 400,8 mg/L, e 10 mEq/L de sódio
 * equivalem a 299,9 mg/L.
 */
public final class IonUnitConverter {

    public double milliequivalentsToMilligrams(Ion ion, double milliequivalentsPerLiter) {
        return milliequivalentsPerLiter * (ion.getMolarMass() / ion.getValence());
    }

    public double milligramsToMilliequivalents(Ion ion, double milligramsPerLiter) {
        return milligramsPerLiter / (ion.getMolarMass() / ion.getValence());
    }

    public double millimolesToMilliequivalents(Ion ion, double millimolesPerLiter) {
        return millimolesPerLiter * ion.getValence();
    }

    public double milliequivalentsToMillimoles(Ion ion, double milliequivalentsPerLiter) {
        return milliequivalentsPerLiter / ion.getValence();
    }

    public double milligramsToMillimoles(Ion ion, double milligramsPerLiter) {
        return (milligramsPerLiter / ion.getValence()) / ion.getMolarMass();
    }
}
