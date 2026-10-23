package br.com.ufersa.qwater.help;

import br.com.ufersa.qwater.R;

/** Informações sobre o aplicativo e a equipe. */
public class AboutActivity extends HelpActivity {

    @Override
    int contentLayout() {
        return R.layout.activity_about;
    }

    @Override
    int backButtonId() {
        return R.id.BUTTON_BACK6;
    }
}
