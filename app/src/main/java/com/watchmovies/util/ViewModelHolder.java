package com.watchmovies.util;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;

public class ViewModelHolder extends Fragment {

    private ViewModel mViewModel;

    public ViewModelHolder() {}


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }



    public static ViewModelHolder createViewModelHolder(@NonNull ViewModel viewModel) {
        ViewModelHolder holder = new ViewModelHolder();
        holder.setViewModel(viewModel);
        return holder;
    }


    public ViewModel getViewModel() {
        return mViewModel;
    }

    public void setViewModel(@NonNull ViewModel viewModel) {
        mViewModel = viewModel;
    }
}
