package paz.gadi.com.avfacedetection.asynctasks;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import paz.gadi.com.avfacedetection.models.FaceImage;
import paz.gadi.com.avfacedetection.utils.Utils;

import static paz.gadi.com.avfacedetection.asynctasks.AsyncTaskInterfaces.*;

public class DownloadImagesAsyncTask extends AsyncTask<Void, Void, Void> {

    public DownloadImagesResponse onComplete;

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected Void doInBackground(Void... voids) {
        Bitmap bitmap;
        File directory = new File(Utils.ExternalStorageDirectory);
        boolean isDirectoryExists = directory.exists() && directory.isDirectory();
        //Create the directory if needed
        if (!isDirectoryExists){
            isDirectoryExists = directory.mkdirs();
        }
        //Download sample images from web
        if (isDirectoryExists && directory.listFiles().length == 0){
            for (int i = 0; i < Utils.IMAGE_URLS.length; i++) {
                try {
                    String filePath = String.format("%s/%d.jpg", Utils.ExternalStorageDirectory, i);
                    InputStream in = new java.net.URL(Utils.IMAGE_URLS[i]).openStream();
                    bitmap = BitmapFactory.decodeStream(in);

                    saveImage(bitmap, filePath);

                } catch (Exception e) {
                    Log.e("Error Message", e.getMessage());
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void param) {
        onComplete.onComplete();
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
