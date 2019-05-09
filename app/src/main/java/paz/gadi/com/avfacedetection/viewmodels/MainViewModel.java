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
import paz.gadi.com.avfacedetection.R;
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

    public final MutableLiveData<Boolean> isDetectingFaces = new MutableLiveData<>();

    MainViewModel(MvvmView view){
        _view = (MainMvvm.View)view;
        _allFragment = new AllFragment();
        _facesFragment = new AllFragment();
        _nonFacesFragment = new AllFragment();
        isDetectingFaces.setValue(false);
        showAllFragment();
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
        new AsyncTaskFaceDetection().execute();
    }

    private class AsyncTaskFaceDetection extends AsyncTask<Void, Void, String>{
        @Override
        protected void onPreExecute() {
            _allFragment.clear();
            _facesFragment.clear();
            _nonFacesFragment.clear();
            isDetectingFaces.setValue(true);
        }

        @Override
        protected String doInBackground(Void... voids) {
            int facesCounter = 0;
            int allImagesCounter = 0;
            Bitmap bitmap;
            String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
            for (int i = 0; i < Utils.IMAGE_URLS.length; i++) {
                try {
                    String filePath = String.format("%s/%d.jpg", extStorageDirectory, i);
                    InputStream in = new java.net.URL(Utils.IMAGE_URLS[i]).openStream();
                    bitmap = BitmapFactory.decodeStream(in);

                    final FaceDetector detector = new FaceDetector.Builder(_view.getContext())
                            .setTrackingEnabled(false)
                            .setLandmarkType(FaceDetector.ALL_LANDMARKS)
                            .build();

                    Frame frame = new Frame.Builder().setBitmap(bitmap).build();
                    SparseArray<Face> faces = detector.detect(frame);

                    saveImage(bitmap, filePath);

                    FaceImage faceImage = new FaceImage(filePath);
                    //if face detected
                    if (faces.size() > 0){
                        updateFacesFragment(faceImage);
                        facesCounter++;
                    }
                    else{
                        updateNonFacesFragment(faceImage);
                    }
                    allImagesCounter++;
                    updateAllFragment(faceImage);

                } catch (Exception e) {
                    Log.e("Error Message", e.getMessage());
                    e.printStackTrace();
                }
            }
            return String.format(_view.getContext().getString(R.string.FaceDetectionResult), facesCounter, allImagesCounter);
        }

        @Override
        protected void onPostExecute(String result) {
            _view.notifyUserOnCompleteDetection(result);
            isDetectingFaces.setValue(false);
        }
    }

    private void updateAllFragment(final FaceImage image) {
        ((AppCompatActivity) _view.getContext()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                _allFragment.addImage(image);
            }
        });
    }
    private void updateFacesFragment(final FaceImage image) {
        ((AppCompatActivity) _view.getContext()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                _facesFragment.addImage(image);
            }
        });
    }
    private void updateNonFacesFragment(final FaceImage image) {
        ((AppCompatActivity) _view.getContext()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                _nonFacesFragment.addImage(image);
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

    private void saveImage(Bitmap bitmap, String path) {

        File file = new File(path);
        if (file.exists()) {
            file.delete();
            file = new File(path);
        }
        try {
            OutputStream outStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, outStream);
            outStream.flush();
            outStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }




    }
}
