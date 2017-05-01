package com.gerus.pulpomatic.sharedPreferences;

import android.content.Context;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by gerus-mac on 30/04/17.
 */

public class MapsSP extends SP {

    private static final String FilE = "Maps";

    private static final String KEY_DESTINY_LAT = "dLat";
    private static final String KEY_DESTINY_LONG = "dLong";

    public MapsSP(Context poContext){
        super(poContext, FilE);

    }

    public LatLng getLastDestinyPosition(){
        Float voLatitud = getSharedFloat(KEY_DESTINY_LAT, 0);
        Float voLongitude = getSharedFloat(KEY_DESTINY_LONG, 0);
        if(voLatitud == 0 | voLongitude == 0){
            return null;
        }else {
            return new LatLng(voLatitud, voLongitude);
        }
    }

    public void setLastDestinyPosition(LatLng poPosition){
        setSharedFloat(KEY_DESTINY_LAT, (float) poPosition.latitude);
        setSharedFloat(KEY_DESTINY_LONG, (float) poPosition.longitude);
    }

    public void removeLastDestinyPosition(){
        removeShared(KEY_DESTINY_LONG, KEY_DESTINY_LONG);
    }
}
