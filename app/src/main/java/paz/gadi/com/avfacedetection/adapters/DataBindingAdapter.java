package paz.gadi.com.avfacedetection.adapters;

import android.databinding.BindingMethod;
import android.databinding.BindingMethods;
import android.support.design.widget.BottomNavigationView;

@BindingMethods({
        @BindingMethod(
                type = BottomNavigationView.class,
                attribute = "app:onNavigationItemSelected",
                method = "setOnNavigationItemSelectedListener"
        ),
})
public class DataBindingAdapter {
}
