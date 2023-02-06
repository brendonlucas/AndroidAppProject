package com.example.prototipoapp.home.ui.funcionario;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class FuncionarioViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public FuncionarioViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is funciuonario fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}