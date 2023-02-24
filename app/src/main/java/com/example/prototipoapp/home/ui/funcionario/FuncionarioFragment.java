package com.example.prototipoapp.home.ui.funcionario;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.prototipoapp.R;
import com.example.prototipoapp.databinding.FragmentFuncionarioBinding;
import com.example.prototipoapp.home.ui.funcionario.Adapters.FuncAdapter;
import com.example.prototipoapp.home.ui.veiculo.FormAddVeiculoActivity;
import com.example.prototipoapp.home.ui.veiculo.VAdapter;
import com.example.prototipoapp.home.ui.veiculo.VeiculoItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class FuncionarioFragment extends Fragment {

    private FragmentFuncionarioBinding binding;
    RecyclerView recyclerView;
    List<User> userItems;
    FuncAdapter funcAdapter;
    public static String SHARED_PREFERENCES = "DadosUser";
    SwipeRefreshLayout refreshItensUser;
    ProgressBar loadingBar;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentFuncionarioBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        recyclerView = root.findViewById(R.id.ListUserFuncViewId);
        userItems = new ArrayList<>();

        setHasOptionsMenu(true);
        SharedPreferences sharedPreferences = getActivity().getApplicationContext().getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
        int id_instituicao = sharedPreferences.getInt("INSTITUICAO_ID", 0);
        if (id_instituicao != 0){
            if (internetIsConnected()){
                GetUsersRequest();
            }
            refreshItensUser = root.findViewById(R.id.RefreshFuncs);
            refreshItensUser.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    if (internetIsConnected()){
                        GetUsersRequest();
                    }
                }
            });
        }

        return root;
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        SharedPreferences sharedPreferences = getActivity().getApplicationContext().getSharedPreferences(SHARED_PREFERENCES,MODE_PRIVATE);
        int id_instituicao = sharedPreferences.getInt("INSTITUICAO_ID", 0);

        if (id_instituicao != 0){
            inflater.inflate(R.menu.menu_search_v, menu);
            inflater.inflate(R.menu.menu_add_veiculo, menu);
            MenuItem createVitem = menu.findItem(R.id.id_create_v);
            createVitem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    Intent intent = new Intent(getContext(), FormAddFuncActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
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
                    funcAdapter.getFilter().filter(s);
                    return false;
                }
            });
        }
    }

    private void initRecycleViewU() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        funcAdapter = new FuncAdapter(getActivity().getApplicationContext(), userItems);
        recyclerView.setAdapter(funcAdapter);
    }

    public boolean internetIsConnected() {
        try {
            String command = "ping -c 1 google.com";
            return (Runtime.getRuntime().exec(command).waitFor() == 0);
        } catch (Exception e) {
            return false;
        }
    }

    public void GetUsersRequest() {
        SharedPreferences sharedPreferences = getActivity().getApplicationContext().getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
        int id_instituicao = sharedPreferences.getInt("INSTITUICAO_ID", 0);

        String url = "https://brendonlulucas.pythonanywhere.com/" + id_instituicao + "/API/GetAllFuncInst/";
        RequestQueue queue = Volley.newRequestQueue(binding.getRoot().getContext());
        StringRequest jsonArrayRequest = new StringRequest(Request.Method.GET, url,new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                userItems = new ArrayList<>();
                loadingBar = getActivity().findViewById(R.id.progressBarFuncs);
                loadingBar.setVisibility(View.GONE);
                try {
                    JSONArray responseJSON = new JSONArray(response);
                    for (int i = 0; i < responseJSON.length(); i++) {
                        try {
                            JSONObject userObject = responseJSON.getJSONObject(i);
                            User userItem = new User();
                            userItem.setPk(userObject.getInt("pk"));
                            userItem.setName(userObject.getString("name").toString());
                            userItem.setCargo(userObject.getString("cargo").toString());
//                            userItem.setImageUrl(userObject.getString("image").toString());
                            userItems.add(userItem);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    refreshItensUser.setRefreshing(false);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                initRecycleViewU();
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
                return params;
            }
            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                try {
                    String jsonString = new String(response.data, "UTF-8");
                    return Response.success(jsonString, HttpHeaderParser.parseCacheHeaders(response));
                } catch (UnsupportedEncodingException e) {
                    return Response.error(new ParseError(e));
                }
            }
        };
        queue.add(jsonArrayRequest);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}