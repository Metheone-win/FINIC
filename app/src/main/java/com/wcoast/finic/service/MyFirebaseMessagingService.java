package com.wcoast.finic.service;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.wcoast.finic.notifications.NotificationHelper;
import com.wcoast.finic.utility.Constant;
import com.wcoast.finic.utility.SessionManager;

import static com.wcoast.finic.notifications.FcmNotificationBuilder.KEY_SENDER_ID;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    String TAG = MyFirebaseMessagingService.class.getSimpleName();


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
        }

        // Check if message contains a ic_notification payload.
        if (remoteMessage.getNotification() != null) {

            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            Log.d(TAG, "onMessageReceived: tag " + remoteMessage.getNotification().getTag());

            if (remoteMessage.getNotification().getTag() == null) throw new AssertionError();

            switch (remoteMessage.getNotification().getTag()) {

                case Constant.TAG_USER_CHAT:

                    if (!new SessionManager(getApplicationContext()).getRunningStatus().get(SessionManager.KEY_IS_CHAT_USER_RUNNING) ||
                            !(new SessionManager(getApplicationContext()).getLastChatUserId() + "").equals(remoteMessage.getData().get(KEY_SENDER_ID))
                            ) {

                        showNotification(remoteMessage.getNotification().getBody(), remoteMessage.getNotification().getTitle(), Constant.REQ_CODE_USER_CHAT_TYPE);
                    }

                    break;
                case Constant.TAG_ADMIN_CHAT:

                    if (!new SessionManager(getApplicationContext()).getRunningStatus().get(SessionManager.KEY_IS_CHAT_RUNNING)) {

                        showNotification(remoteMessage.getNotification().getBody(), remoteMessage.getNotification().getTitle(), Constant.REQ_CODE_CHAT_TYPE);
                    }

                    break;

                case Constant.TAG_REDEEM_STATUS:

                    if (!new SessionManager(getApplicationContext()).getRunningStatus().get(SessionManager.KEY_IS_WALLET_RUNNING)) {

                        showNotification(remoteMessage.getNotification().getBody(), remoteMessage.getNotification().getTitle(), Constant.REQ_CODE_WALLET_TYPE);
                    }

                    break;

                default:
                    if (!new SessionManager(getApplicationContext()).getRunningStatus().get(SessionManager.KEY_IS_NOTI_RUNNING)) {

                        showNotification(remoteMessage.getNotification().getBody(), remoteMessage.getNotification().getTitle(), Constant.REQ_CODE_REFERAL_TYPE);
                    }

                    break;
            }

        }
    }


    @Override
    public void onNewToken(String s) {
        Log.e(TAG, "onNewToken: " + s, null);
        super.onNewToken(s);
    }

    private void showNotification(String NotificationBody, String NotificationTitle, int notificationType) {

        NotificationHelper notificationHelper = new NotificationHelper(this);
        notificationHelper.notificationType(notificationType);
        notificationHelper.createNotification(NotificationTitle, NotificationBody);
    }

}
