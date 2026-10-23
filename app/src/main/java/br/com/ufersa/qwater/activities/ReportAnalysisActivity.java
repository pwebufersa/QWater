package br.com.ufersa.qwater.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

import br.com.ufersa.qwater.R;
import br.com.ufersa.qwater.beans.Report;
import br.com.ufersa.qwater.database.QWaterDatabase;
import br.com.ufersa.qwater.help.CoherenceActivity;
import br.com.ufersa.qwater.help.PhActivity;
import br.com.ufersa.qwater.help.SalinityActivity;
import br.com.ufersa.qwater.help.SarActivity;
import br.com.ufersa.qwater.help.ToxicityActivity;
import br.com.ufersa.qwater.util.RiskLevel;
import br.com.ufersa.qwater.util.SarCalculator;
import br.com.ufersa.qwater.util.WaterQualityClassifier;

import static br.com.ufersa.qwater.util.IntentContract.Key.PURPOSE;
import static br.com.ufersa.qwater.util.IntentContract.Key.REPORT;
import static br.com.ufersa.qwater.util.IntentContract.Key.REPORT_DATE;
import static br.com.ufersa.qwater.util.IntentContract.Key.SOURCE_ID;
import static br.com.ufersa.qwater.util.IntentContract.Key.SOURCE_NAME;
import static br.com.ufersa.qwater.util.IntentContract.Purpose.EDIT_REPORT;
import static br.com.ufersa.qwater.util.IntentContract.Purpose.VIEW_REPORT;
import static br.com.ufersa.qwater.util.IntentContract.Request.SAVE_NEW_REPORT;
import static br.com.ufersa.qwater.util.IntentContract.Request.SAVE_UPDATED_REPORT;

/**
 * Tela de análise: calcula a RAS normal e a corrigida e apresenta as
 * classificações de salinidade, infiltração, toxidez, pH e coerência iônica.
 *
 * O menu muda conforme a finalidade recebida: inserir, atualizar ou apenas
 * visualizar o relatório.
 */
