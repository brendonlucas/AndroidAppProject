package com.example.prototipoapp.home.ui.funcionario.Adapters;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prototipoapp.R;
import com.example.prototipoapp.home.ui.funcionario.User;
import com.example.prototipoapp.home.ui.veiculo.VAdapter;
import com.example.prototipoapp.home.ui.veiculo.VeiculoItem;
import com.example.prototipoapp.home.ui.veiculo.veiculoDetail.EditVeiculoActivity;
import com.example.prototipoapp.home.ui.veiculo.veiculoDetail.VeiculoDetail;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class FuncAdapter extends RecyclerView.Adapter<FuncAdapter.ViewHolder> implements Filterable {
    LayoutInflater inflater;
    List<User> userItems;
    List<User> userItemsFull;
    AlertDialog alerta;
    public static String SHARED_PREFERENCES = "DadosUser";

    public FuncAdapter(Context ctx, List<User> userItems) {
        this.inflater = LayoutInflater.from(ctx);
        this.userItems = userItems;
        userItemsFull = new ArrayList<>(userItems);
    }



    @NonNull
    @Override
    public FuncAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.layout_card_user, parent, false);
        return new FuncAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FuncAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.name.setText(userItems.get(position).getName());
        holder.cargo.setText(userItems.get(position).getCargo());
//        Picasso.get().load(userItems.get(position).getImageUrl()).into(holder.imageUser);
        holder.imageUser.setImageResource(R.drawable.icc_user_f);

        holder.imageUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(view.getContext(), VeiculoDetail.class);
//                intent.putExtra("name_veiculo", veiculoItems.get(position).getName());
//                intent.putExtra("id_veiculo", veiculoItems.get(position).getPk());
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                view.getContext().startActivity(intent);
            }
        });
        holder.btnMenuV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopUpMenu(view, position);
            }
        });
        holder.card_user.setAnimation(AnimationUtils.loadAnimation(inflater.getContext(), R.anim.slide_in));

    }

    @Override
    public int getItemCount() {
        return userItems.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, cargo;
        ImageView imageUser;
        ImageButton btnMenuV;
        CardView card_user;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            card_user = itemView.findViewById(R.id.card_user_func);
            name = itemView.findViewById(R.id.txt_name_user_card);
            cargo = itemView.findViewById(R.id.txt_cargo_user_card);
            imageUser = itemView.findViewById(R.id.img_user_profile_card);
            btnMenuV = itemView.findViewById(R.id.btn_options_card);
        }
    }
    @Override
    public Filter getFilter() {
        return itensFilter;
    }

    private Filter itensFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<User> filteredList = new ArrayList<>();

            if (charSequence == null || charSequence.length() == 0) {
                filteredList.addAll(userItemsFull);
            } else {
                String filterPattern = charSequence.toString().toLowerCase().trim();
                for (User item : userItemsFull) {
                    if (item.getName().toLowerCase().contains(filterPattern) || item.getCargo().toLowerCase().contains(filterPattern)) {
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
            userItems.clear();
            userItems.addAll((List) filterResults.values);
            notifyDataSetChanged();
        }
    };

    private void showPopUpMenu(View view, int position) {
        final PopupMenu menupop = new PopupMenu(view.getContext(), view);
        menupop.getMenu().add("Editar").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
//                Intent intent = new Intent(view.getContext(), EditVeiculoActivity.class);
//                intent.putExtra("id_veiculo", userItems.get(position).getPk());
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                view.getContext().startActivity(intent);
//                PutVeiculoVolley("r", "d", position, view);
                return false;
            }
        });
        menupop.getMenu().add("Apagar").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
//                showPopMenuConfirmDelete(view, position);
//                Snackbar.make(view,"Retwittado com sucesso!", Snackbar.LENGTH_LONG).show();
                return false;
            }
        });
        menupop.show();
    }

    private void showPopMenuConfirmDelete(View view, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getRootView().getContext());
        builder.setTitle("Remover Funcionario?");
        builder.setMessage("O item será apagado e não poderá ser recuperado!");
        builder.setIcon(R.drawable.ic_baseline_warning_24);
        builder.setPositiveButton("Remover", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
//                DeleteVeiculoVolley(position, view);
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
}
