package com.gerus.pulpomatic.navigator;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.gerus.pulpomatic.services.LocationService;
import com.gerus.pulpomatic.views.maps.MapsActivity;

/**
 * Created by gerus-mac on 29/04/17.
 */

public class Navigator {

    public static Intent getService(Context poContext){
        return new Intent(poContext, LocationService.class);
    }

    public static Intent getMaps(Context poContext){
        return new Intent(poContext, MapsActivity.class);
    }

    public static Intent getTwitter(Uri poUri, String psTxt) {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, psTxt);
        shareIntent.putExtra(Intent.EXTRA_STREAM, poUri);
        shareIntent.setType("*/*");
        return shareIntent;
    }
}
