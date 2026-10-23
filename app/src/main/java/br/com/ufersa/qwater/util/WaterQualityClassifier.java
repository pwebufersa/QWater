package br.com.ufersa.qwater.util;

/**
 * Classificação da água para irrigação: salinidade, infiltração, toxidez de
 * cloreto, boro e sódio, pH e coerência iônica.
 *
 * Cada classificação devolve o rótulo usado no código ("C1", "S2", "CL3"...) e o
 * nível de risco correspondente. Os rótulos são os nomes dos textos em
 * res/values/strings.xml.
 *
 * Exemplo: condutividade 5,0 dS/m resulta em C5, com risco DANGER.
 */
public final class WaterQualityClassifier {

    private static final double IONIC_BALANCE_TOLERANCE_PERCENT = 5.0;
    private static final double IONIC_BALANCE_WARNING_PERCENT = 10.0;

    private WaterQualityClassifier() {
    }

    public static Classification classifySalinity(double electricalConductivity) {
        if (electricalConductivity < 0.25) {
            return new Classification("C1", RiskLevel.OK);
        }
        if (electricalConductivity < 0.75) {
            return new Classification("C2", RiskLevel.CAUTION);
        }
        if (electricalConductivity < 2.25) {
            return new Classification("C3", RiskLevel.ALERT);
        }
        if (electricalConductivity < 4.0) {
            return new Classification("C4", RiskLevel.ALERT);
        }
        if (electricalConductivity < 6.0) {
            return new Classification("C5", RiskLevel.DANGER);
        }
        return new Classification("C6", RiskLevel.DANGER);
    }

    public static Classification classifyInfiltration(double correctedSar) {
        if (correctedSar < 10.0) {
            return new Classification("S1", RiskLevel.OK);
        }
        if (correctedSar < 18.0) {
            return new Classification("S2", RiskLevel.CAUTION);
        }
        if (correctedSar < 26.0) {
            return new Classification("S3", RiskLevel.ALERT);
        }
        return new Classification("S4", RiskLevel.DANGER);
    }

    public static Classification classifyChloride(double chloride) {
        if (chloride <= 2.0) {
            return new Classification("CL1", RiskLevel.OK);
        }
        if (chloride <= 4.0) {
            return new Classification("CL2", RiskLevel.CAUTION);
        }
        if (chloride <= 10.0) {
            return new Classification("CL3", RiskLevel.ALERT);
        }
        return new Classification("CL4", RiskLevel.DANGER);
    }

    public static Classification classifyBoron(double boron) {
        if (boron <= 0.5) {
            return new Classification("B1", RiskLevel.OK);
        }
        if (boron <= 1.0) {
            return new Classification("B2", RiskLevel.CAUTION);
        }
        if (boron <= 2.0) {
            return new Classification("B3", RiskLevel.ALERT);
        }
        return new Classification("B4", RiskLevel.DANGER);
    }

    public static Classification classifySodiumSurfaceIrrigation(double correctedSar) {
        if (correctedSar < 3.0) {
            return new Classification("SS1", RiskLevel.OK);
        }
        if (correctedSar <= 9.0) {
            return new Classification("SS2", RiskLevel.CAUTION);
        }
        return new Classification("SS3", RiskLevel.DANGER);
    }

    public static Classification classifySodiumSprinklerIrrigation(double sodium) {
        if (sodium <= 3.0) {
            return new Classification("SA1", RiskLevel.OK);
        }
        return new Classification("SA2", RiskLevel.CAUTION);
    }

    public static Classification classifyPh(double ph) {
        if (ph < 7.0) {
            return new Classification("PH1", RiskLevel.OK);
        }
        if (ph <= 8.0) {
            return new Classification("PH2", RiskLevel.CAUTION);
        }
        return new Classification("PH3", RiskLevel.DANGER);
    }

    /**
     * Classifica a coerência iônica pela diferença relativa entre a soma dos
     * cátions e a soma dos ânions, em porcentagem.
     *
     * @param cations soma dos cátions em mmolc/L
     * @param anions soma dos ânions em mmolc/L
     * @return classificação, ou CO3 quando a soma é zero
     */
    public static Classification classifyIonicBalance(double cations, double anions) {
        double errorPercent = Math.abs((cations - anions) / (cations + anions)) * 100;

        if (errorPercent < IONIC_BALANCE_TOLERANCE_PERCENT) {
            return new Classification("CO1", RiskLevel.OK);
        }
        if (errorPercent <= IONIC_BALANCE_WARNING_PERCENT) {
            return new Classification("CO2", RiskLevel.CAUTION);
        }
        return new Classification("CO3", RiskLevel.DANGER);
    }

    /** Rótulo da classificação e o risco que ele representa. */
    public static final class Classification {

        private final String label;
        private final RiskLevel riskLevel;

        public Classification(String label, RiskLevel riskLevel) {
            this.label = label;
            this.riskLevel = riskLevel;
        }

        public String getLabel() {
            return label;
        }

        public RiskLevel getRiskLevel() {
            return riskLevel;
        }
    }
}
