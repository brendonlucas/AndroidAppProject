package com.example.prototipoapp.home.ui.ordem.GerenciaOrdem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.prototipoapp.R;

public class AdadpterListVeiculo extends BaseAdapter {

    Context context;
    String optionsList[];
    int icList[];
    LayoutInflater inflater;

    public AdadpterListVeiculo(Context ctx, String[] optionsList,int[] icList){
        this.context = ctx;
        this.optionsList = optionsList;
        this.icList = icList;
        inflater =LayoutInflater.from(ctx);

    }

    @Override
    public int getCount() {
        return optionsList.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.custom_item_list,null);
        TextView textView = view.findViewById(R.id.txt_name_item_list);
        ImageView imageView = view.findViewById(R.id.ic_item_list) ;
        ImageView ic_img_set = view.findViewById(R.id.ic_img_set) ;
        textView.setText(optionsList[i]);
        imageView.setImageResource(icList[i]);
        ic_img_set.setImageResource(R.drawable.ic_baseline_chevron_right_24);
        return view;
    }
}
