package paz.gadi.com.avfacedetection.models;

import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class FaceImage {

    private String _imageUrl;

    public FaceImage(String path){
        _imageUrl = path;
    }
    @BindingAdapter("imageUrl")
    public static void loadImage(ImageView view, String imageUrl) {
        Glide.with(view.getContext())
                .load(imageUrl)
                .into(view);
    }

    public String getImageUrl() {
        return _imageUrl;
    }

}
