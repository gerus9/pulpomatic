package com.gerus.pulpomatic.notifications;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;

import com.gerus.pulpomatic.R;
import com.gerus.pulpomatic.views.maps.MapsActivity;

/**
 * Created by Gerus on 28/01/2015.
 */
@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
public class Notifications {
    private NotificationManager mNotificationManager;
    private Notification.Builder mNotificationBuilder;
    private Context mContext;
    private int ID_STATUS = 0;


    public Notifications(Context poContext) {
        mContext = poContext;
        mNotificationManager = (NotificationManager) poContext.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationBuilder = new Notification.Builder(poContext);
    }

    public void prcStatus(String psMsg) {

        PendingIntent voPendientMap = PendingIntent.getActivity(mContext, ID_STATUS, new Intent(mContext, MapsActivity.class), PendingIntent.FLAG_CANCEL_CURRENT);


        mNotificationBuilder.setSmallIcon(R.drawable.ic_notification)
                .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ic_marker))
                .setContentTitle(mContext.getString(R.string.app_name))
                .setContentText(psMsg)
                .setAutoCancel(true)
                .setContentIntent(voPendientMap)
                .setDefaults(Notification.DEFAULT_ALL)
                .setTicker(psMsg);

        Notification notification = mNotificationBuilder.build();
        notification.flags = Notification.FLAG_ONLY_ALERT_ONCE;
        mNotificationManager.notify(ID_STATUS, notification);
    }

    public void prcRemoveNotificationStatus(){
        mNotificationManager.cancel(ID_STATUS);
    }


}