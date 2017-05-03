package com.gerus.pulpomatic.base;

import android.content.Context;

import com.gerus.pulpomatic.sharedPreferences.MapsSP;

/**
 * Created by gerus-mac on 29/04/17.
 */

public abstract class BaseInteractorImpl {
    protected Context mContext;
    protected MapsSP mMapsSP;

    public BaseInteractorImpl(Context poContext){
        mContext = poContext;
        mMapsSP = new MapsSP(mContext);
    }

}
