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
import br.com.ufersa.qwater.adapters.SourceListAdapter;
import br.com.ufersa.qwater.beans.Source;
import br.com.ufersa.qwater.database.QWaterDatabase;
import br.com.ufersa.qwater.util.FragmentTag;

import static br.com.ufersa.qwater.util.IntentContract.Caller.MAIN;

/**
 * Lista das fontes cadastradas, com botão flutuante para cadastrar outra.
 *
 * Cada linha abre a tela de detalhes da fonte.
 */
public class SourceListFragment extends Fragment {

    private RecyclerView sourceRecyclerView;
    private FloatingActionButton addSourceButton;
    private QWaterDatabase database;
    private View rootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list_sources, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle(getString(R.string.sources_list));
        this.rootView = view;

        bindViews();

        //Popula a lista
        new LoadSourcesTask().execute();
    }

    private void bindViews() {
        sourceRecyclerView = rootView.findViewById(R.id.WATER_SOURCE_RECYCLER_VIEW);
        sourceRecyclerView.setLayoutManager(new LinearLayoutManager(rootView.getContext()));

        //prepara o bd
        database = QWaterDatabase.getInstance(getActivity());

        addSourceButton = rootView.findViewById(R.id.FAB_LIST_WATERSOURCES);
        addSourceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View clickedView) {
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content_frame, new SourceFormFragment(), FragmentTag.SOURCE_FORM)
                        .commit();
            }
        });
    }

    private class LoadSourcesTask extends AsyncTask<Void, Void, List<Source>> {

        @Override
        protected List<Source> doInBackground(Void... voids) {
            return database.sourceDao().getAll();
        }

        @Override
        protected void onPostExecute(List<Source> sources) {
            RecyclerView.Adapter adapter = new SourceListAdapter(MAIN, getActivity(), sources);
            sourceRecyclerView.setAdapter(adapter);
        }
    }
}
