package paz.gadi.com.avfacedetection.mvvm;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.view.MenuItem;

import paz.gadi.com.avfacedetection.base.view.MvvmView;

public class MainMvvm {

    public interface View extends MvvmView {
        Context getContext();

        @IdRes
        int getFragmentContainer();

        void notifyUserOnCompleteDetection(String text);
    }

    public interface ViewModel {

        boolean onNavigationClick(@NonNull MenuItem item);
    }
}
