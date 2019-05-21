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

    private List<FaceImage> imagesList;
    private LayoutInflater layoutInflater;

    public ImagesAdapter() {
        imagesList = new ArrayList<>();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(viewGroup.getContext());
        }
        ImageRowItemBinding binding =
                DataBindingUtil.inflate(layoutInflater, R.layout.image_row_item, viewGroup, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int i) {
        holder.binding.setFaceImage(imagesList.get(i));
    }

    @Override
    public int getItemCount() {
        return imagesList.size();
    }

    public void setImages(List<FaceImage> images) {
        this.imagesList = images;
        notifyDataSetChanged();
    }

    public void clear() {
        this.imagesList.clear();
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
