package br.com.ufersa.qwater.help;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Base das telas de informação, que compartilham o botão de voltar.
 *
 * Cada subclasse informa o layout do conteúdo e o identificador do botão de
 * voltar.
 */
abstract class HelpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(contentLayout());

        ImageButton backButton = findViewById(backButtonId());
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View clickedView) {
                onBackPressed();
            }
        });
    }

    abstract int contentLayout();

    abstract int backButtonId();
}
