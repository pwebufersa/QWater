package br.com.ufersa.qwater.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import br.com.ufersa.qwater.R;
import br.com.ufersa.qwater.adapters.ReportListAdapter;
import br.com.ufersa.qwater.beans.Report;
import br.com.ufersa.qwater.database.QWaterDatabase;
import br.com.ufersa.qwater.util.FragmentTag;

/**
 * Lista dos relatórios salvos, com botão flutuante para analisar uma nova
 * amostra.
 *
 * Cada linha abre a tela de detalhes do relatório.
 */
public class ReportListFragment extends Fragment {

    private RecyclerView reportRecyclerView;
    private QWaterDatabase database;
    private View rootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list_reports, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle(getString(R.string.reports_list));
        this.rootView = view;

        bindViews();

        // Popula a lista
        new LoadReportsTask().execute();
    }

    private void bindViews() {
        reportRecyclerView = rootView.findViewById(R.id.WATER_REPORT_RECYCLER_VIEW);
        reportRecyclerView.setLayoutManager(new LinearLayoutManager(rootView.getContext()));

        //prepara o bd
        database = QWaterDatabase.getInstance(getActivity());

        FloatingActionButton addReportButton = rootView.findViewById(R.id.FAB_LIST_WATER_REPORTS);
        addReportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View clickedView) {
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content_frame, new ReportFormFragment(), FragmentTag.REPORT_FORM)
                        .commit();
            }
        });
    }

    private class LoadReportsTask extends AsyncTask<Void, Void, List<Report>> {

        @Override
        protected List<Report> doInBackground(Void... voids) {
            return database.reportDao().getAll();
        }

        @Override
        protected void onPostExecute(List<Report> reports) {
            RecyclerView.Adapter adapter = new ReportListAdapter(rootView.getContext(), reports);
            reportRecyclerView.setAdapter(adapter);
        }
    }
}
