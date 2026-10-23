package br.com.ufersa.qwater.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import br.com.ufersa.qwater.R;
import br.com.ufersa.qwater.activities.ReportDetailsActivity;
import br.com.ufersa.qwater.beans.Report;

import static br.com.ufersa.qwater.util.IntentContract.Key.REPORT;

/**
 * Lista dos relatórios, com nome da fonte e data de criação.
 *
 * O clique abre os detalhes do relatório.
 */
public class ReportListAdapter extends RecyclerView.Adapter<ReportListAdapter.ReportViewHolder> {

    private final List<Report> reports;
    private final Context context;

    public ReportListAdapter(Context context, List<Report> reports) {
        this.reports = reports;
        this.context = context;
    }

    @NonNull
    @Override
    public ReportViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(parent.getContext()).inflate(R.layout.report_row, parent, false);
        return new ReportViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull ReportViewHolder holder, int position) {
        holder.bind(reports.get(position));
    }

    @Override
    public int getItemCount() {
        return reports.size();
    }

    class ReportViewHolder extends RecyclerView.ViewHolder {
        private final TextView reportSourceNameTextView;
        private final TextView reportDateTextView;
        final View rowLayout;

        ReportViewHolder(View row) {
            super(row);
            reportSourceNameTextView = row.findViewById(R.id.TEXTVIEW_REPORT_SOURCE_NAME);
            reportDateTextView = row.findViewById(R.id.TEXTVIEW_REPORT_DATE);
            rowLayout = row.findViewById(R.id.LAYOUT_WATER_REPORT_ROW);
        }

        void bind(Report report) {
            reportSourceNameTextView.setText(report.getSourceName());
            reportDateTextView.setText(formatDate(report.getDate()));
            rowLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View clickedView) {
                    openReportDetails(report);
                }
            });
        }

        private void openReportDetails(Report report) {
            Intent intent = new Intent(context, ReportDetailsActivity.class);
            intent.putExtra(REPORT, report);
            context.startActivity(intent);
        }

        private String formatDate(long timeInMillis) {
            return new SimpleDateFormat("dd/MM/yyyy").format(new Date(timeInMillis));
        }
    }
}
