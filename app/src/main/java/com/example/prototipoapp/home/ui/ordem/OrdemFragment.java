package com.example.prototipoapp.home.ui.ordem;

import static com.example.prototipoapp.home.ui.ordem.Fragments.FragmentOrdem_1.recycleVOrdemAdapterA1;
import static com.example.prototipoapp.home.ui.ordem.Fragments.FragmentOrdem_2.recycleVOrdemAdapterA2;
import static com.example.prototipoapp.home.ui.ordem.Fragments.FragmentOrdem_3.recycleVOrdemAdapterA3;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.prototipoapp.R;
import com.example.prototipoapp.databinding.FragmentOrdemBinding;
import com.example.prototipoapp.home.ui.ordem.Fragments.FragmentOrdem_1;
import com.example.prototipoapp.home.ui.ordem.Fragments.FragmentOrdem_2;
import com.example.prototipoapp.home.ui.ordem.Fragments.FragmentOrdem_3;
import com.example.prototipoapp.home.ui.ordem.Fragments.VPAdapter;
import com.example.prototipoapp.home.ui.veiculo.FormAddVeiculoActivity;
import com.example.prototipoapp.home.ui.veiculo.veiculoDetail.AdapterV.RecycleVOrdemAdapter;
import com.google.android.material.tabs.TabLayout;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;


public class OrdemFragment extends Fragment {
    public static String SHARED_PREFERENCES = "DadosUser";
    private FragmentOrdemBinding binding;
    private TabLayout tableLayout;
    private ViewPager viewPager;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentOrdemBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        setHasOptionsMenu(true);



        tableLayout = root.findViewById(R.id.tabLayout);
        viewPager = root.findViewById(R.id.viewr_pager_ordem);

        tableLayout.setupWithViewPager(viewPager);
        VPAdapter vpAdapter = new VPAdapter(this.getChildFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        vpAdapter.addFragment(new FragmentOrdem_1(), "Aguardando");
        vpAdapter.addFragment(new FragmentOrdem_2(), "Andamento");
        vpAdapter.addFragment(new FragmentOrdem_3(), "Finalizados");
        viewPager.setAdapter(vpAdapter);

        return root;
    }



    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_search_v, menu);
        inflater.inflate(R.menu.menu_add_veiculo, menu);
        MenuItem createVitem = menu.findItem(R.id.id_create_v);
        createVitem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Intent intent = new Intent(getContext(), FormAddOrdemActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
//                Toast.makeText(getContext(), "Criar olha so", Toast.LENGTH_SHORT).show();
                return false;
            }
        });

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
                recycleVOrdemAdapterA1.getFilter().filter(s);
                recycleVOrdemAdapterA2.getFilter().filter(s);
                recycleVOrdemAdapterA3.getFilter().filter(s);
                return false;
            }
        });


    }




    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}