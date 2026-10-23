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

import java.text.SimpleDateFormat;
import java.util.Date;

import br.com.ufersa.qwater.R;
import br.com.ufersa.qwater.beans.Report;
import br.com.ufersa.qwater.database.QWaterDatabase;

import static br.com.ufersa.qwater.util.IntentContract.Key.PURPOSE;
import static br.com.ufersa.qwater.util.IntentContract.Key.REPORT;
import static br.com.ufersa.qwater.util.IntentContract.Purpose.EDIT_REPORT;
import static br.com.ufersa.qwater.util.IntentContract.Purpose.REPORT_DELETED;
import static br.com.ufersa.qwater.util.IntentContract.Purpose.VIEW_REPORT;

/**
 * Detalhes de um relatório salvo, com os valores informados e a RAS corrigida.
 *
 * O menu permite ver a análise, editar o relatório ou excluí-lo.
 */
public class ReportDetailsActivity extends AppCompatActivity {

    private TextView conductivityTextView;
    private TextView calciumTextView;
    private TextView magnesiumTextView;
    private TextView potassiumTextView;
    private TextView sodiumTextView;
    private TextView carbonateTextView;
    private TextView bicarbonateTextView;
    private TextView chlorideTextView;
    private TextView phTextView;
    private TextView sulfateTextView;
    private TextView boronTextView;
    private TextView sourceNameTextView;
    private TextView correctedSarTextView;
    private TextView dateTextView;
    private Report report;
    private QWaterDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_details);

        bindViews();
        showIncomingReport();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.report_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        Intent intent;
        if (item.getItemId() == R.id.action_see_report) {
            intent = new Intent(ReportDetailsActivity.this, ReportAnalysisActivity.class);
            intent.putExtra(PURPOSE, VIEW_REPORT);
            intent.putExtra(REPORT, report);
            startActivity(intent);
            return true;

        } else if (item.getItemId() == R.id.action_edit_report) {
            intent = new Intent(ReportDetailsActivity.this, MainActivity.class);
            intent.putExtra(PURPOSE, EDIT_REPORT);
            intent.putExtra(REPORT, report);
            startActivity(intent);
            return true;

        } else if (item.getItemId() == R.id.action_delete_report) {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.deletando_relatorio)
                    .setMessage(R.string.certeza_deletar_relatorio)
                    .setPositiveButton(R.string.sim, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            deleteReport();
                        }
                    })
                    .setNegativeButton(R.string.nao, null)
                    .show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void deleteReport() {
        try {
            new DeleteReportTask().execute();
            // no post execute é direcionado para o fragment de listar relatórios
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, R.string.erro_deletar_relatorio, Toast.LENGTH_SHORT).show();
            startActivity(new Intent(ReportDetailsActivity.this, MainActivity.class));
        }
    }

    private void showIncomingReport() {
        if (getIntent().hasExtra(REPORT)) {

            report = getIntent().getParcelableExtra(REPORT);
            renderReport();
        }
    }

    private void renderReport() {

        conductivityTextView.setText(String.valueOf(report.getElectricalConductivity()));
        calciumTextView.setText(String.valueOf(report.getCalcium()));
        magnesiumTextView.setText(String.valueOf(report.getMagnesium()));
        potassiumTextView.setText(String.valueOf(report.getPotassium()));
        sodiumTextView.setText(String.valueOf(report.getSodium()));
        carbonateTextView.setText(String.valueOf(report.getCarbonate()));
        bicarbonateTextView.setText(String.valueOf(report.getBicarbonate()));
        chlorideTextView.setText(String.valueOf(report.getChloride()));
        phTextView.setText(String.valueOf(report.getPh()));
        sourceNameTextView.setText(String.valueOf(report.getSourceName()));
        correctedSarTextView.setText(String.valueOf(report.getCorrectedSar()));
        dateTextView.setText(formatDate(report.getDate()));
        boronTextView.setText(String.valueOf(report.getBoron()));
        sulfateTextView.setText(String.valueOf(report.getSulfate()));
    }

    private void bindViews() {
        conductivityTextView = findViewById(R.id.CEA_VALUE);
        calciumTextView = findViewById(R.id.CA_VALUE);
        magnesiumTextView = findViewById(R.id.MG_VALUE);
        potassiumTextView = findViewById(R.id.K_VALUE);
        sodiumTextView = findViewById(R.id.NA_VALUE);
        carbonateTextView = findViewById(R.id.CO3_VALUE);
        bicarbonateTextView = findViewById(R.id.HCO3_VALUE);
        chlorideTextView = findViewById(R.id.CL_VALUE);
        phTextView = findViewById(R.id.PH_VALUE);
        sourceNameTextView = findViewById(R.id.SOURCE_NAME);
        correctedSarTextView = findViewById(R.id.CORRECTED_SAR_VALUE);
        dateTextView = findViewById(R.id.CREATED_AT_VALUE);
        boronTextView = findViewById(R.id.B_VALUE);
        sulfateTextView = findViewById(R.id.SO4_VALUE);

        Toolbar topToolbar = findViewById(R.id.REPORT_DETAILS_TOOLBAR);
        setSupportActionBar(topToolbar);
        topToolbar.setNavigationIcon(R.drawable.action_navigation_arrow);
        topToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View clickedView) {
                finish();
            }
        });

        // prepara o bd
        database = QWaterDatabase.getInstance(ReportDetailsActivity.this);
    }

    private String formatDate(long timeInMillis) {
        //TODO rever formato da data para internacionalizaço
        return new SimpleDateFormat("dd/MM/yyyy").format(new Date(timeInMillis));
    }

    private class DeleteReportTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {

            database.reportDao().delete(report);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            Toast.makeText(ReportDetailsActivity.this, R.string.relatorio_deletado, Toast.LENGTH_SHORT).show();
            // se eu der somente um finish(), a lista de relatórios não atualiza...
            Intent intent = new Intent(ReportDetailsActivity.this, MainActivity.class);
            intent.putExtra(PURPOSE, REPORT_DELETED);
            startActivity(intent);
        }
    }
}
