package br.com.ufersa.qwater.help;

import br.com.ufersa.qwater.R;

/** Explicação das classes de salinidade C1 a C6. */
public class SalinityActivity extends HelpActivity {

    @Override
    int contentLayout() {
        return R.layout.activity_salinity;
    }

    @Override
    int backButtonId() {
        return R.id.BUTTON_BACK2;
    }
}
