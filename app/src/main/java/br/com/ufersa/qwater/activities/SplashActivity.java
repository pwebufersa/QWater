package br.com.ufersa.qwater.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import br.com.ufersa.qwater.R;

/**
 * Tela de abertura, que exibe a marca por dois segundos e abre a tela
 * principal.
 */
public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_DISPLAY_MILLIS = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
                SplashActivity.this.startActivity(mainIntent);
                SplashActivity.this.finish();
            }
        }, SPLASH_DISPLAY_MILLIS);
    }
}
