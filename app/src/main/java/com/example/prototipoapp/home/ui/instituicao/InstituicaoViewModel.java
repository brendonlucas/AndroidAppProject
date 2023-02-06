package com.example.prototipoapp.home.ui.instituicao;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class InstituicaoViewModel extends ViewModel {
    private final MutableLiveData<String> mText;

    public InstituicaoViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is gallery fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
