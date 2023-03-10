package com.example.prototipoapp.home.ui.veiculo.veiculoDetail.AdapterV;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.wear.tiles.material.Button;

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
import com.example.prototipoapp.home.ui.funcionario.perfil.DialogDataInfos;
import com.example.prototipoapp.home.ui.ordem.GerenciaOrdem.ConfirmOrdem;
import com.example.prototipoapp.home.ui.ordem.Ordem;
import com.example.prototipoapp.home.ui.ordem.ordemDetail.OrdemDetailActivity;
import com.example.prototipoapp.home.ui.veiculo.veiculoDetail.EditVeiculoActivity;
import com.google.android.material.button.MaterialButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class RecycleVOrdemAdapter extends RecyclerView.Adapter<RecycleVOrdemAdapter.ViewHolder> implements Filterable {
    public static String SHARED_PREFERENCES = "DadosUser";
    LayoutInflater inflater;
    List<Ordem> ordemsItems;
    List<Ordem> ordemItemsFull;
    AlertDialog alerta, alerta_confirm_2;

    public RecycleVOrdemAdapter(Context ctx, List<Ordem> ordemsItems) {
        this.inflater = LayoutInflater.from(ctx);
        this.ordemsItems = ordemsItems;
        ordemItemsFull = new ArrayList<>(ordemsItems);
    }

    @NonNull
    @Override
    public RecycleVOrdemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.layout_card_ordem_1, parent, false);
        return new RecycleVOrdemAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecycleVOrdemAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.name.setText(ordemsItems.get(position).getSolicitante());
        holder.data.setText(ordemsItems.get(position).getData_solicitacao().toString());
        String label_status = "none";
        switch (ordemsItems.get(position).getStatus().toString()) {
            case "1":
                label_status = "Finalizado";
                break;
            case "2":
            case "3":
                label_status = "Aguardando";
                break;
            case "4":
                label_status = "Em Curso";
                break;
            case "5":
                label_status = "Recusado";
                break;
        }
        holder.status.setText(label_status);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), OrdemDetailActivity.class);
                intent.putExtra("id_ordem", ordemsItems.get(position).getPk());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                view.getContext().startActivity(intent);
            }
        });
        holder.card_ordem.setAnimation(AnimationUtils.loadAnimation(inflater.getContext(), R.anim.slide_in));
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                return false;
            }
        });

        holder.btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopUpMenu(view, position);
            }
        });
    }

    private void showPopUpMenu(View view, int position) {
        final PopupMenu menupop = new PopupMenu(view.getContext(), view);
        menupop.getMenu().add("Ger??nciar").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (ordemsItems.get(position).getStatus().toString()) {
                    case "1":
                        Toast.makeText(view.getContext(), "Set nada acabou", Toast.LENGTH_SHORT).show();
                        break;
                    case "2":
                        DialogConfirme2(view, position);
                        break;
                    case "3":
                        Intent intent = new Intent(view.getContext(), ConfirmOrdem.class);
                        intent.putExtra("id_ordem", ordemsItems.get(position).getPk());
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        view.getContext().startActivity(intent);
                        break;
                    case "4":
                        DialogConfirmFinalizar(view, position);
                        Toast.makeText(view.getContext(), "Set finalizar", Toast.LENGTH_SHORT).show();
                        // em curso
                        break;
                    case "5":
                        Toast.makeText(view.getContext(), "Set recusar", Toast.LENGTH_SHORT).show();
                        // "Recusado";
                        break;
                }


                return false;
            }
        });
        if (ordemsItems.get(position).getStatus().toString().equals("3") ||
                ordemsItems.get(position).getStatus().toString().equals("2")) {
            menupop.getMenu().add("Modificar").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    return false;
                }
            });
        }

        if (ordemsItems.get(position).getStatus().toString().equals("3")) {
            menupop.getMenu().add("Recusar").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    return false;
                }
            });
        }
        menupop.getMenu().add("Apagar").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                showPopMenuConfirmDelete(position, view);
                return false;
            }
        });
        menupop.show();
    }


    @Override
    public int getItemCount() {
        return ordemsItems.size();
    }

    @Override
    public Filter getFilter() {
        return itensFilter;
    }

    private Filter itensFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<Ordem> filteredList = new ArrayList<>();

            if (charSequence == null || charSequence.length() == 0) {
                filteredList.addAll(ordemItemsFull);
            } else {
                String filterPattern = charSequence.toString().toLowerCase().trim();
                for (Ordem item : ordemItemsFull) {
                    if (item.getSolicitante().toLowerCase().contains(filterPattern) || item.getData_solicitacao().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }

            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            ordemsItems.clear();
            ordemsItems.addAll((List) filterResults.values);
            notifyDataSetChanged();
        }
    };

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, data, label_name, status;
        CardView card_ordem;
        ImageButton btn1;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.text_id_nome_solicitante);
            label_name = itemView.findViewById(R.id.label_soli);
            data = itemView.findViewById(R.id.text_data_ordem);
            status = itemView.findViewById(R.id.text_status_id);
            card_ordem = itemView.findViewById(R.id.card_o_v);
            btn1 = itemView.findViewById(R.id.btn_options_card_ordem);
        }
    }


    private void showPopMenuConfirmDelete(int position, View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getRootView().getContext());
        builder.setTitle("Remover Ordem?");
        builder.setMessage("O item ser?? apagado e n??o poder?? ser recuperado!");
        builder.setIcon(R.drawable.ic_baseline_warning_24);
        builder.setPositiveButton("Remover", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                DeleteOrdemRequest(position);
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
            }
        });
        alerta = builder.create();
        alerta.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alerta.getWindow().setBackgroundDrawableResource(R.drawable.dialog_bg);
        alerta.show();
    }


    public void DeleteOrdemRequest(int position) {
        SharedPreferences sharedPreferences = inflater.getContext().getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
        int id_instituicao = sharedPreferences.getInt("INSTITUICAO_ID", 0);

        String url = inflater.getContext().getString(R.string.dominio) + id_instituicao
                + inflater.getContext().getString(R.string.REMOVE_ORDEM);

        RequestQueue queue = Volley.newRequestQueue(inflater.getContext());
        StringRequest jsonArrayRequest = new StringRequest(Request.Method.DELETE, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(inflater.getContext(), "Item Removido", Toast.LENGTH_SHORT).show();
                notifyItemChanged(position);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                final String statusCode = String.valueOf(error.networkResponse.statusCode);
                String body = "";
                try {
                    body = new String(error.networkResponse.data, "UTF-8");
                } catch (UnsupportedEncodingException e) {
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
        };
        queue.add(jsonArrayRequest);
    }


    private void DialogConfirme2(View view, int position) {
        final EditText input = new EditText(view.getRootView().getContext());
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getRootView().getContext());

        LinearLayout container = new LinearLayout(view.getRootView().getContext());
        container.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(50, 0, 50, 0);
        input.setGravity(android.view.Gravity.TOP | android.view.Gravity.LEFT);
        input.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        input.setLines(1);
        input.setMaxLines(1);
        input.setLayoutParams(lp);
        input.setHint("----");
        builder.setTitle("----------");
        container.addView(input, lp);
        builder.setView(container);

        builder.setPositiveButton("Enviar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                String name = input.getText().toString();
                if (!name.equals("")) {
//                    PutDataUserVolley("name", name);
                    Toast.makeText(view.getRootView().getContext(), name, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(view.getRootView().getContext(), "Valor invalido", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
            }
        });
        alerta_confirm_2 = builder.create();
        alerta_confirm_2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alerta_confirm_2.getWindow().setBackgroundDrawableResource(R.drawable.dialog_bg);
        alerta_confirm_2.show();
    }

    private void DialogConfirmFinalizar(View view, int position) {
        final EditText input = new EditText(view.getRootView().getContext());
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getRootView().getContext());

        LinearLayout container = new LinearLayout(view.getRootView().getContext());
        container.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(50, 0, 50, 0);
        input.setGravity(android.view.Gravity.TOP | android.view.Gravity.LEFT);
        input.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        input.setLines(1);
        input.setMaxLines(1);
        input.setLayoutParams(lp);
        input.setHint("----");
        builder.setTitle("Finalizar Ordem");
        container.addView(input, lp);
        builder.setView(container);

        builder.setPositiveButton("Enviar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                String name = input.getText().toString();
                if (!name.equals("")) {
//                    PutDataUserVolley("name", name);
                    Toast.makeText(view.getRootView().getContext(), name, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(view.getRootView().getContext(), "Valor invalido", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
            }
        });
        alerta_confirm_2 = builder.create();
        alerta_confirm_2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alerta_confirm_2.getWindow().setBackgroundDrawableResource(R.drawable.dialog_bg);
        alerta_confirm_2.show();
    }
}
