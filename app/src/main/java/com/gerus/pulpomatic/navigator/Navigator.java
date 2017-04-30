package com.gerus.pulpomatic.navigator;

import android.content.Context;
import android.content.Intent;

import com.gerus.pulpomatic.services.LocationService;

/**
 * Created by gerus-mac on 29/04/17.
 */

public class Navigator {

    public static Intent getService(Context poContext){
        return new Intent(poContext, LocationService.class);
    }

}
