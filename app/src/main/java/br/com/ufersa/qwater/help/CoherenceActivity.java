package br.com.ufersa.qwater.help;

import br.com.ufersa.qwater.R;

/** Explicação da coerência entre a soma de cátions e a de ânions. */
public class CoherenceActivity extends HelpActivity {

    @Override
    int contentLayout() {
        return R.layout.activity_coherence;
    }

    @Override
    int backButtonId() {
        return R.id.BUTTON_BACK3;
    }
}
