package com.gerus.pulpomatic.views.maps;

import android.content.ServiceConnection;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by gerus-mac on 29/04/17.
 */

public interface MapsInteractor {

    LatLng getCurrentPosition();
    LatLng getMarkerPosition();
    void setMarkerPosition(LatLng poLastPosition);


    ServiceConnection getLocationConnection();
}
