package com.example.user.goeat_3;

import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
            remoteMessage.getNotification();
//        Log.d("abcd", ""+remoteMessage.getData());
        showNotification( remoteMessage.getData().get("title"), remoteMessage.getData().get("body"));
    }

    public void showNotification(String title,String message){
        //將通知與主頁面的Firebase做連結。
        NotificationCompat.Builder builder=new NotificationCompat.Builder(this,"MyNotifications")
                .setContentTitle(title)
                .setSmallIcon(R.drawable.ic_20)
                .setAutoCancel(true)
                .setContentText(message);
        NotificationManagerCompat manager =NotificationManagerCompat.from(this);
        manager.notify(9999,builder.build());
        Log.d("FCM00",title+": 0 :"+message);
    }

}
