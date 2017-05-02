package com.gerus.pulpomatic.models;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;

import com.gerus.pulpomatic.R;
import com.gerus.pulpomatic.services.LocationService;

/**
 * Created by gerus-mac on 30/04/17.
 */

public class RulesVO {

    public int LOCATION_INTERVAL = LocationService.LOCATION_INTERVAL_DEFAULT;  // 10 min
    public float LOCATION_DISTANCE = LocationService.LOCATION_DISTANCE_DEFAULT;

    public int LOCATION_INTERVAL_4 = 480000, LOCATION_INTERVAL_3 = 480000, LOCATION_INTERVAL_2 = 300000, LOCATION_INTERVAL_1 = 120000; // min
    public float LOCATION_DISTANCE_4 = 30, LOCATION_DISTANCE_3 = 20, LOCATION_DISTANCE_2 = 10, LOCATION_DISTANCE_1 = 2; // meters

    public static final int MAX_VALUE = 200;

    private Context mContext;
    private double mDistance = MAX_VALUE + 1;
    private String mText = "";
    private int mColor = Color.BLACK;

    private boolean nearly = false;

    public RulesVO(Context poContext) {
        mContext = poContext;
        getValues(mDistance);
    }

    public double getDistance() {
        return mDistance;
    }

    public int getColor() {
        return mColor;
    }

    public boolean isNearly() {
        return nearly;
    }

    public void setDistance(double poDistance) {
        getValues(poDistance);
    }

    public String getText() {
        return mText;
    }

    private void getValues(double poDistance) {
        if (poDistance > MAX_VALUE) {
            mText = mContext.getString(R.string.distance_200);
            mColor = ContextCompat.getColor(mContext, R.color.circle_level_5);

            boolean isLowerDouble = (MAX_VALUE * 2) < poDistance;
            if (isLowerDouble) {
                LOCATION_INTERVAL = LOCATION_INTERVAL_4;
                LOCATION_DISTANCE = LOCATION_DISTANCE_4;
            }
            nearly = false;
        } else if (poDistance <= MAX_VALUE && poDistance > 100) {
            mText = mContext.getString(R.string.distance_100);
            mColor = ContextCompat.getColor(mContext, R.color.circle_level_4);
            LOCATION_INTERVAL = LOCATION_INTERVAL_4;
            LOCATION_DISTANCE = LOCATION_DISTANCE_4;
            nearly = false;
        } else if (poDistance <= 100 && poDistance > 50) {
            mText = mContext.getString(R.string.distance_50);
            mColor = ContextCompat.getColor(mContext, R.color.circle_level_3);
            LOCATION_INTERVAL = LOCATION_INTERVAL_3;
            LOCATION_DISTANCE = LOCATION_DISTANCE_3;
            nearly = false;
        } else if (poDistance <= 50 && poDistance > 10) {
            mText = mContext.getString(R.string.distance_10);
            mColor = ContextCompat.getColor(mContext, R.color.circle_level_2);
            LOCATION_INTERVAL = LOCATION_INTERVAL_2;
            LOCATION_DISTANCE = LOCATION_DISTANCE_2;
            nearly = false;
        } else if (poDistance < 10) {
            mText = mContext.getString(R.string.distance_0);
            mColor = ContextCompat.getColor(mContext, R.color.circle_level_1);
            LOCATION_INTERVAL = LOCATION_INTERVAL_1;
            LOCATION_DISTANCE = LOCATION_DISTANCE_1;
            nearly = true;
        }
        if (poDistance > MAX_VALUE) poDistance = MAX_VALUE + 1;
        this.mDistance = poDistance;
    }
}
