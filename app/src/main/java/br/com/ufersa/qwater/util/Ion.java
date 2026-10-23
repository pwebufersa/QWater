package br.com.ufersa.qwater.util;

/**
 * Íons considerados na análise da água para irrigação, com a massa molar em
 * g/mol e a valência usadas nas conversões de unidade.
 *
 * A massa do sódio é a do código original, 29,99 g/mol, e não a da tabela
 * periódica, 22,99 g/mol. A divergência foi preservada para não alterar
 * resultados já registrados; ela está descrita em docs/calculos.md.
 */
public enum Ion {

    CALCIUM(40.08, 2),
    MAGNESIUM(24.31, 2),
    POTASSIUM(39.10, 1),
    SODIUM(29.99, 1),
    CARBONATE(60.0089, 2),
    BICARBONATE(61.0168, 1),
    CHLORIDE(35.453, 1);

    private final double molarMass;
    private final int valence;

    Ion(double molarMass, int valence) {
        this.molarMass = molarMass;
        this.valence = valence;
    }

    public double getMolarMass() {
        return molarMass;
    }

    public int getValence() {
        return valence;
    }
}
