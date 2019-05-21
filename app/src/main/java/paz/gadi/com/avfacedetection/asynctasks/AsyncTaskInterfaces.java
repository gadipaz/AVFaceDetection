package paz.gadi.com.avfacedetection.asynctasks;

import java.util.ArrayList;

import paz.gadi.com.avfacedetection.models.FaceImage;

public class AsyncTaskInterfaces {
    public interface DownloadImagesResponse{
        void onComplete();
    }

    //faceDetections[0]: Faces
    //faceDetections[1]: Non Faces
    public interface FaceDetectionResponse{
        void onComplete(ArrayList<ArrayList<FaceImage>> facesDetections);
    }
}
