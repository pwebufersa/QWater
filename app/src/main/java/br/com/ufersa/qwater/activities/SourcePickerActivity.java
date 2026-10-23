package br.com.ufersa.qwater.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import br.com.ufersa.qwater.R;
import br.com.ufersa.qwater.adapters.SourceListAdapter;
import br.com.ufersa.qwater.beans.Source;
import br.com.ufersa.qwater.database.QWaterDatabase;

import static br.com.ufersa.qwater.util.IntentContract.Caller.MAIN;
import static br.com.ufersa.qwater.util.IntentContract.Key.CALLER;

/**
 * Seleção da fonte que receberá o relatório.
 *
 * A fonte escolhida é devolvida pelo {@link SourceListAdapter} como resultado
 * para a tela de salvamento. Esta activity existe porque o fragmento de lista da
 * MainActivity não devolve resultado.
 */
public class SourcePickerActivity extends AppCompatActivity {

    private RecyclerView sourceRecyclerView;
    private QWaterDatabase database;
    private ExecutorService databaseExecutor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_sources);

        sourceRecyclerView = findViewById(R.id.WATER_SOURCE_RECYCLER_VIEW);
        sourceRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Prepara o banco de dados
        database = QWaterDatabase.getInstance(this);
        databaseExecutor = Executors.newSingleThreadExecutor();

        loadSources();
    }

    private void loadSources() {
        databaseExecutor.execute(() -> {
            List<Source> sources = database.sourceDao().getAll();

            runOnUiThread(() -> {
                SourceListAdapter adapter = new SourceListAdapter(callerActivity(), SourcePickerActivity.this, sources);
                sourceRecyclerView.setAdapter(adapter);
            });
        });
    }

    private int callerActivity() {
        if (getIntent().hasExtra(CALLER)) {
            return getIntent().getIntExtra(CALLER, MAIN);
        }
        return MAIN;
    }

    // Para não voltar para a activity de coordenadas
    @Override
    public void onBackPressed() {
        startActivity(new Intent(SourcePickerActivity.this, MainActivity.class));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        databaseExecutor.shutdown(); // Para encerrar o executor quando não for mais necessário
    }
}
