package paz.gadi.com.avfacedetection.viewmodels;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import paz.gadi.com.avfacedetection.mvvm.MainMvvm;

public class MainViewModelFactory implements ViewModelProvider.Factory {
    private MainMvvm.View _view;

    public MainViewModelFactory(MainMvvm.View view) {
        _view = view;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new MainViewModel(_view);
    }
}
