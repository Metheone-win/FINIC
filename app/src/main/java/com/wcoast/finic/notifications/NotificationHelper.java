package com.wcoast.finic.notifications;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;

import com.wcoast.finic.R;
import com.wcoast.finic.activity.AllChats;
import com.wcoast.finic.activity.ChatActivity;
import com.wcoast.finic.activity.ChatUsersActivity;
import com.wcoast.finic.activity.NotificationAct;
import com.wcoast.finic.activity.RedeemWalletAct;
import com.wcoast.finic.utility.Constant;

public class NotificationHelper {
    private Context mContext;
    private static final String NOTIFICATION_CHANNEL_ID = "10001";
    private int notificationType;

    public NotificationHelper(Context context) {
        mContext = context;
    }

    /**
     * Create and push the notification
     */

    public void notificationType(int notificationType) {
        this.notificationType = notificationType;
    }

    public void cancelNotification(Context ctx, int notifyId) {
        String ns = Context.NOTIFICATION_SERVICE;
        NotificationManager nMgr = (NotificationManager) ctx.getSystemService(ns);
        assert nMgr != null;
        nMgr.cancel(notifyId);
    }


    public void createNotification(String title, String message) {

        NotificationCompat.Builder mBuilder;
        Intent resultIntent = null;

        switch (notificationType) {

            case Constant.REQ_CODE_REFERAL_TYPE:

                resultIntent = new Intent(mContext, NotificationAct.class);

                break;
            case Constant.REQ_CODE_CHAT_TYPE:

                resultIntent = new Intent(mContext, ChatActivity.class);

                break;

            case Constant.REQ_CODE_USER_CHAT_TYPE:

                resultIntent = new Intent(mContext, AllChats.class);
                break;

                case Constant.REQ_CODE_WALLET_TYPE:

                resultIntent = new Intent(mContext, RedeemWalletAct.class);
                break;
        }

        resultIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent resultPendingIntent = PendingIntent.getActivity(mContext,
                0, resultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        mBuilder = new NotificationCompat.Builder(mContext, NOTIFICATION_CHANNEL_ID);
        mBuilder.setSmallIcon(R.mipmap.ic_launcher);
        mBuilder.setContentTitle(title)
                .setAutoCancel(true)
                .setContentText(message)
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                .setContentIntent(resultPendingIntent);

        NotificationManager mNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "NOTIFICATION_CHANNEL_NAME", importance);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            assert mNotificationManager != null;
            mBuilder.setChannelId(NOTIFICATION_CHANNEL_ID);
            mNotificationManager.createNotificationChannel(notificationChannel);

        }

        assert mNotificationManager != null;
        mNotificationManager.notify(0, mBuilder.build());
    }
}