package br.com.ufersa.qwater.util;

/**
 * Nível de risco usado nos ícones de alerta da tela de análise.
 *
 * A ordem da declaração define a gravidade crescente; o ícone de toxidez mostra
 * sempre o maior nível entre cloreto, boro e sódio.
 */
public enum RiskLevel {
    OK,
    CAUTION,
    ALERT,
    DANGER
}
