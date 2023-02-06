package com.example.prototipoapp.home.ui.veiculo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Adapter;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.prototipoapp.MainActivity;
import com.example.prototipoapp.R;
import com.example.prototipoapp.databinding.FragmentVeiculoBinding;
import com.example.prototipoapp.home.ui.veiculo.veiculoDetail.VeiculoDetail;
import com.example.prototipoapp.login.LoginActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.JarException;

//implements VAdapter.OnClickItemListener
public class VeiculoFragment extends Fragment {

    private FragmentVeiculoBinding binding;
    RecyclerView recyclerView;
    List<VeiculoItem> veiculoItems;
    VAdapter vadapter;
    public static String SHARED_PREFERENCES = "DadosUser";
    SwipeRefreshLayout refreshItensVeiculo;
    ProgressBar loadingBar;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        VeiculoViewModel galleryViewModel =
                new ViewModelProvider(this).get(VeiculoViewModel.class);

        binding = FragmentVeiculoBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        recyclerView = root.findViewById(R.id.ListVeiculosViewID);
        veiculoItems = new ArrayList<>();
        setHasOptionsMenu(true);
        if (internetIsConnected()){
            GetVeiculosRequest();
        }
        refreshItensVeiculo = root.findViewById(R.id.RefreshVeiculos);
        refreshItensVeiculo.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (internetIsConnected()){
                    GetVeiculosRequest();
                }
            }
        });

//        final TextView textView = binding.textVeiculo;
//        galleryViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }


    public void GetVeiculosRequest() {
        SharedPreferences sharedPreferences = getActivity().getApplicationContext().getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
        int id_instituicao = sharedPreferences.getInt("INSTITUICAO_ID", 0);
        String url = "https://brendonlulucas.pythonanywhere.com/" + id_instituicao + "/API/APIveiculos/";
        RequestQueue queue = Volley.newRequestQueue(binding.getRoot().getContext());
        StringRequest jsonArrayRequest = new StringRequest(Request.Method.GET, url,new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                veiculoItems = new ArrayList<>();
                loadingBar = getActivity().findViewById(R.id.progressBar);
                loadingBar.setVisibility(View.GONE);
                try {
                    JSONArray responseJSON = new JSONArray(response);
                    for (int i = 0; i < responseJSON.length(); i++) {
                        try {
                            JSONObject veiculoObject = responseJSON.getJSONObject(i);
                            VeiculoItem veiculoItem = new VeiculoItem();
                            veiculoItem.setPk(veiculoObject.getInt("pk"));
                            veiculoItem.setName(veiculoObject.getString("name").toString());
                            veiculoItem.setPlaca(veiculoObject.getString("placa").toString());
                            veiculoItem.setImageUrl(veiculoObject.getString("image").toString());
                            veiculoItem.setTipo(veiculoObject.getInt("tipo"));
                            veiculoItems.add(veiculoItem);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    refreshItensVeiculo.setRefreshing(false);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                initRecycleViewV();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                final String statusCode = String.valueOf(error.networkResponse.statusCode);
                String body = "";
                try {
                    body = new String(error.networkResponse.data, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    // exception
                }
                System.out.println(body);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Token " + sharedPreferences.getString("TOKEN", "null"));
                System.out.println("Authorization:" + "Token " + sharedPreferences.getString("TOKEN", "null"));
                return params;
            }
        };
        queue.add(jsonArrayRequest);

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
                Intent intent = new Intent(getContext(), FormAddVeiculoActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                Toast.makeText(getContext(), "Criar olha so", Toast.LENGTH_SHORT).show();
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
                vadapter.getFilter().filter(s);
                return false;
            }
        });


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void initRecycleViewV() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        vadapter = new VAdapter(getActivity().getApplicationContext(), veiculoItems);
        recyclerView.setAdapter(vadapter);
    }

    public boolean internetIsConnected() {
        try {
            String command = "ping -c 1 google.com";
            return (Runtime.getRuntime().exec(command).waitFor() == 0);
        } catch (Exception e) {
            return false;
        }
    }
//    @Override
//    public void onItemClickV(int position) {
////        Toast.makeText(getActivity().getApplicationContext(), "Luuuaa" + veiculoItems.get(position).getName(), Toast.LENGTH_SHORT).show();
//
//        Intent intent = new Intent(getActivity().getApplicationContext(), VeiculoDetail.class);
//        intent.putExtra("name_veiculo",veiculoItems.get(position).getName());
//        startActivity(intent);
//    }
}