package br.com.ufersa.qwater.util;

/**
 * Tabela de cálcio corrigido em função da razão HCO3/Ca e da condutividade
 * elétrica, com interpolação linear dupla.
 *
 * A primeira coluna da matriz guarda as razões HCO3/Ca; a primeira linha guarda
 * as condutividades elétricas em dS/m. O restante são os valores de cálcio
 * corrigido em mEq/L, transcritos do apêndice 2 do livro "A qualidade da água
 * para irrigação".
 *
 * Referência da interpolação:
 * https://pt.wikihow.com/Fazer-uma-Interpola%C3%A7%C3%A3o-Linear-Dupla
 *
 * Exemplo: razão 25 e condutividade 7 dS/m resultam em 0,3175 mEq/L.
 */
public final class CorrectedCalciumTable {

    private static final int RATIO_COLUMN = 0;
    private static final int CONDUCTIVITY_ROW = 0;

    private static final double[][] TABLE = new double[][]{
            {0,      0.1,   0.2,   0.3,   0.5,   0.7,   1.0,   1.5,   2.0,   3.0,   4.0,   6.0,   8.0},
            {0.05, 13.20, 13.61, 13.92, 14.40, 14.79, 15.26, 15.91, 16.43, 17.28, 17.97, 19.07, 19.94},
            {0.10,  8.31,  8.57,  8.77,  9.07,  9.31,  9.62, 10.02, 10.35, 10.89, 11.32, 12.01, 12.56},
            {0.15,  6.34,  6.54,  6.69,  6.92,  7.11,  7.34,  7.65,  7.90,  8.31,  8.64,  9.17,  9.58},
            {0.20,  5.24,  5.40,  5.52,  5.71,  5.87,  6.06,  6.31,  6.52,  6.86,  7.13,  7.57,  7.91},
            {0.25,  4.51,  4.65,  4.76,  4.92,  5.06,  5.22,  5.44,  5.62,  5.91,  6.15,  6.52,  6.82},
            {0.30,  4.00,  4.12,  4.21,  4.36,  4.48,  4.62,  4.82,  4.98,  5.24,  5.44,  5.77,  6.04},
            {0.35,  3.61,  3.72,  3.80,  3.94,  4.04,  4.17,  4.35,  4.49,  4.72,  4.91,  5.21,  5.45},
            {0.40,  3.30,  3.40,  3.48,  3.60,  3.70,  3.82,  3.98,  4.11,  4.32,  4.49,  4.77,  4.98},
            {0.45,  3.05,  3.14,  3.22,  3.33,  3.42,  3.53,  3.68,  3.80,  4.00,  4.15,  4.41,  4.61},
            {0.50,  2.84,  2.93,  3.00,  3.10,  3.19,  3.29,  3.43,  3.54,  3.72,  3.87,  4.11,  4.30},
            {0.75,  2.17,  2.24,  2.29,  2.37,  2.43,  2.51,  2.62,  2.70,  2.84,  2.95,  3.14,  3.28},
            {1.00,  1.79,  1.85,  1.89,  1.96,  2.01,  2.09,  2.16,  2.23,  2.35,  2.44,  2.59,  2.71},
            {1.25,  1.54,  1.59,  1.63,  1.68,  1.73,  1.78,  1.86,  1.92,  2.02,  2.10,  2.23,  2.33},
            {1.50,  1.37,  1.41,  1.44,  1.49,  1.53,  1.58,  1.65,  1.70,  1.79,  1.86,  1.97,  2.07},
            {1.75,  1.23,  1.27,  1.30,  1.35,  1.38,  1.43,  1.49,  1.54,  1.62,  1.68,  1.78,  1.86},
            {2.00,  1.13,  1.16,  1.19,  1.23,  1.26,  1.31,  1.36,  1.40,  1.48,  1.54,  1.63,  1.70},
            {2.25,  1.04,  1.06,  1.10,  1.14,  1.17,  1.21,  1.26,  1.30,  1.37,  1.42,  1.51,  1.58},
            {2.50,  0.97,  1.00,  1.02,  1.06,  1.09,  1.12,  1.17,  1.21,  1.27,  1.32,  1.40,  1.47},
            {3.00,  0.85,  0.89,  0.91,  0.94,  0.96,  1.00,  1.04,  1.07,  1.13,  1.17,  1.24,  1.30},
            {3.50,  0.78,  0.80,  0.82,  0.85,  0.87,  0.90,  0.94,  0.97,  1.02,  1.06,  1.12,  1.17},
            {4.00,  0.71,  0.73,  0.75,  0.78,  0.80,  0.82,  0.86,  0.88,  0.93,  0.97,  1.03,  1.07},
            {4.50,  0.66,  0.68,  0.69,  0.72,  0.74,  0.76,  0.79,  0.82,  0.86,  0.90,  0.95,  0.99},
            {5.00,  0.61,  0.63,  0.65,  0.67,  0.69,  0.71,  0.74,  0.76,  0.80,  0.83,  0.88,  0.93},
            {7.00,  0.49,  0.50,  0.52,  0.53,  0.55,  0.57,  0.59,  0.61,  0.64,  0.67,  0.71,  0.74},
            {10.0,  0.39,  0.40,  0.41,  0.42,  0.43,  0.45,  0.47,  0.48,  0.51,  0.53,  0.56,  0.58},
            {20.0,  0.24,  0.25,  0.26,  0.26,  0.27,  0.28,  0.29,  0.30,  0.32,  0.33,  0.35,  0.37},
            {30.0,  0.18,  0.19,  0.20,  0.20,  0.21,  0.21,  0.22,  0.23,  0.24,  0.25,  0.27,  0.28}

    };

