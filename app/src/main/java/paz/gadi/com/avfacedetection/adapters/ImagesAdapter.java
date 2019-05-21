package paz.gadi.com.avfacedetection.adapters;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import paz.gadi.com.avfacedetection.R;
import paz.gadi.com.avfacedetection.databinding.ImageRowItemBinding;
import paz.gadi.com.avfacedetection.models.FaceImage;

public class ImagesAdapter extends RecyclerView.Adapter<ImagesAdapter.MyViewHolder>{

    private List<FaceImage> _imagesList;
    private LayoutInflater _layoutInflater;

    public ImagesAdapter() {
        _imagesList = new ArrayList<>();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (_layoutInflater == null) {
            _layoutInflater = LayoutInflater.from(viewGroup.getContext());
        }
        ImageRowItemBinding binding =
                DataBindingUtil.inflate(_layoutInflater, R.layout.image_row_item, viewGroup, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int i) {
        holder.binding.setFaceImage(_imagesList.get(i));
    }

    @Override
    public int getItemCount() {
        return _imagesList.size();
    }

    public void setImages(List<FaceImage> images) {
        this._imagesList = images;
        notifyDataSetChanged();
    }

    public void clear() {
        this._imagesList.clear();
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private final ImageRowItemBinding binding;

        public MyViewHolder(final ImageRowItemBinding itemBinding) {
            super(itemBinding.getRoot());
            this.binding = itemBinding;
        }
    }
}
