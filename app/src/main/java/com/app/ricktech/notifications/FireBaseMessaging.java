package com.app.ricktech.notifications;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.app.ricktech.R;
import com.app.ricktech.models.NotFireModel;
import com.app.ricktech.models.UserModel;
import com.app.ricktech.preferences.Preferences;
import com.app.ricktech.tags.Tags;
import com.app.ricktech.uis.general_module.activity_home.HomeActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;


import org.greenrobot.eventbus.EventBus;

import java.util.Map;

public class FireBaseMessaging extends FirebaseMessagingService {

    private Preferences preferences = Preferences.getInstance();
    private Map<String, String> map;


    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        map = remoteMessage.getData();

        for (String key : map.keySet()) {
            Log.e("Key=", key + "_value=" + map.get(key));
        }

        if (getSession().equals(Tags.session_login)) {
            manageNotification(map);

        }
    }

    private void manageNotification(Map<String, String> map) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNewNotificationVersion(map);
        } else {
            createOldNotificationVersion(map);

        }

    }


    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);


    }

    @SuppressLint("NewApi")
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void createNewNotificationVersion(Map<String, String> map) {

        String sound_Path = "";
        if (sound_Path.isEmpty()) {
            Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            sound_Path = uri.toString();
        }

        final NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        String CHANNEL_ID = "my_channel_02";
        CharSequence CHANNEL_NAME = "my_channel_name";
        int IMPORTANCE = NotificationManager.IMPORTANCE_HIGH;

        final NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, IMPORTANCE);

        channel.setShowBadge(true);
        channel.setSound(Uri.parse(sound_Path), new AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_NOTIFICATION_EVENT)
                .setLegacyStreamType(AudioManager.STREAM_NOTIFICATION)
                .build()
        );
        builder.setChannelId(CHANNEL_ID);
        builder.setSound(Uri.parse(sound_Path), AudioManager.STREAM_NOTIFICATION);
        builder.setSmallIcon(R.mipmap.ic_launcher_round);

        String title = map.get("title");
        String body = map.get("body");
        Intent intent;
        intent = new Intent(this, HomeActivity.class);

        builder.setContentTitle(title);
        builder.setStyle(new NotificationCompat.BigTextStyle().bigText(body));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(this);
        taskStackBuilder.addNextIntent(intent);
        PendingIntent pendingIntent = taskStackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.logo1);
        builder.setLargeIcon(bitmap);
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (manager != null) {

            manager.createNotificationChannel(channel);
            manager.notify(Tags.not_tag, Tags.not_id, builder.build());
            EventBus.getDefault().post(new NotFireModel(true));

        }


    }

    private void createOldNotificationVersion(Map<String, String> map) {

        String sound_Path = "";
        if (sound_Path.isEmpty()) {
            Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            sound_Path = uri.toString();
        }

        final NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

        builder.setSound(Uri.parse(sound_Path), AudioManager.STREAM_NOTIFICATION);
        builder.setSmallIcon(R.mipmap.ic_launcher_round);

        String title = map.get("title");
        String body = map.get("body");
        Intent intent;
        intent = new Intent(this, HomeActivity.class);

        builder.setContentTitle(title);
        builder.setStyle(new NotificationCompat.BigTextStyle().bigText(body));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(this);
        taskStackBuilder.addNextIntent(intent);
        PendingIntent pendingIntent = taskStackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.logo1);
        builder.setLargeIcon(bitmap);
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (manager != null) {

            manager.notify(Tags.not_tag, Tags.not_id, builder.build());
            EventBus.getDefault().post(new NotFireModel(true));

        }



    }



    private String getSession() {
        return preferences.getSession(this);
    }



}
