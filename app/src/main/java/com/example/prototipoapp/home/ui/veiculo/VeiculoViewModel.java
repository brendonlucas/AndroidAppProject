package com.example.prototipoapp.home.ui.veiculo;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class VeiculoViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public  VeiculoViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is veiculo fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
