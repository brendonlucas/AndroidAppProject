package com.example.prototipoapp.home.ui.funcionario;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.prototipoapp.databinding.FragmentFuncionarioBinding;


public class FuncionarioFragment extends Fragment {

    private FragmentFuncionarioBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        FuncionarioViewModel galleryViewModel =
                new ViewModelProvider(this).get(FuncionarioViewModel.class);

        binding = FragmentFuncionarioBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}