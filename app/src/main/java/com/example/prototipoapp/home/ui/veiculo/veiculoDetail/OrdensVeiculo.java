package com.example.prototipoapp.home.ui.veiculo.veiculoDetail;

import static com.example.prototipoapp.home.ui.veiculo.veiculoDetail.VOrdemAtivoFragment.recycleVOrdemAdapterAtivo;
import static com.example.prototipoapp.home.ui.veiculo.veiculoDetail.VOrdemConcluidoFragment.recycleVOrdemAdapterFinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prototipoapp.R;
import com.example.prototipoapp.home.ui.ordem.Ordem;
import com.example.prototipoapp.home.ui.veiculo.VAdapter;
import com.example.prototipoapp.home.ui.veiculo.VeiculoItem;
import com.example.prototipoapp.home.ui.veiculo.veiculoDetail.AdapterV.OVAdapter;
import com.example.prototipoapp.home.ui.veiculo.veiculoDetail.AdapterV.RecycleVOrdemAdapter;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class OrdensVeiculo extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    public int veiculo_id;

    public static String SHARED_PREFERENCES = "DadosUser";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ordens_veiculo);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);



        setTitle("Registros");
        veiculo_id = getIntent().getExtras().getInt("id_veiculo");
//        Bundle bundle = new Bundle();
//        bundle.putInt("id_veiculo", veiculo_id);

        tabLayout = findViewById(R.id.tab_o_V);
        viewPager = findViewById(R.id.view_page_o_v);

        tabLayout.setupWithViewPager(viewPager);
        OVAdapter ovAdapter = new OVAdapter(this.getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        ovAdapter.addFragment(new VOrdemAtivoFragment(), "Ativo");
        ovAdapter.addFragment(new VOrdemConcluidoFragment(), "Concluido");
        viewPager.setAdapter(ovAdapter);


    }



    public int getIdVeiculo(){
        return veiculo_id;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_options_veiculo, menu);

        inflater.inflate(R.menu.menu_search_v, menu);
        MenuItem searchitem = menu.findItem(R.id.id_search_vv);
        SearchView searchView = (SearchView) searchitem.getActionView();
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                recycleVOrdemAdapterAtivo.getFilter().filter(s);
                recycleVOrdemAdapterFinal.getFilter().filter(s);
                return false;
            }
        });

        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.menu_filtro:
                Toast.makeText(this, "Abriu filtro", Toast.LENGTH_SHORT).show();
                ShowAlertaFiltro();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    AlertDialog alerta;
    private void ShowAlertaFiltro() {
        //Lista de itens
        ArrayList<String> itens = new ArrayList<String>();
        itens.add("Ruim");
        itens.add("Mediano");
        itens.add("Bom");
        itens.add("Ótimo");

        //adapter utilizando um layout customizado (TextView)
        TextView textView = new TextView(this);
        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.item_alerta, itens);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Qualifique este software:");
        //define o diálogo como uma lista, passa o adapter.
        builder.setSingleChoiceItems(adapter, 0, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                Toast.makeText(OrdensVeiculo.this, "posição selecionada=" + arg1, Toast.LENGTH_SHORT).show();
                alerta.dismiss();
            }
        });

        alerta = builder.create();
        alerta.show();
    }
}