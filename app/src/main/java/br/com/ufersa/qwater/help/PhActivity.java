package br.com.ufersa.qwater.help;

import br.com.ufersa.qwater.R;

/** Explicação das restrições de uso associadas ao pH. */
public class PhActivity extends HelpActivity {

    @Override
    int contentLayout() {
        return R.layout.activity_ph;
    }

    @Override
    int backButtonId() {
        return R.id.BUTTON_BACK5;
    }
}
