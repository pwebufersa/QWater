package br.com.ufersa.qwater.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;

import br.com.ufersa.qwater.R;
import br.com.ufersa.qwater.beans.Report;
import br.com.ufersa.qwater.beans.Source;
import br.com.ufersa.qwater.fragments.HomeFragment;
import br.com.ufersa.qwater.fragments.ReportFormFragment;
import br.com.ufersa.qwater.fragments.ReportListFragment;
import br.com.ufersa.qwater.fragments.SourceFormFragment;
import br.com.ufersa.qwater.fragments.SourceListFragment;
import br.com.ufersa.qwater.help.AboutActivity;
import br.com.ufersa.qwater.util.FragmentTag;

import static br.com.ufersa.qwater.util.IntentContract.Key.PURPOSE;
import static br.com.ufersa.qwater.util.IntentContract.Key.REPORT;
import static br.com.ufersa.qwater.util.IntentContract.Key.SOURCE;
import static br.com.ufersa.qwater.util.IntentContract.Purpose.EDIT_REPORT;
import static br.com.ufersa.qwater.util.IntentContract.Purpose.EDIT_SOURCE;
import static br.com.ufersa.qwater.util.IntentContract.Purpose.REPORT_DELETED;
import static br.com.ufersa.qwater.util.IntentContract.Purpose.SOURCE_DELETED;
import static br.com.ufersa.qwater.util.IntentContract.Purpose.SOURCE_UPDATED;

/**
 * Tela principal, com menu lateral e os fragmentos de cadastro e de listagem.
 *
 * A finalidade recebida em {@link br.com.ufersa.qwater.util.IntentContract.Key#PURPOSE}
 * define a tela inicial: editar um relatório, editar uma fonte, ou abrir uma
 * lista depois de uma exclusão.
 */
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private NavigationView navigationView;
    private boolean openedForEditing = false; // Para controle do onBackPressed

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Se tem a finalidade e ela for para atualizar, então o extra contém um relatório para ser atualizado
        // Se a finalidade for de delete, a intenção é colocar o fragmento de lista para a lista ser atualizada
        if (getIntent().hasExtra(PURPOSE)) {
            openRequestedScreen(getIntent().getIntExtra(PURPOSE, 0));
        } else {
            showScreen(R.id.HOME);
            navigationView.setCheckedItem(R.id.HOME);
        }
    }

    private void openRequestedScreen(int purpose) {
        switch (purpose) {
            case EDIT_REPORT:
                openedForEditing = true;
                showReportForm(getIntent().getParcelableExtra(REPORT));
                break;
            case REPORT_DELETED:
                showScreen(R.id.LIST_REPORTS);
                navigationView.setCheckedItem(R.id.LIST_REPORTS);
                break;
            case EDIT_SOURCE:
                openedForEditing = true;
                showSourceForm(getIntent().getParcelableExtra(SOURCE));
                break;
            case SOURCE_DELETED:
            case SOURCE_UPDATED:
                showScreen(R.id.LIST_SOURCES);
                navigationView.setCheckedItem(R.id.LIST_SOURCES);
                break;
            default:
                showScreen(R.id.HOME);
                navigationView.setCheckedItem(R.id.HOME);
                break;
        }
    }

    /*
    SEND DATA TO FRAGMENT
     */
    private void showReportForm(Report report) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(REPORT, report);

        Fragment fragment = new ReportFormFragment();
        fragment.setArguments(bundle);

        replaceContent(fragment, FragmentTag.REPORT_EDIT);
    }

    private void showSourceForm(Source source) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(SOURCE, source);

        Fragment fragment = new SourceFormFragment();
        fragment.setArguments(bundle);

        replaceContent(fragment, FragmentTag.SOURCE_EDIT);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_about) {
            startActivity(new Intent(MainActivity.this, AboutActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showScreen(int menuItemId) {
        Fragment fragment = fragmentFor(menuItemId);
        String tag = tagFor(menuItemId);

        if (fragment != null) {
            replaceContent(fragment, tag);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    private Fragment fragmentFor(int menuItemId) {
        if (menuItemId == R.id.HOME) {
            return new HomeFragment();
        }
        if (menuItemId == R.id.CREATE_SOURCE) {
            return new SourceFormFragment();
        }
        if (menuItemId == R.id.CREATE_REPORT) {
            return new ReportFormFragment();
        }
        if (menuItemId == R.id.LIST_REPORTS) {
            return new ReportListFragment();
        }
        if (menuItemId == R.id.LIST_SOURCES) {
            return new SourceListFragment();
        }
        return null;
    }

    private String tagFor(int menuItemId) {
        if (menuItemId == R.id.HOME) {
            return FragmentTag.HOME;
        }
        if (menuItemId == R.id.CREATE_SOURCE) {
            return FragmentTag.SOURCE_FORM;
        }
        if (menuItemId == R.id.CREATE_REPORT) {
            return FragmentTag.REPORT_FORM;
        }
        if (menuItemId == R.id.LIST_REPORTS) {
            return FragmentTag.REPORT_LIST;
        }
        if (menuItemId == R.id.LIST_SOURCES) {
            return FragmentTag.SOURCE_LIST;
        }
        return null;
    }

    private void replaceContent(Fragment fragment, String tag) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content_frame, fragment, tag);
        transaction.commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        showScreen(item.getItemId());
        return true;
    }

    @Override
    public void onBackPressed() {

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
            return;
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment currentFragment = fragmentManager.findFragmentById(R.id.content_frame);

        // se estiver atualizando um relatório, deve voltar para a activity de detalhes
        if (openedForEditing || currentFragment == null || FragmentTag.HOME.equals(currentFragment.getTag())) {
            super.onBackPressed();
            return;
        }

        // Se o fragmento atual exibido não for a home, o botão voltar direciona para ela
        showScreen(R.id.HOME);
        navigationView.setCheckedItem(R.id.HOME);
    }
}
