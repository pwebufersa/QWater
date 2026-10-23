package br.com.ufersa.qwater.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import br.com.ufersa.qwater.R;
import br.com.ufersa.qwater.activities.ReportAnalysisActivity;
import br.com.ufersa.qwater.beans.Report;

import static br.com.ufersa.qwater.util.IntentContract.Key.PURPOSE;
import static br.com.ufersa.qwater.util.IntentContract.Key.REPORT;
import static br.com.ufersa.qwater.util.IntentContract.Purpose.EDIT_REPORT;

/**
 * Formulário dos parâmetros da água: cátions, ânions, condutividade elétrica,
 * pH e boro.
 *
 * Campo vazio é lido como zero e texto não numérico também. Com um
 * {@link Report} em {@link br.com.ufersa.qwater.util.IntentContract.Key#REPORT},
 * o formulário abre preenchido e o botão passa a avaliar antes de atualizar.
 *
 * Exemplo: o botão "Assess the quality" abre a tela de análise com o relatório
 * montado a partir dos campos.
 */
public class ReportFormFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    //TODO na CorrectedCalciumTable, o valor limite para a Cea é 8.0

    private EditText conductivityEditText;
    private EditText calciumEditText;
    private EditText magnesiumEditText;
    private EditText potassiumEditText;
    private EditText sodiumEditText;
    private EditText carbonateEditText;
    private EditText bicarbonateEditText;
    private EditText chlorideEditText;
    private EditText phEditText;
    private EditText sulfateEditText;
    private EditText boronEditText;
    private Spinner conductivityUnitSpinner;
    private Button assessButton;
    private boolean isEditingReport = false;
    private Report report;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_create_report, container, false);
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle(getString(R.string.analisar_relatorio));

        bindViews(view);

        // checa se o bundle vem vazio, se contem algo, é para dar update
        // Se o fragmento está atualizando o relatório, não é necessário instanciar um novo relatório
        // Dessa forma, o ID e o nome da fonte não são perdidos
        if (this.getArguments() != null) {
            try {
                report = this.getArguments().getParcelable(REPORT);
                showReportValues(report);
                isEditingReport = true;
            } catch (Exception e) {
                e.printStackTrace();
                report = new Report();
                Toast.makeText(view.getContext(), R.string.erro_atualizar_relatorio, Toast.LENGTH_LONG).show();
            }
        } else {
            report = new Report();
        }

        assessButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View clickedView) {

                try {
                    report = readReportFromForm();
                    if (hasInvalidParameter(report)) {
                        Toast.makeText(getContext(), getString(R.string.erro_verifique_dados), Toast.LENGTH_LONG).show();
                        return;
                    }

                    Intent intent = new Intent(view.getContext(), ReportAnalysisActivity.class);
                    intent.putExtra(REPORT, report);

                    // Sinaliza que o relatório está vindo para ser atualizado (ao invés de insert, será update no BD)
                    if (isEditingReport) {
                        intent.putExtra(PURPOSE, EDIT_REPORT);
                    }

                    startActivity(intent);

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), getString(R.string.erro_verifique_dados), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void bindViews(View view) {
        conductivityEditText = view.findViewById(R.id.EDIT_CEA);
        calciumEditText = view.findViewById(R.id.EDIT_CA);
        magnesiumEditText = view.findViewById(R.id.EDIT_MG);
        potassiumEditText = view.findViewById(R.id.EDIT_K);
        sodiumEditText = view.findViewById(R.id.EDIT_NA);
        carbonateEditText = view.findViewById(R.id.EDIT_CO3);
        bicarbonateEditText = view.findViewById(R.id.EDIT_HCO3);
        chlorideEditText = view.findViewById(R.id.EDIT_CL);
        phEditText = view.findViewById(R.id.EDIT_PH);
        boronEditText = view.findViewById(R.id.EDIT_B);
        sulfateEditText = view.findViewById(R.id.EDIT_SO4);

        assessButton = view.findViewById(R.id.ANALYZE_BUTTON);

        Spinner moleculeUnitSpinner = view.findViewById(R.id.MOLECULES_SPINNER);
        moleculeUnitSpinner.setOnItemSelectedListener(this);

        conductivityUnitSpinner = view.findViewById(R.id.CEA_SPINNER);
        conductivityUnitSpinner.setOnItemSelectedListener(this);
    }

    private void showReportValues(Report report) {
        // troca o texto do botão quando vem para update
        assessButton.setText(R.string.avaliar_antes_de_atualizar);

        conductivityEditText.setText(String.valueOf(report.getElectricalConductivity()));
        calciumEditText.setText(String.valueOf(report.getCalcium()));
        magnesiumEditText.setText(String.valueOf(report.getMagnesium()));
        potassiumEditText.setText(String.valueOf(report.getPotassium()));
        sodiumEditText.setText(String.valueOf(report.getSodium()));
        carbonateEditText.setText(String.valueOf(report.getCarbonate()));
        bicarbonateEditText.setText(String.valueOf(report.getBicarbonate()));
        chlorideEditText.setText(String.valueOf(report.getChloride()));
        phEditText.setText(String.valueOf(report.getPh()));
        boronEditText.setText(String.valueOf(report.getBoron()));
        sulfateEditText.setText(String.valueOf(report.getSulfate()));
    }

    /**
     * Verifica se algum dos parâmetros do relatório é NaN.
     *
     * @param report relatório lido do formulário
     * @return true caso algum parâmetro seja NaN
     */
    private boolean hasInvalidParameter(Report report) {
        try {
            return (Double.isNaN(report.getElectricalConductivity()) || Double.isNaN(report.getCalcium())
                    || Double.isNaN(report.getMagnesium()) || Double.isNaN(report.getPotassium())
                    || Double.isNaN(report.getSodium()) || Double.isNaN(report.getCarbonate())
                    || Double.isNaN(report.getChloride()) || Double.isNaN(report.getBicarbonate())
                    || Double.isNaN(report.getPh()) || Double.isNaN(report.getSulfate())
                    || Double.isNaN(report.getBoron()));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private Report readReportFromForm() {
        report.setMagnesium(readNumber(magnesiumEditText));
        report.setPotassium(readNumber(potassiumEditText));
        report.setCalcium(readNumber(calciumEditText));
        report.setSodium(readNumber(sodiumEditText));
        report.setChloride(readNumber(chlorideEditText));
        report.setCarbonate(readNumber(carbonateEditText));
        report.setBicarbonate(readNumber(bicarbonateEditText));
        report.setPh(readNumber(phEditText));
        report.setSulfate(readNumber(sulfateEditText));
        report.setBoron(readNumber(boronEditText));
        report.setElectricalConductivity(readConductivityInDecisiemensPerMeter());

        return report;
    }

    private double readNumber(EditText field) {
        if (field.getText().length() == 0) {
            return 0.0;
        }
        return parseNumber(field.getText().toString());
    }

    /**
     * Converte um valor numérico em texto para double.
     *
     * @param valueInText valor em texto
     * @return double equivalente, ou zero quando o texto não é numérico
     */
    private double parseNumber(String valueInText) {
        try {
            return Double.parseDouble(valueInText);
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * Lê a condutividade elétrica e converte microsiemens por centímetro para
     * decisiemens por metro, que é a unidade do cálculo. dS/m e mS/cm são
     * equivalentes.
     */
    private double readConductivityInDecisiemensPerMeter() {
        double conductivity = readNumber(conductivityEditText);

        if (conductivity != 0 && conductivityUnitSpinner.getSelectedItemPosition() == 2) {
            conductivity = conductivity / 1000;
        }

        return conductivity;
    }

    /**
     * A escolha das unidades é lida no momento de montar o relatório, portanto
     * o evento de seleção não tem o que atualizar.
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }
}