    /**
     * Interpola o cálcio corrigido entre quatro células da tabela.
     *
     * @param bicarbonateToCalciumRatio razão HCO3/Ca, de 0,05 a 30
     * @param electricalConductivity    condutividade elétrica em dS/m, de 0 a 8
     * @return cálcio corrigido em mEq/L, ou NaN fora da faixa da tabela
     */
    public double interpolate(double bicarbonateToCalciumRatio, double electricalConductivity) {
        int lowerRatioRow = findLowerRatioRow(bicarbonateToCalciumRatio);
        int lowerConductivityColumn = findLowerConductivityColumn(electricalConductivity);

        if (lowerRatioRow < 0 || lowerConductivityColumn < 0) {
            return Double.NaN;
        }

        int upperRatioRow = lowerRatioRow + 1;
        int upperConductivityColumn = lowerConductivityColumn + 1;

        double lowerRatio = TABLE[lowerRatioRow][RATIO_COLUMN];
        double upperRatio = TABLE[upperRatioRow][RATIO_COLUMN];
        double lowerConductivity = TABLE[CONDUCTIVITY_ROW][lowerConductivityColumn];
        double upperConductivity = TABLE[CONDUCTIVITY_ROW][upperConductivityColumn];

        double atLowerRatio = interpolateConductivity(
                TABLE[lowerRatioRow][lowerConductivityColumn],
                TABLE[lowerRatioRow][upperConductivityColumn],
                lowerConductivity, upperConductivity, electricalConductivity);
        double atUpperRatio = interpolateConductivity(
                TABLE[upperRatioRow][lowerConductivityColumn],
                TABLE[upperRatioRow][upperConductivityColumn],
                lowerConductivity, upperConductivity, electricalConductivity);

        double lowerRatioWeight = (upperRatio - bicarbonateToCalciumRatio) / (upperRatio - lowerRatio);
        double upperRatioWeight = (bicarbonateToCalciumRatio - lowerRatio) / (upperRatio - lowerRatio);

        return atLowerRatio * lowerRatioWeight + atUpperRatio * upperRatioWeight;
    }

    private double interpolateConductivity(double lowerValue, double upperValue,
                                           double lowerConductivity, double upperConductivity,
                                           double electricalConductivity) {
        double lowerWeight = (upperConductivity - electricalConductivity) / (upperConductivity - lowerConductivity);
        double upperWeight = (electricalConductivity - lowerConductivity) / (upperConductivity - lowerConductivity);
        return lowerWeight * lowerValue + upperWeight * upperValue;
    }

    private int findLowerRatioRow(double bicarbonateToCalciumRatio) {
        for (int row = 0; row < TABLE.length - 1; row++) {
            if (TABLE[row + 1][RATIO_COLUMN] >= bicarbonateToCalciumRatio) {
                return row;
            }
        }
        return -1;
    }

    private int findLowerConductivityColumn(double electricalConductivity) {
        int lastColumn = TABLE[CONDUCTIVITY_ROW].length - 1;
        for (int column = 0; column < lastColumn; column++) {
            if (TABLE[CONDUCTIVITY_ROW][column + 1] >= electricalConductivity) {
                return column;
            }
        }
        return -1;
    }
}
