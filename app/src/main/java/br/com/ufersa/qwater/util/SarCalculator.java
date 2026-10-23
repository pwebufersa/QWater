package br.com.ufersa.qwater.util;

/**
 * Cálculo da razão de adsorção de sódio (RAS), nas formas normal e corrigida.
 *
 * A RAS compara o sódio com o cálcio e o magnésio: RAS = Na / raiz((Ca + Mg)/2),
 * com os cátions em mEq/L. A RAS corrigida troca o cálcio informado pelo cálcio
 * corrigido, obtido da tabela do apêndice 2 do livro "A qualidade da água para
 * irrigação" a partir da razão HCO3/Ca e da condutividade elétrica.
 *
 * Exemplo: Ca 4, Mg 2 e Na 10 resultam em RAS 5,77.
 */
public final class SarCalculator {

    private final CorrectedCalciumTable correctedCalciumTable;

    public SarCalculator() {
        this(new CorrectedCalciumTable());
    }

    public SarCalculator(CorrectedCalciumTable correctedCalciumTable) {
        this.correctedCalciumTable = correctedCalciumTable;
    }

    public double calculateNormalSar(double calcium, double magnesium, double sodium) {
        double calciumAndMagnesium = (calcium + magnesium) / 2;
        return sodium / Math.sqrt(calciumAndMagnesium);
    }

    public double calculateCorrectedSar(double calcium, double magnesium, double sodium,
                                        double electricalConductivity, double bicarbonate) {
        double correctedCalcium = calculateCorrectedCalcium(electricalConductivity, bicarbonate, calcium);
        double correctedCalciumAndMagnesium = (correctedCalcium + magnesium) / 2;
        return sodium / Math.sqrt(correctedCalciumAndMagnesium);
    }

    private double calculateCorrectedCalcium(double electricalConductivity, double bicarbonate, double calcium) {
        return correctedCalciumTable.interpolate(bicarbonate / calcium, electricalConductivity);
    }
}
