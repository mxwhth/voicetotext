package com.example.administrator.voicetotext;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.example.administrator.voicetotext.entity.Directory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class SplashActivity extends AppCompatActivity {
    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private static final int UI_ANIMATION_DELAY = 300;

    //权限map e.g:<READ_PHONE_STATE ,Manifest.permission.READ_PHONE_STATE>
    private static final HashMap<String, String> permissionMap = new HashMap<>();

    //自定义权限Map e.g:<READ_PHONE_STATE , MY_PERMISSIONS_READ_PHONE_STATE>
    private static final HashMap<String, Integer> myPermissionMap = new HashMap<>();

    //自定义权限码
    private static final int MY_PERMISSIONS_READ_PHONE_STATE = 91;
    private static final int MY_PERMISSIONS_WRITE_SETTINGS = 92;
    private static final int MY_PERMISSIONS_RECORD_AUDIO = 93;

    private final Handler mHideHandler = new Handler();
    private View mContentView;
    private Button mStartButton;
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };
    private View mControlsView;
    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
            mControlsView.setVisibility(View.VISIBLE);
        }
    };
    private boolean mVisible;
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };


    private Handler mMainHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mMainHandler.post(goToMainActivity);
        }
    };

    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };

    //进入下一个Activity
    Runnable goToMainActivity = new Runnable() {

        @Override
        public void run() {
            SplashActivity.this.startActivity(new Intent(SplashActivity.this,
                    MainActivity.class));
            finish();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.splash_activity);

        mVisible = true;
        mControlsView = findViewById(R.id.fullscreen_content_controls);
        mContentView = findViewById(R.id.fullscreen_content);
        mStartButton = (Button) findViewById(R.id.dummy_button);

        checkPermission();
        // Set up the user interaction to manually show or hide the system UI.
        //点击button进入主页面
        mStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonToggle();
            }
        });

        //点击其余部分
        mContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggle();
            }
        });

        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
        findViewById(R.id.dummy_button).setOnTouchListener(mDelayHideTouchListener);

        //初始化词典
        Directory.getInstance().initDirectory(getAssets());
    }

    //检查权限
    private void checkPermission() {

        //初始化PermissionMap
        initPermissionMap();


        StringBuilder sb = new StringBuilder("");

        for (String key : permissionMap.keySet()) {
            if (PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(SplashActivity.this, permissionMap.get(key))) {
                Log.i("Check Permission", key + " 权限已取得！");
            } else {
                //权限未取得
                if (ActivityCompat.shouldShowRequestPermissionRationale(SplashActivity.this, permissionMap.get(key))) {

                    // Show an explanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.
                    // TODO: 2017/3/8 告知用户为啥需要权限
                    Log.i("Check Permission", "we should explain why we need this permission!");
                } else {

                    // No explanation needed, we can request the permission.
                    Log.i("Check Permission", key + " 请求权限");

                    sb.append(permissionMap.get(key)+" ");

//                    ActivityCompat.requestPermissions(SplashActivity.this, new String[]{permissionMap.get(key)}, myPermissionMap.get(key));

                    // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                }
            }
        }

        //请求权限 91为暂时设定的成功回调码
        ActivityCompat.requestPermissions(SplashActivity.this, sb.toString().split(" "), 91);

//        checkSpecialPermission(); 检查特殊授权
    }

    @Override
    //权限请求回调函数
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 91: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    Log.i("Check Permission", "user granted the permission!");

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Log.i("Check Permission", "user denied the permission!");
                }
                return;
            }
            default: {
                break;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    //初始化permission map
    private void initPermissionMap() {
        //READ_PHONE_STATE permission
        permissionMap.put("READ_PHONE_STATE", Manifest.permission.READ_PHONE_STATE);
        myPermissionMap.put("READ_PHONE_STATE", MY_PERMISSIONS_READ_PHONE_STATE);
        //WRITE_SETTINGS permission
        permissionMap.put("WRITE_SETTINGS", Manifest.permission.WRITE_SETTINGS);
        myPermissionMap.put("WRITE_SETTINGS", MY_PERMISSIONS_WRITE_SETTINGS);
        //RECORD_AUDIO permission
        permissionMap.put("RECORD_AUDIO", Manifest.permission.RECORD_AUDIO);
        myPermissionMap.put("RECORD_AUDIO", MY_PERMISSIONS_RECORD_AUDIO);

    }

    //检查 WRITE_SETTINGS（特殊权限授权）
    private void checkSpecialPermission() {
        if (PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(SplashActivity.this, Manifest.permission.WRITE_SETTINGS)) {
            Log.i("Check Permission", "WRITE_SETTINGS 权限已取得！");
        } else {
            Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_WRITE_SETTINGS);
            intent.setData(Uri.parse("package:" + getPackageName()));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
    }

    private void buttonToggle() {
        SplashActivity.this.startActivity(new Intent(SplashActivity.this,
                MainActivity.class));
        finish();
    }

    private void toggle() {
        if (mVisible) {
            hide();
        } else {
            show();
        }
    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
//        mControlsView.setVisibility(View.GONE);
        mVisible = false;

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    @SuppressLint("InlinedApi")
    private void show() {
        // Show the system bar
        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        mVisible = true;

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
    }

    /**
     * Schedules a call to hide() in [delay] milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }
}
