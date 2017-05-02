package com.gerus.pulpomatic.views.maps;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import com.gerus.pulpomatic.models.RulesVO;
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
    private ServiceConnection mServiceConnection;

    public MapsInteractorImpl(MapsPresenterImpl poPresenter) {
        super(poPresenter.getContext());
        mPresenter = poPresenter;
        mLocationCallback = this;
    }

    @Override
    public LatLng getCurrentPosition() {
        return mLocationService==null?null:mLocationService.getPosition();
    }

    @Override
    public LatLng getDestinyMarker() {
        return mMapsSP.getLastDestinyPosition();
    }

    @Override
    public void getDistance() {
        mLocationService.getDistance();
    }

    @Override
    public void setDestinyMarker(LatLng poLastPosition) {
        mMapsSP.setLastDestinyPosition(poLastPosition);
        if(mLocationService!=null) mLocationService.setDestiny(poLastPosition);
    }

    @Override
    public void removeDestinyMarker() {
        mMapsSP.removeLastDestinyPosition();
        if(mLocationService!=null) mLocationService.setDestiny(null);
    }

    @Override
    public void getLastKnowPosition() {
        if(mLocationService!=null) mLocationService.getLastPosition();
    }

    @Override
    public ServiceConnection getLocationConnection() {
        if(mServiceConnection ==null){
            mServiceConnection = new ServiceConnection() {

                @Override
                public void onServiceConnected(ComponentName className, IBinder service) {
                    LocationService.LocalBinder binder = (LocationService.LocalBinder) service;
                    mLocationService = binder.getService(mLocationCallback);
                    Log.e("Gerus", "onServiceConnected");
                }

                @Override
                public void onServiceDisconnected(ComponentName arg0) {
                    Log.e("Gerus", "onServiceDisconnected");
                }
            };
        }
        return mServiceConnection;
    }

    @Override
    public void setActivityLive(boolean poActivityLive) {
        if(mLocationService!=null) mLocationService.onActivityLive(poActivityLive);
    }

    @Override
    public void onLocationChange(LatLng poLocation) {
        Log.d("MapsInterImpl","Llegue");
        mPresenter.onPositionChange(poLocation);
    }

    @Override
    public void onProviderDisabled(String psProvider) {

    }

    @Override
    public void onDistance(RulesVO poRules) {
        mPresenter.onDistance(poRules);
    }


}
