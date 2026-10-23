package br.com.ufersa.qwater.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Faixas de classificação da água, conferidas nos limites de cada classe.
 */
public class WaterQualityClassifierTest {

    @Test
    public void classifiesSalinityFromC1ToC6() {
        assertClassification("C1", RiskLevel.OK, WaterQualityClassifier.classifySalinity(0.24));
        assertClassification("C2", RiskLevel.CAUTION, WaterQualityClassifier.classifySalinity(0.25));
        assertClassification("C2", RiskLevel.CAUTION, WaterQualityClassifier.classifySalinity(0.74));
        assertClassification("C3", RiskLevel.ALERT, WaterQualityClassifier.classifySalinity(0.75));
        assertClassification("C3", RiskLevel.ALERT, WaterQualityClassifier.classifySalinity(2.24));
        assertClassification("C4", RiskLevel.ALERT, WaterQualityClassifier.classifySalinity(2.25));
        assertClassification("C4", RiskLevel.ALERT, WaterQualityClassifier.classifySalinity(3.99));
        assertClassification("C5", RiskLevel.DANGER, WaterQualityClassifier.classifySalinity(4.0));
        assertClassification("C5", RiskLevel.DANGER, WaterQualityClassifier.classifySalinity(5.99));
        assertClassification("C6", RiskLevel.DANGER, WaterQualityClassifier.classifySalinity(6.0));
    }

    @Test
    public void classifiesInfiltrationFromS1ToS4() {
        assertClassification("S1", RiskLevel.OK, WaterQualityClassifier.classifyInfiltration(9.99));
        assertClassification("S2", RiskLevel.CAUTION, WaterQualityClassifier.classifyInfiltration(10.0));
        assertClassification("S2", RiskLevel.CAUTION, WaterQualityClassifier.classifyInfiltration(17.99));
        assertClassification("S3", RiskLevel.ALERT, WaterQualityClassifier.classifyInfiltration(18.0));
        assertClassification("S3", RiskLevel.ALERT, WaterQualityClassifier.classifyInfiltration(25.99));
        assertClassification("S4", RiskLevel.DANGER, WaterQualityClassifier.classifyInfiltration(26.0));
    }

    @Test
    public void classifiesChlorideFromCL1ToCL4() {
        assertClassification("CL1", RiskLevel.OK, WaterQualityClassifier.classifyChloride(2.0));
        assertClassification("CL2", RiskLevel.CAUTION, WaterQualityClassifier.classifyChloride(2.01));
        assertClassification("CL2", RiskLevel.CAUTION, WaterQualityClassifier.classifyChloride(4.0));
        assertClassification("CL3", RiskLevel.ALERT, WaterQualityClassifier.classifyChloride(4.01));
        assertClassification("CL3", RiskLevel.ALERT, WaterQualityClassifier.classifyChloride(10.0));
        assertClassification("CL4", RiskLevel.DANGER, WaterQualityClassifier.classifyChloride(10.01));
    }

    @Test
    public void classifiesBoronFromB1ToB4() {
        assertClassification("B1", RiskLevel.OK, WaterQualityClassifier.classifyBoron(0.5));
        assertClassification("B2", RiskLevel.CAUTION, WaterQualityClassifier.classifyBoron(0.51));
        assertClassification("B2", RiskLevel.CAUTION, WaterQualityClassifier.classifyBoron(1.0));
        assertClassification("B3", RiskLevel.ALERT, WaterQualityClassifier.classifyBoron(1.01));
        assertClassification("B3", RiskLevel.ALERT, WaterQualityClassifier.classifyBoron(2.0));
        assertClassification("B4", RiskLevel.DANGER, WaterQualityClassifier.classifyBoron(2.01));
    }

    @Test
    public void classifiesSodiumForSurfaceIrrigation() {
        assertClassification("SS1", RiskLevel.OK, WaterQualityClassifier.classifySodiumSurfaceIrrigation(2.99));
        assertClassification("SS2", RiskLevel.CAUTION, WaterQualityClassifier.classifySodiumSurfaceIrrigation(3.0));
        assertClassification("SS2", RiskLevel.CAUTION, WaterQualityClassifier.classifySodiumSurfaceIrrigation(9.0));
        assertClassification("SS3", RiskLevel.DANGER, WaterQualityClassifier.classifySodiumSurfaceIrrigation(9.01));
    }

    @Test
    public void classifiesSodiumForSprinklerIrrigation() {
        assertClassification("SA1", RiskLevel.OK, WaterQualityClassifier.classifySodiumSprinklerIrrigation(3.0));
        assertClassification("SA2", RiskLevel.CAUTION, WaterQualityClassifier.classifySodiumSprinklerIrrigation(3.01));
    }

    @Test
    public void classifiesPhRestriction() {
        assertClassification("PH1", RiskLevel.OK, WaterQualityClassifier.classifyPh(6.99));
        assertClassification("PH2", RiskLevel.CAUTION, WaterQualityClassifier.classifyPh(7.0));
        assertClassification("PH2", RiskLevel.CAUTION, WaterQualityClassifier.classifyPh(8.0));
        assertClassification("PH3", RiskLevel.DANGER, WaterQualityClassifier.classifyPh(8.01));
    }

    @Test
    public void classifiesIonicBalanceByTheRelativeDifference() {
        assertClassification("CO1", RiskLevel.OK, WaterQualityClassifier.classifyIonicBalance(10.0, 10.0));
        assertClassification("CO2", RiskLevel.CAUTION, WaterQualityClassifier.classifyIonicBalance(10.5, 9.5));
        assertClassification("CO2", RiskLevel.CAUTION, WaterQualityClassifier.classifyIonicBalance(11.0, 9.0));
        assertClassification("CO3", RiskLevel.DANGER, WaterQualityClassifier.classifyIonicBalance(10.0, 8.0));
    }

    @Test
    public void treatsMissingIonicSumsAsIncoherent() {
        assertClassification("CO3", RiskLevel.DANGER, WaterQualityClassifier.classifyIonicBalance(0, 0));
    }

    private void assertClassification(String expectedLabel, RiskLevel expectedRisk,
                                      WaterQualityClassifier.Classification classification) {
        assertEquals(expectedLabel, classification.getLabel());
        assertEquals(expectedRisk, classification.getRiskLevel());
    }
}
