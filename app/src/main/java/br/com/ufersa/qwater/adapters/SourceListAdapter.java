package br.com.ufersa.qwater.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import br.com.ufersa.qwater.R;
import br.com.ufersa.qwater.activities.SourceDetailsActivity;
import br.com.ufersa.qwater.beans.Source;

import static android.app.Activity.RESULT_OK;
import static br.com.ufersa.qwater.util.IntentContract.Caller.MAIN;
import static br.com.ufersa.qwater.util.IntentContract.Caller.REPORT_SAVE;
import static br.com.ufersa.qwater.util.IntentContract.Key.SOURCE;
import static br.com.ufersa.qwater.util.IntentContract.Key.SOURCE_ID;
import static br.com.ufersa.qwater.util.IntentContract.Key.SOURCE_NAME;

/**
 * Lista de fontes usada pela tela de fontes e pelo seletor de fonte do
 * relatório.
 *
 * Quando quem chama é {@link br.com.ufersa.qwater.util.IntentContract.Caller#REPORT_SAVE},
 * o clique devolve identificador e nome da fonte para a tela de salvamento; nos
 * demais casos, abre os detalhes da fonte.
 */
public class SourceListAdapter extends RecyclerView.Adapter<SourceListAdapter.SourceViewHolder> {

    private final List<Source> sources;
    private final Context context;
    private final int callerActivity;

    public SourceListAdapter(int callerActivity, Context context, List<Source> sources) {
        this.sources = sources;
        this.context = context;
        this.callerActivity = callerActivity;
    }

    @NonNull
    @Override
    public SourceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(parent.getContext()).inflate(R.layout.source_row, parent, false);
        return new SourceViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull SourceViewHolder holder, int position) {
        holder.bind(sources.get(position));
    }

    @Override
    public int getItemCount() {
        return sources.size();
    }

    class SourceViewHolder extends RecyclerView.ViewHolder {
        private final TextView sourceNameTextView;
        final ConstraintLayout rowLayout;

        SourceViewHolder(View row) {
            super(row);
            sourceNameTextView = row.findViewById(R.id.TEXTVIEW_WATERSOURCE_NAME);
            rowLayout = row.findViewById(R.id.LAYOUT_WATER_SOURCE_ROW);
        }

        void bind(Source source) {
            sourceNameTextView.setText(source.getName());
            rowLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View clickedView) {
                    handleClick(source);
                }
            });
        }

        private void handleClick(Source source) {
            if (callerActivity == REPORT_SAVE) {
                Intent result = new Intent();
                result.putExtra(SOURCE_ID, source.getId());
                result.putExtra(SOURCE_NAME, source.getName());
                ((Activity) context).setResult(RESULT_OK, result);
                ((Activity) context).finish();
            } else if (callerActivity == MAIN) {
                Intent intent = new Intent(context, SourceDetailsActivity.class)
                        .putExtra(SOURCE, source);
                context.startActivity(intent);
            }
        }
    }
}
