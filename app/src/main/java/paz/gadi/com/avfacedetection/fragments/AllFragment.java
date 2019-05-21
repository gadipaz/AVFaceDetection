package paz.gadi.com.avfacedetection.fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import paz.gadi.com.avfacedetection.R;
import paz.gadi.com.avfacedetection.adapters.ImagesAdapter;
import paz.gadi.com.avfacedetection.databinding.FragmentAllBinding;
import paz.gadi.com.avfacedetection.models.FaceImage;

public class AllFragment extends Fragment {

    public FragmentAllBinding binding;
    private ImagesAdapter _magesAdapter;

    public AllFragment(){
        _magesAdapter = new ImagesAdapter();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_all, container, false);
        binding.setLifecycleOwner(this);

        binding.recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        binding.recyclerView.setAdapter(_magesAdapter);

        return binding.getRoot();
    }

    public void setImages(List<FaceImage> images) {
        _magesAdapter.setImages(images);
    }

    public void clear() {
        _magesAdapter.clear();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}
