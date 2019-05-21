package paz.gadi.com.avfacedetection.viewmodels;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseArray;
import android.view.MenuItem;
import android.view.View;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import paz.gadi.com.avfacedetection.R;
import paz.gadi.com.avfacedetection.asynctasks.AsyncTaskInterfaces;
import paz.gadi.com.avfacedetection.asynctasks.DownloadImagesAsyncTask;
import paz.gadi.com.avfacedetection.asynctasks.FaceDetectionAsyncTask;
import paz.gadi.com.avfacedetection.base.view.MvvmView;
import paz.gadi.com.avfacedetection.fragments.*;
import paz.gadi.com.avfacedetection.models.FaceImage;
import paz.gadi.com.avfacedetection.mvvm.MainMvvm;
import paz.gadi.com.avfacedetection.utils.Utils;

public class MainViewModel extends ViewModel implements MainMvvm.ViewModel {

    private MainMvvm.View _view;

    private AllFragment _allFragment;
    private AllFragment _facesFragment;
    private AllFragment _nonFacesFragment;
    private int _numOfImagesToDetect;

    public final MutableLiveData<Boolean> isInProcess = new MutableLiveData<>();

    MainViewModel(MvvmView view){
        _view = (MainMvvm.View)view;
        _allFragment = new AllFragment();
        _facesFragment = new AllFragment();
        _nonFacesFragment = new AllFragment();
        showAllFragment();
        DownloadImagesAsyncTask task = new DownloadImagesAsyncTask();
        task.onComplete = new AsyncTaskInterfaces.DownloadImagesResponse() {
            @Override
            public void onComplete() {
                List<FaceImage> images = new ArrayList<>();
                File directory = new File(Utils.ExternalStorageDirectory);
                if (directory.exists()){
                    for (File file : directory.listFiles()) {
                        images.add(new FaceImage(file.getPath()));
                    }
                }
                updateAllFragment(images);
                _numOfImagesToDetect = images.size();
                isInProcess.setValue(false);
            }
        };
        isInProcess.setValue(true);
        task.execute();
    }

    public boolean onNavigationClick(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigation_all:
                showAllFragment();
                break;
            case R.id.navigation_faces:
                showFacesFragment();
                break;
            case R.id.navigation_non_faces:
                showNonFacesFragment();
                break;
            default:
                showAllFragment();
                break;
        }
        return true;
    }

    public void onFabClick(View view){
        clearFacesAndNonFacesImages();
        FaceDetectionAsyncTask task = new FaceDetectionAsyncTask();
        task.onComplete = new AsyncTaskInterfaces.FaceDetectionResponse() {
            @Override
            public void onComplete(ArrayList<ArrayList<FaceImage>> facesDetections) {
                updateFacesFragment(facesDetections.get(0));
                updateNonFacesFragment(facesDetections.get(1));
                String result = String.format(_view.getContext().getString(R.string.FaceDetectionResult), facesDetections.get(0).size(), _numOfImagesToDetect);
                _view.notifyUserOnCompleteDetection(result);
                isInProcess.setValue(false);
            }
        };
        isInProcess.setValue(true);
        task.execute(_view.getContext());
    }

    private void clearFacesAndNonFacesImages(){
        ((AppCompatActivity) _view.getContext()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                _facesFragment.clear();
                _nonFacesFragment.clear();
            }
        });
    }
    private void updateAllFragment(final List<FaceImage> images) {
        ((AppCompatActivity) _view.getContext()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                _allFragment.setImages(images);
            }
        });
    }
    private void updateFacesFragment(final List<FaceImage> images) {
        ((AppCompatActivity) _view.getContext()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                _facesFragment.setImages(images);
            }
        });
    }
    private void updateNonFacesFragment(final List<FaceImage> images) {
        ((AppCompatActivity) _view.getContext()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                _nonFacesFragment.setImages(images);
            }
        });
    }

    private void showAllFragment() {
        replaceFragment(_allFragment);
    }

    private void showFacesFragment() {
        replaceFragment(_facesFragment);
    }

    private void showNonFacesFragment() {
        replaceFragment(_nonFacesFragment);
    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = ((AppCompatActivity) _view.getContext()).getSupportFragmentManager().beginTransaction();
        transaction.replace(_view.getFragmentContainer(), fragment);
        transaction.commit();
    }
}
