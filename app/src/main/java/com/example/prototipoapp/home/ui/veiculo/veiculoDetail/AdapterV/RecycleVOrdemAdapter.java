package com.example.prototipoapp.home.ui.veiculo.veiculoDetail.AdapterV;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prototipoapp.R;
import com.example.prototipoapp.home.ui.ordem.Ordem;
import com.example.prototipoapp.home.ui.ordem.ordemDetail.OrdemDetailActivity;
import com.example.prototipoapp.home.ui.veiculo.VAdapter;
import com.example.prototipoapp.home.ui.veiculo.VeiculoItem;
import com.example.prototipoapp.home.ui.veiculo.veiculoDetail.VeiculoDetail;

import java.util.ArrayList;
import java.util.List;

public class RecycleVOrdemAdapter extends RecyclerView.Adapter<RecycleVOrdemAdapter.ViewHolder> implements Filterable {
    public static String SHARED_PREFERENCES = "DadosUser";
    LayoutInflater inflater;
    List<Ordem> ordemsItems;
    List<Ordem> ordemItemsFull;

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
        holder.status.setText(ordemsItems.get(position).getStatus().toString());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), OrdemDetailActivity.class);
                intent.putExtra("id_ordem", ordemsItems.get(position).getPk());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                view.getContext().startActivity(intent);
            }
        });

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


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.text_id_nome_solicitante);
            label_name = itemView.findViewById(R.id.label_soli);
            data = itemView.findViewById(R.id.text_data_ordem);
            status = itemView.findViewById(R.id.text_status_id);

        }
    }
}
