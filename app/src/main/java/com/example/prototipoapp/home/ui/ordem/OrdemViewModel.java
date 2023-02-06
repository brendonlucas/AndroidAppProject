package com.example.prototipoapp.home.ui.ordem;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class OrdemViewModel extends ViewModel{
    private final MutableLiveData<String> mText;

    public  OrdemViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is ordem fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
