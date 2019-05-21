package paz.gadi.com.avfacedetection.asynctasks;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.util.SparseArray;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;

import java.io.File;
import java.util.ArrayList;

import paz.gadi.com.avfacedetection.asynctasks.AsyncTaskInterfaces.FaceDetectionResponse;
import paz.gadi.com.avfacedetection.models.FaceImage;
import paz.gadi.com.avfacedetection.utils.Utils;

public class FaceDetectionAsyncTask extends AsyncTask<Context, Void, ArrayList<ArrayList<FaceImage>>> {

    public FaceDetectionResponse onComplete;

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected ArrayList<ArrayList<FaceImage>> doInBackground(Context... params) {
        Context context = params[0];
        ArrayList<ArrayList<FaceImage>> result = new ArrayList<>();
        result.add(new ArrayList<FaceImage>());
        result.add(new ArrayList<FaceImage>());
        File directory = new File(Utils.ExternalStorageDirectory);
        if (directory.exists()){
            try {
                for (File file : directory.listFiles()) {
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                    Bitmap bitmap = BitmapFactory.decodeFile(file.getPath(), options);

                    final FaceDetector detector = new FaceDetector.Builder(context)
                            .setTrackingEnabled(false)
                            .setLandmarkType(FaceDetector.ALL_LANDMARKS)
                            .build();

                    Frame frame = new Frame.Builder().setBitmap(bitmap).build();
                    SparseArray<Face> faces = detector.detect(frame);

                    FaceImage faceImage = new FaceImage(file.getPath());
                    //if face detected
                    if (faces.size() > 0) {
                        result.get(0).add(faceImage);
                    } else {
                        result.get(1).add(faceImage);
                    }
                }
            }catch (Exception ex){
                Log.e("Error Message", ex.getMessage());
                ex.printStackTrace();
            }
        }
        return result;
    }

    @Override
    protected void onPostExecute(ArrayList<ArrayList<FaceImage>> result) {
        onComplete.onComplete(result);
    }
}
