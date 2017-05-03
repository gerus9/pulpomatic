package com.gerus.pulpomatic.views.splash;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.gerus.pulpomatic.BuildConfig;
import com.gerus.pulpomatic.R;
import com.gerus.pulpomatic.dialogs.Dialogs;
import com.gerus.pulpomatic.navigator.Navigator;
import com.gerus.pulpomatic.views.maps.MapsActivity;
import com.gerus.pulpomatic.views.maps.MapsPresenterImpl;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;

import butterknife.ButterKnife;

/**
 * Created by gerus-mac on 01/05/17.
 */

public class SplashActivity extends AppCompatActivity {

    private Activity mActivity;
    private Dialogs mDialogs;
    private static final int MY_PERMISSIONS = 100;
    private static final int DELAY = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_layout);
        mDialogs = new Dialogs(this);
        mActivity = this;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                prcCheckPermission();
            }
        },DELAY);
    }

    private void prcCheckPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            mDialogs.showInfoMessage(getString(R.string.permission), new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    ActivityCompat.requestPermissions(mActivity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS);
                }
            });
        } else {
            fncGoMap();
        }
    }

    private void fncGoMap() {
        startActivity(Navigator.getMaps(this));
        finish();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case MY_PERMISSIONS: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("onMapReady","PERMISSION_GRANTED");
                    fncGoMap();
                } else {
                    Log.d("onMapReady","PERMISSION_DENIED");
                    mDialogs.showNegativeMessage(getString(R.string.error_permissions), new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            startActivity(new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + BuildConfig.APPLICATION_ID)));
                            mActivity.finish();
                        }
                    });
                }
            }
        }
    }

}
