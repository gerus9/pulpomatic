package com.gerus.pulpomatic.views.maps;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import com.gerus.pulpomatic.base.BaseInteractorImpl;
import com.gerus.pulpomatic.services.LocationService;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by gerus-mac on 29/04/17.
 */

public class MapsInteractorImpl extends BaseInteractorImpl implements MapsInteractor, LocationService.LocationCallback  {
    private MapsPresenter mPresenter;
    private LocationService.LocationCallback mLocationCallback;
    private LocationService mLocationService;

    public MapsInteractorImpl(MapsPresenterImpl poPresenter) {
        super();
        mPresenter = poPresenter;
        mLocationCallback = this;
    }

    @Override
    public LatLng getCurrentPosition() {
        return mLocationService.getPosition();
    }

    @Override
    public LatLng getMarkerPosition() {
        return null;
    }

    @Override
    public void setMarkerPosition(LatLng poLastPosition) {

    }

    @Override
    public ServiceConnection getLocationConnection() {
        return new ServiceConnection() {

            @Override
            public void onServiceConnected(ComponentName className, IBinder service) {
                LocationService.LocalBinder binder = (LocationService.LocalBinder) service;
                mLocationService = binder.getService(mLocationCallback);
            }

            @Override
            public void onServiceDisconnected(ComponentName arg0) {

            }
        };
    }

    @Override
    public void onLocationChange(LatLng poLocation) {
        Log.d("MapsInterImpl","Llegue");
        mPresenter.onPositionChange(poLocation);
    }

    @Override
    public void onProviderDisabled(String psProvider) {

    }
}
