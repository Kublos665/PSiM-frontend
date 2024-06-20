package com.example.myapplication.ui.rankings;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class GalleryViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public GalleryViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("1. krzys\n2. grzes\n3. andrzej");
    }

    public LiveData<String> getText() {
        return mText;
    }
}