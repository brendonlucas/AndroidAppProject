package com.example.prototipoapp.home.ui.veiculo;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.prototipoapp.R;
import com.example.prototipoapp.home.ui.veiculo.veiculoDetail.EditVeiculoActivity;
import com.example.prototipoapp.home.ui.veiculo.veiculoDetail.VeiculoDetail;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class VAdapter extends RecyclerView.Adapter<VAdapter.ViewHolder> implements Filterable {
    LayoutInflater inflater;
    List<VeiculoItem> veiculoItems;
    List<VeiculoItem> veiculoItemsFull;
    AlertDialog alerta;
    public static String SHARED_PREFERENCES = "DadosUser";


    public VAdapter(Context ctx, List<VeiculoItem> veiculoItems) {
        this.inflater = LayoutInflater.from(ctx);
        this.veiculoItems = veiculoItems;
        veiculoItemsFull = new ArrayList<>(veiculoItems);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.layout_card_item_veiculo, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.name.setText(veiculoItems.get(position).getName());
        holder.placa.setText(veiculoItems.get(position).getPlaca());
        if (veiculoItems.get(position).getTipo() > 0) {
            holder.tipo.setText("carro");
        }
//        holder.imageCar.setImageResource(R.drawable.icon_veiculo);
        Picasso.get().load(veiculoItems.get(position).getImageUrl()).into(holder.imageCar);
        holder.imageCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), VeiculoDetail.class);
                intent.putExtra("name_veiculo", veiculoItems.get(position).getName());
                intent.putExtra("id_veiculo", veiculoItems.get(position).getPk());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                view.getContext().startActivity(intent);
            }
        });

        holder.btnMenuV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopUpMenu(view, position);
            }
        });
        holder.card_veiculo.setAnimation(AnimationUtils.loadAnimation(inflater.getContext(), R.anim.slide_in));
    }


    @Override
    public int getItemCount() {
        return veiculoItems.size();
    }

    @Override
    public Filter getFilter() {
        return itensFilter;
    }

    private Filter itensFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<VeiculoItem> filteredList = new ArrayList<>();

            if (charSequence == null || charSequence.length() == 0) {
                filteredList.addAll(veiculoItemsFull);
            } else {
                String filterPattern = charSequence.toString().toLowerCase().trim();
                for (VeiculoItem item : veiculoItemsFull) {
                    if (item.getName().toLowerCase().contains(filterPattern) || item.getPlaca().toLowerCase().contains(filterPattern)) {
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
            veiculoItems.clear();
            veiculoItems.addAll((List) filterResults.values);
            notifyDataSetChanged();
        }
    };

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, placa, tipo;
        ImageView imageCar;
        ImageButton btnMenuV;
        CardView card_veiculo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            card_veiculo = itemView.findViewById(R.id.card_veiculo_id);
            name = itemView.findViewById(R.id.card_name_veiculo_id);
            placa = itemView.findViewById(R.id.card_placa_veiculo_id);
            tipo = itemView.findViewById(R.id.card_tipo_veiculo_id);
            imageCar = itemView.findViewById(R.id.card_image_veiculo_id);
            btnMenuV = itemView.findViewById(R.id.btn_menu_v);
        }
    }

    private void showPopUpMenu(View view, int position) {
        final PopupMenu menupop = new PopupMenu(view.getContext(), view);
        menupop.getMenu().add("Editar").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = new Intent(view.getContext(), EditVeiculoActivity.class);
                intent.putExtra("id_veiculo", veiculoItems.get(position).getPk());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                view.getContext().startActivity(intent);

//                PutVeiculoVolley("r", "d", position, view);
                return false;
            }
        });
        menupop.getMenu().add("Apagar").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                showPopMenuConfirmDelete(view, position);
//                Snackbar.make(view,"Retwittado com sucesso!", Snackbar.LENGTH_LONG).show();
                return false;
            }
        });
        menupop.show();
    }

    private void showPopMenuConfirmDelete(View view, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getRootView().getContext());
        builder.setTitle("Remover Veiculo?");
        builder.setMessage("O item será apagado e não poderá ser recuperado!");
        builder.setIcon(R.drawable.ic_baseline_warning_24);
        builder.setPositiveButton("Remover", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                DeleteVeiculoVolley(position, view);
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

    public void DeleteVeiculoVolley(int position, View view) {
        SharedPreferences sharedPreferences = view.getContext().getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);

        RequestQueue MyRequestQueue = Volley.newRequestQueue(inflater.getContext());
        String url = "https://brendonlulucas.pythonanywhere.com/" + veiculoItems.get(position).getPk() + "/API/APIveiculo_detail/";
        StringRequest MyStringRequest = new StringRequest(Request.Method.DELETE, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(view.getContext(), veiculoItems.get(position).getPk()+"" , Toast.LENGTH_SHORT).show();
                veiculoItems.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, veiculoItems.size());
                Snackbar.make(view, "Removido com sucesso!", Snackbar.LENGTH_LONG).show();
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
                if (statusCode.equals("400")) {
                    Toast.makeText(inflater.getContext(), body, Toast.LENGTH_SHORT).show();
                }
                System.out.println(body);
            }
        }) {
            @Override       //Send Header
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", "Token " + sharedPreferences.getString("TOKEN", "null"));

                return params;
            }
        };
        MyRequestQueue.add(MyStringRequest);
    }
}
