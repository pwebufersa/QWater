package br.com.ufersa.qwater.activities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import br.com.ufersa.qwater.R;

import static br.com.ufersa.qwater.util.IntentContract.Caller.REPORT_SAVE;
import static br.com.ufersa.qwater.util.IntentContract.Key.CALLER;
import static br.com.ufersa.qwater.util.IntentContract.Key.REPORT_DATE;
import static br.com.ufersa.qwater.util.IntentContract.Key.SOURCE_ID;
import static br.com.ufersa.qwater.util.IntentContract.Key.SOURCE_NAME;
import static br.com.ufersa.qwater.util.IntentContract.Request.PICK_SOURCE;

/**
 * Escolha da data e da fonte antes de salvar o relatório.
 *
 * Devolve data, identificador e nome da fonte para a tela que chamou.
 * Ao editar um relatório, os três valores chegam por extra e já aparecem
 * preenchidos.
 */
public class ReportSaveActivity extends AppCompatActivity {

    private TextView dateTextView;
    private TextView sourceTextView;
    private DatePickerDialog datePickerDialog;
    private Button saveButton;
    private boolean dateChosen;
    private boolean sourceChosen;
    private Intent resultIntent;
    private long reportDate;
    private int sourceId;
    private String sourceName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_report);
        bindViews();
        showIncomingSelection();

        dateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View clickedView) {
                // calender class's instance and get current date , month and year from calender
                final Calendar calendar = Calendar.getInstance();
                int currentYear = calendar.get(Calendar.YEAR); // current year
                int currentMonth = calendar.get(Calendar.MONTH); // current month
                int currentDay = calendar.get(Calendar.DAY_OF_MONTH); // current day
                // date picker dialog
                datePickerDialog = new DatePickerDialog(ReportSaveActivity.this, AlertDialog.THEME_HOLO_DARK, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        dateTextView.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);

                        Calendar chosenDate = new GregorianCalendar(year, monthOfYear, dayOfMonth);
                        reportDate = chosenDate.getTimeInMillis();
                        dateChosen = true;
                    }
                }, currentYear, currentMonth, currentDay);

                datePickerDialog.show();
                // Tentei usar o método setButton no dialog, mas deu algum conflito com o onDateSet
            }
        });

        sourceTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View clickedView) {
                Intent intent = new Intent(ReportSaveActivity.this, SourcePickerActivity.class);
                intent.putExtra(CALLER, REPORT_SAVE);
                startActivityForResult(intent, PICK_SOURCE);
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View clickedView) {
                if (dateChosen && sourceChosen) {
                    resultIntent.putExtra(REPORT_DATE, reportDate);
                    resultIntent.putExtra(SOURCE_ID, sourceId);
                    resultIntent.putExtra(SOURCE_NAME, sourceName);

                    setResult(RESULT_OK, resultIntent);
                    finish();
                } else {
                    Toast.makeText(ReportSaveActivity.this, R.string.selecione_a_data_e_fonte, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void bindViews() {
        dateChosen = false;
        sourceChosen = false;
        resultIntent = new Intent();
        dateTextView = findViewById(R.id.DATE_EDIT);
        sourceTextView = findViewById(R.id.SOURCE_EDIT);
        saveButton = findViewById(R.id.SAVE_CONFIRM_BUTTON);
    }

    private void showIncomingSelection() {
        if (getIntent().hasExtra(SOURCE_NAME)) {
            sourceName = getIntent().getStringExtra(SOURCE_NAME);
            sourceId = getIntent().getIntExtra(SOURCE_ID, 0);
            reportDate = getIntent().getLongExtra(REPORT_DATE, System.currentTimeMillis());
            dateChosen = true;
            sourceChosen = true;

            sourceTextView.setText(sourceName);
            dateTextView.setText(formatDate(reportDate));
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case PICK_SOURCE:
                if (resultCode == RESULT_OK) {

                    sourceId = data.getIntExtra(SOURCE_ID, 0);
                    sourceName = data.getStringExtra(SOURCE_NAME);

                    sourceTextView.setText(sourceName);
                    sourceChosen = true;
                }
                break;
        }
    }

    private String formatDate(long timeInMillis) {
        //TODO rever formato da data para internacionalizaço
        return new SimpleDateFormat("dd/MM/yyyy").format(new Date(timeInMillis));
    }
}
