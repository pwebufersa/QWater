package br.com.ufersa.qwater.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import br.com.ufersa.qwater.R;
import br.com.ufersa.qwater.beans.Source;
import br.com.ufersa.qwater.database.QWaterDatabase;

import static br.com.ufersa.qwater.util.IntentContract.Key.PURPOSE;
import static br.com.ufersa.qwater.util.IntentContract.Key.SOURCE;
import static br.com.ufersa.qwater.util.IntentContract.Purpose.EDIT_SOURCE;
import static br.com.ufersa.qwater.util.IntentContract.Purpose.SOURCE_DELETED;

/**
 * Detalhes de uma fonte de água: nome, tipo e coordenadas.
 *
 * O menu permite editar ou excluir a fonte. A exclusão remove também os
 * relatórios associados, por cascata no banco.
 */
public class SourceDetailsActivity extends AppCompatActivity {

    private TextView sourceNameTextView;
    private TextView sourceTypeTextView;
    private TextView sourceLatitudeTextView;
    private TextView sourceLongitudeTextView;
    private QWaterDatabase database;
    private Source source;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_source_details);

        bindViews();
        showIncomingSource();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.source_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        Intent intent;
        if (item.getItemId() == R.id.action_edit_source) {
            intent = new Intent(SourceDetailsActivity.this, MainActivity.class)
                    .putExtra(PURPOSE, EDIT_SOURCE)
                    .putExtra(SOURCE, source);
            startActivity(intent);
            return true;

        } else if (item.getItemId() == R.id.action_delete_source) {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.deletando_fonte)
                    .setMessage(R.string.certeza_deletar_fonte)
                    .setPositiveButton(R.string.sim, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            deleteSource();
                        }
                    })
                    .setNegativeButton(R.string.nao, null)
                    .show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void bindViews() {
        sourceNameTextView = findViewById(R.id.SOURCE_DETAILS_NAME);
        sourceTypeTextView = findViewById(R.id.SOURCE_DETAILS_TYPE);
        sourceLatitudeTextView = findViewById(R.id.SOURCE_DETAILS_LATITUDE);
        sourceLongitudeTextView = findViewById(R.id.SOURCE_DETAILS_LONGITUDE);
        Toolbar topToolbar = findViewById(R.id.SOURCE_DETAILS_TOOLBAR);
        setSupportActionBar(topToolbar);
        topToolbar.setNavigationIcon(R.drawable.action_navigation_arrow);
        topToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View clickedView) {
                finish();
            }
        });

        // prepara o bd
        database = QWaterDatabase.getInstance(SourceDetailsActivity.this);
    }

    private void showIncomingSource() {
        if (getIntent().hasExtra(SOURCE)) {

            source = getIntent().getParcelableExtra(SOURCE);
            renderSource();
        }
    }

    private void renderSource() {

        sourceNameTextView.setText(source.getName());
        sourceTypeTextView.setText(source.getType());
        sourceLatitudeTextView.setText(source.getLatitude());
        sourceLongitudeTextView.setText(source.getLongitude());
    }

    private void deleteSource() {
        try {
            new DeleteSourceTask().execute();
            // no post execute é direcionado para o fragment de listar fontes
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, R.string.erro_deletar_fonte, Toast.LENGTH_SHORT).show();
            startActivity(new Intent(SourceDetailsActivity.this, MainActivity.class));
        }
    }

    private class DeleteSourceTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {

            database.sourceDao().delete(source);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            Toast.makeText(SourceDetailsActivity.this, R.string.fonte_deletada, Toast.LENGTH_SHORT).show();
            // se eu der somente um finish(), a lista não atualiza...
            Intent intent = new Intent(SourceDetailsActivity.this, MainActivity.class);
            intent.putExtra(PURPOSE, SOURCE_DELETED);
            startActivity(intent);
        }
    }
}
