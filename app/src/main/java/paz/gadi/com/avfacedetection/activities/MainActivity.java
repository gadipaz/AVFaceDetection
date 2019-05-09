package paz.gadi.com.avfacedetection.activities;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import paz.gadi.com.avfacedetection.R;
import paz.gadi.com.avfacedetection.databinding.ActivityMainBinding;
import paz.gadi.com.avfacedetection.mvvm.MainMvvm;
import paz.gadi.com.avfacedetection.viewmodels.MainViewModel;
import paz.gadi.com.avfacedetection.viewmodels.MainViewModelFactory;

public class MainActivity extends AppCompatActivity implements MainMvvm.View {

    private boolean isActive;
    private MainViewModel mViewModel;
    private final int MY_PERMISSIONS_REQUEST_READ_WRITE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mViewModel = ViewModelProviders.of(this, new MainViewModelFactory(this)).get(MainViewModel.class);
        binding.setVm(mViewModel);
        binding.setLifecycleOwner(this);

        isStoragePermissionGranted();
    }

    public void isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {

            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                    checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                //Permission is granted
            } else {
                //Permission is revoked so ask for permission
                ActivityCompat.requestPermissions(this, new String[]{ Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_WRITE);
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_WRITE:
                boolean isPerpermissionForAllGranted = false;
                if (grantResults.length > 0 && permissions.length == grantResults.length) {
                    for (int i = 0; i < permissions.length; i++){
                        if (grantResults[i] == PackageManager.PERMISSION_GRANTED){
                            isPerpermissionForAllGranted = true;
                        }else{
                            isPerpermissionForAllGranted = false;
                        }
                    }
                } else {
                    isPerpermissionForAllGranted = false;
                }
                if(!isPerpermissionForAllGranted){
                    finish();
                }
                break;
        }
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public int getFragmentContainer() {
        return R.id.main_container;
    }

    //if App is on foreground display alert,
    //otherwise generate local notification
    @Override
    public void notifyUserOnCompleteDetection(String text) {
        //isActive means the app is on foreground
        if (isActive) {
            displayAlert(text);
        }
        else{
            displayNotification(text);
        }
    }
    private void displayAlert(String text){
        new AlertDialog.Builder(this)
                .setTitle(R.string.Result)
                .setMessage(text)
                .setPositiveButton(R.string.Awesome, null)
                .setIcon(android.R.drawable.ic_dialog_info)
                .show();
    }
    private void displayNotification(String text) {
        try {
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getContext(), "MyNotification");

            notificationBuilder.setAutoCancel(true)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setSmallIcon(android.R.drawable.ic_dialog_info)
                    .setWhen(System.currentTimeMillis())
                    .setContentTitle(getString(R.string.Result))
                    .setContentText(text);
            NotificationManager notificationManager = (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            {
                String channelId = "Your_channel_id";
                NotificationChannel channel = new NotificationChannel(channelId,
                        "Face Detection Ended", NotificationManager.IMPORTANCE_HIGH);
                notificationManager.createNotificationChannel(channel);
                notificationBuilder.setChannelId(channelId);
            }

            notificationManager.notify(1, notificationBuilder.build());
        }
        catch(Exception ex){
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        isActive = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        isActive = false;
    }
}
