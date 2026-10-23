package br.com.ufersa.qwater.help;

import br.com.ufersa.qwater.R;

/** Explicação da razão de adsorção de sódio e das classes S1 a S4. */
public class SarActivity extends HelpActivity {

    @Override
    int contentLayout() {
        return R.layout.activity_sar;
    }

    @Override
    int backButtonId() {
        return R.id.BUTTON_BACK1;
    }
}
