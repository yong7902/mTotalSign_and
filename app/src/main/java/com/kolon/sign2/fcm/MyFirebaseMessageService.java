package com.kolon.sign2.fcm;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.kolon.sign2.R;
import com.kolon.sign2.activity.SchemeActivity;
import com.kolon.sign2.utils.Constants;
import com.kolon.sign2.utils.SharedPreferenceManager;

public class MyFirebaseMessageService extends FirebaseMessagingService {
    /**
     * Token값 신규 or 갱신 시 호출
     * */
    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        SharedPreferenceManager pref = SharedPreferenceManager.getInstance(getApplicationContext());
        String savedPushTokenValue = pref.getStringPreference(Constants.PREF_TOKEN_KEY);
        if (null == savedPushTokenValue || savedPushTokenValue.isEmpty()) {
            pref.setStringPreference(Constants.PREF_TOKEN_KEY, token);
        } else {
            if (!savedPushTokenValue.equalsIgnoreCase(token)) {
                pref.setStringPreference(Constants.PREF_TOKEN_KEY, token);
            }
        }
    }

    /**
     * push이벤트 전달
     * */
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        String title = remoteMessage.getData().get("title");
        String contents = remoteMessage.getData().get("body");
        String linkUrl = remoteMessage.getData().get("linkUrl");
        String badgeCnt = remoteMessage.getData().get("badge");
        Log.w("push onMessageReceived", "title = " + title + ", contents = " + contents + ", linkUrl = " + linkUrl + ", badgeCnt = " + badgeCnt );
        sendNotification(getApplicationContext(), title, contents, linkUrl, badgeCnt);
    }


    public void sendNotification(Context context, String title, String content, String linkUrl, String badgeCnt) {
        int badgeCount = 0;
        if (null != badgeCnt && !"null".equalsIgnoreCase(badgeCnt) && !TextUtils.isEmpty(badgeCnt)) {
            try{
                badgeCount = Integer.parseInt(badgeCnt);
            } catch (NumberFormatException e) {
                badgeCount= 0;
            }
        }
        Log.w("11111111", "badgeCount = " + badgeCount);

        Intent intent = new Intent(context, SchemeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("pushData", linkUrl);

        int pendingIntentRequestCode = (int) (Math.random() * 10000) + 1;
        PendingIntent contentIntent = PendingIntent.getActivity(context, pendingIntentRequestCode, intent, 0);

        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = nm.getNotificationChannel("testkpl_channel_id");
            mChannel = new NotificationChannel("kolon_total_sign_id", "kolon_total_sign_channel", NotificationManager.IMPORTANCE_HIGH);
            mChannel.enableVibration(true);
            mChannel.setDescription("total_sign_push_channel");
            mChannel.setLightColor(Color.RED);
            nm.createNotificationChannel(mChannel);
            builder = new NotificationCompat.Builder(context, "kolon_total_sign_id");
        } else {
            builder = new NotificationCompat.Builder(context);
        }

        builder.setContentTitle(title)
                .setContentText(content)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true)
                .setWhen(System.currentTimeMillis())
                .setVibrate(new long[]{0,1500})
                .setOnlyAlertOnce(false)
                .setNumber(badgeCount)
                .setContentIntent(contentIntent)
                .setDefaults(Notification.DEFAULT_SOUND);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            builder.setCategory(Notification.CATEGORY_MESSAGE)
                    .setPriority(Notification.PRIORITY_HIGH)
                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC);
        }
        int notiId = (int)(Math.random() * 10000);
        nm.notify(notiId, builder.build());
    }

}
