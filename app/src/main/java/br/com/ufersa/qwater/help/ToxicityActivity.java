package br.com.ufersa.qwater.help;

import br.com.ufersa.qwater.R;

/** Explicação dos riscos de toxidez por cloreto, boro e sódio. */
public class ToxicityActivity extends HelpActivity {

    @Override
    int contentLayout() {
        return R.layout.activity_toxity;
    }

    @Override
    int backButtonId() {
        return R.id.BUTTON_BACK4;
    }
}