public class ReportAnalysisActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView salinityClassificationTextView;
    private TextView infiltrationClassificationTextView;
    private TextView chlorideClassificationTextView;
    private TextView boronClassificationTextView;
    private TextView phClassificationTextView;
    private TextView ionicBalanceClassificationTextView;
    private TextView correctedSarTextView;
    private TextView sodiumSurfaceClassificationTextView;
    private TextView sodiumSprinklerClassificationTextView;
    private ImageView ionicBalanceStatusImageView;
    private ImageView salinityStatusImageView;
    private ImageView infiltrationStatusImageView;
    private ImageView toxicityStatusImageView;
    private ImageView phStatusImageView;
    private Report report;
    private QWaterDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analyze_report);

        bindViews();
        showIncomingReport();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Muda o menu caso o usuário esteja atualizando um report, vendo o relatório ou inserindo um novo
        if (getIntent().hasExtra(REPORT)) {
            if (getIntent().getIntExtra(PURPOSE, 0) == EDIT_REPORT) {
                getMenuInflater().inflate(R.menu.analyze_update, menu);
            } else if (getIntent().getIntExtra(PURPOSE, 0) == VIEW_REPORT) {
                getMenuInflater().inflate(R.menu.analyze_see, menu);
            } else {
                getMenuInflater().inflate(R.menu.analyze_insert, menu);
            }
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_insert) {
            startActivityForResult(new Intent(ReportAnalysisActivity.this, ReportSaveActivity.class), SAVE_NEW_REPORT);
        } else if (item.getItemId() == R.id.action_update) {
            Intent intent = new Intent(ReportAnalysisActivity.this, ReportSaveActivity.class);
            intent.putExtra(SOURCE_NAME, report.getSourceName());
            intent.putExtra(REPORT_DATE, report.getDate());
            intent.putExtra(SOURCE_ID, report.getSourceId());
            startActivityForResult(intent, SAVE_UPDATED_REPORT);
        }

        return super.onOptionsItemSelected(item);
    }

    private void showIncomingReport() {
        if (getIntent().hasExtra(REPORT)) {
            applyReport((Report) getIntent().getParcelableExtra(REPORT));
        }
    }

    private void applyReport(Report report) {
        try {
            report.setNormalSar(calculateNormalSar(report.getCalcium(), report.getMagnesium(), report.getSodium()));
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(ReportAnalysisActivity.this, R.string.erro_calcular_ras, Toast.LENGTH_SHORT).show();
            report.setNormalSar(0.0);
        }
        try {
            report.setCorrectedSar(calculateCorrectedSar(report.getCalcium(), report.getMagnesium(),
                    report.getSodium(), report.getBicarbonate(), report.getElectricalConductivity()));
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(ReportAnalysisActivity.this, R.string.erro_calcular_ras_corrigida, Toast.LENGTH_SHORT).show();
            report.setCorrectedSar(0.0);
        }

        // A classificação e armazenamento está sendo feito após o arredondamento das casas decimais!
        this.report = report;

        renderClassifications();
    }

    /**
     * Calcula a RAS normal.
     *
     * @param calcium   cálcio
     * @param magnesium magnésio
     * @param sodium    sódio
     * @return valor da RAS normal ou NaN quando falta algum dos três íons
     */
    private double calculateNormalSar(double calcium, double magnesium, double sodium) {

        if (calcium != 0.0 && magnesium != 0.0 && sodium != 0.0) {
            SarCalculator sarCalculator = new SarCalculator();
            return roundUpToFourDecimals(sarCalculator.calculateNormalSar(calcium, magnesium, sodium));
        }
        return Double.NaN;
    }

    /**
     * Calcula a RAS corrigida.
     *
     * @param calcium                 cálcio
     * @param magnesium               magnésio
     * @param sodium                  sódio
     * @param bicarbonate             bicarbonato
     * @param electricalConductivity condutividade elétrica
     * @return valor da RAS corrigida ou NaN quando falta algum dos cinco valores
     */
    private double calculateCorrectedSar(double calcium, double magnesium, double sodium,
                                         double bicarbonate, double electricalConductivity) {

        if (calcium != 0.0 && magnesium != 0.0 && sodium != 0.0
                && bicarbonate != 0.0 && electricalConductivity != 0.0) {
            SarCalculator sarCalculator = new SarCalculator();
            return roundUpToFourDecimals(sarCalculator.calculateCorrectedSar(
                    calcium, magnesium, sodium, electricalConductivity, bicarbonate));
        }
        return Double.NaN;
    }

    private void renderClassifications() {
        double correctedSar = report.getCorrectedSar();

        correctedSarTextView.setText(Double.isNaN(correctedSar)
                ? getString(R.string.ras_nao_calculado)
                : String.valueOf(correctedSar));

        // riscos de infiltração
        if (Double.isNaN(correctedSar)) {
            infiltrationClassificationTextView.setText(getString(R.string.ras_nao_informado));
        } else {
            applyClassification(infiltrationClassificationTextView, infiltrationStatusImageView,
                    WaterQualityClassifier.classifyInfiltration(correctedSar));
        }

        // salinidade
        if (report.getElectricalConductivity() != 0) {
            applyClassification(salinityClassificationTextView, salinityStatusImageView,
                    WaterQualityClassifier.classifySalinity(report.getElectricalConductivity()));
        } else {
            salinityClassificationTextView.setText(getString(R.string.cea_nao_informado));
        }

        renderToxicity();

        // Coherence
        if (hasAllCationsAndAnions()) {
            applyClassification(ionicBalanceClassificationTextView, ionicBalanceStatusImageView,
                    WaterQualityClassifier.classifyIonicBalance(sumOfCations(), sumOfAnions()));
        } else {
            ionicBalanceClassificationTextView.setText(getString(R.string.coerencia_nao_informado));
        }
    }

    /**
     * Apresenta cloreto, boro e as duas formas de sódio e, no ícone de toxidez,
     * o maior risco entre os parâmetros informados.
     */
    private void renderToxicity() {
        List<RiskLevel> toxicityRisks = new ArrayList<>();

        if (report.getChloride() != 0) {
            WaterQualityClassifier.Classification chloride =
                    WaterQualityClassifier.classifyChloride(report.getChloride());
            chlorideClassificationTextView.setText(getString(stringResourceId(chloride.getLabel())));
            toxicityRisks.add(chloride.getRiskLevel());
        } else {
            chlorideClassificationTextView.setText(getString(R.string.cl_nao_informado));
        }

        if (report.getBoron() != 0) {
            WaterQualityClassifier.Classification boron =
                    WaterQualityClassifier.classifyBoron(report.getBoron());
            boronClassificationTextView.setText(getString(stringResourceId(boron.getLabel())));
            toxicityRisks.add(boron.getRiskLevel());
        } else {
            boronClassificationTextView.setText(getString(R.string.boro_nao_informado));
        }

        /* Sódio
            SS = Irrigação por superfície
            SA = Irrigação por aspersão
        */
        if (!Double.isNaN(report.getCorrectedSar())) {
            WaterQualityClassifier.Classification sodiumSurface =
                    WaterQualityClassifier.classifySodiumSurfaceIrrigation(report.getCorrectedSar());
            sodiumSurfaceClassificationTextView.setText(getString(stringResourceId(sodiumSurface.getLabel())));
            toxicityRisks.add(sodiumSurface.getRiskLevel());
        } else {
            sodiumSurfaceClassificationTextView.setText(getString(R.string.na1_nao_informado));
        }

        if (report.getSodium() != 0) {
            WaterQualityClassifier.Classification sodiumSprinkler =
                    WaterQualityClassifier.classifySodiumSprinklerIrrigation(report.getSodium());
            sodiumSprinklerClassificationTextView.setText(getString(stringResourceId(sodiumSprinkler.getLabel())));
            toxicityRisks.add(sodiumSprinkler.getRiskLevel());
        } else {
            sodiumSprinklerClassificationTextView.setText(getString(R.string.na2_nao_informado));
        }

        if (!toxicityRisks.isEmpty()) {
            applyRiskIcon(toxicityStatusImageView, highestRisk(toxicityRisks));
        }

        // pH
        if (report.getPh() != 0) {
            applyClassification(phClassificationTextView, phStatusImageView,
                    WaterQualityClassifier.classifyPh(report.getPh()));
        } else {
            phClassificationTextView.setText(getString(R.string.ph_nao_informado));
        }
    }

    private void applyClassification(TextView textView, ImageView statusImageView,
                                     WaterQualityClassifier.Classification classification) {
        textView.setText(getString(stringResourceId(classification.getLabel())));
        applyRiskIcon(statusImageView, classification.getRiskLevel());
    }

    private RiskLevel highestRisk(List<RiskLevel> risks) {
        RiskLevel highest = RiskLevel.OK;
        for (RiskLevel risk : risks) {
            if (risk.compareTo(highest) > 0) {
                highest = risk;
            }
        }
        return highest;
    }

    private boolean hasAllCationsAndAnions() {
        return report.getMagnesium() != 0 && report.getPotassium() != 0 && report.getCalcium() != 0
                && report.getSodium() != 0 && report.getChloride() != 0 && report.getCarbonate() != 0
                && report.getSulfate() != 0 && report.getBicarbonate() != 0;
    }

    private double sumOfCations() {
        return report.getMagnesium() + report.getPotassium() + report.getCalcium() + report.getSodium();
    }

    private double sumOfAnions() {
        return report.getChloride() + report.getCarbonate() + report.getSulfate() + report.getBicarbonate();
    }

    private int stringResourceId(String resourceName) {
        return this.getResources().getIdentifier(resourceName, "string", this.getPackageName());
    }

    private void applyRiskIcon(ImageView imageView, RiskLevel riskLevel) {
        switch (riskLevel) {
            case OK:
                imageView.setImageDrawable(ContextCompat.getDrawable(ReportAnalysisActivity.this, R.drawable.status_ok));
                break;
            case CAUTION:
                imageView.setImageDrawable(ContextCompat.getDrawable(ReportAnalysisActivity.this, R.drawable.status_caution));
                break;
            case ALERT:
                imageView.setImageDrawable(ContextCompat.getDrawable(ReportAnalysisActivity.this, R.drawable.status_alert));
                break;
            case DANGER:
                imageView.setImageDrawable(ContextCompat.getDrawable(ReportAnalysisActivity.this, R.drawable.status_danger));
                break;
        }
    }

    /**
     * Arredonda o valor de um double para cima, com 4 casas decimais.
     *
     * @param value valor a ser arredondado
     * @return valor arredondado
     */
    private double roundUpToFourDecimals(double value) {
        try {
            return (Math.ceil(value * 10000.0)) / 10000.0;
        } catch (Exception e) {
            e.printStackTrace();
            return value;
        }
    }

    private void bindViews() {
        infiltrationClassificationTextView = findViewById(R.id.SAR_CLASSIFICATION_TEXTVIEW);
        salinityClassificationTextView = findViewById(R.id.SALINITY_TEXTVIEW);
        chlorideClassificationTextView = findViewById(R.id.CL_CLASSIFICATION_TEXTVIEW);
        boronClassificationTextView = findViewById(R.id.B_CLASSIFICATION_TEXTVIEW);
        sodiumSurfaceClassificationTextView = findViewById(R.id.NA1_CLASSIFICATION_TEXTVIEW);
        sodiumSprinklerClassificationTextView = findViewById(R.id.NA2_CLASSIFICATION_TEXTVIEW);
        phClassificationTextView = findViewById(R.id.PH_CLASSIFICATION_TEXTVIEW);
        ionicBalanceClassificationTextView = findViewById(R.id.COHERENCE_TEXTVIEW);
        correctedSarTextView = findViewById(R.id.CORRECTED_SAR_TEXTVIEW);
        // status
        ionicBalanceStatusImageView = findViewById(R.id.COHERENCE_STATUS);
        salinityStatusImageView = findViewById(R.id.SALINITY_STATUS);
        infiltrationStatusImageView = findViewById(R.id.SAR_STATUS);
        toxicityStatusImageView = findViewById(R.id.TOXITY_STATUS);
        phStatusImageView = findViewById(R.id.PH_STATUS);
        // info
        ImageButton coherenceInfo = findViewById(R.id.COHERENCE_INFO);
        coherenceInfo.setOnClickListener(this);
        ImageButton salinityInfo = findViewById(R.id.SALINITY_INFO);
        salinityInfo.setOnClickListener(this);
        ImageButton sarInfo = findViewById(R.id.SAR_INFO);
        sarInfo.setOnClickListener(this);
        ImageButton toxicityInfo = findViewById(R.id.TOXITY_INFO);
        toxicityInfo.setOnClickListener(this);
        ImageButton phInfo = findViewById(R.id.PH_INFO);
        phInfo.setOnClickListener(this);

        // prepara o bd
        database = QWaterDatabase.getInstance(ReportAnalysisActivity.this);

        Toolbar topToolbar = findViewById(R.id.analyze_toolbar);
        setSupportActionBar(topToolbar);
        topToolbar.setNavigationIcon(R.drawable.action_navigation_arrow);
        topToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View clickedView) {
                finish();
            }
        });
    }

    /**
     * Manipula os cliques nos botões de informação.
     *
     * @param clickedView view que contém o ID do botão
     */
    @Override
    public void onClick(View clickedView) {
        if (clickedView.getId() == R.id.COHERENCE_INFO) {
            startActivity(new Intent(ReportAnalysisActivity.this, CoherenceActivity.class));
        } else if (clickedView.getId() == R.id.SALINITY_INFO) {
            startActivity(new Intent(ReportAnalysisActivity.this, SalinityActivity.class));
        } else if (clickedView.getId() == R.id.SAR_INFO) {
            startActivity(new Intent(ReportAnalysisActivity.this, SarActivity.class));
        } else if (clickedView.getId() == R.id.TOXITY_INFO) {
            startActivity(new Intent(ReportAnalysisActivity.this, ToxicityActivity.class));
        } else if (clickedView.getId() == R.id.PH_INFO) {
            startActivity(new Intent(ReportAnalysisActivity.this, PhActivity.class));
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case SAVE_NEW_REPORT:

                if (resultCode == RESULT_OK) {
                    readDateAndSource(data);

                    try {
                        // Pronto para armazenar no BD
                        new InsertReportTask().execute();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(ReportAnalysisActivity.this, getString(R.string.erro_salvar), Toast.LENGTH_SHORT).show();
                    }
                }
                break;

            case SAVE_UPDATED_REPORT:

                if (resultCode == RESULT_OK) {
                    readDateAndSource(data);

                    try {
                        // Pronto para atualizar no BD
                        new UpdateReportTask().execute();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(ReportAnalysisActivity.this, getString(R.string.erro_atualizar_relatorio), Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }

    private void readDateAndSource(Intent data) {
        report.setDate(data.getLongExtra(REPORT_DATE, System.currentTimeMillis()));
        report.setSourceId(data.getIntExtra(SOURCE_ID, 0));
        report.setSourceName(data.getStringExtra(SOURCE_NAME));
    }

    private void replaceNaNWithZero() {
        //tentativa de corrigir o problema de salvar o relatório
        if (Double.isNaN(report.getCorrectedSar())) {
            report.setCorrectedSar(0.0);
        }

        if (Double.isNaN(report.getNormalSar())) {
            report.setNormalSar(0.0);
        }
    }

    //TODO resolver o problema do leak https://stackoverflow.com/questions/44309241/warning-this-asynctask-class-should-be-static-or-leaks-might-occur
    private class InsertReportTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            replaceNaNWithZero();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            database.reportDao().insert(report);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            Toast.makeText(ReportAnalysisActivity.this, R.string.relatorio_salvo, Toast.LENGTH_SHORT).show();
        }
    }

    private class UpdateReportTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            replaceNaNWithZero();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            database.reportDao().update(report);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            // passar o relatorio para a activity de detalhes junto com uma flag apontando que vem da atualização
            // preciso limpar o histórico para o usuário não apertar back e ver os dados desatualizados
            Toast.makeText(ReportAnalysisActivity.this, R.string.relatorio_atualizado, Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(ReportAnalysisActivity.this, ReportDetailsActivity.class);
            intent.putExtra(REPORT, report);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//app deu crash, o log do erro pediu essa flag.
            startActivity(intent);
        }
    }
}
